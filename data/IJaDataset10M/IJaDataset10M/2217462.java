package com.android.imftest.samples;

import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import com.android.imftest.R;

public class BigEditTextActivityScrollablePanScanTests extends ImfBaseTestCase<BigEditTextActivityScrollablePanScan> {

    public final String TAG = "BigEditTextActivityScrollablePanScanTests";

    public BigEditTextActivityScrollablePanScanTests() {
        super(BigEditTextActivityScrollablePanScan.class);
    }

    @LargeTest
    public void testAppAdjustmentPanScan() {
        pause(2000);
        View rootView = ((BigEditTextActivityScrollablePanScan) mTargetActivity).getRootView();
        View servedView = ((BigEditTextActivityScrollablePanScan) mTargetActivity).getDefaultFocusedView();
        assertNotNull(rootView);
        assertNotNull(servedView);
        destructiveCheckImeInitialState(rootView, servedView);
        verifyEditTextAdjustment(servedView, rootView.getMeasuredHeight());
    }
}
