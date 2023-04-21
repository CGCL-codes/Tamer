package org.synthful.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.apache.commons.logging.*;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.Text;
import org.jdom.input.JDOMParseException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;
import org.synthful.util.HashTreeNode;

/**
 * @author Blessed Geek
 */
public class XmlParser extends DefaultHandler {

    /**
     * Creates a new instance of FiStXParser.
     */
    public XmlParser() {
    }

    /**
     * Parses the.
     * 
     * @param folder
     * @param filename
     */
    public void parse(String folder, String filename) {
        parse(new File(folder, filename));
    }

    /**
     * Parses the.
     * 
     * @param filename
     */
    public void parse(String filename) {
        parse(new File(filename));
    }

    /**
     * Parses the.
     * 
     * @param file
     */
    public void parse(File file) {
        FileInputStream fins;
        try {
            fins = new FileInputStream(file);
            InputSource insrc = new InputSource(fins);
            parse(insrc);
        } catch (FileNotFoundException ex) {
        }
    }

    /**
     * Parses the.
     * 
     * @param ins
     */
    public void parse(InputSource ins) {
        try {
            SAXBuilder saxb = new SAXBuilder(validateXML);
            String msg = "No errors!";
            LOG.info(this + "-" + ins + "-" + msg);
            try {
                doc = saxb.build(ins);
                Element root = doc.getRootElement();
                getChildren(root);
            } catch (JDOMParseException e) {
                LOG.error(e.getMessage());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    /**
     * Parses the string.
     * 
     * @param s
     */
    public void parseString(String s) {
        StringReader rdr = new StringReader(s);
        InputSource insrc = new InputSource(rdr);
        parse(insrc);
    }

    /**
     * Gets the Children.
     * 
     * @param element
     * @return the Children as void
     */
    public void getChildren(Element element) {
        List ElementList = element.getChildren();
        Iterator iter = ElementList.iterator();
        while (iter.hasNext()) {
            Object oj = iter.next();
            if (!(oj instanceof Element)) continue;
            Element ej = (Element) oj;
            getChildren(ej);
        }
    }

    /**
     * Digest.
     * 
     * @param ei
     * @throws IOException
     */
    public void digest(Element ei) throws java.io.IOException {
        Iterator iter = ei.getContent().iterator();
        Hashtable HReplaceWithChildren = new HashTreeNode();
        while (iter.hasNext()) {
            Object oj = iter.next();
            if (oj == null) continue;
            if (oj instanceof Element) {
                Element ej = (Element) oj;
                digestAttributes(ej);
                digest(ej);
                Vector ReplaceWithChildren = digestElement(ej);
                if (ReplaceWithChildren != null && ReplaceWithChildren.size() > 0) HReplaceWithChildren.put(oj, ReplaceWithChildren);
            } else if (oj instanceof Text) {
                Vector newChildren = digestText((Text) oj);
            }
        }
        ReplaceWithChildren(HReplaceWithChildren);
    }

    /**
     * Replace with children.
     * 
     * @param replacement
     */
    public void ReplaceWithChildren(Hashtable replacement) {
        Enumeration enu = replacement.keys();
        while (enu.hasMoreElements()) {
            Object oj = enu.nextElement();
            if (oj instanceof Element) {
                Element ej = (Element) oj;
                Element pei = ej.getParentElement();
                if (pei == null) continue;
                Object voj = replacement.get(oj);
                if (voj == null || !(voj instanceof Vector)) continue;
                int k = pei.indexOf(ej);
                if (k >= 0) ej.detach(); else continue;
                Vector vj = (Vector) voj;
                for (int i = 0; i < vj.size(); i++) {
                    Object vok = vj.get(i);
                    if (vok == null) continue;
                    if (vok instanceof Content) {
                        Content vck = (Content) ((Content) vok).clone();
                        pei.addContent(k++, vck);
                    } else {
                        String vsk = vok.toString();
                        if (vsk.trim().length() > 0) pei.addContent(k++, new Text(vsk));
                    }
                }
            }
        }
    }

    /**
     * Replace children.
     * 
     * @param replacement
     */
    public void ReplaceChildren(Hashtable replacement) {
        Enumeration enu = replacement.keys();
        while (enu.hasMoreElements()) {
            Object oj = enu.nextElement();
            if (!(oj instanceof Element)) continue;
            Element ej = (Element) oj;
            Object voj = replacement.get(oj);
            if (voj != null && voj instanceof Vector) {
                Vector vj = (Vector) voj;
                for (int i = 0; i < vj.size(); i++) {
                    Object ok = vj.get(i);
                    if (ok == null && !(ok instanceof Content[])) continue;
                    Content[] ack = (Content[]) ok;
                    if (ack.length < 2) continue;
                    ej = ack[0].getParentElement();
                    if (ej == null) continue;
                    int k = ej.indexOf(ack[0]);
                    if (k >= 0) {
                        ack[0].detach();
                        ej.addContent(ack[1]);
                    }
                }
            }
        }
    }

    /**
     * Digest attributes.
     * 
     * @param ej
     * @throws IOException
     */
    public void digestAttributes(Element ej) throws java.io.IOException {
        if (ej == null) return;
        Iterator iter = ej.getAttributes().iterator();
        while (iter.hasNext()) {
            Object oj = iter.next();
            if (oj != null && oj instanceof Attribute) {
                Attribute aj = (Attribute) oj;
                digestAttribute(ej, aj);
            }
        }
    }

    /**
     * Digest attribute.
     * 
     * @param ej
     * @param attrib
     * @throws IOException
     */
    public void digestAttribute(Element ej, Attribute attrib) throws java.io.IOException {
    }

    /**
     * Digest element.
     * 
     * @param elem
     * @return Digest element as Vector
     * @throws IOException
     */
    public Vector digestElement(Element elem) throws java.io.IOException {
        return null;
    }

    /**
     * Digest text.
     * 
     * @param tj
     * @return Digest text as Vector
     * @throws IOException
     */
    public Vector digestText(Text tj) throws java.io.IOException {
        if (tj == null) return null;
        String sj = tj.getTextNormalize();
        if (sj.length() == 0) return null;
        return null;
    }

    /**
     * Gets the ElementAttributeValue.
     * 
     * @param e
     * @param attr
     * @param defaultValue
     * @return the ElementAttributeValue as String
     */
    public static String getElementAttributeValue(Element e, String attr, String defaultValue) {
        String av = getElementAttributeValue(e, attr);
        return av == null ? defaultValue : av;
    }

    /**
     * Gets the ElementAttributeValue.
     * 
     * @param e
     * @param attr
     * @return the ElementAttributeValue as String
     */
    public static String getElementAttributeValue(Element e, String attr) {
        if (e == null) return null;
        Attribute a = e.getAttribute(attr);
        if (a != null) return a.getValue();
        return null;
    }

    /**
     * Linearise children.
     * 
     * @param sbuf
     * @param ei
     */
    public static void lineariseChildren(StringBuilder sbuf, Element ei) {
        if (ei == null) return;
        Iterator iter = ei.getContent().iterator();
        while (iter.hasNext()) {
            Object oj = iter.next();
            if (oj == null) continue;
            if (oj instanceof Element) lineariseElement(sbuf, (Element) oj); else if (oj instanceof Text) {
                lineariseText(sbuf, (Text) oj);
            }
        }
    }

    /**
     * Linearise element.
     * 
     * @param sbuf
     * @param ei
     */
    public static void lineariseElement(StringBuilder sbuf, Element ei) {
        if (ei == null) return;
        lineariseStartTag(sbuf, ei);
        lineariseAttributes(sbuf, ei);
        sbuf.append(">\n");
        lineariseChildren(sbuf, ei);
        sbuf.append("</").append(ei.getName()).append(">\n");
    }

    /**
     * Linearise attributes.
     * 
     * @param sbuf
     * @param ej
     */
    protected static void lineariseAttributes(StringBuilder sbuf, Element ej) {
        Iterator iter = ej.getAttributes().iterator();
        while (iter.hasNext()) {
            Object oj = iter.next();
            if (oj != null && oj instanceof Attribute) {
                Attribute aj = (Attribute) oj;
                sbuf.append(' ').append(aj.getName()).append("=\"").append(aj.getValue()).append("\"");
            }
        }
    }

    /**
     * Linearise start tag.
     * 
     * @param sbuf
     * @param ej
     */
    protected static void lineariseStartTag(StringBuilder sbuf, Element ej) {
        String ejtag = ej.getName();
        sbuf.append("\n<").append(ejtag);
    }

    private static void lineariseText(StringBuilder sbuf, Text text) {
        sbuf.append(text.getTextNormalize()).append('\n');
    }

    /**
     * Gets the Document.
     * 
     * @return the Document as Document
     */
    public Document getDocument() {
        return doc;
    }

    /**
     * Sets the validate xml.
     * 
     * @param v
     *            the ValidateXml
     */
    public void setValidateXml(boolean v) {
        validateXML = v;
    }

    private static final Log LOG = LogFactory.getLog(XmlParser.class);

    /** Variable doc. */
    protected Document doc = null;

    /** Variable validateXML. */
    boolean validateXML = true;
}
