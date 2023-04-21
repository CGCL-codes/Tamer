package net.sf.mpxj.mpp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class reads in the data from a VarMeta block. This block contains
 * meta data about variable length data items stored in a Var2Data block.
 * The meta data allows the size of the Var2Data block to be determined,
 * along with the number of data items it contains, identifiers for each item,
 * and finally the offset of each item within the block.
 */
final class VarMeta9 extends AbstractVarMeta {

    /**
    * Constructor. Extracts that makes up this block from the input stream.
    *
    * @param is Input stream from whic data is read
    * @throws IOException on file read error
    */
    VarMeta9(InputStream is) throws IOException {
        if (readInt(is) != MAGIC) {
            throw new IOException("Bad magic number");
        }
        m_unknown1 = readInt(is);
        m_itemCount = readInt(is);
        m_unknown2 = readInt(is);
        m_unknown3 = readInt(is);
        m_dataSize = readInt(is);
        Integer uniqueID;
        Integer type;
        Integer offset;
        Map<Integer, Integer> map;
        m_offsets = new int[m_itemCount];
        byte[] uniqueIDArray = new byte[4];
        for (int loop = 0; loop < m_itemCount; loop++) {
            is.read(uniqueIDArray, 0, 3);
            uniqueID = Integer.valueOf(MPPUtility.getInt(uniqueIDArray));
            type = Integer.valueOf(readByte(is));
            offset = Integer.valueOf(readInt(is));
            map = m_table.get(uniqueID);
            if (map == null) {
                map = new TreeMap<Integer, Integer>();
                m_table.put(uniqueID, map);
            }
            map.put(type, offset);
            m_offsets[loop] = offset.intValue();
        }
    }

    private static final int MAGIC = 0xFADFADBA;
}
