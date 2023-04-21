package com.primianotucci.smartcard.openpgp;

import java.security.SecureRandom;

/**
 * Random bytes PKCS1 padding utility class
 * @author Primiano Tucci - http://www.primianotucci.com/
 */
public class PKCS1Padding {

    private static SecureRandom rnd = new SecureRandom();

    /**
     * Get PKCS1 padded data
     * @param iData input data
     * @param iLen desidered output length
     * @return PKCS1 padded data
     */
    public static byte[] getPadding(byte[] iData, int iLen) {
        byte[] outBuf = new byte[iLen];
        outBuf[0] = 0x00;
        outBuf[1] = 0x01;
        int rndLen = iLen - 3 - iData.length;
        byte[] rndBytes = new byte[rndLen];
        rnd.nextBytes(rndBytes);
        for (int i = 0; i < rndLen; i++) outBuf[i + 2] = rndBytes[i];
        outBuf[rndLen + 2] = 0x00;
        for (int i = 0; i < iData.length; i++) outBuf[rndLen + 3 + i] = iData[i];
        return outBuf;
    }
}
