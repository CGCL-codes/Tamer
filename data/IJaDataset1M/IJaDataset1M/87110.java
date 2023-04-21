package com.dbxml.db.core.data;

/**
 * Key extends Value by providing a hash value for the Key.
 */
public final class Key extends Value {

    private int hash;

    public Key(Value value) {
        super(value);
    }

    public Key(byte[] data) {
        super(data);
    }

    public Key(byte[] data, int pos, int len) {
        super(data, pos, len);
    }

    public Key(String data) {
        super(data);
    }

    public Key(String data, boolean utf8) {
        super(data, utf8);
    }

    /** @todo This has to be revisited */
    private void calculateHash() {
        hash = 0;
        int pl = pos + len;
        for (int i = pos; i < pl; i++) {
            hash = (hash << 5) ^ data[i];
            hash = hash % 1234567891;
        }
        hash = Math.abs(hash);
    }

    public int getHash() {
        if (hash == 0) calculateHash();
        return hash;
    }

    public boolean equals(Value value) {
        if (value instanceof Key) {
            Key key = (Key) value;
            if (getHash() == key.getHash()) return compareTo(key) == 0; else return false;
        } else return super.equals(value);
    }

    public int hashCode() {
        return getHash();
    }

    public boolean equals(Object obj) {
        if (obj instanceof Key) return equals((Key) obj); else return super.equals(obj);
    }
}
