package org.diffxml.diffxml;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.diffxml.diffxml.fmes.ParserInitialisationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DiffXMLTest {

    @Test
    public final void testSimpleDiff() {
        Diff d = DiffFactory.createDiff();
        try {
            d.diff(new File("test1a.xml"), new File("test2a.xml"));
        } catch (DiffException e) {
            fail("An exception was thrown during the diff: " + e.getMessage());
        }
    }

    /**
	 * Tests outputXML method.
	 */
    @Test
    public final void testOutputXML() {
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DOMOps.initParser(fac);
        try {
            String docString = "<?xml version=\"1.0\" encoding=\"UTF-8\"" + " standalone=\"yes\"?><x>  <y> " + " <z/> </y></x>";
            ByteArrayInputStream is = new ByteArrayInputStream(docString.getBytes("utf-8"));
            Document doc = fac.newDocumentBuilder().parse(is);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            DOMOps.outputXML(doc, os);
            assertEquals(os.toString(), docString);
        } catch (SAXException e) {
            fail("Caught SAX Exception");
        } catch (IOException e) {
            fail("Caught IOException");
        } catch (ParserConfigurationException e) {
            fail("Caught ParserConfigurationException");
        }
    }

    /**
     * Test handling document referencing non-existent DTD.
     */
    @Test
    public final void testNoDTDDiff() {
        Diff d = DiffFactory.createDiff();
        try {
            d.diff(new File("suite/error_handling/nodtd1.xml"), new File("suite/error_handling/nodtd2.xml"));
            fail("Expected exception due to non resolvable entity");
        } catch (DiffException e) {
        }
    }
}
