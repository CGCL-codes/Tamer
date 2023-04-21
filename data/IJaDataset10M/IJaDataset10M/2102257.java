package rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class RSA {

    private static final BigInteger one = new BigInteger("1");

    private static final SecureRandom random = new SecureRandom();

    private BigInteger privateKey;

    private BigInteger publicKey;

    private BigInteger modulus;

    RSA(int N) {
        BigInteger p = BigInteger.probablePrime(N / 2, random);
        BigInteger q = BigInteger.probablePrime(N / 2, random);
        BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));
        modulus = p.multiply(q);
        publicKey = new BigInteger("65537");
        privateKey = publicKey.modInverse(phi);
    }

    BigInteger encrypt(BigInteger message) {
        return message.modPow(publicKey, modulus);
    }

    BigInteger decrypt(BigInteger encrypted) {
        return encrypted.modPow(privateKey, modulus);
    }

    public String toString() {
        String s = "";
        s += "public exponent\t\t= " + publicKey + "\n";
        s += "private exponent\t= " + privateKey + "\n";
        s += "modulus\t\t\t= " + modulus;
        return s;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Key size? ");
        int N = sc.nextInt();
        RSA key = new RSA(N);
        System.out.println(key);
        BigInteger message = new BigInteger(N - 1, random);
        BigInteger encrypt = key.encrypt(message);
        BigInteger decrypt = key.decrypt(encrypt);
        System.out.println("message\t\t\t= " + message);
        System.out.println("encrypted\t\t= " + encrypt);
        System.out.println("decrypted\t\t= " + decrypt);
    }
}
