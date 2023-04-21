package com.phloc.commons.state;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test class for class {@link ETopBottom}.
 * 
 * @author philip
 */
public final class ETopBottomTest {

    @Test
    public void testAll() {
        for (final ETopBottom e : ETopBottom.values()) assertSame(e, ETopBottom.valueOf(e.name()));
        assertTrue(ETopBottom.TOP.isTop());
        assertFalse(ETopBottom.TOP.isBottom());
        assertFalse(ETopBottom.BOTTOM.isTop());
        assertTrue(ETopBottom.BOTTOM.isBottom());
    }
}
