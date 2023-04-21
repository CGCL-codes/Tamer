package net.sf.planofattack.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.sf.planofattack.io.StreamUtil;

public class HashUtil {

    private HashUtil() {
    }

    public static String generateHexHash(byte data[], String codec) {
        try {
            MessageDigest digest = MessageDigest.getInstance(codec);
            BigInteger bigInt = new BigInteger(1, digest.digest(data));
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException err) {
            throw new IllegalArgumentException("Unknown hash type: " + codec, err);
        }
    }

    public static String generateHexHash(String string, String codec) {
        return generateHexHash(string.getBytes(), codec);
    }

    public static String generateHexHash(int integer, String codec) {
        return generateHexHash(getBytes(integer), codec);
    }

    private static byte[] getBytes(int integer) {
        ByteArrayOutputStream baos = null;
        DataOutputStream dataOut = null;
        try {
            baos = new ByteArrayOutputStream();
            dataOut = new DataOutputStream(baos);
            dataOut.writeInt(integer);
            dataOut.flush();
            return baos.toByteArray();
        } catch (IOException err) {
            throw new RuntimeException(err);
        } finally {
            StreamUtil.close(dataOut);
        }
    }
}
