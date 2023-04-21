package org.jcvi.common.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jcvi.common.core.Range;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author dkatzel
 *
 *
 */
public class TestRangeComparatorLongestToShortest extends AbstractTestSizeRangeComparator {

    @Test
    public void sort() {
        Collections.sort(ranges, Range.Comparators.LONGEST_TO_SHORTEST);
        List<Range> expectedOrder = Arrays.asList(large, medium, small);
        assertEquals(expectedOrder, ranges);
    }
}
