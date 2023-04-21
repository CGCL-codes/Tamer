package image.png;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.zip.CRC32;

/**
 * Read chunks idat
 */
public class PngIDatChunkInputStream extends InputStream {

    private final InputStream inputStream;

    private final CRC32 crcEngine;

    private int lenLastChunk;

    private byte[] idLastChunk = new byte[4];

    private int toReadThisChunk = 0;

    private boolean ended = false;

    private long offset;

    /**
     **/
    public PngIDatChunkInputStream(InputStream iStream, int lenFirstChunk, int offset) {
        this.offset = (long) offset;
        inputStream = iStream;
        crcEngine = new CRC32();
        this.lenLastChunk = lenFirstChunk;
        toReadThisChunk = lenFirstChunk;
        System.arraycopy(PngHelper.IDAT, 0, idLastChunk, 0, 4);
        crcEngine.update(idLastChunk, 0, 4);
        PngHelper.logdebug("Init: len=" + lenLastChunk);
        if (lenFirstChunk == 0) {
            endChunkGoForNext();
        }
    }

    /**
     * close
     */
    @Override
    public void close() throws IOException {
        super.close();
    }

    private void endChunkGoForNext() {
        do {
            int crc = PngHelper.readInt4(inputStream);
            int crccalc = (int) crcEngine.getValue();
            if (crc != crccalc) {
                throw new PngBadCrcException("error reading idat; offset: " + offset);
            }
            crcEngine.reset();
            lenLastChunk = PngHelper.readInt4(inputStream);
            if (lenLastChunk < 0) {
                throw new PngInputException("invalid len for chunk: " + lenLastChunk);
            }
            toReadThisChunk = lenLastChunk;
            PngHelper.readBytes(inputStream, idLastChunk, 0, 4);
            offset += 12;
            ended = !Arrays.equals(idLastChunk, PngHelper.IDAT);
            if (!ended) {
                crcEngine.update(idLastChunk, 0, 4);
            }
        } while (lenLastChunk == 0 && !ended);
    }

    /**

     */
    public void forceChunkEnd() {
        if (!ended) {
            byte[] dummy = new byte[toReadThisChunk];
            PngHelper.readBytes(inputStream, dummy, 0, toReadThisChunk);
            crcEngine.update(dummy, 0, toReadThisChunk);
            endChunkGoForNext();
        }
    }

    /**

     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (toReadThisChunk == 0) {
            throw new PngException("this should not happen");
        }
        int n = inputStream.read(b, off, len >= toReadThisChunk ? toReadThisChunk : len);
        if (n > 0) {
            crcEngine.update(b, off, n);
            this.offset += n;
            toReadThisChunk -= n;
        }
        if (toReadThisChunk == 0) {
            endChunkGoForNext();
        }
        return n;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public int read() throws IOException {
        byte[] b1 = new byte[1];
        int r = this.read(b1, 0, 1);
        return r < 0 ? -1 : (int) b1[0];
    }

    public int getLenLastChunk() {
        return lenLastChunk;
    }

    public byte[] getIdLastChunk() {
        return idLastChunk;
    }

    public long getOffset() {
        return offset;
    }

    public boolean isEnded() {
        return ended;
    }
}
