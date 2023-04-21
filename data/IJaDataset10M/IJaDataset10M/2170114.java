package org.custommonkey.xmlunit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * JUnit test for DoctypeInputStream
 */
public class test_DoctypeInputStream extends AbstractDoctypeTests {

    private File testFile;

    public void tearDown() {
        if (testFile != null) {
            testFile.delete();
        }
    }

    private FileInputStream testDocument(String content) throws IOException {
        testFile = File.createTempFile("xmlunit_", ".xml");
        FileOutputStream fos = new FileOutputStream(testFile);
        OutputStreamWriter w = new OutputStreamWriter(fos, "ISO-8859-1");
        w.write(content);
        w.close();
        return new FileInputStream(testFile);
    }

    private static String readFully(DoctypeInputStream dis) throws IOException {
        StringBuffer buf = new StringBuffer();
        char[] ch = new char[1024];
        int numChars;
        InputStreamReader reader = new InputStreamReader(dis, "ISO-8859-1");
        while ((numChars = reader.read(ch)) != -1) {
            buf.append(ch, 0, numChars);
        }
        return buf.toString();
    }

    protected void assertEquals(String expected, String input, String docType, String systemId) throws IOException {
        FileInputStream fis = null;
        try {
            fis = testDocument(input);
            DoctypeInputStream doctypeInputStream = new DoctypeInputStream(fis, "ISO-8859-1", docType, systemId);
            assertEquals(expected, readFully(doctypeInputStream));
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public void testGetContent() throws IOException {
        String source = "WooPDeDoO!\nGooRanga!\n plIng! ";
        DoctypeInputStream dis = new DoctypeInputStream(new java.io.StringBufferInputStream(source), null, "nonsense", "words");
        assertEquals(source, dis.getContent(null));
        assertEquals(source, dis.getContent("UTF-8"));
    }

    public test_DoctypeInputStream(String name) {
        super(name);
    }
}
