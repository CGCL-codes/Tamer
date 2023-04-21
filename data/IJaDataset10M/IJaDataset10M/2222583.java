package com.google.zxing.oned.rss.expanded;

import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 * @author Eduardo Castillejo, University of Deusto (eduardo.castillejo@deusto.es)
 */
public final class ExpandedInformationDecoderTest extends Assert {

    @Test
    public void testNoAi() throws Exception {
        BitArray information = BinaryUtil.buildBitArrayFromString(" .......X ..XX..X. X.X....X .......X ....");
        AbstractExpandedDecoder decoder = AbstractExpandedDecoder.createDecoder(information);
        String decoded = decoder.parseInformation();
        assertEquals("(10)12A", decoded);
    }
}
