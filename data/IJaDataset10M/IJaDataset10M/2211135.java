package net.sourceforge.cruisecontrol.publishers;

import java.io.File;
import junit.framework.TestCase;
import net.sourceforge.cruisecontrol.CruiseControlException;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import net.sourceforge.cruisecontrol.builders.Property;

public class HTMLEmailPublisherTest extends TestCase {

    private HTMLEmailPublisher publisher;

    private File tmpFile;

    public void setUp() {
        publisher = new HTMLEmailPublisher();
    }

    private class BrokenTestPublisher extends HTMLEmailPublisher {

        private String[] newXslFileNames = null;

        BrokenTestPublisher() {
            setXSLFileNames(newXslFileNames);
        }
    }

    /**
     * test is set up this way because I expect setXSLFileNames to be used
     * from derived clases, so the test mirrors the expected use.
     */
    public void testSetXSLFileNames() {
        try {
            new BrokenTestPublisher();
            fail("setXSLFileNames should fail when called with null");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testSetLogDir() {
        try {
            publisher.setLogDir(null);
            fail("setLogDir should fail when called with null");
        } catch (IllegalArgumentException e) {
        }
    }

    private void checkXslFileNamesExistInDir(String dir, String[] fileNames) {
        assertNotNull("getXslFileNames() returned null", fileNames);
        for (int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            File file = new File(dir, fileName);
            assertTrue("file should exist: " + file.getAbsolutePath(), file.exists());
            assertTrue("value shouldn't be a directory: " + file.getAbsolutePath(), file.isFile());
        }
    }

    public void testSetXSLFileList() throws IOException {
        try {
            publisher.setXSLFileList(null);
            fail("setXSLFileList should fail when called with null");
        } catch (IllegalArgumentException expected) {
        }
        try {
            publisher.setXSLFileList("");
            fail("setXSLFileList should fail if specified xslFileList file is empty");
        } catch (IllegalArgumentException expected) {
        }
        tmpFile = File.createTempFile("HTMLEmailPublisherTest", null);
        tmpFile.deleteOnExit();
        String xsldir = tmpFile.getParent();
        publisher.setXSLDir(xsldir);
        publisher.setXSLFileList(tmpFile.getName());
        String[] newFileNames = publisher.getXslFileNames();
        checkXslFileNamesExistInDir(xsldir, newFileNames);
        assertEquals(1, newFileNames.length);
        publisher.setXSLFileList("  ,, " + tmpFile.getName() + "   ,,," + tmpFile.getName());
        newFileNames = publisher.getXslFileNames();
        checkXslFileNamesExistInDir(xsldir, newFileNames);
        assertEquals(2, newFileNames.length);
        publisher.setXSLFileList("+" + tmpFile.getName());
        newFileNames = publisher.getXslFileNames();
        checkXslFileNamesExistInDir(xsldir, newFileNames);
        assertEquals(3, newFileNames.length);
        publisher.setXSLFileList("     +" + tmpFile.getName());
        newFileNames = publisher.getXslFileNames();
        checkXslFileNamesExistInDir(xsldir, newFileNames);
        assertEquals(4, newFileNames.length);
    }

    public void testCreateLinkLine() {
        String serverURL = "http://myserver/context/servlet";
        publisher.setBuildResultsURL(serverURL);
        String path = "logs" + File.separator;
        String date = "20020607115519";
        String label = "mylabel.100";
        String successFilePrefix = "log" + date + "L" + label;
        String successURL = serverURL + "?log=" + successFilePrefix;
        String successLink = "View results here -> <a href=\"" + successURL + "\">" + successURL + "</a>";
        String successFile = successFilePrefix + ".xml";
        String successLogFileName = path + successFile;
        assertEquals(successLink, publisher.createLinkLine(successLogFileName));
        publisher.setBuildResultsURL(null);
        assertEquals("", publisher.createLinkLine(successLogFileName));
        publisher.setBuildResultsURL(serverURL);
        String failFilePrefix = "log" + date;
        String failURL = serverURL + "?log=" + failFilePrefix;
        String failLink = "View results here -> <a href=\"" + failURL + "\">" + failURL + "</a>";
        String failFile = failFilePrefix + ".xml";
        String failLogFileName = path + failFile;
        assertEquals(failLink, publisher.createLinkLine(failLogFileName));
    }

    public void testQuestionMarkInBuildResultsURL() {
        String serverURL = "http://myserver/context/servlet?key=value";
        publisher.setBuildResultsURL(serverURL);
        String path = "logs" + File.separator;
        String date = "20020607115519";
        String label = "mylabel.100";
        String successFilePrefix = "log" + date + "L" + label;
        String successURL = serverURL + "&log=" + successFilePrefix;
        String successLink = "View results here -> <a href=\"" + successURL + "\">" + successURL + "</a>";
        String successFile = successFilePrefix + ".xml";
        String successLogFileName = path + successFile;
        assertEquals(successLink, publisher.createLinkLine(successLogFileName));
    }

    public void testValidate() {
        setEmailPublisherVariables(publisher);
        String[] origFileNames = publisher.getXslFileNames();
        try {
            publisher.validate();
            fail("should fail if xslFileNames is null");
        } catch (CruiseControlException ex) {
        }
        publisher.setXSLFileNames(origFileNames);
        publisher.setXSLFile("this file doesn't exist");
        try {
            publisher.validate();
            fail("should fail if the specified xslFile doesn't exist");
        } catch (CruiseControlException ex) {
        }
        publisher.setXSLFileNames(origFileNames);
        publisher.setXSLFileList("this-file-doesn't-exist");
        try {
            publisher.validate();
            fail("should fail if specified xslFileList file doesn't exist");
        } catch (CruiseControlException ex) {
        }
        publisher.setXSLFileNames(origFileNames);
        publisher.setXSLFileList("+this-file-doesn't-exist");
        try {
            publisher.validate();
            fail("should fail if sepcified xslFileList file to append doesn't exist");
        } catch (CruiseControlException ex) {
        }
        publisher.setXSLFileNames(origFileNames);
        publisher.setXSLFileList(".");
        try {
            publisher.validate();
            fail("should fail if xslFileList file is a directory");
        } catch (CruiseControlException ex) {
        }
    }

    public void testGetContentType() {
        String defaultType = "text/html";
        assertEquals(defaultType, publisher.getContentType());
        publisher.setCharset("ISO-8859-1");
        String withCharset = "text/html; charset=\"ISO-8859-1\"";
        assertEquals(withCharset, publisher.getContentType());
    }

    public void testTransformWithParameter() throws IOException, TransformerException {
        String expected = "with.a.value";
        Property param = publisher.createParameter();
        param.setName("some.parameter");
        param.setValue(expected);
        Source xsl = new StreamSource(new StringReader("<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n" + "<xsl:output method='text'/>\n" + "<xsl:param name='some.parameter'/>\n" + "<xsl:template match='/'>\n" + "  <xsl:value-of select='$some.parameter' />\n" + "</xsl:template>" + "</xsl:stylesheet>"));
        Source xml = new StreamSource(new StringReader("<cruisecontrol/>"));
        String message = publisher.transformFile(xml, TransformerFactory.newInstance(), xsl);
        assertEquals(expected, message);
    }

    private void setEmailPublisherVariables(HTMLEmailPublisher htmlemailpublisher) {
        htmlemailpublisher.setBuildResultsURL("url");
        htmlemailpublisher.setMailHost("host");
        htmlemailpublisher.setReturnAddress("address");
        htmlemailpublisher.setLogDir(".");
        htmlemailpublisher.setXSLDir(".");
    }
}
