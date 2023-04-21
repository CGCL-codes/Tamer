package org.gbif.checklistbank.api.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ReferenceTest {

    @Test
    public void testEquals() {
        Reference r1 = new Reference();
        r1.setKey(123);
        r1.setLink("http://www.example.org");
        r1.setTitle("Please cite www.example.org");
        Reference r2 = new Reference();
        r2.setKey(123);
        r2.setLink("http://www.example.org");
        r2.setTitle("Please cite www.example.org");
        assertEquals(r1, r2);
        r2.setKey(124);
        assertFalse(r1.equals(r2));
    }
}
