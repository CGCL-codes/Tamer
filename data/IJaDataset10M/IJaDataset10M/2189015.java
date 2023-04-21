package es.realtimesystems.simplemulticast;

import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import iaik.security.cipher.PBEKeyBMP;
import java.security.Security;
import java.security.NoSuchAlgorithmException;
import iaik.security.provider.IAIK;

public class cripto1 {

    private Cipher cipher = null;

    private FileInputStream fileInputStream = null;

    private FileOutputStream fileOutputStream = null;

    PBEKeySpec pbeKeySpec;

    PBEParameterSpec pbeParamSpec;

    SecretKeyFactory keyFac;

    public cripto1(String sFileName) throws NoSuchPaddingException, NoSuchAlgorithmException, java.security.spec.InvalidKeySpecException, java.security.InvalidAlgorithmParameterException, javax.crypto.BadPaddingException, java.security.InvalidKeyException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchProviderException, java.io.IOException {
        IAIK.addAsProvider(true);
        byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };
        int count = 20;
        pbeKeySpec = new PBEKeySpec("pepe".toCharArray());
        Log.log("DEPU 1", "");
        Cipher pbeCipher = Cipher.getInstance("RC2/ECB/PKCS5Padding");
        Cipher pbeUNCipher = Cipher.getInstance("RC2/ECB/PKCS5Padding");
        Log.log("DEPU 4", "");
        keyFac = SecretKeyFactory.getInstance("PBE", "IAIK");
        Log.log("DEPU 2", "");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        Log.log("DEPU 3", "");
        long lTiempoInicial = System.currentTimeMillis();
        pbeParamSpec = new PBEParameterSpec(salt, count);
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
        pbeUNCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
        Log.log("DEPU 5", "");
        long lTiempoFin = System.currentTimeMillis();
        Log.log("TIEMPO INIT: " + (lTiempoFin - lTiempoInicial), "");
        byte[] cleartext = "HOLA PEPE, HOLA PEPE, HOLA PEPE".getBytes();
        lTiempoInicial = System.currentTimeMillis();
        byte[] ciphertext1 = pbeCipher.doFinal(cleartext);
        lTiempoFin = System.currentTimeMillis();
        Log.log("TIEMPO ENCRIPTAR: " + (lTiempoFin - lTiempoInicial), "");
        byte[] ciphertext2 = pbeCipher.doFinal(cleartext);
        byte[] ciphertext3 = pbeCipher.doFinal(cleartext);
        byte[] ciphertext4 = pbeCipher.doFinal(cleartext);
        Log.log("1. Texto cifrado:", "" + new String(ciphertext1));
        Log.log("2. Texto cifrado:", "" + new String(ciphertext2));
        Log.log("3. Texto cifrado:", "" + new String(ciphertext3));
        Log.log("4. Texto cifrado:", "" + new String(ciphertext4));
        Log.log("3. Texto descifr:", "" + new String(pbeUNCipher.doFinal(ciphertext3)));
        Log.log("1. Texto descifr:", "" + new String(pbeUNCipher.doFinal(ciphertext1)));
        Log.log("2. Texto descifr:", "" + new String(pbeUNCipher.doFinal(ciphertext2)));
        Log.log("4. Texto descifr:", "" + new String(pbeUNCipher.doFinal(ciphertext4)));
    }

    public static void main(String[] args) {
        try {
            new cripto1(null);
        } catch (Exception e) {
            Log.log("" + e, "");
        }
    }
}
