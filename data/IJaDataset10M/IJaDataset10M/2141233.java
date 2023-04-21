package org.nakedobjects.metamodel.commons.encoding;

import java.io.IOException;
import java.io.OutputStream;

public class TestOutputStream extends OutputStream {

    byte[] data = new byte[1000];

    int pos = 0;

    @Override
    public void write(final int b) throws IOException {
        data[pos++] = (byte) b;
    }
}
