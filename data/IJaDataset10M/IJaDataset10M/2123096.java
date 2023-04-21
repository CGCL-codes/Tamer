package eu.medsea.mimeutil.detector;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import eu.medsea.mimeutil.MimeUtil2;
import eu.medsea.mimeutil.TextMimeDetector;
import eu.medsea.mimeutil.TextMimeType;
import eu.medsea.mimeutil.handler.TextMimeHandler;
import eu.medsea.util.EncodingGuesser;
import eu.medsea.util.StringUtil;
import junit.framework.TestCase;

public class TextMimeDetectorTest extends TestCase {

    static {
    }

    MimeUtil2 mimeUtil = new MimeUtil2();

    public void setUp() {
        EncodingGuesser.setSupportedEncodings(EncodingGuesser.getCanonicalEncodingNamesSupportedByJVM());
    }

    public void tearDown() {
        EncodingGuesser.setSupportedEncodings(new ArrayList());
    }

    public void testBoundaryCases() {
        assertEquals(mimeUtil.getMimeTypes(new File("src/test/resources/porrasturvat-1.0.3.tar.gz")), "application/octet-stream");
    }

    public void testGetMimeTypesFile() {
        assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/a.html")).contains("text/plain"));
        assertFalse(mimeUtil.getMimeTypes(new File("src/test/resources/b-jpg.img")).contains("text/plain"));
        assertFalse(mimeUtil.getMimeTypes(new File("src/test/resources/b.jpg")).contains("text/plain"));
        assertFalse(mimeUtil.getMimeTypes(new File("src/test/resources/c-gif.img")).contains("text/plain"));
        assertFalse(mimeUtil.getMimeTypes(new File("src/test/resources/c.gif")).contains("text/plain"));
        assertFalse(mimeUtil.getMimeTypes(new File("src/test/resources/d-png.img")).contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/e-svg.img")).contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/e.svg")).contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/e.xml")).contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/e[xml]")).contains("text/plain"));
        assertFalse(mimeUtil.getMimeTypes(new File("src/test/resources/f.tar.gz")).contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/log4j.properties")).contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/magic.mime")).contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/mime-types.properties")).contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/plaintext")).contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/plaintext.txt")).contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/test.bin")).contains("text/plain"));
    }

    public void testGetMimeTypesString() {
        assertTrue(mimeUtil.getMimeTypes("src/test/resources/a.html").contains("text/plain"));
        assertFalse(mimeUtil.getMimeTypes("src/test/resources/b-jpg.img").contains("text/plain"));
        assertFalse(mimeUtil.getMimeTypes("src/test/resources/b.jpg").contains("text/plain"));
        assertFalse(mimeUtil.getMimeTypes("src/test/resources/c-gif.img").contains("text/plain"));
        assertFalse(mimeUtil.getMimeTypes("src/test/resources/c.gif").contains("text/plain"));
        assertFalse(mimeUtil.getMimeTypes("src/test/resources/d-png.img").contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes("src/test/resources/e-svg.img").contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes("src/test/resources/e.svg").contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes("src/test/resources/e.xml").contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes("src/test/resources/e[xml]").contains("text/plain"));
        assertFalse(mimeUtil.getMimeTypes("src/test/resources/f.tar.gz").contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes("src/test/resources/log4j.properties").contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes("src/test/resources/magic.mime").contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes("src/test/resources/mime-types.properties").contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes("src/test/resources/plaintext").contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes("src/test/resources/plaintext.txt").contains("text/plain"));
        assertTrue(mimeUtil.getMimeTypes("src/test/resources/test.bin").contains("text/plain"));
    }

    public void testGetMimeTypesStringWithExtensionMimeDetector() {
        try {
            mimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector");
            assertTrue(mimeUtil.getMimeTypes("src/test/resources/a.html").contains("text/plain"));
            assertFalse(mimeUtil.getMimeTypes("src/test/resources/b-jpg.img").contains("text/plain"));
            assertFalse(mimeUtil.getMimeTypes("src/test/resources/b.jpg").contains("text/plain"));
            assertFalse(mimeUtil.getMimeTypes("src/test/resources/c-gif.img").contains("text/plain"));
            assertFalse(mimeUtil.getMimeTypes("src/test/resources/c.gif").contains("text/plain"));
            assertFalse(mimeUtil.getMimeTypes("src/test/resources/d-png.img").contains("text/plain"));
            assertFalse(mimeUtil.getMimeTypes("src/test/resources/d.png").contains("text/plain"));
            assertFalse(mimeUtil.getMimeTypes("src/test/resources/f.tar.gz").contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes("src/test/resources/e-svg.img").contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes("src/test/resources/e.svg").contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes("src/test/resources/e.xml").contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes("src/test/resources/e[xml]").contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes("src/test/resources/log4j.properties").contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes("src/test/resources/magic.mime").contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes("src/test/resources/mime-types.properties").contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes("src/test/resources/plaintext").contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes("src/test/resources/plaintext.txt").contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes("src/test/resources/test.bin").contains("text/plain"));
            Collection mimeTypes = mimeUtil.getMimeTypes("src/test/resources/plaintext.txt");
            assertTrue(mimeTypes.contains("text/plain"));
            Collection retain = new HashSet();
            retain.add("text/plain");
            mimeTypes.retainAll(retain);
            MimeType mimeType = (MimeType) mimeTypes.iterator().next();
            assertTrue(mimeType instanceof TextMimeType);
            assertTrue(((TextMimeType) mimeType).getSpecificity() == 2);
        } finally {
            mimeUtil.unregisterMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector");
        }
    }

    public void testGetMimeTypesInputStream() {
        try {
            assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/a.html").toURI().toURL().openStream()).contains("text/plain"));
            assertFalse(mimeUtil.getMimeTypes(new File("src/test/resources/b-jpg.img").toURI().toURL().openStream()).contains("text/plain"));
            assertFalse(mimeUtil.getMimeTypes(new File("src/test/resources/b.jpg").toURI().toURL().openStream()).contains("text/plain"));
            assertFalse(mimeUtil.getMimeTypes(new File("src/test/resources/c-gif.img").toURI().toURL().openStream()).contains("text/plain"));
            assertFalse(mimeUtil.getMimeTypes(new File("src/test/resources/c.gif").toURI().toURL().openStream()).contains("text/plain"));
            assertFalse(mimeUtil.getMimeTypes(new File("src/test/resources/d-png.img").toURI().toURL().openStream()).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/e-svg.img").toURI().toURL().openStream()).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/e.svg").toURI().toURL().openStream()).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/e.xml").toURI().toURL().openStream()).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/e[xml]").toURI().toURL().openStream()).contains("text/plain"));
            assertFalse(mimeUtil.getMimeTypes(new File("src/test/resources/f.tar.gz").toURI().toURL().openStream()).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/log4j.properties").toURI().toURL().openStream()).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/magic.mime").toURI().toURL().openStream()).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/mime-types.properties").toURI().toURL().openStream()).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/plaintext").toURI().toURL().openStream()).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/plaintext.txt").toURI().toURL().openStream()).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(new File("src/test/resources/test.bin").toURI().toURL().openStream()).contains("text/plain"));
        } catch (Exception e) {
            fail("Should never get here");
        }
    }

    public void testGetMimeTypesInputStreamAndEnsureStreamIsReset() {
        try {
            InputStream in = (new File("src/test/resources/a.html").toURI().toURL()).openStream();
            assertTrue(mimeUtil.getMimeTypes(in).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(in).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(in).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(in).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(in).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(in).contains("text/plain"));
            assertTrue(mimeUtil.getMimeTypes(in).contains("text/plain"));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            assertEquals("<html>", br.readLine());
        } catch (Exception e) {
            fail("Should never get here");
        }
    }

    public void testAddMimeHandler() {
        TextMimeDetector.registerTextMimeHandler(new XMLHandler());
        TextMimeDetector.registerTextMimeHandler(new SVGHandler());
        TextMimeDetector.registerTextMimeHandler(new NeverFireHandler());
        Collection c = mimeUtil.getMimeTypes("src/test/resources/e.xml");
        assertTrue(c.size() == 1);
        assertTrue(c.contains("text/xml"));
        c = mimeUtil.getMimeTypes("src/test/resources/e.svg");
        assertTrue(c.size() == 1);
        assertTrue(c.contains("image/svg+xml"));
    }

    class XMLHandler implements TextMimeHandler {

        public boolean handle(TextMimeType mimeType, String content) {
            if (content.startsWith("<?xml")) {
                mimeType.setMimeType(new MimeType("text/xml"));
                int index = content.indexOf("encoding=\"");
                if (index != -1) {
                    int endindex = content.indexOf("\"", index + 10);
                    mimeType.setEncoding(content.substring(index + 10, endindex));
                }
            }
            return false;
        }
    }

    class SVGHandler implements TextMimeHandler {

        public boolean handle(TextMimeType mimeType, String content) {
            if (mimeType.equals(new MimeType("text/xml"))) {
                if (StringUtil.contains(content, "<svg  ")) {
                    mimeType.setMimeType(new MimeType("image/svg+xml"));
                    return true;
                }
            }
            return false;
        }
    }

    class NeverFireHandler implements TextMimeHandler {

        public boolean handle(TextMimeType mimeType, String content) {
            if ("svg+xml".equals(mimeType.getSubType())) {
                mimeType.setMediaType("very-funny");
            }
            return false;
        }
    }

    public void testUnicodeAndWestern() {
        String[] encodings = { "UTF-8", "ISO-8859-1", "ISO-8859-15", "ASCII" };
        Collection c_encodings = new ArrayList();
        c_encodings.addAll(Arrays.asList(encodings));
        EncodingGuesser.setSupportedEncodings(c_encodings);
        TextMimeDetector.setPreferredEncodings(encodings);
        assertEquals(MimeUtil.getMimeTypes(new File("src/test/resources/textfiles/western")), "text/plain;charset=ISO-8859-1");
        assertEquals(MimeUtil.getMimeTypes(new File("src/test/resources/textfiles/unicode")), "text/plain;charset=UTF-8");
    }
}
