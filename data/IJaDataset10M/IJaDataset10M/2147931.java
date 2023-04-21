package java.nio.charset;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

/**
 * Encoder for UTF-16, UTF-15LE, and UTF-16BE.
 * 
 * @author Jesse Rosenstock
 */
final class UTF_16Encoder extends CharsetEncoder {

    static final int BIG_ENDIAN = 0;

    static final int LITTLE_ENDIAN = 1;

    private static final char BYTE_ORDER_MARK = 0xFEFF;

    private final ByteOrder byteOrder;

    private final boolean useByteOrderMark;

    private boolean needsByteOrderMark;

    UTF_16Encoder(Charset cs, int byteOrder, boolean useByteOrderMark) {
        super(cs, 2.0f, useByteOrderMark ? 4.0f : 2.0f, byteOrder == BIG_ENDIAN ? new byte[] { (byte) 0xFF, (byte) 0xFD } : new byte[] { (byte) 0xFD, (byte) 0xFF });
        this.byteOrder = (byteOrder == BIG_ENDIAN) ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        this.useByteOrderMark = useByteOrderMark;
        this.needsByteOrderMark = useByteOrderMark;
    }

    protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
        ByteOrder originalBO = out.order();
        out.order(byteOrder);
        if (needsByteOrderMark) {
            if (out.remaining() < 2) {
                out.order(originalBO);
                return CoderResult.OVERFLOW;
            }
            out.putChar(BYTE_ORDER_MARK);
            needsByteOrderMark = false;
        }
        int inPos = in.position();
        try {
            while (in.hasRemaining()) {
                char c = in.get();
                if (0xD800 <= c && c <= 0xDFFF) {
                    if (c > 0xDBFF) return CoderResult.malformedForLength(1);
                    if (in.remaining() < 1) return CoderResult.UNDERFLOW;
                    char d = in.get();
                    if (d < 0xDC00 || d > 0xDFFF) return CoderResult.malformedForLength(1);
                    out.putChar(c);
                    out.putChar(d);
                    inPos += 2;
                } else {
                    if (out.remaining() < 2) {
                        out.order(originalBO);
                        return CoderResult.OVERFLOW;
                    }
                    out.putChar(c);
                    inPos++;
                }
            }
            out.order(originalBO);
            return CoderResult.UNDERFLOW;
        } finally {
            in.position(inPos);
        }
    }

    protected void implReset() {
        needsByteOrderMark = useByteOrderMark;
    }
}
