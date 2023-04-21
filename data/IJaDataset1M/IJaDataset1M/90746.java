package com.example.android.supportv4.app;

import com.example.android.supportv4.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Demonstrates how to show an AlertDialog that is managed by a Fragment.
 */
public class FragmentAlertDialogSupport extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dialog);
        View tv = findViewById(R.id.text);
        ((TextView) tv).setText("Example of displaying an alert dialog with a DialogFragment");
        Button button = (Button) findViewById(R.id.show);
        button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                showDialog();
            }
        });
    }

    void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(R.string.alert_dialog_two_buttons_title);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick() {
        Log.i("FragmentAlertDialog", "Positive click!");
    }

    public void doNegativeClick() {
        Log.i("FragmentAlertDialog", "Negative click!");
    }

    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(int title) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt("title");
            return new AlertDialog.Builder(getActivity()).setIcon(R.drawable.alert_dialog_icon).setTitle(title).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    ((FragmentAlertDialogSupport) getActivity()).doPositiveClick();
                }
            }).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    ((FragmentAlertDialogSupport) getActivity()).doNegativeClick();
                }
            }).create();
        }
    }
}
