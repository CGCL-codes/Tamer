package maze.common.adv_nio.reader.impl;

import java.nio.ByteBuffer;
import maze.common.adv_io.basic.impl.BasicReaderImpl;

/**
 * @author Normunds Mazurs (MAZE)
 * 
 */
public final class ByteBufferReaderUtil {

    private ByteBufferReaderUtil() {
    }

    public static String readString(final ByteBuffer inputBuffer, final int readingOffset, final int readingLen) {
        return BasicReaderImpl.create(inputBuffer, readingOffset).readFixString(readingLen);
    }

    public static byte[] readBytes(final ByteBuffer inputBuffer, final int readingOffset, final int readingLen) {
        final byte[] bytes = new byte[readingLen];
        BasicReaderImpl.create(inputBuffer, readingOffset).readFixBytes(bytes);
        return bytes;
    }
}
