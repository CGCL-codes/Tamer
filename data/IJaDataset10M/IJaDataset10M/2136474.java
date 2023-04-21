package net.sf.mailsomething.auth;

import java.security.*;
import javax.crypto.*;

public class SimpleExample {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java SimpleExample text");
            System.exit(1);
        }
        String text = args[0];
        System.out.println("Generating a DESede (TripleDES) key ... ");
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
        keyGenerator.init(168);
        Key key = keyGenerator.generateKey();
        System.out.println("Done generating the key.");
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        ;
        byte[] plaintext = text.getBytes("UTF8");
        System.out.println("\nPlaintext: ");
        for (int i = 0; i < plaintext.length; i++) {
            System.out.println(plaintext + " ");
        }
        byte[] ciphertext = cipher.doFinal(plaintext);
        System.out.println("\n\nCiphertext: " + new String(ciphertext));
        for (int i = 0; i < ciphertext.length; i++) {
            System.out.println(ciphertext[i] + " ");
        }
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedText = cipher.doFinal(ciphertext);
        String output = new String(decryptedText, "UTF8");
        System.out.println("\n\nDecrypted Text: '" + output);
    }
}
