package org.toobsframework.servlet.filters.compression;

import java.io.*;
import javax.servlet.ServletOutputStream;

public class FilterOutputStream extends ServletOutputStream {

    private DataOutputStream stream;

    public FilterOutputStream(OutputStream outputstream) {
        stream = new DataOutputStream(outputstream);
    }

    public void write(int i) throws IOException {
        stream.write(i);
    }

    public void write(byte buf[]) throws IOException {
        stream.write(buf);
    }

    public void write(byte buf[], int i, int j) throws IOException {
        stream.write(buf, i, j);
    }
}
