package com.vg.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.apache.commons.io.input.CountingInputStream;

public class RandomAccessFileBufferedInputStream extends SeekableInputStream {

    private final RandomAccessFile raf;

    private final FileInputStream fin;

    private final long startPosition;

    private CountingInputStream cin;

    private long pos;

    public RandomAccessFileBufferedInputStream(File file, long offset) throws IOException {
        this(raf(file, offset));
    }

    private static RandomAccessFile raf(File file, long offset) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        if (offset != 0) {
            forceSeek(raf, offset);
        }
        return raf;
    }

    public RandomAccessFileBufferedInputStream(File file) throws IOException {
        this(raf(file, 0));
    }

    public RandomAccessFileBufferedInputStream(RandomAccessFile raf) throws IOException {
        this.raf = raf;
        this.fin = new FileInputStream(raf.getFD());
        this.startPosition = raf.getFilePointer();
        _reset();
    }

    private void _reset() throws IOException {
        this.pos = raf.getFilePointer() - this.startPosition;
        this.cin = new CountingInputStream(new BufferedInputStream(fin, 4096));
    }

    @Override
    public int read() throws IOException {
        return checkRead(cin.read());
    }

    @Override
    public int read(byte[] b) throws IOException {
        return checkRead(cin.read(b));
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return checkRead(cin.read(b, off, len));
    }

    @Override
    public void close() throws IOException {
        cin.close();
        cin = null;
        raf.close();
    }

    @Override
    public long seek(long position) throws IOException {
        checkIdx(position);
        forceSeek(raf, startPosition + position);
        _reset();
        return position();
    }

    public long position() throws IOException {
        return pos + cin.getByteCount();
    }

    @Override
    public long length() {
        try {
            return raf.length() - startPosition;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static long forceSeek(RandomAccessFile raf, long offset) throws IOException {
        long length = raf.length();
        if (offset >= length) {
            throw new IllegalStateException("trying to seek to (" + (offset) + ") outside of len (" + length + ")");
        }
        raf.seek(offset);
        if (offset != raf.getFilePointer()) {
            throw new IllegalStateException("seek failed");
        }
        return offset;
    }
}
