package org.globus.io.streams;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

public class GlobusFileInputStream extends GlobusInputStream {

    private FileInputStream input;

    private long size = -1;

    public GlobusFileInputStream(String file) throws IOException {
        File f = new File(file);
        input = new FileInputStream(f);
        size = f.length();
    }

    public long getSize() {
        return size;
    }

    public void abort() {
        try {
            input.close();
        } catch (Exception e) {
        }
    }

    public void close() throws IOException {
        input.close();
    }

    public int read(byte[] msg) throws IOException {
        return input.read(msg);
    }

    public int read(byte[] buf, int off, int len) throws IOException {
        return input.read(buf, off, len);
    }

    public int read() throws IOException {
        return input.read();
    }

    public int available() throws IOException {
        return input.available();
    }
}
