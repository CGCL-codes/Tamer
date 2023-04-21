package com.commonsware.cwac.endless.demo;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.commonsware.cwac.endless.EndlessAdapter;
import java.util.ArrayList;

public class EndlessAdapterExceptionDemo extends ListActivity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        ArrayList<Integer> items = new ArrayList<Integer>();
        for (int i = 0; i < 25; i++) {
            items.add(i);
        }
        setListAdapter(new DemoAdapter(items));
    }

    class DemoAdapter extends EndlessAdapter {

        DemoAdapter(ArrayList<Integer> list) {
            super(EndlessAdapterExceptionDemo.this, new SpecialAdapter(list), R.layout.pending);
        }

        @Override
        protected boolean cacheInBackground() throws Exception {
            SystemClock.sleep(10000);
            if (getWrappedAdapter().getCount() < 75) {
                return (true);
            }
            throw new Exception("Gadzooks!");
        }

        @Override
        protected void appendCachedData() {
            if (getWrappedAdapter().getCount() < 75) {
                @SuppressWarnings("unchecked") ArrayAdapter<Integer> a = (ArrayAdapter<Integer>) getWrappedAdapter();
                for (int i = 0; i < 25; i++) {
                    a.add(a.getCount());
                }
            }
        }
    }

    class SpecialAdapter extends ArrayAdapter<Integer> {

        SpecialAdapter(ArrayList<Integer> items) {
            super(EndlessAdapterExceptionDemo.this, R.layout.row, android.R.id.text1, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            return (row);
        }
    }
}
