package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author <a href="mailto:david@steadystate.co.uk">David Schweinsberg</a>
 * @version $Id: LangSys.java,v 1.1 2005/11/21 09:51:23 dev Exp $
 */
public class LangSys {

    private int lookupOrder;

    private int reqFeatureIndex;

    private int featureCount;

    private int[] featureIndex;

    /** Creates new LangSys */
    protected LangSys(RandomAccessFile raf) throws IOException {
        lookupOrder = raf.readUnsignedShort();
        reqFeatureIndex = raf.readUnsignedShort();
        featureCount = raf.readUnsignedShort();
        featureIndex = new int[featureCount];
        for (int i = 0; i < featureCount; i++) {
            featureIndex[i] = raf.readUnsignedShort();
        }
    }

    protected boolean isFeatureIndexed(int n) {
        for (int i = 0; i < featureCount; i++) {
            if (featureIndex[i] == n) {
                return true;
            }
        }
        return false;
    }
}
