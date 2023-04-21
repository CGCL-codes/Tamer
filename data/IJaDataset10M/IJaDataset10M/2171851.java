package eu.keep.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import org.xml.sax.SAXException;
import eu.keep.softwarearchive.pathway.Pathway;

/**
 * A Junit Test class for {@link eu.keep.util.XMLUtilities }
 * @author Bram Lohman
 */
public class TestXMLUtilities {

    @Test
    public void testValidateXML() throws IOException, SAXException {
        File source = new File("./testData/digitalObjects/text.txt.xml");
        File schema = new File("./resources/external/softwarearchive/PathwaySchema.xsd");
        boolean result = XMLUtilities.validateXML(schema, source);
        assertTrue("Validation not succesful", result);
    }

    @Test
    public void testLoadFromXML() throws IOException, SAXException, JAXBException {
        File source = new File("./testData/digitalObjects/text.txt.xml");
        Pathway pw = (Pathway) XMLUtilities.loadFromXML(source);
        assertEquals("Pathway not loaded correctly", "QBasic", pw.getApplication().getName());
    }

    @Test
    public void testSaveToXML() throws IOException, SAXException, JAXBException {
        File source = new File("./testData/digitalObjects/text.txt.xml");
        Pathway pw = (Pathway) XMLUtilities.loadFromXML(source);
        assertEquals("Pathway not loaded correctly", "QBasic", pw.getApplication().getName());
        String tmpDir = System.getProperty("java.io.tmpdir");
        File target = new File(tmpDir + "/pathway.xml");
        XMLUtilities.saveToXML(pw, target);
        assertTrue("File not written", target.exists());
    }
}
