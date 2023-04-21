package sasc.util;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;

public class RSAKeyGenerator {

    public static class RSAKeyPair {

        private RSAPublicKey publicKey;

        private RSAPrivateKey privateKey;

        RSAKeyPair(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public RSAPublicKey getPublicKey() {
            return publicKey;
        }

        public RSAPrivateKey getPrivateKey() {
            return privateKey;
        }
    }

    public static RSAKeyPair generateRSAKeys(int numBits, int exponent) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        AlgorithmParameterSpec spec = new RSAKeyGenParameterSpec(numBits, BigInteger.valueOf(exponent));
        keyGen.initialize(spec);
        KeyPair keyPair = keyGen.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        RSAPrivateKey rsaPrivKey = (RSAPrivateKey) privateKey;
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
        return new RSAKeyPair(rsaPublicKey, rsaPrivKey);
    }

    private static void dump(RSAKeyPair rsaKeyPair) {
        RSAPrivateKey rsaPrivKey = rsaKeyPair.getPrivateKey();
        RSAPublicKey rsaPublicKey = rsaKeyPair.getPublicKey();
        System.out.println(rsaPrivKey);
        System.out.println(rsaPublicKey);
        System.out.println("\nRSA Key Shared Modulus Hex:");
        System.out.println(Util.prettyPrintHex(rsaPrivKey.getModulus()));
        System.out.println("\nRSA Private Key Exponent Hex:");
        System.out.println(Util.prettyPrintHex(rsaPrivKey.getPrivateExponent()));
        System.out.println("\nRSA Public Key Exponent Hex:");
        System.out.println(Util.prettyPrintHex(rsaPublicKey.getPublicExponent()));
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        dump(generateRSAKeys(1152, 3));
    }
}
