package com.googlecode.ehcache.annotations;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import org.junit.Test;

/**
 * @author Eric Dalquist
 * @version $Revision: 656 $
 */
public class ParameterMaskTest {

    @Test
    public void testEmptyParameterMask() {
        final ParameterMask mask = ParameterMask.create(new boolean[0]);
        assertTrue(Arrays.equals(new boolean[0], mask.getMask()));
        assertArrayEquals(new Object[0], mask.maskParameters(new Object[0]));
        final Object[] originalParameters = new Object[0];
        mask.unmaskParameters(originalParameters, new Object[0]);
        assertArrayEquals(new Object[0], originalParameters);
        mask.unmaskParameters(originalParameters, originalParameters);
        assertEquals(32, mask.hashCode());
        assertEquals("ParameterMask [mask=[]]", mask.toString());
        assertEquals(mask, ParameterMask.create(new boolean[0]));
    }

    @Test
    public void testParameterMask() {
        final ParameterMask mask = ParameterMask.create(new boolean[] { true, false, true });
        assertTrue(Arrays.equals(new boolean[] { true, false, true }, mask.getMask()));
        assertArrayEquals(new Object[] { "a", "c" }, mask.maskParameters(new Object[] { "a", "b", "c" }));
        final Object[] originalParameters = new Object[] { "a", "b", "c" };
        mask.unmaskParameters(originalParameters, new Object[] { "a", "3" });
        assertArrayEquals(new Object[] { "a", "b", "3" }, originalParameters);
        mask.unmaskParameters(originalParameters, originalParameters);
        assertEquals(1252391, mask.hashCode());
        assertEquals("ParameterMask [mask=[true, false, true]]", mask.toString());
        assertEquals(mask, ParameterMask.create(new boolean[] { true, false, true }));
    }
}
