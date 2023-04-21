package de.exti.replayanalyzer;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipException;

/**
 * This is input stream to read packed data in zlib format from replay file.
 * It can read whole packed data from replay file starting from first block header's first byte.
 *
 * @author Anomandaris
 *
 */
public class RepAnalyzer extends InputStream {

    private InputStream in;

    private byte[] dData;

    private int position = 0;

    private int cLength;

    private int dLength;

    private long currBlock = 0;

    private long maxBlock;

    /**
	 * Creates new instance of stream, that will read packed data from given source stream
	 * @param in stream with packed data
	 * @param maxBlock number of packed blocks in stream
	 */
    public RepAnalyzer(InputStream in, long maxBlock) {
        this.in = in;
        this.maxBlock = maxBlock;
    }

    /**
	 * Returns one byte (value 0 - 255) of unpacked data, or -1 if end of stream is reached
	 */
    public int read() throws IOException {
        if (position == 0) {
            if (currBlock == maxBlock) {
                return -1;
            }
            byte[] cData = new byte[cLength];
            dData = new byte[dLength];
            int l = in.read(cData, 0, cLength);
            if (l != cLength) throw new IOException(String.format("Needs to read %d compressed bytes from input, but only %d available", cLength, l));
            Inflater inflater = new Inflater(false);
            inflater.setInput(cData, 0, cLength);
            try {
                l = inflater.inflate(dData, 0, dLength);
            } catch (DataFormatException e) {
                throw new ZipException("Invalid data");
            }
            if (l != dLength) throw new ZipException(String.format("Inflated %d bytes instead of %d", l, dLength));
        }
        int ret = dData[position];
        position++;
        if (position == dLength) position = 0;
        if (ret < 0) ret = 256 + ret;
        return ret;
    }
}
