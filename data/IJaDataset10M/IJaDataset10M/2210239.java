package net.sourceforge.javautil.common.encode;

/**
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class EncodingUtil {

    public static final Base64EncodingAlgorithm BASE64 = new Base64EncodingAlgorithm();

    public static byte[] base64Encode(byte[] original) {
        return BASE64.encode(original);
    }

    public static byte[] base64Decode(byte[] encoded) {
        return BASE64.decode(encoded);
    }
}
