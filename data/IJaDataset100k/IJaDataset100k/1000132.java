package com.gargoylesoftware.htmlunit.javascript.host;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Contains some missing features of Rhino NativeString.
 *
 * @version $Revision: 6701 $
 * @author Ahmed Ashour
 */
public final class StringCustom {

    private StringCustom() {
    }

    /**
     * Removes whitespace from the left end of the string.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the left trimmed string
     */
    public static String trimLeft(final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        final String string = Context.toString(thisObj);
        int start = 0;
        final int length = string.length();
        while (start < length && ScriptRuntime.isJSWhitespaceOrLineTerminator(string.charAt(start))) {
            start++;
        }
        if (start == 0) {
            return string;
        }
        return string.substring(start, length);
    }

    /**
     * Removes whitespace from the right end of the string.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the right trimmed string
     */
    public static String trimRight(final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        final String string = Context.toString(thisObj);
        final int length = string.length();
        int end = length;
        while (end > 0 && ScriptRuntime.isJSWhitespaceOrLineTerminator(string.charAt(end - 1))) {
            end--;
        }
        if (end == length) {
            return string;
        }
        return string.substring(0, end);
    }
}
