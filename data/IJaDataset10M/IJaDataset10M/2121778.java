package net.sf.jschematron.test;

import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sf.jschematron.transform.SchematronTransformer;
import net.sf.jschematron.transform.SchematronTransformerFactory;
import net.sf.jschematron.util.XMLUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class SchematronTests {

    protected static Log log = LogFactory.getLog(SchematronTests.class);

    public String exampleSchematron = "test/example.sch";

    public String exampleXML = "test/example.xml";

    @Test
    @Ignore
    public void schematronTextTransform() throws TransformerException {
        log.info("schematronTextTransform:");
        TransformerFactory factory = TransformerFactory.newInstance("net.sf.jschematron.transform.SchematronTransformerFactory", ClassLoader.getSystemClassLoader());
        factory.setAttribute(SchematronTransformerFactory.SCHEMATRON_TYPE_FEATURE, SchematronTransformerFactory.SchematronType.TEXT);
        Transformer transformer = factory.newTransformer(new StreamSource(new File(exampleSchematron)));
        assertNotNull(transformer);
        transformer.setOutputProperty("method", "xml");
        transformer.transform(new StreamSource(new File("test/example.xml")), new StreamResult(System.out));
    }

    @Test
    @Ignore
    public void testXMLUtil() throws SAXException, IOException, ParserConfigurationException {
        log.info("testXMLUtil:");
        Document example = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(exampleXML);
        System.out.println(XMLUtil.serializeNode(example));
    }

    @Test
    public void testSchematronTransformer() throws TransformerException {
        StreamSource schematron = new StreamSource(new File(exampleSchematron));
        StreamSource source = new StreamSource(new File(exampleXML));
        StreamResult result = new StreamResult(System.out);
        SchematronTransformer.transformSchematron(schematron, source, result);
    }

    static {
        BasicConfigurator.configure();
    }
}
