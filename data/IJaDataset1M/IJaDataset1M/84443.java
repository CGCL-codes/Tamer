package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Simple Macintosh cmap table, mapping only the ASCII character set to glyphs.
 *
 * @version $Id: CmapFormat0.java,v 1.1 2005/11/21 09:51:23 dev Exp $
 * @author <a href="mailto:david@steadystate.co.uk">David Schweinsberg</a>
 */
public class CmapFormat0 extends CmapFormat {

    private int[] glyphIdArray = new int[256];

    private int first, last;

    protected CmapFormat0(RandomAccessFile raf) throws IOException {
        super(raf);
        format = 0;
        first = -1;
        for (int i = 0; i < 256; i++) {
            glyphIdArray[i] = raf.readUnsignedByte();
            if (glyphIdArray[i] > 0) {
                if (first == -1) first = i;
                last = i;
            }
        }
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }

    public int mapCharCode(int charCode) {
        if (0 <= charCode && charCode < 256) {
            return glyphIdArray[charCode];
        } else {
            return 0;
        }
    }
}
