package org.openremote.android.test.console.net;

import org.openremote.android.console.AppSettingsActivity;
import org.openremote.android.console.model.AppSettingsModel;
import org.openremote.android.console.net.ORControllerServerSwitcher;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

/**
 * 
 * @author handy 2010-04-29
 *
 */
public class ORControllerServerSwitcherTest extends ActivityInstrumentationTestCase2<AppSettingsActivity> {

    public ORControllerServerSwitcherTest() {
        super("org.openremote.android.console", AppSettingsActivity.class);
    }
}
