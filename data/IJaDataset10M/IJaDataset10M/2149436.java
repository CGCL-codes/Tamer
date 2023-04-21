package org.fao.fenix.domain.label;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;

/**
 * @author VanIngen
 * 
 */
public class GaulLabelTest {

    @Test
    public void testGetLable() throws IOException {
        assertEquals(0, 0);
        assertEquals("Hala'ib triangle", GaulLabel.getLable("40760"));
        assertEquals("Administrative unit not available", GaulLabel.getLable("40776"));
        assertEquals("Yesilyurt", GaulLabel.getLable("28106"));
    }
}
