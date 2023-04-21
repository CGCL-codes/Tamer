package com.google.code.cana.suporte;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class that takes a plain-text user supplied password, and applies a one-way
 * message digest algorithm to generate an encrypted password that will be
 * compared against an already encrypted password record, most likely held in a
 * database. This class will typically be used by a servlet or struts action
 * class that needs to enforce programmatic security.
 * 
 * @author Andrew Murphy
 */
public class PasswordCript {

    public static final String MESSAGEDIGEST_SHA = "SHA";

    public static final String MESSAGEDIGEST_MD2 = "MD2";

    public static final String MESSAGEDIGEST_MD5 = "MD5";

    private static PasswordCript instance;

    /** Private modifier to prevent instantiation */
    private PasswordCript() {
    }

    /**
	 * Encrypts the supplied plaintext password with the default message digest
	 * MD5 algorithm.
	 */
    public synchronized String getEncryptedPassword(String plaintext) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return getEncryptedPassword(plaintext, MESSAGEDIGEST_MD5);
    }

    /**
	 * Encrypts the supplied plaintext password with the supplied algorithm.
	 */
    public synchronized String getEncryptedPassword(String plaintext, String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
            md.update(plaintext.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException nsae) {
            throw nsae;
        } catch (UnsupportedEncodingException uee) {
            throw uee;
        }
        return (new BigInteger(1, md.digest())).toString(16);
    }

    /**
	 * Utilises the Singleton pattern as there is no need to create separate
	 * instances
	 */
    public static synchronized PasswordCript getInstance() {
        if (instance == null) instance = new PasswordCript();
        return instance;
    }
}
