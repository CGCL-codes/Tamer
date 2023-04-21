package com.sshtools.j2ssh.openssh;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 *
 *
 * @author $author$
 * @version $Revision: 1.13 $
 */
public class PEM {

    /**  */
    public static final String DSA_PRIVATE_KEY = "DSA PRIVATE KEY";

    /**  */
    public static final String RSA_PRIVATE_KEY = "RSA PRIVATE KEY";

    /**  */
    protected static final String PEM_BOUNDARY = "-----";

    /**  */
    protected static final String PEM_BEGIN = PEM_BOUNDARY + "BEGIN ";

    /**  */
    protected static final String PEM_END = PEM_BOUNDARY + "END ";

    /**  */
    protected static final int MAX_LINE_LENGTH = 75;

    /**  */
    protected static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    private static final int MD5_HASH_BYTES = 0x10;

    /**
     *
     *
     * @param passphrase
     * @param iv
     * @param keySize
     *
     * @return
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws Error
     */
    protected static SecretKey getKeyFromPassphrase(String passphrase, byte[] iv, int keySize) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] passphraseBytes;
        try {
            passphraseBytes = passphrase.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new Error("Mandatory US-ASCII character encoding is not supported by the VM");
        }
        MessageDigest hash = MessageDigest.getInstance("MD5");
        byte[] key = new byte[keySize];
        int hashesSize = keySize & 0xfffffff0;
        if ((keySize & 0xf) != 0) {
            hashesSize += MD5_HASH_BYTES;
        }
        byte[] hashes = new byte[hashesSize];
        byte[] previous;
        for (int index = 0; (index + MD5_HASH_BYTES) <= hashes.length; hash.update(previous, 0, previous.length)) {
            hash.update(passphraseBytes, 0, passphraseBytes.length);
            hash.update(iv, 0, iv.length);
            previous = hash.digest();
            System.arraycopy(previous, 0, hashes, index, previous.length);
            index += previous.length;
        }
        System.arraycopy(hashes, 0, key, 0, key.length);
        return new SecretKeySpec(key, "DESede");
    }
}
