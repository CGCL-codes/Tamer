package org.hl7.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.ElementNameAndAttributeQualifier;
import org.custommonkey.xmlunit.XMLTestCase;
import org.hl7.meta.Datatype;
import org.hl7.meta.impl.SimpleDatatypeImpl;
import org.hl7.types.ANY;
import org.hl7.types.QSET;
import org.hl7.xml.builder.RimGraphXMLSpeaker;
import org.hl7.xml.parser.StandaloneDataTypeContentHandler;

/**
 * This class defines unit test for QSET building and parsing.
 *
 * @author OWNER: Gunther Schadow
 * @author LAST UPDATE $Author: crosenthal $
 * @version revision    $Revision: 5652 $
 *          date        $Date: 2007-03-30 10:35:44 -0500 (Fri, 30 Mar 2007) $
 */
public class QSETTest extends XMLTestCase {

    public static final String HL7_URI = "urn:hl7-org:v3";

    private static Transformer transformer;

    private static Datatype datatype = new SimpleDatatypeImpl("SET<TS>");

    static {
        try {
            final TransformerFactory _transformerFactory = TransformerFactory.newInstance();
            transformer = _transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        } catch (Exception e) {
        }
    }

    public void test1() throws Exception {
        final String input = "<matt xmlns=\"" + HL7_URI + "\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><a xsi:type=\"PIVL_TS\"><period value=\"8\" unit=\"h\"/></a><a xsi:type=\"IVL_TS\" operator=\"I\"><width value=\"10\" unit=\"d\"/></a></matt>";
        final String output = "<matt xmlns=\"" + HL7_URI + "\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><a xsi:type=\"PIVL_TS\"><phase nullFlavor=\"NI\"/><period value=\"8\" unit=\"h\"/></a><a xsi:type=\"IVL_QTY\" operator=\"I\"><width value=\"10\" unit=\"d\"/></a></matt>";
        InputStream is = new ByteArrayInputStream(input.getBytes());
        QSET value = (QSET) StandaloneDataTypeContentHandler.parseValue(is, datatype);
        String fromXML = getXMLForValue(value, "a");
        DetailedDiff detailedDiff = CommonTestUtils.getInstance().doDiff(output, fromXML, new ElementNameAndAttributeQualifier());
        assertEquals("testDifferences ", 0, detailedDiff.getAllDifferences().size());
    }

    private String getXMLForValue(ANY value, String localName) throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
        StringWriter sw = new StringWriter();
        transformer.transform(new SAXSource(new RimGraphXMLSpeaker(), new RimGraphXMLSpeaker.DataValueInputSource(value, localName, datatype, "matt")), new StreamResult(sw));
        return sw.toString();
    }

    public static void main(String args[]) throws Exception {
        new QSETTest().test1();
    }
}
