package com.bccapi.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * A Pseudo Random Number Generator based on SHA-256 which is wrapping
 * {@link SecureRandom}. This way we are certain that we use the same random
 * generator on all platforms, and can generate the same sequence of random
 * bytes from the same seed.
 */
public class PRNG extends SecureRandom {

    private static final long serialVersionUID = 5678497558585271430L;

    private byte[] _rootSeed;

    private byte[] _iterativeSeed;

    MessageDigest _digest;

    /**
    * Constructor based on an input seed.
    * 
    * @param seed
    *           The seed to use.
    * @throws NoSuchAlgorithmException
    */
    public PRNG(byte[] seed) throws NoSuchAlgorithmException {
        _rootSeed = seed;
        _iterativeSeed = new byte[32 - 1];
        _digest = MessageDigest.getInstance("SHA-256");
    }

    @Override
    public String getAlgorithm() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public synchronized void setSeed(byte[] seed) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void setSeed(long seed) {
    }

    @Override
    public synchronized void nextBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = nextByte();
        }
    }

    private byte nextByte() {
        _digest.update(_rootSeed);
        byte[] hash = _digest.digest(_iterativeSeed);
        _digest.reset();
        System.arraycopy(hash, 0, _iterativeSeed, 0, _iterativeSeed.length);
        return hash[hash.length - 1];
    }

    @Override
    public byte[] generateSeed(int numBytes) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public int nextInt() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public int nextInt(int n) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public long nextLong() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean nextBoolean() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public float nextFloat() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public double nextDouble() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public synchronized double nextGaussian() {
        throw new RuntimeException("Not supported");
    }
}
