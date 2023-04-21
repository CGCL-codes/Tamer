package com.rhythm.commons.io.filters;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mlee
 */
public class HiddenDirectoryFileFilterTest {

    @Test
    public void testGetIntanceIsSingleton() {
        assertEquals(HiddenDirectoryFileFilter.getInstance(), HiddenDirectoryFileFilter.getInstance());
    }

    @Test
    public void testFilter() {
    }
}
