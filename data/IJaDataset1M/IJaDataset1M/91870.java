package com.volantis.xml.utilities.sax.stream;

import junit.framework.TestCase;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.TreeMap;

/**
 * Tests the AddRootElementReader class
 */
public class AddRootElementReaderTestCase extends TestCase {

    /**
     * The namespaces to use
     */
    private TreeMap namespaces;

    public AddRootElementReaderTestCase(String name) {
        super(name);
    }

    /**
     * Set up the namespaces
     */
    public void setUp() {
        namespaces = new TreeMap();
        namespaces.put("", "http://defaultNS");
        namespaces.put("aaaa", "http://otherNS");
    }

    /**
     * Tests the reading of an empty reader
     */
    public void testReadBufferEmpty() {
        Reader reader = doConfig("", null, null);
        String result = doRead(reader, -1);
        assertEquals("<fragment></fragment>", result);
    }

    /**
     * Tests the reading of an empty reader with namespaces
     */
    public void testReadBufferEmptyNS() {
        Reader reader = doConfig("", null, namespaces);
        String result = doRead(reader, -1);
        assertEquals("<fragment xmlns=\"http://defaultNS\" xmlns:aaaa=\"http://otherNS\"></fragment>", result);
    }

    /**
     * Tests the reading of characters from the stream
     */
    public void testReadBuffer() {
        String input = "<numbers>123456789012</numbers>";
        Reader reader = doConfig(input, null, null);
        String result = doRead(reader, -1);
        assertEquals("<fragment>" + input + "</fragment>", result);
    }

    /**
     * Tests the reading of characters from the reader with Namespaces
     */
    public void testReadBufferNS() {
        String input = "<numbers>123456789012</numbers>";
        Reader reader = doConfig(input, null, namespaces);
        String result = doRead(reader, -1);
        assertEquals("<fragment xmlns=\"http://defaultNS\" xmlns:aaaa=\"http://otherNS\">" + input + "</fragment>", result);
    }

    /**
     * Test more than one read to the underlying Reader
     */
    public void testMultipleRead() {
        String input = "<numbers>123456789012</numbers>";
        Reader reader = doConfig(input, null, null);
        StringBuffer answer = new StringBuffer();
        String result = doRead(reader, 3);
        answer.append(result);
        result = doRead(reader, -1);
        answer.append(result);
        assertEquals("Output is not as expected", "<fragment>" + input + "</fragment>", answer.toString());
    }

    /**
     * Test more than one read to the underlying Reader with namespaces
     */
    public void testMultipleReadNS() {
        String input = "<numbers>123456789012</numbers>";
        Reader reader = doConfig(input, null, namespaces);
        StringBuffer answer = new StringBuffer();
        String result = doRead(reader, 3);
        answer.append(result);
        result = doRead(reader, -1);
        answer.append(result);
        assertEquals("Output is not as expected", "<fragment xmlns=\"http://defaultNS\" xmlns:aaaa=\"http://otherNS\">" + input + "</fragment>", answer.toString());
    }

    /**
     * Configure a reader ready for testing.
     *
     * @param streamContents
     * @param namespaces
     * @return
     */
    public Reader doConfig(String streamContents, String namespacePrefix, Map namespaces) {
        ByteArrayInputStream bae = new ByteArrayInputStream(streamContents.getBytes());
        Reader r = new InputStreamReader(bae);
        Reader result = null;
        try {
            if (namespaces == null || namespaces.size() < 1) {
                result = new AddRootElementReader(r);
            } else {
                result = new AddRootElementReader(r, namespacePrefix, namespaces);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create new AddRootElementReader: " + e.toString());
        }
        return result;
    }

    /**
     * Read the specified number of bytes from the specified reader. If
     * numBytes < 0 then read all from the input stream
     *
     * @param reader       the reader to read from
     * @param numBytes the number of bytes to read or -1 for all bytes
     * @return the contents of the inputstream as a string
     */
    public String doRead(Reader reader, int numBytes) {
        StringBuffer result = new StringBuffer();
        try {
            if (numBytes >= 0) {
                int value = 0;
                int count = 0;
                while (count < numBytes && (value = reader.read()) != -1) {
                    result.append((char) value);
                    count++;
                }
            } else {
                int value = 0;
                while ((value = reader.read()) != -1) {
                    result.append((char) value);
                }
            }
        } catch (IOException ie) {
            ie.printStackTrace();
            fail("Failed to read from AddRootElementReader: " + ie.toString());
        }
        return result.toString();
    }
}
