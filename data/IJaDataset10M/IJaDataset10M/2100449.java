package org.apache.harmony.luni.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * The class contains static {@link java.io.InputStream} utilities.
 */
public class InputStreamExposer {

    /**
     * Provides access to a protected underlying buffer of
     * <code>ByteArrayInputStream</code>.
     */
    private static final Field BAIS_BUF;

    /**
     * Provides access to a protected position in the underlying buffer of
     * <code>ByteArrayInputStream</code>.
     */
    private static final Field BAIS_POS;

    static {
        final Field[] f = new Field[2];
        AccessController.doPrivileged(new PrivilegedAction<Object>() {

            public Object run() {
                try {
                    f[0] = ByteArrayInputStream.class.getDeclaredField("buf");
                    f[0].setAccessible(true);
                    f[1] = ByteArrayInputStream.class.getDeclaredField("pos");
                    f[1].setAccessible(true);
                } catch (NoSuchFieldException nsfe) {
                    throw new InternalError(nsfe.getLocalizedMessage());
                }
                return null;
            }
        });
        BAIS_BUF = f[0];
        BAIS_POS = f[1];
    }

    /**
     * Reads all bytes from {@link java.io.ByteArrayInputStream} using its
     * underlying buffer directly.
     *
     * @return an underlying buffer, if a current position is at the buffer
     *         beginning, and an end position is at the buffer end, or a copy of
     *         the underlying buffer part.
     */
    private static byte[] expose(ByteArrayInputStream bais) {
        byte[] buffer, buf;
        int pos;
        synchronized (bais) {
            int available = bais.available();
            try {
                buf = (byte[]) BAIS_BUF.get(bais);
                pos = BAIS_POS.getInt(bais);
            } catch (IllegalAccessException iae) {
                throw new InternalError(iae.getLocalizedMessage());
            }
            if (pos == 0 && available == buf.length) {
                buffer = buf;
            } else {
                buffer = new byte[available];
                System.arraycopy(buf, pos, buffer, 0, available);
            }
            bais.skip(available);
        }
        return buffer;
    }

    /**
     * The utility method for reading the whole input stream into a snapshot
     * buffer. To speed up the access it works with an underlying buffer for a
     * given {@link java.io.ByteArrayInputStream}.
     *
     * @param is
     *            the stream to be read.
     * @return the snapshot wrapping the buffer where the bytes are read to.
     * @throws UnsupportedOperationException if the input stream data cannot be exposed
     */
    public static byte[] expose(InputStream is) throws IOException, UnsupportedOperationException {
        if (is.getClass().equals(ByteArrayInputStream.class)) {
            return expose((ByteArrayInputStream) is);
        }
        throw new UnsupportedOperationException();
    }
}
