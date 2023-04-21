package org.fontbox.ttf;

import java.io.IOException;

/**
 * A table in a true type font.
 * 
 * @author Ben Litchfield (ben@benlitchfield.com)
 * @version $Revision: 1.1 $
 */
public class IndexToLocationTable extends TTFTable {

    private static final short SHORT_OFFSETS = 0;

    private static final short LONG_OFFSETS = 1;

    /**
     * A tag that identifies this table type.
     */
    public static final String TAG = "loca";

    private long[] offsets;

    /**
     * This will read the required data from the stream.
     * 
     * @param ttf The font that is being read.
     * @param data The stream to read the data from.
     * @throws IOException If there is an error reading the data.
     */
    public void initData(TrueTypeFont ttf, TTFDataStream data) throws IOException {
        HeaderTable head = ttf.getHeader();
        MaximumProfileTable maxp = ttf.getMaximumProfile();
        int numGlyphs = maxp.getNumGlyphs();
        offsets = new long[numGlyphs + 1];
        for (int i = 0; i < numGlyphs + 1; i++) {
            if (head.getIndexToLocFormat() == SHORT_OFFSETS) {
                offsets[i] = data.readUnsignedShort() * 2;
            } else if (head.getIndexToLocFormat() == LONG_OFFSETS) {
                offsets[i] = data.readUnsignedInt();
            } else {
                throw new IOException("Error:TTF.loca unknown offset format.");
            }
        }
    }

    /**
     * @return Returns the offsets.
     */
    public long[] getOffsets() {
        return offsets;
    }

    /**
     * @param offsetsValue The offsets to set.
     */
    public void setOffsets(long[] offsetsValue) {
        this.offsets = offsetsValue;
    }
}
