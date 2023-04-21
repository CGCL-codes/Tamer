package com.volantis.map.ics.imageprocessor.utilities;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.File;

public class FileUtilitiesTestCase extends TestCase {

    public void testGetExtension() {
        File file = new File("test.svg");
        String expectedExtension = "svg";
        String actualExtension = FileUtilities.getExtension(file);
        String message = "Extensions are not the same";
        assertEquals(message, expectedExtension, actualExtension);
    }

    public static Test suite() {
        return new TestSuite(FileUtilitiesTestCase.class);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}
