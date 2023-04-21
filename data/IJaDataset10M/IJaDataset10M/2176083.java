package com.codetastrophe.cellfinder.utils;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;

public class StyledResourceHelper {

    public static CharSequence GetStyledString(Context context, int stringid, Object... args) {
        String str = context.getString(stringid);
        if (str != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].getClass() == String.class) args[i] = TextUtils.htmlEncode((String) args[i]);
            }
            String strtxt = String.format(str, args);
            return Html.fromHtml(strtxt);
        } else {
            return null;
        }
    }
}
