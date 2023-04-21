package demo.examples;

import iaik.me.security.Cipher;
import iaik.me.security.CryptoBag;
import iaik.me.security.rsa.RSAKeyPairGenerator;

public class TestEncrypt {

    public TestEncrypt() {
        System.out.println("Original text:This is a RSA encryption/decryption test...");
        try {
            RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
            rsaKeyPairGenerator.initialize(2048, null, null);
            CryptoBag cryptoBag = rsaKeyPairGenerator.generateKeyPair();
            Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsa.init(Cipher.ENCRYPT_MODE, cryptoBag.getCryptoBag(CryptoBag.V_KEY_PUBLIC), null, null);
            byte[] data = "This is a RSA encryption/decryption test...".getBytes();
            byte[] crypted = rsa.doFinal(data);
            System.out.println("Encrypted text:" + new String(crypted));
            rsa.init(Cipher.DECRYPT_MODE, cryptoBag.getCryptoBag(CryptoBag.V_KEY_PRIVATE), null, null);
            byte[] zu = rsa.doFinal(crypted);
            System.out.println("Decrypted text:" + new String(zu));
        } catch (Exception z) {
            System.out.println(z);
        }
    }

    public static void main(String args[]) {
        new TestEncrypt();
    }
}
