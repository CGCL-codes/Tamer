package android.speech.srec;

import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream which transforms 16 bit pcm data to ulaw data.
 * 
 * Not yet ready to be supported, so
 * @hide
 */
public final class UlawEncoderInputStream extends InputStream {

    private static final String TAG = "UlawEncoderInputStream";

    private static final int MAX_ULAW = 8192;

    private static final int SCALE_BITS = 16;

    private InputStream mIn;

    private int mMax = 0;

    private final byte[] mBuf = new byte[1024];

    private int mBufCount = 0;

    private final byte[] mOneByte = new byte[1];

    public static void encode(byte[] pcmBuf, int pcmOffset, byte[] ulawBuf, int ulawOffset, int length, int max) {
        if (max <= 0) max = MAX_ULAW;
        int coef = MAX_ULAW * (1 << SCALE_BITS) / max;
        for (int i = 0; i < length; i++) {
            int pcm = (0xff & pcmBuf[pcmOffset++]) + (pcmBuf[pcmOffset++] << 8);
            pcm = (pcm * coef) >> SCALE_BITS;
            int ulaw;
            if (pcm >= 0) {
                ulaw = pcm <= 0 ? 0xff : pcm <= 30 ? 0xf0 + ((30 - pcm) >> 1) : pcm <= 94 ? 0xe0 + ((94 - pcm) >> 2) : pcm <= 222 ? 0xd0 + ((222 - pcm) >> 3) : pcm <= 478 ? 0xc0 + ((478 - pcm) >> 4) : pcm <= 990 ? 0xb0 + ((990 - pcm) >> 5) : pcm <= 2014 ? 0xa0 + ((2014 - pcm) >> 6) : pcm <= 4062 ? 0x90 + ((4062 - pcm) >> 7) : pcm <= 8158 ? 0x80 + ((8158 - pcm) >> 8) : 0x80;
            } else {
                ulaw = -1 <= pcm ? 0x7f : -31 <= pcm ? 0x70 + ((pcm - -31) >> 1) : -95 <= pcm ? 0x60 + ((pcm - -95) >> 2) : -223 <= pcm ? 0x50 + ((pcm - -223) >> 3) : -479 <= pcm ? 0x40 + ((pcm - -479) >> 4) : -991 <= pcm ? 0x30 + ((pcm - -991) >> 5) : -2015 <= pcm ? 0x20 + ((pcm - -2015) >> 6) : -4063 <= pcm ? 0x10 + ((pcm - -4063) >> 7) : -8159 <= pcm ? 0x00 + ((pcm - -8159) >> 8) : 0x00;
            }
            ulawBuf[ulawOffset++] = (byte) ulaw;
        }
    }

    /**
     * Compute the maximum of the absolute value of the pcm samples.
     * The return value can be used to set ulaw encoder scaling.
     * @param pcmBuf array containing 16 bit pcm data.
     * @param offset offset of start of 16 bit pcm data.
     * @param length number of pcm samples (not number of input bytes)
     * @return maximum abs of pcm data values
     */
    public static int maxAbsPcm(byte[] pcmBuf, int offset, int length) {
        int max = 0;
        for (int i = 0; i < length; i++) {
            int pcm = (0xff & pcmBuf[offset++]) + (pcmBuf[offset++] << 8);
            if (pcm < 0) pcm = -pcm;
            if (pcm > max) max = pcm;
        }
        return max;
    }

    /**
     * Create an InputStream which takes 16 bit pcm data and produces ulaw data.
     * @param in InputStream containing 16 bit pcm data.
     * @param max pcm value corresponding to maximum ulaw value.
     */
    public UlawEncoderInputStream(InputStream in, int max) {
        mIn = in;
        mMax = max;
    }

    @Override
    public int read(byte[] buf, int offset, int length) throws IOException {
        if (mIn == null) throw new IllegalStateException("not open");
        while (mBufCount < 2) {
            int n = mIn.read(mBuf, mBufCount, Math.min(length * 2, mBuf.length - mBufCount));
            if (n == -1) return -1;
            mBufCount += n;
        }
        int n = Math.min(mBufCount / 2, length);
        encode(mBuf, 0, buf, offset, n, mMax);
        mBufCount -= n * 2;
        for (int i = 0; i < mBufCount; i++) mBuf[i] = mBuf[i + n * 2];
        return n;
    }

    @Override
    public int read(byte[] buf) throws IOException {
        return read(buf, 0, buf.length);
    }

    @Override
    public int read() throws IOException {
        int n = read(mOneByte, 0, 1);
        if (n == -1) return -1;
        return 0xff & (int) mOneByte[0];
    }

    @Override
    public void close() throws IOException {
        if (mIn != null) {
            InputStream in = mIn;
            mIn = null;
            in.close();
        }
    }

    @Override
    public int available() throws IOException {
        return (mIn.available() + mBufCount) / 2;
    }
}
