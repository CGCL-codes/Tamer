package org.armedbear.j.mail;

import gnu.regexp.RE;
import gnu.regexp.REMatch;
import gnu.regexp.UncheckedRE;
import java.io.UnsupportedEncodingException;
import org.armedbear.j.FastStringBuffer;
import org.armedbear.j.Log;
import org.armedbear.j.Utilities;

public final class RFC2047 {

    private static final RE prefixRE = new UncheckedRE("=\\?[^?]+\\?[bq]\\?");

    public static String decode(String encoded) {
        if (encoded == null) return null;
        if (encoded.indexOf("=?") < 0) return encoded;
        REMatch match = prefixRE.getMatch(encoded.toLowerCase());
        if (match == null) {
            Log.error("RFC2047.decode prefix is null");
            Log.error("encoded = |" + encoded + "|");
            return encoded;
        }
        String prefix = match.toString();
        int index = match.getStartIndex();
        String charset = prefix.substring(2, prefix.length() - 3);
        String encoding = Utilities.getEncodingFromCharset(charset);
        FastStringBuffer sb = new FastStringBuffer();
        sb.append(encoded.substring(0, index));
        String remaining = encoded.substring(index);
        while (true) {
            int begin = prefix.length();
            int end = remaining.indexOf("?=", begin);
            if (end < 0) {
                sb.append(remaining);
                Log.error("RFC2047.decode error");
                Log.error("encoded = |" + encoded + "|");
                Log.error("remaining = |" + remaining + "|");
                return sb.toString();
            }
            byte[] bytes = null;
            if (prefix.endsWith("?b?")) bytes = Base64Decoder.decode(remaining.substring(begin, end)); else if (prefix.endsWith("?q?")) bytes = QuotedPrintableDecoder.decode(remaining.substring(begin, end));
            if (bytes == null) {
                Log.error("RFC2047.decode error");
                Log.error("encoded = |" + encoded + "|");
                return encoded;
            }
            try {
                sb.append(new String(bytes, encoding));
            } catch (UnsupportedEncodingException e) {
                Log.error(e);
                return encoded;
            }
            remaining = remaining.substring(end + 2);
            index = remaining.toLowerCase().indexOf(prefix);
            if (index < 0) {
                sb.append(remaining);
                break;
            }
            sb.append(remaining.substring(0, index));
            remaining = remaining.substring(index);
        }
        return sb.toString();
    }
}
