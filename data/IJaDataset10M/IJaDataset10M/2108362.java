package net.sf.sanity4j.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import net.sf.sanity4j.gen.checkstyle_4_4.Checkstyle;
import net.sf.sanity4j.gen.checkstyle_4_4.File;

/**
 * JaxbMarshaller_Test - unit test for JaxbMarshaller.
 *
 * @author Yiannis Paschalidis
 * @since Sanity4J 1.0
 */
public class JaxbMarshaller_Test extends TestCase {

    /** A temporary file to write the test data to. */
    private java.io.File tempFile;

    /** The target java package for unmarshalling the test data. */
    private static final String TARGET_PACKAGE = "net.sf.sanity4j.gen.checkstyle_4_4";

    /** The XML data used for testing. */
    private static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<checkstyle version=\"4.4\">\n" + "    <file name=\"/someDir/SomeJavaFile.java\">\n" + "        <error line=\"33\" column=\"53\" severity=\"warning\" message=\"&apos;;&apos; is preceded with whitespace.\" source=\"com.puppycrawl.tools.checkstyle.checks.whitespace.NoWhitespaceBeforeCheck\"/>\n" + "        <error line=\"33\" column=\"67\" severity=\"warning\" message=\"&apos;;&apos; is preceded with whitespace.\" source=\"com.puppycrawl.tools.checkstyle.checks.whitespace.NoWhitespaceBeforeCheck\"/>\n" + "        <error line=\"33\" column=\"69\" severity=\"warning\" message=\"&apos;;&apos; is followed by whitespace.\" source=\"com.puppycrawl.tools.checkstyle.checks.whitespace.EmptyForIteratorPadCheck\"/>\n" + "    </file>\n" + "</checkstyle>\n";

    public void setUp() throws Exception {
        super.setUp();
        tempFile = java.io.File.createTempFile("JaxbMarshaller_Test", ".xml");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        if (!tempFile.delete()) {
            throw new IOException("Failed to delete temp file: " + tempFile.getPath());
        }
    }

    public void testUnmarshalMissingFile() {
        try {
            java.io.File file = new java.io.File("JaxbMarshaller_Test.nonExistantFile");
            JaxbMarshaller.unmarshal(file, TARGET_PACKAGE, "http://net.sf.sanity4j/namespace/dummy");
            fail("Should have thrown a QAException");
        } catch (QAException expected) {
            assertNotNull("Thrown exception should contain a message", expected.getMessage());
            assertTrue("Throws exception should contain the original cause", expected.getCause() instanceof IOException);
        }
    }

    public void testUnmarshallMalformedFile() throws IOException {
        try {
            FileUtil.writeToFile(XML.substring(0, XML.length() / 2), tempFile);
            JaxbMarshaller.unmarshal(tempFile, TARGET_PACKAGE, "http://net.sf.sanity4j/namespace/dummy");
            fail("Should have thrown a QAException");
        } catch (QAException expected) {
            assertNotNull("Thrown exception should contain a message", expected.getMessage());
            assertNotNull("Throws exception should contain the original cause", expected.getCause());
        }
    }

    public void testUnmarshall() throws IOException {
        FileUtil.writeToFile(XML, tempFile);
        Object result = JaxbMarshaller.unmarshal(tempFile, TARGET_PACKAGE, "http://net.sf.sanity4j/namespace/checkstyle-4.4");
        assertNotNull("unmarshall returned null", result);
        assertTrue("Incorrect type unmarshalled", Checkstyle.class.isAssignableFrom(result.getClass()));
        Checkstyle checkStyle = (Checkstyle) result;
        assertEquals("Incorrect version field value unmarshalled", new BigDecimal("4.4"), checkStyle.getVersion());
        assertEquals("Incorrect number of files unmarshalled", 1, checkStyle.getFile().size());
        File file = checkStyle.getFile().get(0);
        assertEquals("Incorrect file name value unmarshalled", "/someDir/SomeJavaFile.java", file.getName());
        List<Object> errors = new ArrayList<Object>();
        for (Object obj : file.getContent()) {
            if (obj instanceof net.sf.sanity4j.gen.checkstyle_4_4.Error) {
                errors.add(obj);
            }
        }
        assertEquals("Incorrect number of errors unmarshalled", 3, errors.size());
    }
}
