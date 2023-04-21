package edu.uiuc.android.scorch.tests;

import android.test.ActivityInstrumentationTestCase;
import edu.uiuc.android.scorch.ui.ShopActivity;

/**
 * Make sure that the main launcher activity opens up properly, which will be verified by
 * {@link ActivityInstrumentationTestCase#testActivityTestCaseSetUpProperly}.
 */
public class ShopActivityTest extends ActivityInstrumentationTestCase<ShopActivity> {

    /**
     * Default constructor
     */
    public ShopActivityTest() {
        super("edu.uiuc.android.scorch", ShopActivity.class);
    }
}
