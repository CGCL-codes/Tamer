package com.example.android.apis.app;

import com.example.android.apis.R;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FragmentDialogOrActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dialog_or_activity);
        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            DialogFragment newFragment = MyDialogFragment.newInstance();
            ft.add(R.id.embedded, newFragment);
            ft.commit();
        }
        Button button = (Button) findViewById(R.id.show_dialog);
        button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                showDialog();
            }
        });
    }

    void showDialog() {
        DialogFragment newFragment = MyDialogFragment.newInstance();
        newFragment.show(getFragmentManager(), "dialog");
    }

    public static class MyDialogFragment extends DialogFragment {

        static MyDialogFragment newInstance() {
            return new MyDialogFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.hello_world, container, false);
            View tv = v.findViewById(R.id.text);
            ((TextView) tv).setText("This is an instance of MyDialogFragment");
            return v;
        }
    }
}
