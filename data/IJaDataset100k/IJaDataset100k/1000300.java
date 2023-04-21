package junitpackage;

import junit.framework.TestCase;

public class WriteToXMLFileTest extends TestCase {

    WriteToXMLFile write;

    public void setUp() {
        write = new WriteToXMLFile();
    }

    public void tearDown() {
        write = null;
    }

    public void testWriteToXML() {
        try {
            write.writeToFile();
        } catch (Exception e) {
            assertEquals("ClassNotFoundException", e.getClass(), ClassNotFoundException.class);
        }
    }
}
