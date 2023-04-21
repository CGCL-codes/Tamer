package javabouncycastle.security;

import java.util.Random;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.bouncycastle.crypto.prng.DigestRandomGenerator;

/**
 * An implementation of SecureRandom specifically for the light-weight API, JDK
 * 1.0, and the J2ME. Random generation is based on the traditional SHA1 with
 * counter. Calling setSeed will always increase the entropy of the hash.
 */
public class SecureRandom extends java.util.Random {

    private static SecureRandom rand = new SecureRandom(new DigestRandomGenerator(new SHA1Digest()));

    protected RandomGenerator generator;

    public SecureRandom() {
        super(0);
        this.generator = new DigestRandomGenerator(new SHA1Digest());
        setSeed(System.currentTimeMillis());
    }

    public SecureRandom(byte[] inSeed) {
        super(0);
        this.generator = new DigestRandomGenerator(new SHA1Digest());
        setSeed(inSeed);
    }

    protected SecureRandom(RandomGenerator generator) {
        super(0);
        this.generator = generator;
    }

    public static SecureRandom getInstance(String algorithm) {
        if (algorithm.equals("SHA1PRNG")) {
            return new SecureRandom(new DigestRandomGenerator(new SHA1Digest()));
        }
        if (algorithm.equals("SHA256PRNG")) {
            return new SecureRandom(new DigestRandomGenerator(new SHA256Digest()));
        }
        return new SecureRandom();
    }

    public static SecureRandom getInstance(String algorithm, String provider) {
        return getInstance(algorithm);
    }

    public static byte[] getSeed(int numBytes) {
        byte[] rv = new byte[numBytes];
        rand.setSeed(System.currentTimeMillis());
        rand.nextBytes(rv);
        return rv;
    }

    public byte[] generateSeed(int numBytes) {
        byte[] rv = new byte[numBytes];
        nextBytes(rv);
        return rv;
    }

    public void setSeed(byte[] inSeed) {
        generator.addSeedMaterial(inSeed);
    }

    public void nextBytes(byte[] bytes) {
        generator.nextBytes(bytes);
    }

    public void setSeed(long rSeed) {
        if (rSeed != 0) {
            generator.addSeedMaterial(rSeed);
        }
    }

    public int nextInt() {
        byte[] intBytes = new byte[4];
        nextBytes(intBytes);
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) + (intBytes[i] & 0xff);
        }
        return result;
    }

    protected final int next(int numBits) {
        int size = (numBits + 7) / 8;
        byte[] bytes = new byte[size];
        nextBytes(bytes);
        int result = 0;
        for (int i = 0; i < size; i++) {
            result = (result << 8) + (bytes[i] & 0xff);
        }
        return result & ((1 << numBits) - 1);
    }
}
