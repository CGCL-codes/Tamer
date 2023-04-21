package org.databene.commons;

import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the Period class.<br/><br/>
 * Created at 02.05.2008 15:39:31
 * @since 0.4.3
 * @author Volker Bergmann
 */
public class PeriodTest {

    @Test
    public void testEquals() {
        Period second = Period.SECOND;
        assertFalse(second.equals(null));
        assertFalse(second.equals(""));
        assertTrue(second.equals(second));
        assertFalse(second.equals(Period.MINUTE));
    }
}
