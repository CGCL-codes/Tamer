package android.view.cts;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.test.AndroidTestCase;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import com.android.internal.util.XmlUtils;
import com.android.cts.stub.R;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;

@TestTargetClass(ViewGroup.MarginLayoutParams.class)
public class ViewGroup_MarginLayoutParamsTest extends AndroidTestCase {

    private ViewGroup.MarginLayoutParams mMarginLayoutParams;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMarginLayoutParams = null;
    }

    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test MarginLayoutParams constructor", method = "ViewGroup.MarginLayoutParams", args = { android.content.Context.class, android.util.AttributeSet.class }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test MarginLayoutParams constructor", method = "ViewGroup.MarginLayoutParams", args = { int.class, int.class }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test MarginLayoutParams constructor", method = "ViewGroup.MarginLayoutParams", args = { android.view.ViewGroup.LayoutParams.class }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test MarginLayoutParams constructor", method = "ViewGroup.MarginLayoutParams", args = { android.view.ViewGroup.MarginLayoutParams.class }) })
    public void testConstructor() {
        mMarginLayoutParams = null;
        XmlResourceParser p = mContext.getResources().getLayout(R.layout.viewgroup_margin_layout);
        try {
            XmlUtils.beginDocument(p, "LinearLayout");
        } catch (Exception e) {
            fail("Fail in preparing AttibuteSet.");
        }
        mMarginLayoutParams = new ViewGroup.MarginLayoutParams(mContext, p);
        assertNotNull(mMarginLayoutParams);
        mMarginLayoutParams = null;
        mMarginLayoutParams = new ViewGroup.MarginLayoutParams(320, 480);
        assertNotNull(mMarginLayoutParams);
        mMarginLayoutParams = null;
        MarginLayoutParams temp = new ViewGroup.MarginLayoutParams(320, 480);
        mMarginLayoutParams = new ViewGroup.MarginLayoutParams(temp);
        assertNotNull(mMarginLayoutParams);
        mMarginLayoutParams = null;
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(320, 480);
        mMarginLayoutParams = new ViewGroup.MarginLayoutParams(lp);
        assertNotNull(mMarginLayoutParams);
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test setMargins function", method = "setMargins", args = { int.class, int.class, int.class, int.class })
    public void testSetMargins() {
        mMarginLayoutParams = new ViewGroup.MarginLayoutParams(320, 480);
        mMarginLayoutParams.setMargins(20, 30, 120, 140);
        assertEquals(20, mMarginLayoutParams.leftMargin);
        assertEquals(30, mMarginLayoutParams.topMargin);
        assertEquals(120, mMarginLayoutParams.rightMargin);
        assertEquals(140, mMarginLayoutParams.bottomMargin);
    }
}
