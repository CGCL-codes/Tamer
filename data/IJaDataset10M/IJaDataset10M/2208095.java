package com.example.android.supportv4.app;

import com.example.android.supportv4.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentStackSupport extends FragmentActivity {

    int mStackLevel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_stack);
        Button button = (Button) findViewById(R.id.new_fragment);
        button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                addFragmentToStack();
            }
        });
        if (savedInstanceState == null) {
            Fragment newFragment = CountingFragment.newInstance(mStackLevel);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.simple_fragment, newFragment).commit();
        } else {
            mStackLevel = savedInstanceState.getInt("level");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("level", mStackLevel);
    }

    void addFragmentToStack() {
        mStackLevel++;
        Fragment newFragment = CountingFragment.newInstance(mStackLevel);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.simple_fragment, newFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    public static class CountingFragment extends Fragment {

        int mNum;

        /**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
        static CountingFragment newInstance(int num) {
            CountingFragment f = new CountingFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);
            return f;
        }

        /**
		 * When creating, retrieve this instance's number from its arguments.
		 */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
		 * The Fragment's UI is just a simple text view showing its instance
		 * number.
		 */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.hello_world, container, false);
            View tv = v.findViewById(R.id.text);
            ((TextView) tv).setText("Fragment #" + mNum);
            tv.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.gallery_thumb));
            return v;
        }
    }
}
