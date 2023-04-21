package ch.ethz.dcg.spamato.filter.razor.hash;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author simon This class is for decoding base64-encoded mails. It does this
 *         wrong, but the same way razor does it, which is more important than
 *         correctness.
 */
public class Base64Decoder {

    private static final Pattern VALID_CONTENT_PATTERN = Pattern.compile("[^=]*");

    private static final Pattern BEGIN_OF_CONTENT_PATTERN = Pattern.compile("\r?\n\r?\n");

    private static final Pattern BASE64_HEADER_PATTERN = Pattern.compile("Content-Transfer-Encoding: base64");

    private static final Pattern DECODE_PATTERN = Pattern.compile("(.{1,60})");

    private static final char[] DECODE_TRANSLATE_TABLE;

    static {
        DECODE_TRANSLATE_TABLE = new char[256];
        char translateInto = ' ';
        for (int i = 0; i < DECODE_TRANSLATE_TABLE.length; i++) DECODE_TRANSLATE_TABLE[i] = (char) i;
        for (int i = 'A'; i <= 'Z'; i++) {
            DECODE_TRANSLATE_TABLE[i] = translateInto;
            if (translateInto != '_') translateInto++;
        }
        for (int i = 'a'; i <= 'z'; i++) {
            DECODE_TRANSLATE_TABLE[i] = translateInto;
            if (translateInto != '_') translateInto++;
        }
        for (int i = '0'; i <= '9'; i++) {
            DECODE_TRANSLATE_TABLE[i] = translateInto;
            if (translateInto != '_') translateInto++;
        }
        DECODE_TRANSLATE_TABLE['+'] = translateInto;
        if (translateInto != '_') translateInto++;
        DECODE_TRANSLATE_TABLE['/'] = translateInto;
    }

    /**
	 * tells if a mail message is base64encoded. needs at least the header to be
	 * correct.
	 * 
	 * @param header
	 *        at least the header of the message
	 * @return <code>true</code> if the message is base 64 encoded,
	 *         <code>false</code> otherwise.
	 */
    public static boolean isBase64Encoded(String header) {
        return (header != null) && BASE64_HEADER_PATTERN.matcher(header).find();
    }

    /**
	 * decode a base 64 encoded mail-message
	 * 
	 * @param headerAndBody
	 *        header and body of the message together. split by
	 *        <code>\n\r*\n</code>.
	 * @return a string containing the header joined with the decoded body.
	 */
    public static String decodeBase64(String headerAndBody) {
        if (headerAndBody == null) return null;
        String header = headerAndBody.split("\n\r*\n")[0];
        String base64Part = extractBase64(headerAndBody);
        if (base64Part == null) return null;
        base64Part = base64Part.replaceAll("[^0-9a-zA-Z+=/]", "");
        StringBuffer uuEncoded = new StringBuffer(base64Part);
        int curChar = 0;
        while (curChar < uuEncoded.length()) {
            uuEncoded.setCharAt(curChar, DECODE_TRANSLATE_TABLE[(int) uuEncoded.charAt(curChar)]);
            curChar++;
        }
        StringBuffer decoded;
        decoded = uuDecode(uuEncoded);
        String res = header + "\n\n" + decoded.toString();
        return res;
    }

    /**
	 * UUdecoding of a provided string.
	 * 
	 * @param uuEncoded
	 *        the string to decode
	 * @return the decoded string
	 * @see sun.misc.UUDecoder
	 */
    private static StringBuffer uuDecode(StringBuffer uuEncoded) {
        StringBuffer decoded = new StringBuffer();
        for (int in = 0; (in + 3) < uuEncoded.length(); in += 4) {
            for (int t = 0; t < 4; t++) uuEncoded.setCharAt(in + t, (char) ((uuEncoded.charAt(in + t) - ' ') & 0x3f));
            decoded.append((char) (((uuEncoded.charAt(in) << 2) & 0xfc) | ((uuEncoded.charAt(in + 1) >>> 4) & 3)));
            decoded.append((char) (((uuEncoded.charAt(in + 1) << 4) & 0xf0) | ((uuEncoded.charAt(in + 2) >>> 2) & 0xf)));
            decoded.append((char) (((uuEncoded.charAt(in + 2) << 6) & 0xc0) | (uuEncoded.charAt(in + 3) & 0x3f)));
        }
        return decoded;
    }

    /**
	 * get the part of the message that is base64-encoded.
	 * 
	 * @param headerAndBody
	 *        header joined with body of the message
	 * @return the part that seems to be base64 encoded.
	 */
    private static String extractBase64(String headerAndBody) {
        Matcher headerMatch = BASE64_HEADER_PATTERN.matcher(headerAndBody);
        if (headerMatch.find()) {
            String withoutHeader = headerAndBody.substring(headerMatch.end());
            Matcher beginOfContentMatcher = BEGIN_OF_CONTENT_PATTERN.matcher(withoutHeader);
            if (!beginOfContentMatcher.find()) return null;
            String content = withoutHeader.substring(beginOfContentMatcher.end());
            Matcher validContentMatcher = VALID_CONTENT_PATTERN.matcher(content);
            if (!validContentMatcher.find()) return null;
            String resultingContent = content.substring(validContentMatcher.start(), validContentMatcher.end());
            return resultingContent + "==";
        } else return null;
    }
}
