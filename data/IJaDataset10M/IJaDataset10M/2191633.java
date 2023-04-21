package com.jxva.entity;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2009-01-20 11:50:06 by Jxva
 */
public class DES {

    private String key;

    private String iv;

    private String encoding;

    public DES() {
        this("jxva_des", "jxva_IvS");
    }

    public DES(String key, String iv, String encoding) {
        this(key, iv);
        this.encoding = encoding;
    }

    public DES(String key, String iv) {
        this.key = key;
        this.iv = iv;
        this.encoding = Encoding.UTF_8;
    }

    /**
	 * 
	 * @param str
	 * @return
	 * @throws EntityException
	 */
    public String encrypt(String str) throws EntityException {
        try {
            byte[] b = createCipher(true).doFinal(str.getBytes(encoding));
            return Base64.encodeBytes(b);
        } catch (Exception e) {
            throw new EntityException(e);
        }
    }

    /**
	 * 
	 * @param str
	 * @return
	 * @throws EntityException
	 */
    public String decrypt(String str) throws EntityException {
        try {
            byte[] bytesrc = Base64.decodeBytes(str);
            byte[] retByte = createCipher(false).doFinal(bytesrc);
            return new String(retByte);
        } catch (Exception e) {
            throw new EntityException(e);
        }
    }

    private Cipher createCipher(boolean isEncrypt) throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(encoding));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes(encoding));
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKey, ips);
        return cipher;
    }

    public static void main(String[] args) {
        DES des = new DES();
        String e = des.encrypt("jxvaframework");
        System.out.println(e);
        System.out.println(des.decrypt(e));
    }
}
