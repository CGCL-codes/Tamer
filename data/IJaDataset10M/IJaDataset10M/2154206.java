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
import org.custommonkey.xmlunit.ElementNameAndTextQualifier;
import org.custommonkey.xmlunit.XMLTestCase;
import org.hl7.meta.Datatype;
import org.hl7.meta.impl.SimpleDatatypeImpl;
import org.hl7.types.ANY;
import org.hl7.types.SC;
import org.hl7.xml.builder.RimGraphXMLSpeaker;
import org.hl7.xml.parser.StandaloneDataTypeContentHandler;

/**
 * This class defines unit test for SC.
 *
 * @author OWNER: Scott Jiang
 * @author LAST UPDATE $Author: crosenthal $
 * @version Since HL7 SDK v1.2
 *          revision    $Revision: 5652 $
 *          date        $Date: 2007-03-30 11:35:44 -0400 (Fri, 30 Mar 2007) $
 */
public class SCTest extends XMLTestCase {

    /**
	 * Logging constant used to identify source of log entry, that could be later used to create
	 * logging mechanism to uniquely identify the logged class.
	 */
    private static final String LOGID = "$RCSfile$";

    /**
	 * String that identifies the class version and solves the serial version UID problem.
	 * This String is for informational purposes only and MUST not be made final.
	 *
	 * @see <a href="http://www.visi.com/~gyles19/cgi-bin/fom.cgi?file=63">JBuilder vice javac serial version UID</a>
	 */
    public static String RCSID = "$Header$";

    public static final String HL7_URI = "urn:hl7-org:v3";

    private static Transformer transformer;

    private static Datatype datatype = new SimpleDatatypeImpl("SC");

    static {
        try {
            final TransformerFactory _transformerFactory = TransformerFactory.newInstance();
            transformer = _transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        } catch (Exception e) {
        }
    }

    public void testSC_01() throws Exception {
        final String input = "<characterStringWithCode xmlns=\"" + HL7_URI + "\" representation=\"TXT\" code=\"30971-6\" codeSystem=\"2.16.840.1.113883.19\" displayName=\"ADVERSE EVENT\">Health Level Seven</characterStringWithCode>";
        final String output = "<characterStringWithCode xmlns=\"" + HL7_URI + "\" representation=\"TXT\" code=\"30971-6\" codeSystem=\"2.16.840.1.113883.19\" displayName=\"ADVERSE EVENT\">Health Level Seven</characterStringWithCode>";
        InputStream is = new ByteArrayInputStream(input.getBytes());
        SC value = (SC) StandaloneDataTypeContentHandler.parseValue(is, datatype);
        String fromXML = getXMLForValue(value, "characterStringWithCode");
        DetailedDiff detailedDiff = CommonTestUtils.getInstance().doDiff(output, fromXML, new ElementNameAndTextQualifier());
        assertEquals("testDifferences ", 0, detailedDiff.getAllDifferences().size());
    }

    private String getXMLForValue(ANY value, String localName) throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
        StringWriter sw = new StringWriter();
        transformer.transform(new SAXSource(new RimGraphXMLSpeaker(), new RimGraphXMLSpeaker.DataValueInputSource(value, localName, datatype)), new StreamResult(sw));
        return sw.toString();
    }
}
