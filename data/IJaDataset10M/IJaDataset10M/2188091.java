package com.oldportal.objectsbuilder.document;

/**
 * GUID (UUID) generated from random value.
 */
public class GUID {

    public GUID() {
        clear();
    }

    public GUID(final GUID src) {
        for (int i = 0; i < src.bytes.length; i++) bytes[i] = src.bytes[i];
    }

    byte bytes[] = new byte[16];

    static java.security.SecureRandom random = new java.security.SecureRandom();

    public void clear() {
        for (int i = 0; i < bytes.length; i++) bytes[i] = 0;
    }

    protected java.lang.Object clone() {
        return new GUID(this);
    }

    public boolean less(final GUID guid) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] < guid.bytes[i]) return true;
        }
        return false;
    }

    public static GUID fromString(String guidString) {
        GUID ret = new GUID();
        if (guidString.length() != ret.bytes.length * 2) return null;
        for (int i = 0; i < ret.bytes.length; i++) {
            String byteString = guidString.substring(0, 1);
            ret.bytes[i] = new Byte(byteString).byteValue();
        }
        return ret;
    }

    public static GUID generateNew() {
        GUID ret = new GUID();
        random.nextBytes(ret.bytes);
        return ret;
    }

    public String toString() {
        String ret = "";
        for (int i = 0; i < bytes.length; i++) {
            ret += Integer.toHexString(bytes[i]);
        }
        return ret;
    }

    public boolean equals(GUID guid) {
        for (int i = 0; i < bytes.length; i++) if (bytes[i] != guid.bytes[i]) return false;
        return true;
    }

    public boolean equals(java.lang.Object src) {
        if (src.getClass() != this.getClass()) return false;
        return equals((GUID) src);
    }
}
