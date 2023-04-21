package com.google.code.jtracert.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class SizeOutputStream extends OutputStream {

    private long size = 0;

    /**
     * @param b
     * @throws IOException
     */
    @Override
    public void write(int b) throws IOException {
        size++;
    }

    /**
     * @param b
     * @throws IOException
     */
    @Override
    public void write(byte b[]) throws IOException {
        size += b.length;
    }

    /**
     * @param b
     * @param off
     * @param len
     * @throws IOException
     */
    @Override
    public void write(byte b[], int off, int len) throws IOException {
        size += len;
    }

    /**
     * @return
     */
    public long getSize() {
        return size;
    }
}
