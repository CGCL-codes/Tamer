package com.android.cnes.groundsupport.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.android.cnes.groundsupport.BuildingActivity;
import com.android.cnes.groundsupport.R;

public class MyAlertDialogFragmentAudio extends DialogFragment {

    public static MyAlertDialogFragmentAudio newInstance(int title) {
        MyAlertDialogFragmentAudio frag = new MyAlertDialogFragmentAudio();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        return new AlertDialog.Builder(getActivity()).setTitle(title).setPositiveButton(R.string.alert_dialog_yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                ((BuildingActivity) getActivity()).doPositiveAudioClick();
            }
        }).setNegativeButton(R.string.alert_dialog_no, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                ((BuildingActivity) getActivity()).doNegativeAudioClick();
            }
        }).create();
    }
}
