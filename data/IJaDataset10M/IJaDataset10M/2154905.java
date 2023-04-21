package net.sf.mpxj.mpp;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents the Props files found in Microsoft Project MPP9 files.
 */
final class Props9 extends Props {

    /**
    * Constructor, reads the property data from an input stream.
    *
    * @param is input stream for reading props data
    */
    Props9(InputStream is) throws IOException {
        byte[] header = new byte[16];
        byte[] data;
        is.read(header);
        int headerCount = MPPUtility.getShort(header, 12);
        int foundCount = 0;
        int availableBytes = is.available();
        while (foundCount < headerCount) {
            int itemSize = readInt(is);
            int itemKey = readInt(is);
            readInt(is);
            availableBytes -= 12;
            if (availableBytes < itemSize || itemSize < 1) {
                break;
            }
            data = new byte[itemSize];
            is.read(data);
            availableBytes -= itemSize;
            m_map.put(Integer.valueOf(itemKey), data);
            ++foundCount;
            if (data.length % 2 != 0) {
                is.skip(1);
            }
        }
    }
}
