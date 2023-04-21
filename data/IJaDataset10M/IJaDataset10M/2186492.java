package org.owasp.esapi.codecs;

/** Encode and decode to/from hexadecimal strings to byte arrays. */
public class Hex {

    /** Output byte representation as hexadecimal representation.
     * 
     * @param b				Bytes to encode to hexadecimal representation.
     * @param leading0x		If true, return with leading "0x".
     * @return				Hexadecimal representation of specified bytes.
     */
    public static String toHex(byte[] b, boolean leading0x) {
        StringBuffer hexString = new StringBuffer();
        if (leading0x) {
            hexString.append("0x");
        }
        for (int i = 0; i < b.length; i++) {
            int j = b[i] & 0xff;
            String hex = Integer.toHexString(j);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Output byte representation as hexadecimal representation.
     * Alias for <code>toHex()</code> method.
     * 
     * @param b				Bytes to encode to hexadecimal representation.
     * @param leading0x		If true, return with leading "0x".
     * @return				Hexadecimal representation of specified bytes.
     */
    public static String encode(byte[] b, boolean leading0x) {
        return toHex(b, leading0x);
    }

    /**
     * Decode hexadecimal-encoded string and return raw byte array.
     * Important note: This method preserves leading 0 filled bytes on the
     * conversion process, which is important for cryptographic operations
     * in dealing with things like keys, initialization vectors, etc. For
     * example, the string "0x0000face" is going to return a byte array
     * whose length is 4, not 2.
     * 
     * @param hexStr	Hexadecimal-encoded string, with or without leading "0x".
     * @return			The equivalent byte array.
     */
    public static byte[] fromHex(String hexStr) {
        String hexRep = hexStr;
        if (hexStr.startsWith("0x")) {
            hexRep = hexStr.substring(2);
        }
        int len = hexRep.length() / 2;
        byte[] rawBytes = new byte[len];
        for (int i = 0; i < len; i++) {
            String substr = hexRep.substring(i * 2, (i * 2) + 2);
            rawBytes[i] = (byte) (Integer.parseInt(substr, 16));
        }
        return rawBytes;
    }

    /** Decode hexadecimal-encoded string and return raw byte array.
     * Alias for <code>fromHex()</code> method.
     * 
     * @param hexStr	Hexadecimal-encoded string, with or without leading "0x".
     * @return			The equivalent byte array. 
     */
    public static byte[] decode(String hexStr) {
        return fromHex(hexStr);
    }
}
