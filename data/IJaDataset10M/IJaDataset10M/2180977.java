package org.jecars.client;

/**
 * BASE64Encoder
 *
 * @version $Id: BASE64Encoder.java,v 1.1 2008/05/06 08:19:49 weertj Exp $
 */
public class BASE64Encoder {

    public static String encodeBufferGZIP(byte origBytes[], int off, int len) {
        return Base64.encodeBytes(origBytes, off, len, Base64.GZIP);
    }

    public static String encodeBuffer(byte origBytes[], int off, int len) {
        return Base64.encodeBytes(origBytes, off, len);
    }

    public static String encodeBuffer(byte origBytes[]) {
        return Base64.encodeBytes(origBytes);
    }

    public static String encodeBuffer(byte origBytes[], int pOptions) {
        return Base64.encodeBytes(origBytes, pOptions);
    }

    public String encode(byte origBytes[]) {
        return Base64.encodeBytes(origBytes);
    }
}
