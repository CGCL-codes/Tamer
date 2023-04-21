package com.hifiremote.jp1;

/**
 * The Class PLEncrypterDecrypter.
 */
public class PLEncrypterDecrypter extends EncrypterDecrypter {

    /**
   * Instantiates a new pL encrypter decrypter.
   * 
   * @param textParms the text parms
   */
    public PLEncrypterDecrypter(String textParms) {
    }

    public short encrypt(short val) {
        val &= 0xFF;
        if (val == 0x7D) return 0x4E;
        if (val == 0x7F) return 0xCE;
        int val1 = (val >> 2 | val << 6) + 111;
        val1 &= 0x00FF;
        int val2 = (val & 0x80) & (val << 7);
        int rc = (val1 ^ val2);
        return (short) rc;
    }

    public short decrypt(short val) {
        if (val == 0x4E) return 0x7D;
        if (val == 0xCE) return 0x7F;
        int val1 = (val + 145) & 0x00FF;
        int mask = (val1 << 1) & (val1 << 2) & 0x80;
        int val2 = val1 ^ mask;
        return (short) ((val2 << 2 | val2 >> 6) & 0xFF);
    }
}
