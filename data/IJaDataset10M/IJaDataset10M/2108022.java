package com.jxva.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 and HMAC encrypt algorithm implement
 * 
 * @author The Jxva Framework
 * @since 1.0
 * @version 2008-12-31 09:53:11 by Jxva
 */
public abstract class MD5 {

    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static byte[] ipod;

    private static byte[] opod;

    static {
        ipod = new byte[64];
        opod = new byte[64];
        for (int i = 0; i < 64; i++) {
            ipod[i] = 0x36;
            opod[i] = 0x5c;
        }
    }

    /**
	 * 
	 * @param str
	 * @return
	 * @throws EntityException
	 */
    public static String encrypt(String str) throws EntityException {
        try {
            byte[] strTemp = str.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                chars[k++] = DIGITS[byte0 >>> 4 & 0xf];
                chars[k++] = DIGITS[byte0 & 0xf];
            }
            return new String(chars);
        } catch (NoSuchAlgorithmException e) {
            throw new EntityException(e);
        }
    }

    /**
	 * 
	 * @param str
	 * @param key
	 * @return
	 * @throws EntityException
	 */
    public static String hmac(String str, String key) throws EntityException {
        try {
            byte[] keyData = key.getBytes();
            byte[] ki = new byte[64];
            byte[] ko = new byte[64];
            int keyLen = keyData.length;
            for (int i = 0; i < 64; i++) {
                if (i < keyLen) {
                    ki[i] = (byte) (keyData[i] ^ ipod[i]);
                    ko[i] = (byte) (keyData[i] ^ opod[i]);
                } else {
                    ki[i] = ipod[i];
                    ko[i] = opod[i];
                }
            }
            byte[] textData = str.getBytes();
            byte[] h1Data = new byte[64 + textData.length];
            System.arraycopy(ki, 0, h1Data, 0, 64);
            System.arraycopy(textData, 0, h1Data, 64, textData.length);
            byte[] h1Result = md5(h1Data);
            byte[] h2Data = new byte[64 + h1Result.length];
            System.arraycopy(ko, 0, h2Data, 0, 64);
            System.arraycopy(h1Result, 0, h2Data, 64, h1Result.length);
            byte[] result = md5(h2Data);
            return new String(encodeHex(result));
        } catch (NoSuchAlgorithmException e) {
            throw new EntityException(e);
        }
    }

    /**
	 * 
	 * @param file
	 * @return
	 * @throws EntityException
	 */
    public static String verifyCode(File file) throws EntityException {
        try {
            FileInputStream in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md = getMd5Digest();
            md.update(byteBuffer);
            return bufferToHex(md.digest());
        } catch (IOException e) {
            throw new EntityException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new EntityException(e);
        }
    }

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = DIGITS[(bt & 0xf0) >> 4];
        char c1 = DIGITS[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    private static MessageDigest getMd5Digest() throws NoSuchAlgorithmException {
        MessageDigest md5MessageDigest = MessageDigest.getInstance("MD5");
        md5MessageDigest.reset();
        return md5MessageDigest;
    }

    private static byte[] md5(byte[] bytes) throws NoSuchAlgorithmException {
        return getMd5Digest().digest(bytes);
    }

    public static void main(String[] args) {
        String s = "jxva framework";
        String key = "admin";
        String encode = encrypt(s);
        System.out.println("    Original: " + s);
        System.out.println(" MD5 Encoded: " + encode);
        System.out.println("    Hmac Key: " + key);
        System.out.println("Hmac Encoded: " + hmac(s, key));
    }
}
