package android.performance2.cts;

import android.app.Activity;
import android.content.Intent;
import android.test.InstrumentationTestCase;

public class AppStartup extends InstrumentationTestCase {

    private static final long MAX_AVG_STARTUP_TIME = 500;

    private static final String PACKAGE_UNDER_TEST = "com.android.music";

    private static final String ACTIVITY_UNDER_TEST = "MusicBrowserActivity";

    private static final int NUMBER_OF_ITERS = 10;

    private Intent buildIntent(final String pkgName, String className) {
        final String fullClassName = pkgName + "." + className;
        Intent intent = new Intent();
        intent.setClassName(pkgName, fullClassName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return intent;
    }

    public void testStartup() throws InterruptedException {
        long totalTime = 0;
        Intent i = buildIntent(PACKAGE_UNDER_TEST, ACTIVITY_UNDER_TEST);
        for (int x = 0; x < 3; x++) {
            Activity a = getInstrumentation().startActivitySync(i);
            a.finish();
        }
        for (int x = 0; x < NUMBER_OF_ITERS; x++) {
            long start = System.currentTimeMillis();
            Activity a = getInstrumentation().startActivitySync(i);
            long end = System.currentTimeMillis();
            long diff = end - start;
            totalTime += diff;
            a.finish();
        }
        long avgStartupTime = totalTime / NUMBER_OF_ITERS;
        android.util.Log.d("AppStartup", "AppStartup for " + PACKAGE_UNDER_TEST + "/" + ACTIVITY_UNDER_TEST + " took " + avgStartupTime + "ms.");
        assertTrue("App Took too long to startup: " + avgStartupTime + " " + MAX_AVG_STARTUP_TIME, avgStartupTime < MAX_AVG_STARTUP_TIME);
    }
}
