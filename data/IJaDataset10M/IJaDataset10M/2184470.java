package org.viewaframework.util;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.w3c.dom.Document;

public class ViewActionDescriptorExpressionsHandlerTest extends TestCase {

    private ViewActionDescriptorExpressionsHandler handler;

    private static final List<String> actionDescriptorsAsXPath = Arrays.asList("/rootNode[@text='rootNode']", "/rootNode/files[@text='files']", "/rootNode/config[@text='config']");

    private static final String xmlDocumentAsString = "<rootNode><files text='files'/><config text='config'/></rootNode>";

    public void setUp() {
        handler = new ViewActionDescriptorExpressionsHandler();
    }

    public void testGetXmlFromList() throws Exception {
        Document document = handler.getXML(actionDescriptorsAsXPath);
        assertNotNull(document);
        assertTrue(document.getElementsByTagName("rootNode").getLength() == 1);
        assertEquals(document.getElementsByTagName("files").item(0).getAttributes().getNamedItem("text").getTextContent(), "files");
    }

    public void testGetXmlFromReader() throws Exception {
        StringReader reader = new StringReader(xmlDocumentAsString);
        Document document = handler.getXML(reader);
        assertNotNull(document);
        assertTrue(document.getElementsByTagName("rootNode").getLength() == 1);
        assertEquals(document.getElementsByTagName("files").item(0).getAttributes().getNamedItem("text").getTextContent(), "files");
    }

    public void testGetXmlAsString() throws Exception {
        StringReader reader = new StringReader(xmlDocumentAsString);
        Document document = handler.getXML(reader);
        String result = handler.getXMLAsString(document);
        String one = xmlDocumentAsString.replaceAll(" ", "");
        String another = result.replaceAll("<\\?.*\\?>", "").replaceAll(" ", "").replaceAll("\n", "").replaceAll("\"", "'");
        assertEquals(one, another);
    }
}
