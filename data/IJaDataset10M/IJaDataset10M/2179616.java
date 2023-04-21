package org.snsmeet.zxing.client.android.wifi;

import java.util.regex.Pattern;
import android.text.TextUtils;

/**
 * Try with:
 * http://chart.apis.google.com/chart?cht=qr&chs=240x240&chl=WIFI:S:linksys;P:mypass;T:WPA;;
 *
 * TODO(vikrama): Test with binary ssid or password.
 *
 * @author Vikram Aggarwal
 */
final class NetworkUtil {

    private static final Pattern HEX_DIGITS = Pattern.compile("[0-9A-Fa-f]+");

    private NetworkUtil() {
    }

    /**
   * Encloses the incoming string inside double quotes, if it isn't already quoted.
   * @param string: the input string
   * @return a quoted string, of the form "input".  If the input string is null, it returns null
   * as well.
   */
    static String convertToQuotedString(String string) {
        if (string == null) {
            return null;
        }
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        int lastPos = string.length() - 1;
        if (lastPos < 0 || (string.charAt(0) == '"' && string.charAt(lastPos) == '"')) {
            return string;
        }
        return '\"' + string + '\"';
    }

    /**
   * Check if wepKey is a valid hexadecimal string.
   * @param wepKey the input to be checked
   * @return true if the input string is indeed hex or empty.  False if the input string is non-hex
   * or null.
   */
    static boolean isHexWepKey(CharSequence wepKey) {
        if (wepKey == null) {
            return false;
        }
        int length = wepKey.length();
        return (length == 10 || length == 26 || length == 58) && HEX_DIGITS.matcher(wepKey).matches();
    }
}
