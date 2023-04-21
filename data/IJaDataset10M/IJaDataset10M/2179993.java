package com.emc.esu.api;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.concord.security.ccjce.cryptix.jce.provider.CryptixCrypto;

/**
 * The checksum class is used to store and compute partial checksums when uploading
 * files.
 */
public class Checksum {

    private static final Logger l4j = Logger.getLogger(Checksum.class);

    static {
        Security.addProvider(new CryptixCrypto());
    }

    /**
	 * The hash algorithm to use.  As of 1.4.0, only SHA0 is supported
	 */
    public static enum Algorithm {

        SHA0, SHA1, MD5
    }

    ;

    /**
     * The intended behavior of this checksum (should we calculate first and send to the server to verify? or should
     * we have the server generate a checksum for the request and return it to us to verify locally? or both?)
     */
    public static enum Behavior {

        SEND_TO_SERVER, GET_FROM_SERVER, BOTH
    }

    ;

    private MessageDigest digest;

    private Algorithm alg;

    private Behavior behavior;

    private long offset;

    private String expectedValue;

    private String serverValue;

    public Checksum(Algorithm alg) throws NoSuchAlgorithmException {
        this(alg, Behavior.SEND_TO_SERVER);
    }

    public Checksum(Algorithm alg, Behavior behavior) throws NoSuchAlgorithmException {
        switch(alg) {
            case SHA0:
                digest = MessageDigest.getInstance("SHA-0");
                break;
            case SHA1:
                digest = MessageDigest.getInstance("SHA-1");
                break;
            case MD5:
                digest = MessageDigest.getInstance("MD5");
                break;
        }
        this.alg = alg;
        this.behavior = behavior;
        offset = 0;
    }

    public String getAlgorithmName() {
        switch(alg) {
            case SHA0:
                return "SHA0";
            case SHA1:
                return "SHA1";
            case MD5:
                return "MD5";
        }
        throw new RuntimeException("Unknown algorithm: " + alg);
    }

    public Behavior getBehavior() {
        return this.behavior;
    }

    public boolean isSendToServer() {
        return this.behavior == Behavior.SEND_TO_SERVER || this.behavior == Behavior.BOTH;
    }

    public boolean isGetFromServer() {
        return this.behavior == Behavior.GET_FROM_SERVER || this.behavior == Behavior.BOTH;
    }

    /**
	 * Updates the checksum with the given buffer's contents
	 * @param buffer data to update
	 * @param offset start in buffer
	 * @param length number of bytes to use from buffer starting at offset
	 */
    public void update(byte[] buffer, int offset, int length) {
        digest.update(buffer, offset, length);
        this.offset += length;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /**
	 * Outputs the current digest checksum in a format
	 * suitable for including in Atmos create/update calls.
	 */
    public String toString(boolean includeOffset) {
        String checksumData = getAlgorithmName();
        if (includeOffset) checksumData += "/" + offset;
        checksumData += "/" + getHashValue();
        l4j.debug("Checksum Value: '" + checksumData + "'");
        return checksumData;
    }

    private String getHashValue() {
        MessageDigest tmpDigest;
        try {
            tmpDigest = (MessageDigest) digest.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone failed", e);
        }
        byte[] currDigest = tmpDigest.digest();
        BigInteger bigInt = new BigInteger(1, currDigest);
        return String.format("%0" + (currDigest.length << 1) + "x", bigInt);
    }

    /**
	 * Sets the expected value for this checksum.  Only used for read operations
	 * @param expectedValue the expectedValue to set
	 */
    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    /**
	 * Gets the expected value for this checksum.  Only used for read operations.
	 * @return the expectedValue
	 */
    public String getExpectedValue() {
        return expectedValue;
    }

    /**
     * Sets the server-calculated checksum for a request.
     * @param serverValue the server-calculated checksum for a request
     */
    public void setServerValue(String serverValue) {
        this.serverValue = serverValue;
    }

    /**
     * Gets the server-calculated checksum for a request.
     * @return the checksum of a request as calculated on the server
     */
    public String getServerValue() {
        return this.serverValue;
    }
}
