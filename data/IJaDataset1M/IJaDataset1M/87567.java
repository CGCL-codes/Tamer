package org.orbeon.oxf.util;

import org.orbeon.oxf.common.OXFException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.server.UID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UUIDUtils {

    /**
     * Return a String that looks like a 128-bit UUID. It really does not follow the spec as to the
     * meaning of the different elements.
     *
     * http://www.opengroup.org/onlinepubs/9629399/apdxa.htm
     */
    public static String createPseudoUUID() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(new UID().toString().getBytes());
            try {
                String localHost = InetAddress.getLocalHost().toString();
                messageDigest.update(localHost.getBytes());
            } catch (UnknownHostException e) {
                throw new OXFException(e);
            }
            byte[] digestBytes = messageDigest.digest();
            StringBuffer sb = new StringBuffer();
            sb.append(toHexString(NumberUtils.readIntBigEndian(digestBytes, 0)));
            sb.append('-');
            sb.append(toHexString(NumberUtils.readShortBigEndian(digestBytes, 4)));
            sb.append('-');
            sb.append(toHexString(NumberUtils.readShortBigEndian(digestBytes, 6)));
            sb.append('-');
            sb.append(toHexString(NumberUtils.readShortBigEndian(digestBytes, 8)));
            sb.append('-');
            sb.append(toHexString(NumberUtils.readShortBigEndian(digestBytes, 10)));
            sb.append(toHexString(NumberUtils.readIntBigEndian(digestBytes, 12)));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new OXFException(e);
        }
    }

    private static String toHexString(int i) {
        final String zeroes = "0000000";
        String s = Integer.toHexString(i).toUpperCase();
        if (s.length() < 8) return zeroes.substring(s.length() - 1) + s; else return s;
    }

    private static String toHexString(short i) {
        return toHexString((int) i).substring(4);
    }
}
