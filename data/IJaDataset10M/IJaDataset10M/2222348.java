package com.androidcommons.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author Denis Migol
 * 
 */
public final class DisplayUtil {

    private DisplayUtil() {
    }

    public static int dipToPx(final Context context, final int dip) {
        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static DisplayMetrics getDisplayMetrics(final Activity activity) {
        final DisplayMetrics ret = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(ret);
        return ret;
    }

    public static DisplayMetrics getDisplayMetrics(final Context context) {
        return context.getResources().getDisplayMetrics();
    }
}
