package jaxlib.jaxlib_private.io;

import java.nio.Buffer;
import java.nio.ReadOnlyBufferException;
import jaxlib.jaxlib_private.CheckArg;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: CheckIOArg.java,v 1.1.1.1 2004/08/02 20:56:15 joerg_wassmer Exp $
 */
public final class CheckIOArg extends Object {

    private CheckIOArg() {
        super();
    }

    public static void copyFileRange(long size, long fromIndex, long toIndex, long destIndex) {
        CheckArg.range(size, fromIndex, toIndex);
        if (destIndex < 0) throw new IndexOutOfBoundsException("destIndex(" + destIndex + ") < 0.");
    }

    /**
   * @throws IllegalArgumentException if <code>count &lt; 0</code>.
   */
    public static void count(long count) {
        if (count < 0) throw new IllegalArgumentException("Negative count " + count + ".");
    }

    public static void failIfReadOnly(Buffer buffer) {
        if (buffer.isReadOnly()) throw new ReadOnlyBufferException();
    }

    public static void range(byte[] b, int off, int len) {
        range(b.length, off, len);
    }

    public static void range(char[] b, int off, int len) {
        range(b.length, off, len);
    }

    public static void range(int length, int off, int len) {
        if (off < 0) throw new IndexOutOfBoundsException("off(" + off + ") < 0.");
        if (len < 0) throw new IllegalArgumentException("len(" + len + ") < 0.");
        if (off + len > length) throw new IndexOutOfBoundsException("off(" + off + ") + len(" + len + ") > data length(" + length + ").");
    }

    public static void range(long length, long off, long len) {
        if (off < 0) throw new IndexOutOfBoundsException("off(" + off + ") < 0.");
        if (len < 0) throw new IllegalArgumentException("len(" + len + ") < 0.");
        if (off + len > length) throw new IndexOutOfBoundsException("off(" + off + ") + len(" + len + ") > data length(" + length + ").");
    }

    public static void readLimit(int readLimit) {
        if (readLimit < 0) throw new IllegalArgumentException("readLimit(" + readLimit + ") < 0.");
    }
}
