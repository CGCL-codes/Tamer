package it.aton.proj.dem.commons.util.xml;

import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XMLDocument {

    /** the document wrapped by this {@link XMLDocument} */
    private Document document;

    /** the XML of this {@link XMLDocument} as a String */
    private String realXML;

    /**
	 * Per comporre un nuovo xml
	 */
    public XMLDocument() {
        document = new SAXReader().getDocumentFactory().createDocument();
        this.realXML = null;
    }

    /**
	 * Builds a new XMLDocument from a String, parsing it into a DOM Document.
	 * 
	 * @param xml
	 *            the XML string to parse
	 * @throws ParseException
	 *             if the XML is invalid
	 */
    public XMLDocument(String xml) throws ParseException {
        createXMLDocument(xml);
    }

    /**
	 * Creates a new document using an already parsed Element. The string will
	 * be null until the first call to {@link #getXML()}. The element will be
	 * detatched from the original document and added to the document wrapped by
	 * this XMLDocument.
	 * 
	 * @param el
	 *            The element
	 */
    public XMLDocument(Element el) {
        this();
        document.add(el.detach());
    }

    private void createXMLDocument(String xml) throws ParseException {
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(new StringReader(xml));
            this.realXML = xml;
        } catch (DocumentException e) {
            throw new ParseException("Error in parsing xml: " + e.getMessage(), 0);
        }
    }

    public String getXML() {
        if (realXML == null) {
            realXML = document.asXML();
            int pos = realXML.indexOf('\n');
            realXML = realXML.substring(pos).trim();
        }
        return realXML;
    }

    public String getXPathValue(String xPath) {
        Node nd = document.selectSingleNode(xPath);
        if (nd != null) return nd.getStringValue();
        return null;
    }

    public String getXPathContent(String xPath, NodeType type) {
        if (type == NodeType.TEXT) return getXPathValue(xPath); else {
            return getSingleNode(xPath).getXML();
        }
    }

    @SuppressWarnings("unchecked")
    public List<XMLNode> getNodes(String xPath) {
        List<XMLNode> ret = new ArrayList<XMLNode>();
        List<Node> list = document.selectNodes(xPath);
        for (Node n : list) {
            XMLNode newNode = new XMLNode();
            newNode.node = n;
            ret.add(newNode);
        }
        return ret;
    }

    public XMLNode getSingleNode(String xPath) {
        Node node = document.selectSingleNode(xPath);
        if (node == null) return null;
        XMLNode ret = new XMLNode();
        ret.node = node;
        return ret;
    }

    /**
	 * Aggiunge o sostituisce un nodo, basandosi su un xPath e sul nuovo valore
	 * da dargli.
	 * 
	 * Da notare che:
	 * 
	 * * l'xPath da accettare � un sottoinsieme che non supporta path non
	 * univoci ("//") e esplorazione di liste ("[...]");
	 * 
	 * * l'unica possibilit� di errore non derivante da xPath malformato � se
	 * l'xPath specifica una root diversa da quella attuale. Infatti non ci
	 * possono essere pi� root in un XML valido.
	 * 
	 * @param xPath
	 * @param value
	 * @throws ParseException
	 */
    public void putNode(String xPath, String value) throws ParseException {
        if (xPath == null || xPath.isEmpty() || value == null) throw new ParseException("The XPath and the value must be specified", 0);
        if (!xPath.startsWith("/")) throw new ParseException("The XPath must be absolute", 0);
        if (xPath.indexOf("//") >= 0) throw new ParseException("Can't specify multiple paths (\"//\")", 0);
        if (xPath.indexOf("[") >= 0) throw new ParseException("Can't specify lists (\"[\")", 0);
        String[] xParts = tokenize(xPath);
        String root = xParts[0];
        Element e = document.getRootElement();
        if (e == null) {
            e = document.addElement(xParts[0]);
            document.setRootElement(e);
        } else if (!root.equals(e.getName())) throw new ParseException("Can't change the root", 0);
        boolean foundAttribute = false;
        for (String s : xParts) {
            if (foundAttribute) throw new ParseException("An Attribute must be the last element", 0);
            foundAttribute = s.startsWith("@");
        }
        int i = 1;
        String attrib = null;
        while (i < xParts.length) {
            if (xParts[i].startsWith("@")) {
                attrib = xParts[i].substring(1);
            } else {
                Element tmpEl = e.element(xParts[i]);
                if (tmpEl == null) tmpEl = e.addElement(xParts[i]);
                e = tmpEl;
            }
            i++;
        }
        if (attrib != null) e.addAttribute(attrib, value); else e.setText(value);
        this.realXML = null;
    }

    private static String PLACEHOLDER = "%%%vbrihkfvwepurfdewkjcbdscviskd%";

    /**
	 * Aggiunge o sostituisce un nodo, basandosi su un xPath e sul nuovo valore
	 * da dargli, discrimando il tipo di nodo: text o xml.
	 * 
	 * Da notare che:
	 * 
	 * * l'xPath da accettare � un sottoinsieme che non supporta path non
	 * univoci ("//") e esplorazione di liste ("[...]");
	 * 
	 * * l'unica possibilit� di errore non derivante da xPath malformato � se
	 * l'xPath specifica una root diversa da quella attuale. Infatti non ci
	 * possono essere pi� root in un XML valido.
	 * 
	 * @param xPath
	 * @param value
	 * @param type
	 * @throws ParseException
	 */
    public void putNode(String xPath, String value, NodeType type) throws ParseException {
        if (type == NodeType.TEXT) putNode(xPath, value); else {
            putNode(xPath, PLACEHOLDER);
            createXMLDocument(getXML().replace(PLACEHOLDER, value));
        }
    }

    private static String[] tokenize(String path) {
        StringTokenizer st = new StringTokenizer(path, "/", false);
        String[] ret = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) ret[i++] = st.nextToken();
        return ret;
    }

    public String getRootName() {
        return document.getRootElement().getName();
    }
}
