package org.happy.commons.ver1x1.patterns;

import static org.junit.Assert.assertNotNull;
import org.happy.commons.patterns.Executeable_1x0;
import org.junit.Test;

public class Executeable_1x0Test {

    @Test
    public void testExecute() {
        Executeable_1x0<Integer, String> e = new Executeable_1x0<Integer, String>() {

            public Integer execute(String parameter) {
                return null;
            }
        };
        assertNotNull(e);
    }
}
