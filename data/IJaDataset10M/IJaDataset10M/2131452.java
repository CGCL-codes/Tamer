package org.kc7bfi.jflac.metadata;

import java.io.IOException;
import org.kc7bfi.jflac.io.BitInputStream;

/**
 * An entry into the cue track.
 * @author kc7bfi
 */
public class CueIndex {

    private static final int CUESHEET_INDEX_OFFSET_LEN = 64;

    private static final int CUESHEET_INDEX_NUMBER_LEN = 8;

    private static final int CUESHEET_INDEX_RESERVED_LEN = 3 * 8;

    protected long offset;

    protected byte number;

    /**
     * The constructor.
     * @param is                The InputBitStream
     * @throws IOException      Thrown if error reading from InputBitStream
     */
    public CueIndex(BitInputStream is) throws IOException {
        offset = is.readRawULong(CUESHEET_INDEX_OFFSET_LEN);
        number = (byte) is.readRawUInt(CUESHEET_INDEX_NUMBER_LEN);
        is.skipBitsNoCRC(CUESHEET_INDEX_RESERVED_LEN);
    }
}
