package com.technosophos.rhizome.document;

import static com.technosophos.rhizome.document.XMLElements.*;
import java.io.File;
import java.io.Reader;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.CharArrayWriter;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class RhizomeDocumentBuilder {

    /**
	 * Creates a new RhizomeDocumentBuilder.
	 * The RhizomeDocumentBuilder is a class that transforms XML documents
	 * of the appropriate format into RhizomeDocument objects.
	 *
	 */
    public RhizomeDocumentBuilder() {
        return;
    }

    /**
	 * Create a new RhizomeDocument given an XML document contained
	 * in a String.
	 * 
	 * The string must be a well-formed XML document of the appropriate 
	 * type (schema).
	 * @throws SAXException
	 * @throws IOException
	 */
    public RhizomeDocument fromXML(String xml) throws SAXException, java.io.IOException, RhizomeParseException {
        DocumentBuilder db = this.getParser();
        Document doc = db.parse(new ByteArrayInputStream(xml.getBytes()));
        return this.fromDOMDocument(doc);
    }

    /**
	 * Creates a new RhizomeDocument given a filename.
	 * This will try to find the file on the filesystem, load it, parse it, 
	 * and then return a representative RhizomeDocument object.
	 * @param filename The name (including path) of a filename.
	 * @return The RhizomeDocument representation of the file.
	 * @throws IOException
	 * @throws SAXException
	 */
    public RhizomeDocument fromXMLDocument(String filename) throws IOException, SAXException, RhizomeParseException {
        File f = new File(filename);
        DocumentBuilder db = this.getParser();
        Document doc = db.parse(f);
        return this.fromDOMDocument(doc);
    }

    /**
	 * Given a File object, this attempts to read the file from the file 
	 * system and return a RhizomeDocument.
	 * The file is expected to be a Rhizome XML document.
	 * @param xmlfile
	 * @return The file in RhizomeDocument form.
	 * @throws IOException
	 * @throws SAXException
	 */
    public RhizomeDocument fromXMLDocument(File xmlfile) throws IOException, SAXException, RhizomeParseException {
        DocumentBuilder db = this.getParser();
        Document doc = db.parse(xmlfile);
        return this.fromDOMDocument(doc);
    }

    /**
	 * Given a file reader, parse an XML document and return a RhizomeDocument.
	 * @param xmlfr
	 * @return a RhizomeDocument object representing a file.
	 * @throws IOException
	 * @throws SAXException
	 */
    public RhizomeDocument fromXMLDocument(Reader xmlfr) throws IOException, SAXException, RhizomeParseException {
        DocumentBuilder db = this.getParser();
        InputSource is = new InputSource(xmlfr);
        Document doc = db.parse(is);
        return this.fromDOMDocument(doc);
    }

    /**
	 * Given an InputStream for an XML document, return a RhizomeDocument.
	 * @param xmlis
	 * @return
	 */
    public RhizomeDocument fromXMLDocument(InputStream xmlis) throws SAXException, IOException, RhizomeParseException {
        DocumentBuilder db = this.getParser();
        Document doc = db.parse(xmlis);
        return this.fromDOMDocument(doc);
    }

    /**
	 * Get a new JAXP DocumentBuilder instance.
	 * @return new parser
	 */
    private DocumentBuilder getParser() throws RhizomeParseException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db;
        } catch (ParserConfigurationException pce) {
            throw new RhizomeParseException("Parser not configured", pce);
        }
    }

    /**
	 * Builds a RhizomeDocument from a DOM document.
	 * @param doc
	 * @return
	 * @throws RhizomeParseException ONLY in the case where the XML parser cannot be found.
	 */
    public RhizomeDocument fromDOMDocument(Document doc) throws RhizomeParseException {
        Element root = doc.getDocumentElement();
        String docID = root.getAttribute(RHIZOME_DOC_ATTR_DOCID);
        RhizomeDocument rd = new RhizomeDocument(docID);
        NodeList md_nodes = root.getElementsByTagName(RHIZOME_DOC_METADATA);
        NodeList data_nodes = root.getElementsByTagName(RHIZOME_DOC_DATA);
        NodeList rel_nodes = root.getElementsByTagName(RHIZOME_DOC_RELATIONS);
        NodeList ext_nodes = root.getElementsByTagName(RHIZOME_DOC_EXTENSIONS);
        if (md_nodes.getLength() > 0) {
            int i, j = md_nodes.getLength();
            Node n;
            for (i = 0; i < j; ++i) {
                n = md_nodes.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName() == RHIZOME_DOC_METADATA) {
                    Element n_ele = (Element) n;
                    NodeList m_nodes = n_ele.getElementsByTagName(RHIZOME_DOC_METADATUM);
                    if (m_nodes.getLength() > 0) {
                        Element m_ele;
                        Metadatum m_obj;
                        int ii, jj = m_nodes.getLength();
                        for (ii = 0; ii < jj; ++ii) {
                            m_ele = (Element) m_nodes.item(ii);
                            m_obj = new Metadatum(m_ele.getAttribute(RHIZOME_DOC_ATTR_NAME));
                            NodeList v_nodes = m_ele.getElementsByTagName(RHIZOME_DOC_VALUE);
                            int iii, jjj = v_nodes.getLength();
                            for (iii = 0; iii < jjj; ++iii) {
                                Element v_ele = (Element) v_nodes.item(iii);
                                String txt = this.getTextFromEle(v_ele);
                                m_obj.addValue(txt);
                            }
                            rd.addMetadatum(m_obj);
                        }
                    }
                }
            }
        }
        RhizomeData data = null;
        if (data_nodes.getLength() > 0) {
            Element data_ele = (Element) data_nodes.item(0);
            NodeList nl_kids = data_ele.getChildNodes();
            Node t_node;
            int ii, jj = nl_kids.getLength();
            for (ii = 0; ii < jj; ++ii) {
                t_node = nl_kids.item(ii);
                if (t_node.getNodeType() == Node.ELEMENT_NODE) {
                    Element base = (Element) t_node;
                    try {
                        data = new RhizomeData(elementToXMLString(base, this.getParser()));
                        data.setXMLParseable(true);
                    } catch (Exception e) {
                        throw new RhizomeParseException("Error getting data XML: " + e.getMessage());
                    }
                    continue;
                }
            }
            if (data == null) {
                String content = this.getTextFromEle(data_ele);
                data = new RhizomeData(content);
                data.setXMLParseable(false);
            }
            if (data_ele.hasAttribute(RHIZOME_DOC_ATTR_MIMETYPE)) data.setMimeType(data_ele.getAttribute(RHIZOME_DOC_ATTR_MIMETYPE));
            if (data_ele.hasAttribute(RHIZOME_DOC_ATTR_INDEX)) data.setIndexible("true".equals(data_ele.getAttribute(RHIZOME_DOC_ATTR_INDEX)));
            rd.setBody(data);
        }
        if (rel_nodes.getLength() > 0) {
            int i, j = rel_nodes.getLength();
            Node n;
            for (i = 0; i < j; ++i) {
                n = rel_nodes.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName() == RHIZOME_DOC_RELATIONS) {
                    Element n_ele = (Element) n;
                    NodeList m_nodes = n_ele.getElementsByTagName(RHIZOME_DOC_RELATION);
                    if (m_nodes.getLength() > 0) {
                        Element m_ele;
                        Relation r_obj;
                        int iii, jjj = m_nodes.getLength();
                        for (iii = 0; iii < jjj; ++iii) {
                            m_ele = (Element) m_nodes.item(iii);
                            String str_docid = this.getTextFromEle(m_ele);
                            if (str_docid != null && str_docid.length() > 0) {
                                r_obj = new Relation(str_docid);
                                if (m_ele.hasAttribute(RHIZOME_DOC_ATTR_RELATIONTYPE)) r_obj.setRelationType(m_ele.getAttribute(RHIZOME_DOC_ATTR_RELATIONTYPE));
                                rd.addRelation(r_obj);
                            }
                        }
                    }
                }
            }
        }
        if (ext_nodes.getLength() > 0) {
            int i, j = ext_nodes.getLength();
            Node n;
            for (i = 0; i < j; ++i) {
                n = ext_nodes.item(i);
                if (n.getNodeName() == RHIZOME_DOC_EXTENSIONS) {
                    Element n_ele = (Element) n;
                    NodeList m_nodes = n_ele.getElementsByTagName(RHIZOME_DOC_EXTENSION);
                    if (m_nodes.getLength() > 0) {
                        Element m_ele;
                        Extension e_obj;
                        DocumentBuilder db = this.getParser();
                        int ii, jj = m_nodes.getLength();
                        for (ii = 0; ii < jj; ++ii) {
                            m_ele = (Element) m_nodes.item(ii);
                            if (m_ele.hasAttribute(RHIZOME_DOC_ATTR_NAME)) {
                                Document d = db.newDocument();
                                d.importNode(m_ele, true);
                                String ext_name = m_ele.getAttribute(RHIZOME_DOC_ATTR_NAME);
                                if (m_ele.hasAttribute(RHIZOME_DOC_ATTR_INDEX) && "true".equals(m_ele.getAttribute(RHIZOME_DOC_ATTR_INDEX).toLowerCase())) {
                                    e_obj = new Extension(ext_name, d, true);
                                } else {
                                    e_obj = new Extension(ext_name, d);
                                }
                                rd.addExtension(e_obj);
                            }
                        }
                    }
                }
            }
        }
        return rd;
    }

    /**
	 * Return the text content of a node.
	 * This runs through all of the child nodes of the given element (direct
	 * descendents only) and appends them all to one string. It captures
	 * both CDATA and PCDATA (aka Text) data.
	 * Returns an empty string ("") if no text is found in the element.
	 * @param ele
	 * @return string of entire text content of element.
	 */
    private String getTextFromEle(Element ele) {
        StringBuffer sb = new StringBuffer();
        NodeList nl = ele.getChildNodes();
        if (nl.getLength() == 0) return "";
        int i, j = nl.getLength();
        Node n;
        for (i = 0; i < j; ++i) {
            n = nl.item(i);
            if (n.getNodeType() == Node.TEXT_NODE || n.getNodeType() == Node.CDATA_SECTION_NODE) {
                Text n_txt = (Text) n;
                if (!n_txt.isElementContentWhitespace()) {
                    try {
                        sb.append(n_txt.getData());
                    } catch (org.w3c.dom.DOMException dome) {
                        int l = n_txt.getLength();
                        int ii = 0;
                        while (ii < l) {
                            try {
                                sb.append(n_txt.substringData(ii, 1));
                            } catch (org.w3c.dom.DOMException e2) {
                                sb.append("--MISSING END OF DATA--");
                                return sb.toString();
                            }
                            ++ii;
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
	 * This takes an XML Element and turns it into a parseable XML document (String).
	 * @param element
	 * @return String representation of an XML document.
	 */
    public static String elementToXMLString(Element m_ele, DocumentBuilder db) throws ParserConfigurationException {
        CharArrayWriter output = new CharArrayWriter();
        Document d = db.newDocument();
        d.appendChild(d.importNode(m_ele, true));
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.transform(new DOMSource(d), new StreamResult(output));
        } catch (Exception e) {
            throw new ParserConfigurationException("Could not create Transformer: " + e.getMessage());
        }
        return output.toString();
    }
}
