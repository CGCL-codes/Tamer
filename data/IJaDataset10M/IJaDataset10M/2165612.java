package org.jsmtpd.tools;

import java.io.IOException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Jean-Francois POUX
 */
public class Base64Helper {

    public static String encode(byte[] in) {
        BASE64Encoder b = new BASE64Encoder();
        return b.encode(in);
    }

    public static byte[] decode(String in) {
        BASE64Decoder b = new BASE64Decoder();
        byte[] res = null;
        try {
            res = b.decodeBuffer(in);
        } catch (IOException e) {
        }
        return res;
    }
}
