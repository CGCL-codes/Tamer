package org.apache.tika.extractor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;

/**
 * Unit tests for the {@link PlainTextExtractor} class.
 */
public class PlainTextExtractorTest extends TestCase {

    /**
     * Text extractor being tested.
     */
    private TextExtractor extractor;

    /**
     * Creates the text extractor to be tested.
     */
    protected void setUp() {
        extractor = new PlainTextExtractor();
    }

    /**
     * Tests that the extractor supportes <code>text/plain</code>.
     */
    public void testContentTypes() {
        Set types = new HashSet();
        types.addAll(Arrays.asList(extractor.getContentTypes()));
        assertTrue("PlainTextExtractor does not support text/plain", types.contains("text/plain"));
        assertEquals("PlainTextExtractor supports unknown content types", 1, types.size());
    }

    /**
     * Tests that the extractor correctly handles an empty stream.
     *
     * @throws IOException on IO errors
     */
    public void testEmptyStream() throws IOException {
        Reader reader = extractor.extractText(new ByteArrayInputStream(new byte[0]), "text/plain", null);
        assertEquals("", ExtractorHelper.read(reader));
    }

    /**
     * Tests that the extractor correctly handles a normal stream.
     *
     * @throws IOException on IO errors
     */
    public void testNormalStream() throws IOException {
        String text = "some test content";
        Reader reader = extractor.extractText(new ByteArrayInputStream(text.getBytes()), "text/plain", null);
        assertEquals(text, ExtractorHelper.read(reader));
    }

    /**
     * Tests that the extractor correctly handles unsupported encodings.
     *
     * @throws IOException on IO errors
     */
    public void testUnsupportedEncoding() throws IOException {
        try {
            String text = "some test content";
            Reader reader = extractor.extractText(new ByteArrayInputStream(text.getBytes()), "text/plain", "unsupported");
            assertEquals("", ExtractorHelper.read(reader));
        } catch (UnsupportedEncodingException e) {
            fail("PlainTextExtractor does not handle unsupported encodings");
        }
    }
}
