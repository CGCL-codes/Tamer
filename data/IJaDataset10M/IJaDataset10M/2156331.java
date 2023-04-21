package org.dyndns.warenix.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void showQuickToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
