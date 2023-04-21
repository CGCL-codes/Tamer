package com.szxys.mhub.ui.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.szxys.mhub.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout.LayoutParams;

/**
 * ListViewActivity ； <功能详细描述>
 * 
 * @author yangzhao
 * @version [版本号V01, 2011-4-29]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ListViewActivity extends ListActivity implements OnScrollListener {

    protected static final String TAG = "ListViewActivity";

    private ListView mListView;

    private LinearLayout mLoadLayout;

    private List<? extends Map<String, ?>> mData0;

    private List<? extends Map<String, ?>> mData1;

    private List<? extends Map<String, ?>> mData2;

    private ListViewAdapter mListViewAdapter = null;

    private int mLastItem = 0;

    private int mCount = 120;

    private final int loadCount = 7;

    private final Handler mHandler = new Handler();

    private final LayoutParams mProgressBarLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    private final LayoutParams mTipContentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    private boolean ismoved = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadLayout = new LinearLayout(this);
        mLoadLayout.setMinimumHeight(60);
        mLoadLayout.setGravity(Gravity.CENTER);
        mLoadLayout.setOrientation(LinearLayout.HORIZONTAL);
        ProgressBar mProgressBar = new ProgressBar(this);
        mProgressBar.setPadding(0, 0, 15, 0);
        mLoadLayout.addView(mProgressBar, mProgressBarLayoutParams);
        TextView mTipContent = new TextView(this);
        mTipContent.setText("加载中...");
        mLoadLayout.addView(mTipContent, mTipContentLayoutParams);
        mListView = getListView();
        mListView.addFooterView(mLoadLayout);
        Map<String, Object>[] maps = testData();
        Map<String, Object>[] maps1 = testData1();
        mData0 = GetMaps.getData(maps);
        mData1 = GetMaps.getData(maps1);
        mData2 = GetMaps.getData(maps1);
        mCount = mData0.size() + mData1.size() + mData2.size();
        int[] ids = new int[] { R.id.img1, R.id.tv1, R.id.tv2, R.id.tv3, R.id.imbtn1 };
        int[] ids1 = new int[] { R.id.tv11, R.id.tv12, R.id.tv13, R.id.tv14 };
        String[] from1 = new String[] { "tv11", "tv12", "tv13", "tv14" };
        String[] from = new String[] { "img1", "tv1", "tv2", "tv3", "imbtn1" };
        mListViewAdapter = new ListViewAdapter(this, mData0, mData1, mData2, new int[] { R.layout.pf_listview_item, R.layout.pf_table_item, R.layout.pf_table_item }, new String[][] { from, from1, from1 }, new int[][] { ids, ids1, ids1 });
        setListAdapter(mListViewAdapter);
        mListView.setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int mFirstVisibleItem, int mVisibleItemCount, int mTotalItemCount) {
        mLastItem = mFirstVisibleItem + mVisibleItemCount - 1;
        if (mListViewAdapter.getCount() > mCount - 1) {
            mListView.removeFooterView(mLoadLayout);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int mScrollState) {
        if (mLastItem == mListViewAdapter.getCount() && mScrollState == OnScrollListener.SCROLL_STATE_IDLE && ismoved) {
            if (mListViewAdapter.getCount() < mCount) {
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mListViewAdapter.setLoadCount(loadCount);
                        mListViewAdapter.addListItem((ArrayList<HashMap<String, Object>>) mData0, (ArrayList<HashMap<String, Object>>) mData1, (ArrayList<HashMap<String, Object>>) mData2);
                        mListViewAdapter.notifyDataSetChanged();
                        mListView.setSelection(mLastItem);
                    }
                }, 1);
            }
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        System.out.println(position);
        if (position % 3 == 0) {
            TextView tv1 = (TextView) v.findViewById(R.id.tv1);
            tv1.setText("i am ok");
        }
        if (position % 3 == 1) {
            TextView tv = (TextView) v.findViewById(R.id.tv11);
            tv.setText("zzzzzzzz");
        }
        if (position % 3 == 2) {
            TextView tv = (TextView) v.findViewById(R.id.tv13);
            tv.setText("hhhhhhhh");
        }
    }

    public HashMap<String, Object>[] testData() {
        String[] key1 = new String[] { "img1", "tv1", "tv2", "tv3", "imbtn1" };
        String[] key2 = new String[] { "img1", "tv1", "tv3", "imbtn1" };
        String[] key3 = new String[] { "img1", "tv1", "tv2" };
        String[] key4 = new String[] { "img1", "tv1" };
        Object[] id1 = new Object[] { android.R.drawable.ic_dialog_alert, "G1", "test 1", "google 1", android.R.drawable.ic_dialog_map };
        Object[] id2 = new Object[] { android.R.drawable.ic_dialog_dialer, "G2", "google 2", android.R.drawable.ic_dialog_map };
        Object[] id3 = new Object[] { android.R.drawable.ic_dialog_email, "G3", "test 3" };
        Object[] id4 = new Object[] { android.R.drawable.ic_dialog_info, "G3" };
        HashMap<String, Object> map1 = GetMaps.getMap(key1, id1);
        HashMap<String, Object> map2 = GetMaps.getMap(key2, id2);
        HashMap<String, Object> map3 = GetMaps.getMap(key3, id3);
        HashMap<String, Object> map4 = GetMaps.getMap(key4, id4);
        @SuppressWarnings("unchecked") HashMap<String, Object>[] maps = new HashMap[] { map1, map2, map3, map4, map1, map2, map3, map4, map1, map2, map3, map4, map1, map2, map3, map4, map1, map2, map3, map4, map1, map2, map3, map4 };
        return maps;
    }

    public HashMap<String, Object>[] testData1() {
        String[] key1 = new String[] { "tv11", "tv12", "tv13", "tv14" };
        String[] key2 = new String[] { "tv11", "tv12", "tv13" };
        String[] key3 = new String[] { "tv11", "tv12" };
        String[] key4 = new String[] { "tv11" };
        Object[] id1 = new Object[] { "yang1", "zhao1", "text", "1" };
        Object[] id2 = new Object[] { "yang2", "zhao2", "text" };
        Object[] id3 = new Object[] { "yang3", "zhao3" };
        Object[] id4 = new Object[] { "yang4" };
        HashMap<String, Object> map1 = GetMaps.getMap(key1, id1);
        HashMap<String, Object> map2 = GetMaps.getMap(key2, id2);
        HashMap<String, Object> map3 = GetMaps.getMap(key3, id3);
        HashMap<String, Object> map4 = GetMaps.getMap(key4, id4);
        @SuppressWarnings("unchecked") HashMap<String, Object>[] maps = new HashMap[] { map1, map2, map3, map4, map1, map2, map3, map4, map1, map2, map3, map4, map1, map2, map3, map4, map1, map2, map3, map4, map1, map2, map3, map4 };
        return maps;
    }
}
