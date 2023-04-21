package unclej.utasks.process;

/**
 * Records the last <tt>n</tt> characters of a stream in a circular buffer so that they can be analyzed when the
 * process that produced them crashes.
 * @author Scott
 */
class BlackBox {

    public BlackBox(int size) {
        buf = new char[size];
    }

    public void put(char c) {
        buf[pos] = c;
        pos = (pos + 1) % buf.length;
        full |= (pos == 0);
    }

    public void put(byte[] b, int offset, int count) {
        int end = offset + count;
        for (int i = offset; i < end; ++i) {
            put((char) b[i]);
        }
    }

    public void put(char[] c, int offset, int count) {
        int end = offset + count;
        for (int i = offset; i < end; ++i) {
            put(c[i]);
        }
    }

    public final void put(char[] c) {
        put(c, 0, c.length);
    }

    public final void put(String s) {
        put(s.toCharArray(), 0, s.length());
    }

    public String toString() {
        if (full) {
            char[] buf2 = new char[buf.length];
            System.arraycopy(buf, pos, buf2, 0, buf.length - pos);
            System.arraycopy(buf, 0, buf2, buf.length - pos, pos);
            return new String(buf2);
        } else {
            return new String(buf, 0, pos);
        }
    }

    private final char[] buf;

    private int pos;

    private boolean full;
}
