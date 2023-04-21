package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @version $Id: CmapFormat6.java,v 1.1 2005/11/21 09:51:23 dev Exp $
 * @author <a href="mailto:david@steadystate.co.uk">David Schweinsberg</a>
 */
public class CmapFormat6 extends CmapFormat {

    private short format;

    private short length;

    private short version;

    private short firstCode;

    private short entryCount;

    private short[] glyphIdArray;

    protected CmapFormat6(RandomAccessFile raf) throws IOException {
        super(raf);
        format = 6;
    }

    public int getFirst() {
        return 0;
    }

    public int getLast() {
        return 0;
    }

    public int mapCharCode(int charCode) {
        return 0;
    }
}
