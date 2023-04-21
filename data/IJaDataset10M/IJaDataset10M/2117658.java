package org.openintents.historify.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * 
 * Helper class for displaying Toast messages.
 * 
 * @author berke.andras
 */
public class Toaster {

    public static void toast(Context context, int resId) {
        toast(context, context.getString(resId));
    }

    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Fragment fragment, int resId) {
        toast(fragment.getActivity(), fragment.getString(resId));
    }

    public static void toast(Fragment fragment, String text) {
        Toast.makeText(fragment.getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Fragment fragment, int resId, String parameterText) {
        Toast.makeText(fragment.getActivity(), String.format(fragment.getString(resId), parameterText), Toast.LENGTH_SHORT).show();
    }
}
