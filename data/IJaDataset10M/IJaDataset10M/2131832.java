package uk.ac.ncl.neresc.dynasoar.webService.utlis;

import org.apache.axis.message.SOAPEnvelope;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * for deubging
 *
 * @author Charles Kubicek
 */
public class SOAPUtils {

    public static void writeSOAPToStream(SOAPEnvelope soap, OutputStream out) {
        try {
            Document d = soap.getAsDocument();
            OutputFormat o = new OutputFormat("xml", "UTF-8", true);
            o.setLineWidth(0);
            XMLSerializer s = null;
            s = new XMLSerializer(out, o);
            s.serialize(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String escapeXml(String str) {
        return str.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
    }

    public static String writeSOAPToString(SOAPEnvelope soap) {
        try {
            Document d = soap.getAsDocument();
            OutputFormat o = new OutputFormat("xml", "UTF-8", true);
            o.setLineWidth(0);
            XMLSerializer s = null;
            StringWriter stringWriter = new StringWriter();
            s = new XMLSerializer(stringWriter, o);
            s.serialize(d);
            return stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Could not render soap document";
        }
    }
}
