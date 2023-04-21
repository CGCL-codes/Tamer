package com.phloc.commons.text.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import org.junit.Test;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.file.FileOperations;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link XMLResourceBundle}.
 * 
 * @author philip
 */
public final class XMLResourceBundleTest extends AbstractPhlocTestCase {

    @Test
    public void testAll() throws IOException {
        final File aFile = new File("target/test-classes/unittest-xml-props.xml");
        try {
            final Properties p = new Properties();
            p.setProperty("prop1", "Value 1");
            p.setProperty("prop2", "äöü");
            p.storeToXML(FileUtils.getOutputStream(aFile), null, CCharset.CHARSET_UTF_8);
            final ResourceBundle aRB = XMLResourceBundle.getXMLBundle("unittest-xml-props");
            assertNotNull(aRB);
            assertEquals("Value 1", aRB.getString("prop1"));
            assertEquals("äöü", aRB.getString("prop2"));
            try {
                aRB.getObject("prop3");
                fail();
            } catch (final MissingResourceException ex) {
            }
        } finally {
            FileOperations.deleteFile(aFile);
        }
    }
}
