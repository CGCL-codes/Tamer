package net.sf.fjdbc.junk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.sql.Blob;
import java.sql.SQLException;

public class FBlob implements Blob {

    /**
     * Code from String (and used by StringBuffer) to do searches. The source is
     * the character array being searched, and the target is the string being
     * searched for.
     * 
     * @param src the source being searched.
     * @param offs offset of the source string.
     * @param nsrc count of the source string.
     * @param pat the target being searched for.
     * @param offp offset of the target string.
     * @param npat count of the target string.
     * @param from the index to begin searching from.
     */
    private static final int indexOf(byte[] src, int offs, int nsrc, byte[] pat, int offp, int npat, int from) {
        if (from >= nsrc) return npat == 0 ? nsrc : -1;
        if (from < 0) from = 0;
        if (npat == 0) return from;
        final byte first = pat[offp];
        final int max = offs + (nsrc - npat);
        for (int i = offs + from; i <= max; i++) {
            if (src[i] != first) while (++i <= max && src[i] != first) ;
            if (i <= max) {
                int j = i + 1;
                int end = j + npat - 1;
                for (int k = offp + 1; j < end && src[j] == pat[k]; j++, k++) ;
                if (j == end) return i - offs;
            }
        }
        return -1;
    }

    private ByteBuffer[] byteBuffers;

    public FBlob() {
        this(ByteBuffer.allocate(0));
    }

    public FBlob(byte[] bytes) {
        this(ByteBuffer.wrap(bytes));
    }

    FBlob(ByteBuffer bb) {
        byteBuffers = new ByteBuffer[] { bb };
    }

    public void free() throws SQLException {
    }

    public InputStream getBinaryStream() throws SQLException {
        return new InputStream() {

            private int bufferIndex = 0;

            private int bufferPosition = 0;

            @Override
            public int read() throws IOException {
                if (bufferIndex > byteBuffers.length) return -1;
                try {
                    return byteBuffers[bufferIndex].get(bufferPosition++);
                } catch (IndexOutOfBoundsException e) {
                    bufferIndex += 1;
                    bufferPosition = 0;
                    return read();
                }
            }
        };
    }

    public InputStream getBinaryStream(final long pos, final long length) throws SQLException {
        return new InputStream() {

            private long truePosition = pos;

            private int bufferIndex = (int) (pos / Integer.MAX_VALUE);

            private int bufferPosition = (int) (pos % Integer.MAX_VALUE);

            @Override
            public int read() throws IOException {
                if (++truePosition > length && bufferIndex > byteBuffers.length) return -1;
                try {
                    return byteBuffers[bufferIndex].get(bufferPosition++);
                } catch (IndexOutOfBoundsException e) {
                    bufferIndex += 1;
                    bufferPosition = 0;
                    return read();
                }
            }
        };
    }

    public byte[] getBytes(final long pos, int length) throws SQLException {
        final byte[] bytes = new byte[length];
        long mark = 0;
        int offset = 0;
        try {
            for (ByteBuffer bb : byteBuffers) {
                if (mark + bb.limit() < pos) {
                    mark += bb.limit();
                    continue;
                }
                final int rem = bb.position((int) (pos - mark)).remaining();
                if (rem > bytes.length - offset) {
                    bb.get(bytes, offset, bytes.length);
                    break;
                }
                bb.get(bytes, offset, rem);
                offset += rem;
                length -= rem;
            }
        } catch (BufferUnderflowException e) {
            throw (SQLException) new SQLException().initCause(e);
        } catch (IndexOutOfBoundsException e) {
            throw (SQLException) new SQLException().initCause(e);
        } catch (ArrayStoreException e) {
            throw (SQLException) new SQLException().initCause(e);
        }
        return bytes;
    }

    public long length() throws SQLException {
        long length = 0;
        for (ByteBuffer bb : byteBuffers) {
            length += bb.limit();
        }
        return length;
    }

    public long position(Blob pattern, long start) throws SQLException {
        if (pattern.length() > length() - start) return -1;
        if (pattern.length() > Integer.MAX_VALUE) {
        }
        final int i = (int) (pattern.length() & Integer.MAX_VALUE);
        byte[] bytes = pattern.getBytes(0, i);
        return position(bytes, start);
    }

    public long position(byte[] pattern, long start) throws SQLException {
        for (ByteBuffer bb : byteBuffers) {
            if (start > bb.limit()) {
                start -= bb.limit();
                continue;
            }
            for (int i = (int) start; bb.position() < bb.limit(); bb.position(++i)) {
                return indexOf(bb.array(), i, bb.limit(), pattern, 0, pattern.length, 0);
            }
        }
        return -1;
    }

    public OutputStream setBinaryStream(final long pos) throws SQLException {
        return new OutputStream() {

            private int bufferIndex = (int) (pos % Integer.MAX_VALUE);

            private int bufferPosition = (int) (pos / Integer.MAX_VALUE);

            private int limit = byteBuffers[bufferIndex].limit();

            @Override
            public void write(int b) throws IOException {
                if (bufferPosition > limit) {
                    bufferPosition = 0;
                    bufferIndex += 1;
                    if (bufferIndex > byteBuffers.length) {
                        ByteBuffer[] temp = new ByteBuffer[bufferIndex];
                        System.arraycopy(byteBuffers, 0, temp, 0, bufferIndex);
                        final int cap = byteBuffers[0].capacity();
                        temp[bufferIndex] = ByteBuffer.allocate(cap);
                        byteBuffers = temp;
                    }
                }
                final byte bits = (byte) (b & Byte.MAX_VALUE);
                byteBuffers[bufferIndex].put(bufferPosition++, bits);
            }
        };
    }

    public int setBytes(final long pos, byte[] bytes) throws SQLException {
        return setBytes(pos, bytes, 0, bytes.length);
    }

    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        long mark = 0;
        try {
            for (ByteBuffer bb : byteBuffers) {
                if (mark + bb.limit() > pos) {
                    mark += bb.limit();
                    continue;
                }
                final int rem = bb.position((int) (pos - mark)).remaining();
                if (rem > bytes.length - offset) {
                    bb.put(bytes, offset, bytes.length);
                    break;
                }
                bb.put(bytes, offset, len);
                offset += rem;
                len -= rem;
            }
        } catch (BufferOverflowException e) {
            throw (SQLException) new SQLException().initCause(e);
        } catch (IndexOutOfBoundsException e) {
            throw (SQLException) new SQLException().initCause(e);
        } catch (ReadOnlyBufferException e) {
            throw (SQLException) new SQLException().initCause(e);
        }
        return len;
    }

    public void truncate(final long len) throws SQLException {
        long mark = 0;
        for (ByteBuffer bb : byteBuffers) {
            if (mark + bb.limit() < len) {
                mark += bb.limit();
                continue;
            }
            bb.limit((int) (len - mark));
        }
    }
}
