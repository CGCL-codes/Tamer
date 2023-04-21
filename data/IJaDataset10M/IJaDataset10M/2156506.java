package org.openemed.LQS;

import org.openemed.LQS.PresentationType;

/***/
public final class PresentationValue {

    java.lang.Object obj;

    boolean _initialized;

    int _discriminator;

    static boolean isEqual(int d0, int d1) {
        return d0 == d1;
    }

    public PresentationValue() {
        _initialized = false;
    }

    public PresentationType discriminator() throws Exception {
        if (!_initialized) throw new Exception();
        return PresentationType.from_int(_discriminator);
    }

    public String the_text() throws Exception {
        if (!_initialized) throw new Exception();
        if (!isEqual(_discriminator, PresentationType._TEXT)) throw new Exception();
        return (String) obj;
    }

    public void the_text(String _text) {
        _initialized = true;
        _discriminator = PresentationType._TEXT;
        obj = _text;
    }

    public byte[] a_Blob() throws Exception {
        if (!_initialized) throw new Exception();
        if (!isEqual(_discriminator, PresentationType._BINARY)) throw new Exception();
        return (byte[]) obj;
    }

    public void a_Blob(byte[] _blob) {
        _initialized = true;
        _discriminator = PresentationType._BINARY;
        obj = _blob;
    }
}
