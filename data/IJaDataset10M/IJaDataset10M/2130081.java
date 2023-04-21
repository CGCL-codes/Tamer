package com.example.android.supportv4.app;

import com.example.android.supportv4.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class FragmentReceiveResultSupport extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(R.id.simple_fragment);
        setContentView(frame, lp);
        if (savedInstanceState == null) {
            Fragment newFragment = new ReceiveResultFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.simple_fragment, newFragment).commit();
        }
    }

    public static class ReceiveResultFragment extends Fragment {

        private static final int GET_CODE = 0;

        private TextView mResults;

        private OnClickListener mGetListener = new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SendResult.class);
                startActivityForResult(intent, GET_CODE);
            }
        };

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.receive_result, container, false);
            mResults = (TextView) v.findViewById(R.id.results);
            mResults.setText(mResults.getText(), TextView.BufferType.EDITABLE);
            Button getButton = (Button) v.findViewById(R.id.get);
            getButton.setOnClickListener(mGetListener);
            return v;
        }

        /**
		 * This method is called when the sending activity has finished, with
		 * the result it supplied.
		 */
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == GET_CODE) {
                Editable text = (Editable) mResults.getText();
                if (resultCode == RESULT_CANCELED) {
                    text.append("(cancelled)");
                } else {
                    text.append("(okay ");
                    text.append(Integer.toString(resultCode));
                    text.append(") ");
                    if (data != null) {
                        text.append(data.getAction());
                    }
                }
                text.append("\n");
            }
        }
    }
}
