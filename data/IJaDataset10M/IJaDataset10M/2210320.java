package net.sourceforge.freejava.util.exception;

public class NoSuchKeyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int keyIndex;

    private final String keyName;

    public NoSuchKeyException(String keyName) {
        super(keyName);
        if (keyName == null) throw new NullPointerException("keyName");
        this.keyIndex = -1;
        this.keyName = keyName;
    }

    public NoSuchKeyException(int keyIndex) {
        super(String.valueOf(keyIndex));
        this.keyIndex = keyIndex;
        this.keyName = null;
    }

    public int getKeyIndex() {
        return keyIndex;
    }

    public String getKeyName() {
        return keyName;
    }
}
