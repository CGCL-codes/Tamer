package com.daffodilwoods.rmi;

import java.io.*;
import java.rmi.*;
import com.daffodilwoods.rmi.interfaces.*;

public class RmiWriter extends Writer {

    _RmiWriter writer_interface;

    private int BUFFER_SIZE = 1000;

    private int position = 0;

    private char[] buff;

    public RmiWriter(_RmiWriter writer_interface) {
        this.writer_interface = writer_interface;
        buff = new char[BUFFER_SIZE];
        position = 0;
    }

    public void write(int c) throws IOException {
        try {
            writechars(new char[] { (char) c }, 0, 1);
        } catch (RemoteException re) {
            throw new RuntimeException(re.getMessage());
        }
    }

    public void write(char cbuf[]) throws IOException {
        try {
            if (cbuf == null) return;
            writechars(cbuf, 0, cbuf.length);
        } catch (RemoteException re) {
            throw new RuntimeException(re.getMessage());
        }
    }

    public void write(char cbuf[], int off, int len) throws IOException {
        ensureOpen();
        if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        writechars(cbuf, off, len);
    }

    public void write(String str) throws IOException {
        try {
            if (str == null) return;
            char[] chs = str.toCharArray();
            writechars(chs, 0, chs.length);
        } catch (RemoteException re) {
            throw new RuntimeException(re.getMessage());
        }
    }

    public void write(String str, int off, int len) throws IOException {
        try {
            if (str == null) throw new NullPointerException();
            char[] chs = str.toCharArray();
            write(chs, 0, chs.length);
        } catch (RemoteException re) {
            throw new RuntimeException(re.getMessage());
        }
    }

    public void flush() throws IOException {
        try {
            flushBuffer();
            writer_interface.flush();
        } catch (RemoteException re) {
            throw new RuntimeException(re.getMessage());
        }
    }

    public void close() throws IOException {
        try {
            flushBuffer();
            writer_interface.close();
        } catch (RemoteException re) {
            throw new RuntimeException(re.getMessage());
        }
    }

    private void writechars(char[] cbuf, int off, int len) throws IOException {
        try {
            if (len >= BUFFER_SIZE) {
                flushBuffer();
                writer_interface.writeChars(cbuf, off, len);
                return;
            }
            int b = off, t = off + len;
            while (b < t) {
                int d = min(BUFFER_SIZE - position, t - b);
                System.arraycopy(cbuf, b, buff, position, d);
                b += d;
                position += d;
                if (position >= BUFFER_SIZE) flushBuffer();
            }
        } catch (RemoteException re) {
            throw new RuntimeException(re.getMessage());
        }
    }

    /**
     * Our own little min method, to avoid loading java.lang.Math if we've run
     * out of file descriptors and we're trying to print a stack trace.
     */
    private int min(int a, int b) {
        if (a < b) return a;
        return b;
    }

    private void flushBuffer() throws IOException {
        ensureOpen();
        if (position == 0) return;
        writer_interface.writeChars(buff, 0, position);
        position = 0;
    }

    /** Check to make sure that the stream has not been closed */
    private void ensureOpen() throws IOException {
        if (writer_interface == null) throw new IOException("Stream closed");
    }

    public void finalize() {
        try {
            flushBuffer();
        } catch (IOException ex) {
        }
    }
}
