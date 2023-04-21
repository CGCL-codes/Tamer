package com.phloc.commons.stats.visit;

import org.junit.Test;

/**
 * Test class for class {@link StatisticsWalker}.
 * 
 * @author philip
 */
public final class StatisticsWalkerTest {

    @Test
    public void testWalkStatistics() {
        StatisticsWalker.walkStatistics(new DefaultStatisticsVisitor());
    }
}
