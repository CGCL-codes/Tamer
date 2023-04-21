package net.sf.ngrease.core.ast;

import java.io.InputStream;

public class InputStreamSourceDefaultImpl implements InputStreamSource {

    private final InputStream inputStream;

    public InputStreamSourceDefaultImpl(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
