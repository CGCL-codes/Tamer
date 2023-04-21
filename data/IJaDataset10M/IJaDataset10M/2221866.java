package it.aton.proj.dem.commons.util.armors;

import it.aton.proj.dem.commons.util.armors.internals.Base64Coder;
import java.io.ByteArrayOutputStream;

public class Base64 {

    public static String encode(byte[] dData) {
        return new String(Base64Coder.encode(dData));
    }

    private static final byte[] ALPHASET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".getBytes();

    public static byte[] decode(String eData) {
        if (eData == null) {
            throw new IllegalArgumentException("Cannot decode null");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (byte b : eData.getBytes()) {
            for (byte b1 : ALPHASET) if (b == b1) {
                baos.write(b);
                break;
            }
        }
        return Base64Coder.decode(new String(baos.toByteArray()));
    }
}
