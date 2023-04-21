package android.text.method;

import android.graphics.Rect;
import android.text.GetChars;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.view.View;

/**
 * This transformation method causes any carriage return characters (\r)
 * to be hidden by displaying them as zero-width non-breaking space
 * characters (﻿).
 */
public class HideReturnsTransformationMethod extends ReplacementTransformationMethod {

    private static char[] ORIGINAL = new char[] { '\r' };

    private static char[] REPLACEMENT = new char[] { '﻿' };

    /**
     * The character to be replaced is \r.
     */
    protected char[] getOriginal() {
        return ORIGINAL;
    }

    /**
     * The character that \r is replaced with is ﻿.
     */
    protected char[] getReplacement() {
        return REPLACEMENT;
    }

    public static HideReturnsTransformationMethod getInstance() {
        if (sInstance != null) return sInstance;
        sInstance = new HideReturnsTransformationMethod();
        return sInstance;
    }

    private static HideReturnsTransformationMethod sInstance;
}
