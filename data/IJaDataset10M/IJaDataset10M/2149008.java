package com.light.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/** 
 * Usage: OctaneUniqueID.getUniqueId())
 * 
 * @author Berlin Brown
 */
public class LightUniqueID {

    private static final String toHexString(byte[] bytes) {
        char[] ret = new char[bytes.length * 2];
        for (int i = 0, j = 0; i < bytes.length; i++) {
            int c = (int) bytes[i];
            if (c < 0) {
                c += 0x100;
            }
            ret[j++] = Character.forDigit(c / 0x10, 0x10);
            ret[j++] = Character.forDigit(c % 0x10, 0x10);
        }
        return new String(ret);
    }

    public static final String getUniqueId() {
        String digest = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String timeVal = "" + (System.currentTimeMillis() + 1);
            String localHost = "";
            try {
                localHost = InetAddress.getLocalHost().toString();
            } catch (UnknownHostException e) {
                throw new RuntimeException("Error trying to get localhost" + e.getMessage());
            }
            String randVal = "" + new Random().nextInt();
            String val = timeVal + localHost + randVal;
            md.reset();
            md.update(val.getBytes());
            digest = toHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException : " + e.getMessage());
        }
        return digest;
    }
}
