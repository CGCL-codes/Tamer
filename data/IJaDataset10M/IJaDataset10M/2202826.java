package com.google.zxing.oned.rss.expanded;

import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.DataCharacter;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 * @author Eduardo Castillejo, University of Deusto (eduardo.castillejo@deusto.es)
 */
public final class BitArrayBuilderTest extends Assert {

    @Test
    public void testBuildBitArray1() {
        int[][] pairValues = { { 19 }, { 673, 16 } };
        String expected = " .......X ..XX..X. X.X....X .......X ....";
        checkBinary(pairValues, expected);
    }

    private static void checkBinary(int[][] pairValues, String expected) {
        BitArray binary = buildBitArray(pairValues);
        assertEquals(expected, binary.toString());
    }

    private static BitArray buildBitArray(int[][] pairValues) {
        List<ExpandedPair> pairs = new ArrayList<ExpandedPair>();
        for (int i = 0; i < pairValues.length; ++i) {
            int[] pair = pairValues[i];
            DataCharacter leftChar;
            if (i == 0) {
                leftChar = null;
            } else {
                leftChar = new DataCharacter(pair[0], 0);
            }
            DataCharacter rightChar;
            if (i == 0) {
                rightChar = new DataCharacter(pair[0], 0);
            } else if (pair.length == 2) {
                rightChar = new DataCharacter(pair[1], 0);
            } else {
                rightChar = null;
            }
            ExpandedPair expandedPair = new ExpandedPair(leftChar, rightChar, null, true);
            pairs.add(expandedPair);
        }
        return BitArrayBuilder.buildBitArray(pairs);
    }
}
