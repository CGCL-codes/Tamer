package net.sf.doolin.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Damien Coraboeuf
 */
public class TestScreenUtils {

    @BeforeClass
    public static void init() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    public void testIsHeadless() {
        assertTrue(ScreenUtils.isHeadless());
    }

    @Test
    public void testGetScreenResolution() {
        assertEquals(ScreenUtils.DEFAULT_SCREEN_RESOLUTION, ScreenUtils.getScreenResolution());
    }
}
