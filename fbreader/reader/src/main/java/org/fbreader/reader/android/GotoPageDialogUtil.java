/*
 * Copyright (C) 2007-2015 FBReader.ORG Limited <contact@fbreader.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.fbreader.reader.android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import net.simonvt.numberpicker.NumberPicker;

import org.geometerplus.zlibrary.core.resources.ZLResource;

import org.fbreader.md.MDAlertDialogBuilder;
import org.fbreader.reader.R;

public abstract class GotoPageDialogUtil {
	public static interface PageSelector {
		void gotoPage(int number);
	}

	public static void showDialog(Activity activity, final PageSelector selector, int current, int total) {
		final View root = activity.getLayoutInflater().inflate(R.layout.goto_page_number, null);
		final NumberPicker picker = (NumberPicker)root.findViewById(R.id.goto_page_number_picker);
		picker.setMinValue(1);
		picker.setMaxValue(total);
		picker.setValue(current);
		picker.setWrapSelectorWheel(false);
		final ZLResource dialogResource = ZLResource.resource("dialog");

		final AlertDialog dialog = new MDAlertDialogBuilder(activity)
			.setTitle(dialogResource.getResource("gotoPage").getValue())
			.setView(root)
			.setPositiveButton(
				dialogResource.getResource("button").getResource("ok").getValue(),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						picker.clearFocus();
						selector.gotoPage(picker.getValue());
					}
				}
			)
			.create();

		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				picker.setFocusable(true);
				picker.setFocusableInTouchMode(true);
				picker.requestFocus();
				final InputMethodManager imm =
					(InputMethodManager)picker.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(picker.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		dialog.show();
	}
}
