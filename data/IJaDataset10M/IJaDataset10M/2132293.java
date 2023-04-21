package org.apache.harmony.crypto.tests.javax.crypto.func;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

public class CipherSymmetricKeyThread extends CipherThread {

    CipherSymmetricKeyThread(String name, int[] keys, String[] modes, String[] paddings) {
        super(name, keys, modes, paddings);
    }

    @Override
    public void crypt() throws Exception {
        byte[] output = new byte[128];
        byte[] decrypted = new byte[128];
        byte[] input = getData().getBytes();
        SecureRandom sr = new SecureRandom();
        byte[] iv = null;
        int outputSize = 0;
        KeyGenerator kg = KeyGenerator.getInstance(getAlgName());
        kg.init(getKeyLength(), new SecureRandom());
        Key key = kg.generateKey();
        Cipher cip = Cipher.getInstance(getAlgName() + "/" + getMode() + "/" + getPadding());
        if (getAlgName() != "AES") {
            iv = new byte[8];
        } else {
            iv = new byte[16];
        }
        sr.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        if (getMode() != "ECB") {
            cip.init(Cipher.ENCRYPT_MODE, key, ivspec);
            cip.doFinal(input, 0, input.length, output);
            outputSize = cip.getOutputSize(input.length);
            iv = cip.getIV();
            ivspec = new IvParameterSpec(iv);
            cip.init(Cipher.DECRYPT_MODE, key, ivspec);
            cip.doFinal(output, 0, outputSize, decrypted);
        } else {
            cip.init(Cipher.ENCRYPT_MODE, key);
            cip.doFinal(input, 0, input.length, output);
            outputSize = cip.getOutputSize(input.length);
            cip.init(Cipher.DECRYPT_MODE, key);
            cip.doFinal(output, 0, outputSize, decrypted);
        }
        checkEncodedData(getData().getBytes(), decrypted);
    }
}
