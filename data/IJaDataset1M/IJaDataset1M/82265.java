package org.openi.test;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * test output stream class that does not buffer anything, 
 * but traps byte length of output stream
 *
 */
public class NullOutputStream extends OutputStream {

    private long size = 0;

    public NullOutputStream() {
    }

    public void write(int b) throws IOException {
        size++;
    }

    public long length() {
        return this.size;
    }
}
