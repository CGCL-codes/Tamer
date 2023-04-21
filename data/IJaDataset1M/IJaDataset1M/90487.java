package org.personalsmartspace.spm.identity.api;

import java.sql.Timestamp;
import java.util.Random;
import java.lang.Math;

public class Signature {

    private static final int SIZE_OF_LONG = 8;

    private static final int SIZE_OF_NUMBER_OF_INSTANCE_IN_TIME = 5;

    private static final char[] ENCODING_TABLE = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private boolean pseudo = false;

    private byte[] signature = null;

    public Signature(String signature) {
        char[] chars = signature.toCharArray();
        this.signature = new byte[(int) Math.round(Math.floor(((float) chars.length) / 2))];
        if (this.signature.length != SIZE_OF_LONG + SIZE_OF_NUMBER_OF_INSTANCE_IN_TIME) {
            this.pseudo = true;
            this.signature = signature.getBytes();
            return;
        }
        for (int i = 0; i < this.signature.length; i++) {
            this.signature[i] = (byte) (value(chars[2 * i]) * 0x10 + value(chars[2 * i + 1]));
        }
    }

    public Signature() {
        signature = generateSignature();
    }

    private int value(char c) {
        int i = 0;
        while (ENCODING_TABLE[i] != c) i++;
        if (i == ENCODING_TABLE.length) return 0;
        return i;
    }

    public String toString() {
        if (pseudo) return new String(signature);
        char[] chars = new char[2 * (SIZE_OF_LONG + SIZE_OF_NUMBER_OF_INSTANCE_IN_TIME)];
        for (int i = 0; i < SIZE_OF_LONG + SIZE_OF_NUMBER_OF_INSTANCE_IN_TIME; i++) {
            chars[2 * i] = ENCODING_TABLE[(signature[i] & 0xf0) / 0x10];
            chars[2 * i + 1] = ENCODING_TABLE[signature[i] & 0xf];
        }
        return new String(chars);
    }

    private byte[] generateSignature() {
        int i;
        long nowNum = System.currentTimeMillis();
        byte[] which = new byte[SIZE_OF_NUMBER_OF_INSTANCE_IN_TIME];
        (new Random((long) (new Timestamp(nowNum)).getNanos())).nextBytes(which);
        byte[] signature = new byte[SIZE_OF_LONG + SIZE_OF_NUMBER_OF_INSTANCE_IN_TIME];
        for (i = SIZE_OF_LONG; i > 0; nowNum /= 0x100, i--) signature[i - 1] = (byte) (nowNum % 0x100);
        for (i = 0; i < SIZE_OF_NUMBER_OF_INSTANCE_IN_TIME; i++) signature[SIZE_OF_LONG + i] = which[i];
        return signature;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Signature)) return false;
        Signature s = (Signature) o;
        if (pseudo) {
            if (!s.pseudo) return false;
            if (signature.length != s.signature.length) return false;
            for (int i = 0; i < signature.length; i++) if (signature[i] != s.signature[i]) return false;
            return true;
        }
        byte[] ds = ((Signature) o).signature;
        for (int i = 0; i < SIZE_OF_LONG + SIZE_OF_NUMBER_OF_INSTANCE_IN_TIME; i++) if (signature[i] != ds[i]) return false;
        return true;
    }
}
