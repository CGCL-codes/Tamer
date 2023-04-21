package org.dcm4chee.arr.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision$ $Date$
 * @since Aug 2, 2006
 */
public class XSLTUtils {

    private static final String SUMMARY_XSL = "arr-summary.xsl";

    private static final String DETAILS_XSL = "arr-details.xsl";

    private static final String IHEYR4_TO_ATNA_XSL = "arr-iheyr4-to-atna.xsl";

    private static Templates summaryTpl;

    private static Templates detailsTpl;

    private static Templates iheYr4toATNATpl;

    private static SAXTransformerFactory tf;

    private static SAXTransformerFactory transfomerFactory() {
        if (tf == null) {
            tf = (SAXTransformerFactory) TransformerFactory.newInstance();
        }
        return tf;
    }

    public static String toXML(byte[] xmldata) {
        try {
            InputStreamReader r = new InputStreamReader(new ByteArrayInputStream(xmldata), "UTF-8");
            StringBuffer sb = new StringBuffer(xmldata.length * 5 / 4);
            sb.append("<pre>");
            int prev = '\n', c;
            while ((c = r.read()) != -1) {
                if (c == '<') {
                    sb.append(prev == '\n' ? "&lt;" : "\n&lt;");
                } else {
                    sb.append((char) c);
                }
                if (c != ' ') {
                    prev = c;
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String toSummary(byte[] xmldata) {
        try {
            if (summaryTpl == null) {
                summaryTpl = loadTemplates(SUMMARY_XSL);
            }
            return transform(summaryTpl.newTransformer(), xmldata);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String toDetails(byte[] xmldata) {
        try {
            if (detailsTpl == null) {
                detailsTpl = loadTemplates(DETAILS_XSL);
            }
            return transform(detailsTpl.newTransformer(), xmldata);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private static String transform(Transformer tr, byte[] xmldata) throws TransformerException {
        StringWriter out = new StringWriter(512);
        tr.transform(new StreamSource(new ByteArrayInputStream(xmldata)), new StreamResult(out));
        return out.toString();
    }

    private static Templates loadTemplates(String name) throws TransformerException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return transfomerFactory().newTemplates(new StreamSource(cl.getResource(name).toString()));
    }

    public static void parseIHEYr4(XMLReader reader, DefaultHandler dh, byte[] xmldata) throws IOException, SAXException, TransformerException {
        if (iheYr4toATNATpl == null) {
            iheYr4toATNATpl = loadTemplates(IHEYR4_TO_ATNA_XSL);
        }
        XMLFilter filter = transfomerFactory().newXMLFilter(iheYr4toATNATpl);
        filter.setParent(reader);
        parseATNA(filter, dh, xmldata);
    }

    public static void parseATNA(XMLReader reader, DefaultHandler dh, byte[] xmldata) throws IOException, SAXException {
        reader.setContentHandler(dh);
        reader.setEntityResolver(dh);
        reader.setErrorHandler(dh);
        reader.setDTDHandler(dh);
        reader.parse(new InputSource(new ByteArrayInputStream(xmldata)));
    }

    public static void main(String[] args) throws Exception {
        File f = new File(args[0]);
        byte[] xmldata = new byte[(int) f.length()];
        FileInputStream in = new FileInputStream(f);
        in.read(xmldata);
        in.close();
        Transformer tr = AuditMessageUtils.isIHEYr4(xmldata) ? loadTemplates(IHEYR4_TO_ATNA_XSL).newTransformer() : transfomerFactory().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.transform(new StreamSource(new ByteArrayInputStream(xmldata)), new StreamResult(System.out));
    }
}
