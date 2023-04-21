package com.phloc.commons.collections.iterate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.NoSuchElementException;
import org.junit.Test;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link SingleElementEnumeration}
 * 
 * @author philip
 */
public final class SingleElementEnumerationTest {

    @Test
    public void testAll() {
        final SingleElementEnumeration<String> eit = new SingleElementEnumeration<String>("any");
        assertTrue(eit.hasMoreElements());
        assertEquals("any", eit.nextElement());
        try {
            eit.nextElement();
            fail();
        } catch (final NoSuchElementException ex) {
        }
        assertFalse(eit.hasMoreElements());
        PhlocTestUtils.testDefaultImplementationWithEqualContentObject(new SingleElementEnumeration<String>("any"), new SingleElementEnumeration<String>("any"));
        PhlocTestUtils.testDefaultImplementationWithDifferentContentObject(new SingleElementEnumeration<String>("any"), new SingleElementEnumeration<String>("any2"));
        PhlocTestUtils.testDefaultImplementationWithDifferentContentObject(new SingleElementEnumeration<String>("any"), new SingleElementEnumeration<Integer>(Integer.valueOf(1)));
    }
}
