package acmsoft.util.codec;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import acmsoft.util.NLSEncodingData;
import acmsoft.util.NLSManager;
import acmsoft.util.StringUtils;

/**
 * 
 * This class is written because standard Java URLEncode.encode
 * method with encoding-name parameter was introduced in JDK1.4 only.
 * 
 * And standard class doesn't have decode method.
 */
public class URLEncodedCodec {

    private static final String GOOD_CHARS = StringUtils.strLiterals + "-.*";

    private static final BitSet setGoodChars = new BitSet(128);

    static {
        StringUtils.initSet(GOOD_CHARS, setGoodChars);
    }

    /**
     * Encodes String aacording to URLEncode format. JDK 1.4 has implementation of this
     * method but JDK1.3 doesn't. This implementation should work faster then one from JDK1.4
     * @param str
     * @param encoding it's recommended to use superset of ASCII encodings here.
     * @return
     */
    public static String encode(final String str, NLSEncodingData encoding) {
        try {
            boolean bModified = false;
            final String strEncoding;
            if (encoding.isSupersetOfASCII()) {
                strEncoding = encoding.getJavaEncodingName();
            } else {
                strEncoding = NLSManager.getUnmarkedUnicodeEncoding(encoding.getJavaEncodingName());
            }
            final int iCharCount = str.length();
            final StringBuffer sb = new StringBuffer(iCharCount * 6);
            final char[] chars = str.toCharArray();
            for (int i = 0; i < iCharCount; i++) {
                final char c = chars[i];
                if (setGoodChars.get(c)) {
                    sb.append(c);
                } else if (c == ' ') {
                    sb.append('+');
                    bModified = true;
                } else {
                    final int iStartPos = i;
                    if (StringUtils.isLowSurrogate(c) && (i < iCharCount - 1) && StringUtils.isHighSurrogate(chars[i + 1])) {
                        i++;
                    }
                    final byte[] bytes = str.substring(iStartPos, i + 1).getBytes(strEncoding);
                    final int iBytesCount = bytes.length;
                    for (int bCounter = 0; bCounter < iBytesCount; bCounter++) {
                        sb.append('%');
                        StringUtils.byteToHex(bytes[bCounter], sb);
                    }
                    bModified = true;
                }
            }
            if (bModified) {
                return sb.toString();
            } else {
                return str;
            }
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }
}
