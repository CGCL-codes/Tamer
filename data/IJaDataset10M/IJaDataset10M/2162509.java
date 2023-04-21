package de.christophlinder.supa.encodings;

public class NullEncoding implements Encoding {

    public byte[] encode(byte[] bytes) {
        return bytes;
    }
}
