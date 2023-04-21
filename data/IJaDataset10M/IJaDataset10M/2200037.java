package com.hcs.protocol.utils;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 * 
 * @author nianchun.li
 * @createTime 2011/5/4 16:50
 */
public class AESEncrypter {

    private static AESEncrypter aes = null;

    /** 约定密钥key */
    public static String KEY = "handkeyindextest";

    /**
	 * key数组
	 */
    private byte[][] keyDatas = { { -42, 35, 67, -86, 19, 29, -11, 84, 94, 111, 75, -104, 71, 46, 86, -21 }, { 19, -53, -7, 68, 24, -92, 10, -48, -49, 110, 57, 104, 17, 23, 43, 12 }, { 7, 24, 18, -6, 5, 7, -2, 12, 13, -23, -5, 28, -10, -65, 78, -33 }, { 24, 3, -8, -14, -19, -7, -22, -4, 99, -1, 87, -5, -17, -10, 14, -3 }, { 12, 1, 33, 22, -85, 36, -10, -43, -49, 53, 32, -14, 10, 2, 54, 9 }, { 11, 8, 4, -54, 31, -23, -98, 65, -83, 35, 28, -87, 110, -1, 98, 101 }, { -78, 59, 88, -76, 68, 92, -108, 42, -37, 25, -11, 6, 32, 15, -58, 50 }, { 108, 94, -69, -77, -2, 53, -43, 37, -45, -57, 33, -68, 73, 23, 24, -49 }, { 40, 18, 34, -67, 43, 65, -88, 101, 13, 37, 17, -11, 68, 1, 3, -9 }, { 84, 12, -7, -13, 22, 18, -54, 12, 24, 5, 8, -77, 52, 3, 62, -55 } };

    private AESEncrypter() {
    }

    /**
	 * 异步操作
	 * 
	 * @return
	 */
    public static synchronized AESEncrypter getInstance() {
        if (aes == null) {
            aes = new AESEncrypter();
        }
        return aes;
    }

    /**
	 * 执行加密
	 * 
	 * @param msg
	 *            需要加密的内容
	 * @param key
	 *            约定key
	 * @return 加密后的内容
	 */
    public byte[] encrypt(byte[] msg, String key) {
        byte[] result = null;
        try {
            Cipher cp = Cipher.getInstance("AES/ECB/NoPadding");
            cp.init(Cipher.ENCRYPT_MODE, this.getKey(key));
            result = cp.doFinal(paddingBytes(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
	 * 执行解密
	 * 
	 * @param msg
	 *            需要解密的内容
	 * @param key
	 *            约定key
	 * @return 解密后的内容
	 */
    public byte[] decrypt(byte[] msg, String key) {
        byte[] result = null;
        try {
            Cipher cp = Cipher.getInstance("AES/ECB/NoPadding");
            cp.init(Cipher.DECRYPT_MODE, this.getKey(key));
            result = cp.doFinal(msg);
            result = delUnwantedBytes(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
	 * 填充字节，如果字节数组的长度不是16的倍数，填充字节使返回的字节数组的长度是16的倍数， 填
	 * 充的字节里面存放数值0，
	 * 
	 * @param src
	 *            数据源
	 * @return 处理后的字节数组内容
	 */
    private byte[] paddingBytes(byte[] src) {
        if (src == null) return null;
        int destByteArrayLength = src.length;
        if (src.length % 16 != 0) {
            destByteArrayLength = (src.length / 16 + 1) * 16;
        }
        byte[] dest = new byte[destByteArrayLength];
        System.arraycopy(src, 0, dest, 0, src.length);
        return dest;
    }

    /**
	 * 删除多余的字节，如果字节数组的最后16个字节中，从最后一个字节开始，如果连续几个字节的数值
	 * 是0，则删除这几个字节
	 * 
	 * @param src
	 *            数据源
	 * @return 处理后的字节数组内容
	 */
    private byte[] delUnwantedBytes(byte[] src) {
        if (src == null) return null;
        int i = src.length - 1;
        for (; i > src.length - 16; i--) {
            if (src[i] != 0) break;
        }
        int destByteArrayLength = i + 1;
        byte[] dest = new byte[destByteArrayLength];
        System.arraycopy(src, 0, dest, 0, destByteArrayLength);
        return dest;
    }

    /**
	 * 获取加解密key
	 * 
	 * @param key
	 *            约定加解密key
	 * @return 加解密key
	 */
    private Key getKey(String key) {
        if (null == key || "".equals(key)) return null;
        try {
            return new SecretKeySpec(key.getBytes("utf-8"), "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String args[]) {
        try {
            byte[] msg = "mac=00-12-3F-CD-9D-76&corp=igrs&token=001258083087664".getBytes("utf-8");
            byte[] cMsg = AESEncrypter.getInstance().encrypt(msg, KEY);
            MsgPrint.showByteArray("cMsg", cMsg);
            byte[] pMsg = AESEncrypter.getInstance().decrypt(cMsg, KEY);
            MsgPrint.showMsg(new String(pMsg, "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
