package org.wits.test;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author FJ
 */
public class XMLValidityCase implements WITSCase {

    private String source = new String();

    public void initCase(String source) {
        this.source = source;
    }

    public String runCase() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setCoalescing(true);
            dbf.setIgnoringComments(true);
            dbf.setXIncludeAware(true);
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource sourceXML = new InputSource(new StringReader(source));
            Document doc = db.parse(sourceXML);
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            String output = result.getWriter().toString();
            return "PASS";
        } catch (Exception ex) {
            return "FAIL";
        }
    }
}
