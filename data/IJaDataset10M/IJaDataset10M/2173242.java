package edu.pitt.dbmi.marx.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class SecurityHelper {

    private byte[] salt = { (byte) 0xab, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xe3, (byte) 0x02, (byte) 0x99 };

    private int count = 20;

    private PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);

    char[] phrase = { 'c', 'a', 'r', 'd', 'i', 'f', 'f', 'm', 'a', 'r', 'x' };

    public char[] promptForPassword(InputStream in) throws IOException {
        char[] lineBuffer;
        char[] buf;
        buf = lineBuffer = new char[128];
        int room = buf.length;
        int offset = 0;
        int c;
        loop: while (true) {
            switch(c = in.read()) {
                case -1:
                case '\n':
                    break loop;
                case '\r':
                    int c2 = in.read();
                    if ((c2 != '\n') && (c2 != -1)) {
                        if (!(in instanceof PushbackInputStream)) {
                            in = new PushbackInputStream(in);
                        }
                        ((PushbackInputStream) in).unread(c2);
                    } else break loop;
                default:
                    if (--room < 0) {
                        buf = new char[offset + 128];
                        room = buf.length - offset - 1;
                        System.arraycopy(lineBuffer, 0, buf, 0, offset);
                        Arrays.fill(lineBuffer, ' ');
                        lineBuffer = buf;
                    }
                    buf[offset++] = (char) c;
                    break;
            }
        }
        if (offset == 0) {
            return null;
        }
        char[] ret = new char[offset];
        System.arraycopy(buf, 0, ret, 0, offset);
        Arrays.fill(buf, ' ');
        return ret;
    }

    private Cipher getCipher(int mode) throws GeneralSecurityException {
        PBEKeySpec pbeKeySpec;
        SecretKeyFactory keyFac;
        pbeKeySpec = new PBEKeySpec(phrase);
        keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(mode, pbeKey, pbeParamSpec);
        return pbeCipher;
    }

    public byte[] encryptIt(String clearText) throws GeneralSecurityException {
        Cipher pbeCipher = getCipher(Cipher.ENCRYPT_MODE);
        byte[] cipherText = pbeCipher.doFinal(clearText.getBytes());
        return cipherText;
    }

    public String decryptIt(byte[] cipherText) throws GeneralSecurityException {
        Cipher pbeCipher = getCipher(Cipher.DECRYPT_MODE);
        byte[] clearText = pbeCipher.doFinal(cipherText);
        return new String(clearText);
    }

    public static void main(String argv[]) throws GeneralSecurityException {
        String tester = "Test String";
        SecurityHelper helper = new SecurityHelper();
        byte[] bytes = helper.encryptIt(tester);
        String result = helper.decryptIt(bytes);
        if (tester.equals(result)) System.out.println("Test passed"); else System.out.println("Test failed");
    }
}
