package hu.sztaki.lpds.wfs.service.angie.utils;

import java.io.StringWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * XML utils... (helper class)
 *
 * @author lpds
 */
public class XMLUtils {

    private static XMLUtils instance = new XMLUtils();

    public XMLUtils() {
    }

    /**
     * XMLUtils peldanyt ad vissza.
     *
     * @return
     */
    public static XMLUtils getInstance() {
        return instance;
    }

    /**
     * A workflow element objektumbol keszit egy stringet.
     *
     * @param Document doc parent document
     * @param Element workflow element
     * @return workflow xml leiro string
     */
    public String transformElementToString(Document doc, Element element) throws Exception {
        doc.appendChild(element);
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        DOMSource source = new DOMSource(doc);
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        transformer.transform(source, streamResult);
        String xmlstr = stringWriter.toString();
        return xmlstr;
    }
}
