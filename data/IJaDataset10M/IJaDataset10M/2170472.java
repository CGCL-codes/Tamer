package de.unkrig.commons.io;

import java.io.FilterInputStream;
import java.io.InputStream;

public class UnclosableInputStream extends FilterInputStream {

    public UnclosableInputStream(InputStream delegate) {
        super(delegate);
    }

    @Override
    public void close() {
        ;
    }
}
