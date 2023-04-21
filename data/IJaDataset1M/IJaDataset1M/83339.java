package net.sf.vat4net.io;

import java.util.zip.Deflater;

/**
 * Implements constants needed in the connected buffer classes such as
 * the size in bytes of the primitive data types.
 * @author $Author: tom77 $
 * @version $Revision: 1.1 $
 */
public interface ConnectedBufferConstants {

    public static final int DEFAULT_BUFFER_SIZE = 1024;

    public static final int BYTE_SIZE = 1;

    public static final int SHORT_SIZE = 2;

    public static final int INT_SIZE = 4;

    public static final int LONG_SIZE = 8;

    public static final int FLOAT_SIZE = 4;

    public static final int DOUBLE_SIZE = 8;

    public static final int STREAM_COMPRESSION = Deflater.BEST_COMPRESSION;
}
