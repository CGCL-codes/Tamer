package miniseq;

import java.io.IOException;
import java.io.InputStream;

public class FeatureInputStream extends InputStream {

    private InputStream in;

    private int buffer = -1;

    public FeatureInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        if (buffer != -1) {
            int x = buffer;
            buffer = -1;
            return x;
        }
        return in.read();
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    public void skipDelimiters() throws IOException {
        int ch = read();
        while (ch == 0x20 || ch == '\r' || ch == '\n' || ch == '\t' || ch == ',') ch = read();
        buffer = ch;
    }

    public int readUtf8() throws IOException {
        int x = read();
        if (x < 192) {
            return x;
        }
        int ch = read();
        if ((ch & 0xC0) == 0x80) {
            int ff = x & 0xE0;
            if (ff == 0xE0) {
                x = ((x & 0x0F) << 6) | (ch & 0x3F);
                ch = read();
            } else if (ff == 0xC0) {
                x = (x & 0x1F);
            }
            return (x << 6) | (ch & 0x3F);
        } else {
            buffer = ch;
            return x;
        }
    }

    public int readInt() throws IOException {
        skipDelimiters();
        int ch = read();
        StringBuilder b = new StringBuilder();
        if (ch == (int) '-') {
            b.append((char) ch);
            ch = read();
        }
        while (ch >= (int) '0' && ch <= (int) '9') {
            b.append((char) ch);
            ch = read();
        }
        buffer = ch;
        return Integer.parseInt(b.toString());
    }

    public char readChar() throws IOException {
        int ch = readUtf8();
        return (char) ch;
    }

    public float readFloat() throws IOException {
        skipDelimiters();
        int ch = read();
        StringBuilder b = new StringBuilder();
        if (ch == '-') {
            b.append("-0");
            ch = read();
        } else b.append('0');
        while (ch >= (int) '0' && ch <= (int) '9') {
            b.append((char) ch);
            ch = read();
        }
        if (ch == '.') {
            b.append((char) ch);
            ch = read();
            while (ch >= (int) '0' && ch <= (int) '9') {
                b.append((char) ch);
                ch = read();
            }
        }
        buffer = ch;
        return Float.parseFloat(b.toString());
    }

    public int peekChar() throws IOException {
        if (buffer != -1) return (char) buffer;
        return (char) (buffer = readUtf8());
    }

    public int readInt(int chars, int base) throws IOException {
        StringBuilder bd = new StringBuilder();
        for (int i = 0; i < chars; i++) {
            bd.append(readChar());
        }
        return Integer.parseInt(bd.toString(), base);
    }
}
