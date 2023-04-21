package com.sun.cldc.i18n.j2me;

import java.io.*;
import com.sun.cldc.i18n.*;

/**
 * Generic interface for stream conversion reading of 
 * specific character encoded input streams.
 *.
 */
public class Gen_Reader extends StreamReader {

    /** Saved encoding string from construction. */
    private String enc;

    /** Native handle for conversion routines. */
    private int id;

    /** Local buffer to read converted characters. */
    private byte[] buf;

    /** Maximum length of characters in local buffer. */
    private int maxByteLen;

    /**
     * Constructor for generic reader.
     * @param enc character encoding to use for byte to character
     * conversion.
     * @exception ClassNotFoundException is thrown if the conversion
     * class is not available
     */
    Gen_Reader(String enc) throws ClassNotFoundException {
        id = Conv.getHandler(enc);
        if (id == -1) {
            throw new ClassNotFoundException();
        }
        this.enc = enc;
        maxByteLen = Conv.getMaxByteLength(id);
        pos = maxByteLen;
        tmp = new byte[pos];
        buf = new byte[pos];
    }

    /**
     * Generic routine to open an InputStream with a specific
     * character encoding.
     * 
     * @param in the input stream to process
     * @param enc the character encoding of the input stream
     * @return Reader instance for converted characters
     * @throws UnsupportedEncodingException if encoding is not supported
     */
    public Reader open(InputStream in, String enc) throws UnsupportedEncodingException {
        if (!enc.equals(this.enc)) {
            throw new UnsupportedEncodingException();
        }
        init();
        return super.open(in, enc);
    }

    /**
     * Read a single converted character.
     *
     * @return a single converted character
     * @exception IOException is thrown if the input stream 
     * could not be read for the raw unconverted character
     */
    public synchronized int read() throws IOException {
        int c = get();
        if (c == -1) {
            return -1;
        }
        char[] cb = { (char) 0xFFFD };
        int bufLen = 0;
        buf[bufLen++] = (byte) c;
        boolean eof = false;
        for (int i = 1; i < maxByteLen; i++) {
            c = get();
            if (c == -1) {
                eof = true;
                break;
            }
            buf[bufLen++] = (byte) c;
        }
        int bytelen = Conv.getByteLength(id, buf, 0, bufLen);
        if (bytelen == -1) {
            if (eof) {
                throw new IOException();
            }
            for (int i = bufLen; i > 1; i--) {
                put(buf[--bufLen]);
            }
            return cb[0];
        }
        if (bytelen == 0) {
            for (int i = bufLen; i > 1; i--) {
                put(buf[--bufLen]);
            }
            return cb[0];
        }
        if (bytelen < bufLen) {
            for (int i = bufLen; i > bytelen; i--) {
                put(buf[--bufLen]);
            }
        }
        int convLen = Conv.byteToChar(id, buf, 0, bufLen, cb, 0, 1);
        if (convLen != 1) {
            throw new IOException();
        }
        return cb[0];
    }

    /**
     * Read a block of converted characters.
     *
     * @param cbuf output buffer for converted characters read
     * @param off initial offset into the provided buffer
     * @param len length of characters in the buffer
     * @return the number of converted characters, or -1 
     * if an error occured in the input arguments
     * @exception IOException is thrown if the input stream 
     * could not be read for the raw unconverted character
     */
    public synchronized int read(char cbuf[], int off, int len) throws IOException {
        int c = get();
        if (c == -1) {
            return -1;
        }
        put(c);
        int maxlen = len * maxByteLen;
        if (buf.length < maxlen) {
            buf = new byte[maxlen];
        }
        int bufLen = readNumOfChars(buf, len);
        int ret = 0;
        if (bufLen > 0) {
            ret = Conv.byteToChar(id, buf, 0, bufLen, cbuf, off, len);
        }
        if (buf.length > maxByteLen) {
            buf = new byte[maxByteLen];
        }
        return ret;
    }

    /**
     * Skip over a number of bytes in the input stream.
     * 
     * @param n number of bytes to bypass from the input stream.
     * @return the number of characters skipped
     * @throws IOException if an I/O error occurs
     */
    public long skip(long n) throws IOException {
        if (n < 0L) {
            throw new IllegalArgumentException("skip value is negative");
        }
        int c = get();
        if (c == -1) {
            return 0;
        }
        put(c);
        return readNumOfChars(null, (int) n);
    }

    /** 
     * Get the size of the converted bytes as a Unicode 
     * byte array.
     *
     * @param c array of bytes to compute size
     * @param offset offset in the provided buffer
     * @param length length of bytes to process
     * @return length of converted characters.
     */
    public int sizeOf(byte[] c, int offset, int length) {
        return Conv.sizeOfByteInUnicode(id, c, offset, length);
    }

    /**
     * Reset the stream.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public void reset() throws IOException {
        init();
    }

    /**
     * Read a specific number of characters from the 
     * input stream.
     * 
     * @param b array of bytes to read
     * @param num count of bytes to read
     * @return number of characters read
     * @exception IOException is thrown, if an error occured 
     * reading the raw input stream
     */
    private int readNumOfChars(byte[] b, int num) throws IOException {
        int charsRead = 0;
        int bbLen = 0;
        byte bb[];
        if (b == null) {
            bb = new byte[num * maxByteLen];
        } else {
            bb = b;
        }
        boolean eof = false;
        while (charsRead < num) {
            int c = get();
            if (c == -1) {
                break;
            }
            int offset = bbLen;
            bb[bbLen++] = (byte) c;
            for (int i = 1; i < maxByteLen; i++) {
                c = get();
                if (c == -1) {
                    eof = true;
                    break;
                }
                bb[bbLen++] = (byte) c;
            }
            int readNum = bbLen - offset;
            int bytelen = Conv.getByteLength(id, bb, offset, readNum);
            if (bytelen == -1) {
                if (eof) {
                    for (int i = readNum; i > 0; i--) {
                        put(bb[--bbLen]);
                    }
                    break;
                }
                for (int i = readNum; i > 1; i--) {
                    put(bb[--bbLen]);
                }
            } else if (bytelen == 0) {
                for (int i = readNum; i > 1; i--) {
                    put(bb[--bbLen]);
                }
            } else if (bytelen < readNum) {
                for (int i = readNum; i > bytelen; i--) {
                    put(bb[--bbLen]);
                }
            }
            charsRead++;
        }
        if (b != null) {
            return bbLen;
        } else {
            return charsRead;
        }
    }

    /** Local buffer for conversion routines. */
    private byte[] tmp;

    /** Current position in the temporary buffer. */
    private int pos;

    /**
     * Get a byte from the temporary buffer or from the 
     * input stream.
     * @return the next byte from the input stream or local buffer
     * @exception IOException is thrown, if an error occured 
     * reading the raw input stream
     */
    private int get() throws IOException {
        if (pos < tmp.length) {
            return tmp[pos++] & 0xff;
        }
        return in.read();
    }

    /**
     * Put a byte back into the temporary buffer.
     * @param b the character to put back in the temporary buffer
     * @exception IOException is thrown, if putting a character back 
     * past the beginning of the local temporary buffer
     */
    private void put(int b) throws IOException {
        if (pos == 0) {
            throw new IOException();
        }
        tmp[--pos] = (byte) b;
    }

    /** reset the state */
    private void init() {
        pos = maxByteLen;
    }
}
