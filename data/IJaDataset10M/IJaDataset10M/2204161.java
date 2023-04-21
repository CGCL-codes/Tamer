package org.happy.commons.patterns.prototype;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class Cloner_1x0Test {

    @Test
    public void testClone() {
        Cloner_1x0<Integer> c = new Cloner_1x0<Integer>() {

            public Integer clone(Integer obj) {
                return null;
            }
        };
        assertNotNull(c);
    }
}
