package org.archive.hcc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import junit.framework.TestCase;

public class SmartPropertiesResolverTest extends TestCase {

    public void testJarredResource() {
        SmartPropertiesResolver.getProperties("/org/archive/hcc/util/test.properties");
        try {
            SmartPropertiesResolver.getProperties("/org/archive/hcc/util/test-not-found.properties");
            assertTrue(false);
        } catch (Throwable e) {
            assertTrue(true);
        }
    }

    public void testUserHomeResource() {
        File file = new File(System.getProperty("user.dir") + File.separator + "myTest.properties");
        file.deleteOnExit();
        try {
            FileOutputStream os = new FileOutputStream(file);
            os.write("test=test".getBytes());
            os.close();
            SmartPropertiesResolver.getProperties("/org/archive/hcc/util/myTest.properties");
            assertTrue(true);
        } catch (Throwable e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }
}
