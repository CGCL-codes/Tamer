package com.android.frameworktest.listview.touch;

import android.test.ActivityInstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.TouchUtils;
import android.widget.ListView;
import android.view.View;
import com.android.frameworktest.listview.ListGetSelectedView;

/**
 * This test is made to check that getSelectedView() will return
 * null in touch mode.
 */
public class ListGetSelectedViewTest extends ActivityInstrumentationTestCase<ListGetSelectedView> {

    private ListGetSelectedView mActivity;

    private ListView mListView;

    public ListGetSelectedViewTest() {
        super("com.android.frameworktest", ListGetSelectedView.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mListView = getActivity().getListView();
    }

    @MediumTest
    public void testPreconditions() {
        assertNotNull(mActivity);
        assertNotNull(mListView);
        assertEquals(0, mListView.getSelectedItemPosition());
    }

    @LargeTest
    public void testGetSelectedView() {
        View last = mListView.getChildAt(1);
        TouchUtils.clickView(this, last);
        assertNull(mListView.getSelectedItem());
        assertNull(mListView.getSelectedView());
        assertEquals(-1, mListView.getSelectedItemPosition());
    }
}
