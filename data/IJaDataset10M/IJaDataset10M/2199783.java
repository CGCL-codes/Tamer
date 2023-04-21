package org.freeworld.medialauncher.model.access.hash;

import java.util.Arrays;

public class Sha1Hash implements Hash {

    private static final long serialVersionUID = 1L;

    private byte[] hash = null;

    public Sha1Hash() {
    }

    public Sha1Hash(byte[] hash) {
        setHashBytes(hash);
    }

    public String toString() {
        return "Sha1 [ " + getHexString(getHashBytes()) + " ]";
    }

    public static String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public void setHashBytes(byte[] hash) {
        this.hash = Arrays.copyOf(hash, hash.length);
    }

    public byte[] getHashBytes() {
        return Arrays.copyOf(hash, hash.length);
    }
}
