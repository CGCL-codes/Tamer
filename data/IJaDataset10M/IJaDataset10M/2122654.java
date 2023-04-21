package org.tcpfile.crypto;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class RSAPublicKey implements Comparable<RSAPublicKey> {

    private String publicKey;

    private int radix;

    private transient PublicKey pk;

    public RSAPublicKey() {
    }

    public RSAPublicKey(String publicKey) {
        super();
        this.publicKey = publicKey;
        if (publicKey.matches("[0-9]*")) {
            radix = 10;
            convertRadix(36);
        } else radix = Character.MAX_RADIX;
        assert (publicKey.matches("[0-9]*") || radix != 10);
        assert (!toString().equals(""));
    }

    public RSAPublicKey(String publicKey, int radix) {
        super();
        this.publicKey = publicKey;
        this.radix = radix;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public int getRadix() {
        return radix;
    }

    public void setRadix(int radix) {
        this.radix = radix;
    }

    public byte[] encrypt(byte[] in) {
        assert (in != null);
        return RSA.encrypt(in, getPublicKeyInt(), true);
    }

    private PublicKey getPublicKeyInt() {
        if (pk != null) return pk;
        PublicKey pub_key;
        try {
            KeyFactory rsakf = KeyFactory.getInstance(RSA.KEYGENALGORITHM);
            pub_key = rsakf.generatePublic(new java.security.spec.RSAPublicKeySpec(new BigInteger(publicKey, radix), RSA.EXPONENT));
            pk = pub_key;
            return pub_key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Key generation may not fail!");
    }

    public String toString() {
        if (radix == 10) {
            assert (publicKey.matches("[0-9]*"));
            convertRadix(36);
        }
        return publicKey;
    }

    public String toString(int radix) {
        return new BigInteger(publicKey, this.radix).toString(radix);
    }

    private void convertRadix(int newradix) {
        publicKey = new BigInteger(publicKey, radix).toString(newradix);
        radix = newradix;
    }

    public boolean equals(Object obj) {
        if (obj instanceof RSAPublicKey) {
            RSAPublicKey other = (RSAPublicKey) obj;
            return other.toString().equals(toString());
        }
        if (obj instanceof String) {
            String other = (String) obj;
            return toString().equals(new RSAPublicKey(other).toString());
        }
        return false;
    }

    public int compareTo(RSAPublicKey o) {
        return this.toString().compareTo(o.toString());
    }
}
