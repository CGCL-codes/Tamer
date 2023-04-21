package org.happy.commons.ver1x1.patterns;

import static org.junit.Assert.assertNotNull;
import org.happy.commons.patterns.Identifiable_1x0;
import org.junit.Test;

public class Identifiable_1x0Test {

    @Test
    public void testGetID() {
        Identifiable_1x0<Long> i = new Identifiable_1x0<Long>() {

            public Long getID() {
                return null;
            }
        };
        assertNotNull(i);
    }
}
