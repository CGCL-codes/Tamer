package com.potix.image;

import junit.framework.*;
import java.util.*;
import java.io.*;

public class ImageTest extends TestCase {

    private static final String FILE1 = "/metainfo/com/potix/image/sample.gif";

    public ImageTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(ImageTest.class);
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void testImage() throws Exception {
        final Image img = new AImage(ImageTest.class.getResourceAsStream(FILE1));
        assertEquals("gif", img.getFormat());
        assertEquals(new Integer(15), new Integer(img.getWidth()));
        assertEquals(new Integer(13), new Integer(img.getHeight()));
    }
}
