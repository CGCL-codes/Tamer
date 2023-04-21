package gleam.executive.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * String Utility Class This is used to encode passwords
 * programmatically
 *
 * <p>
 * <a h ref="StringUtil.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class StringUtil {

    private static final Log log = LogFactory.getLog(StringUtil.class);

    /**
   * Encode a string using algorithm specified in web.xml and return the
   * resulting encrypted password. If exception, the plain credentials
   * string is returned
   *
   * @param password Password or other credentials to use in
   *          authenticating this username
   * @param algorithm Algorithm used to do the digest
   *
   * @return encypted password based on the algorithm.
   */
    public static String encodePassword(String password, String algorithm) {
        byte[] unencodedPassword = password.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            log.error("Exception: " + e);
            return password;
        }
        md.reset();
        md.update(unencodedPassword);
        byte[] encodedPassword = md.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }
        return buf.toString();
    }

    /**
   * Encode a string using Base64 encoding. Used when storing passwords
   * as cookies.
   *
   * This is weak encoding in that anyone can use the decodeString
   * routine to reverse the encoding.
   *
   * @param str
   * @return String
   */
    public static String encodeString(String str) {
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        return encoder.encodeBuffer(str.getBytes()).trim();
    }

    /**
   * Decode a string using Base64 encoding.
   *
   * @param str
   * @return String
   */
    public static String decodeString(String str) {
        sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
        try {
            return new String(dec.decodeBuffer(str));
        } catch (IOException io) {
            throw new RuntimeException(io.getMessage(), io.getCause());
        }
    }

    public static String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date).toString();
    }

    public static String readFromURL(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        in.close();
        return sb.toString();
    }

    public static String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer();
        char current;
        if (in != null) {
            for (int i = 0; i < in.length(); i++) {
                current = in.charAt(i);
                if ((current == 0x9) || (current == 0xA) || (current == 0xD) || ((current >= 0x20) && (current <= 0xD7FF)) || ((current >= 0xE000) && (current <= 0xFFFD)) || ((current >= 0x10000) && (current <= 0x10FFFF))) {
                    out.append(current);
                } else {
                    log.debug("strip char: " + current);
                }
            }
        }
        return out.toString();
    }
}
