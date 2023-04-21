package com.nokia.ats4.appmodel.util;

import com.nokia.ats4.appmodel.MainApplication;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import junit.framework.*;

/**
 * Unit tests for KendoResources class.
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class KendoResourcesTest extends TestCase {

    public KendoResourcesTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of getString method, of class com.nokia.kendo.KendoResources.
     */
    public void testGetString() {
        System.out.println("getString");
        String key = "menu.file";
        String expResult = "File";
        String result = KendoResources.getString(key);
        assertEquals(expResult, result);
        result = null;
        try {
            result = KendoResources.getString("KeyThatDoesNotExist");
        } catch (MissingResourceException e) {
            assertNull(result);
        }
    }

    /**
     * Test of load method, of class com.nokia.kendo.KendoResources.
     */
    public void testLoadLanguageFile() {
        System.out.println("loadLanguageFile");
        ResourceBundle rb = null;
        try {
            rb = KendoResources.load("invalid.filepath");
            fail("Did not throw exception as expected");
        } catch (IOException e) {
            assertNull(rb);
        } catch (Exception e) {
            fail("Caught an unexpected exception");
        }
        try {
            rb = KendoResources.load(MainApplication.FILE_LANGUAGE);
            assertNotNull(rb);
        } catch (IOException ex) {
            fail("Caught an IOException");
        }
    }
}
