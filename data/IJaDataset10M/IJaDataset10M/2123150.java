package org.wwweeeportal.util.security;

import java.io.*;
import java.math.*;
import java.security.*;

/**
 * An {@link OutputStream} implementation for easy writing to a {@link MessageDigest}.
 */
public class MessageDigestOutputStream extends OutputStream {

    /**
   * The {@link MessageDigest} output should be targeted to.
   */
    protected final MessageDigest messageDigest;

    /**
   * Construct a new <code>MessageDigestOutputStream</code> wrapping the supplied <code>messageDigest</code>.
   * 
   * @param messageDigest The {@link MessageDigest} output should be targeted to.
   * @throws IllegalArgumentException If <code>messageDigest</code> was <code>null</code>.
   */
    public MessageDigestOutputStream(final MessageDigest messageDigest) throws IllegalArgumentException {
        if (messageDigest == null) throw new IllegalArgumentException("null messageDigest");
        this.messageDigest = messageDigest;
        return;
    }

    /**
   * Get the {@link MessageDigest} wrapped by this OutputStream.
   * 
   * @return The wrapped {@link MessageDigest}.
   */
    public MessageDigest getMessageDigest() {
        return messageDigest;
    }

    @Override
    public void write(byte[] b) throws IOException {
        messageDigest.update(b);
        return;
    }

    @Override
    public void write(int b) throws IOException {
        messageDigest.update((byte) b);
        return;
    }

    /**
   * Return a hex encoded String containing the {@linkplain MessageDigest#digest() digested} data.
   * 
   * @return A hex String.
   */
    public String getDigestHexString() {
        return new BigInteger(1, messageDigest.digest()).toString(16);
    }
}
