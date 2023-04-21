package org.dmp.chillout.auxiliary.security.engines;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author mlt
 *
 */
public class HashEngine {

    private static HashEngine HashEngine = null;

    /**
	 * Constructor. 	  
	 */
    private HashEngine() {
    }

    /**
	 * get an instance of class HashEngine		 
	 * @return
	 */
    public static HashEngine getInstance() {
        if (HashEngine == null) HashEngine = new HashEngine();
        return HashEngine;
    }

    /**
	 * Generate hash value for the data using the given algorithm
	 * @param data - data to be hashed
	 * @param algorithm - algorithm used to hash the data
	 * @return hash value
	 * @throws NoSuchAlgorithmException 
	 */
    public byte[] generateHash(byte[] data, String algorithm) throws NoSuchAlgorithmException {
        byte[] hashValue;
        MessageDigest hash = MessageDigest.getInstance(algorithm);
        hash.update(data);
        hashValue = hash.digest();
        return hashValue;
    }

    /**
	 * Verify the hash value for the data using the given algorithm
	 * @param data - data which has the given hash value
	 * @param hashValue - hash value for the given data
	 * @param algorithm - algorithm used to hash the data
	 * @return result of verification
	 * @throws NoSuchAlgorithmException 
	 */
    public boolean verifyHash(byte[] data, byte[] hashValue, String algorithm) throws NoSuchAlgorithmException {
        byte[] hashValueComp;
        MessageDigest hash = MessageDigest.getInstance(algorithm);
        hash.update(data);
        hashValueComp = hash.digest();
        int len1, len2, i = 0;
        len1 = hashValue.length;
        len2 = hashValueComp.length;
        if (len1 == len2) {
            for (i = 0; i < len1; i++) {
                if (hashValue[i] != hashValueComp[i]) break;
            }
        }
        if (i == len1) return true; else return false;
    }
}
