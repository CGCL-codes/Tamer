package djudge.common;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import djudge.exceptions.DJudgeXmlException;
import utils.XmlTools;

/**
 * @author alt
 * Base class for data classes which are to be serialized and deserialized to XML
 */
public abstract class XMLSerializable {

    private static final Logger log = Logger.getLogger(XMLSerializable.class);

    public static final String XMLRootElement = "change-me-in-child-class";

    public abstract Document getXML();

    public abstract boolean readXML(Element elem) throws DJudgeXmlException;

    public XMLSerializable() {
    }

    public XMLSerializable(Element elem) {
        try {
            readXML(elem);
        } catch (Exception ex) {
            log.error("Error parsing XML data", ex);
        }
    }

    public boolean loadXML(String filename) {
        boolean result = false;
        try {
            result = readXML((Element) XmlTools.getDocument(filename).getDocumentElement());
        } catch (Exception ex) {
            log.error("Error parsing XML data", ex);
        }
        return result;
    }

    public boolean saveXML(String filename) {
        boolean result = false;
        try {
            XmlTools.saveXmlToFile(getXML(), filename);
        } catch (Exception ex) {
            log.error("Error while data serialzation", ex);
        }
        return result;
    }

    public String getXMLString() {
        String res = "";
        try {
            Document doc = getXML();
            OutputFormat format = new OutputFormat(doc);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(doc);
            res = out.toString();
        } catch (IOException ex) {
            log.error("Error while converting XML to string", ex);
        }
        return res;
    }
}
