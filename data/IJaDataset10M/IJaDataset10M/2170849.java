package org.apache.shindig.gadgets.parse;

import org.cyberneko.html.HTMLElements;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.IOException;
import java.io.StringWriter;

/**
 * This parser does not try to escape entities in text content as it expects the parser
 * to have retained the original entity references rather than its resolved form in text nodes.
 */
public class DefaultHtmlSerializer implements HtmlSerializer {

    /** {@inheritDoc} */
    public String serialize(Document doc) {
        try {
            StringWriter sw = HtmlSerialization.createWriter(doc);
            if (doc.getDoctype() != null) {
                outputDocType(doc.getDoctype(), sw);
            }
            this.serialize(doc, sw);
            return sw.toString();
        } catch (IOException ioe) {
            return null;
        }
    }

    public void serialize(Node n, Appendable output) throws IOException {
        serialize(n, output, false);
    }

    private void serialize(Node n, Appendable output, boolean xmlMode) throws IOException {
        if (n == null) return;
        switch(n.getNodeType()) {
            case Node.CDATA_SECTION_NODE:
                {
                    break;
                }
            case Node.COMMENT_NODE:
                {
                    writeComment(n, output);
                    break;
                }
            case Node.DOCUMENT_NODE:
                {
                    serialize(((Document) n).getDocumentElement(), output, xmlMode);
                    break;
                }
            case Node.ELEMENT_NODE:
                {
                    Element elem = (Element) n;
                    NodeList children = elem.getChildNodes();
                    elem = substituteElement(elem);
                    HTMLElements.Element htmlElement = HTMLElements.getElement(elem.getNodeName());
                    printStartElement(elem, output, xmlMode && htmlElement.isEmpty());
                    boolean childXmlMode = xmlMode || htmlElement.isSpecial();
                    for (int i = 0; i < children.getLength(); i++) {
                        serialize(children.item(i), output, childXmlMode);
                    }
                    if (!htmlElement.isEmpty()) {
                        output.append("</").append(elem.getNodeName()).append('>');
                    }
                    break;
                }
            case Node.ENTITY_REFERENCE_NODE:
                {
                    output.append("&").append(n.getNodeName()).append(";");
                    break;
                }
            case Node.TEXT_NODE:
                {
                    writeText(n, output);
                    break;
                }
        }
    }

    /**
   * Convert OSData and OSTemplate tags to script tags with the appropriate
   * type attribute on output
   */
    private Element substituteElement(Element elem) {
        String scriptType = GadgetHtmlParser.SCRIPT_TYPE_TO_OSML_TAG.inverse().get(elem.getNodeName());
        if (scriptType != null) {
            Element replacement = elem.getOwnerDocument().createElement("script");
            replacement.setAttribute("type", scriptType);
            return replacement;
        }
        return elem;
    }

    protected void writeText(Node n, Appendable output) throws IOException {
        output.append(n.getTextContent());
    }

    protected void writeComment(Node n, Appendable output) throws IOException {
        output.append("<!--").append(n.getNodeValue()).append("-->");
    }

    private void outputDocType(DocumentType docType, Appendable output) throws IOException {
        output.append("<!DOCTYPE ");
        output.append(docType.getOwnerDocument().getDocumentElement().getNodeName());
        if (docType.getPublicId() != null && docType.getPublicId().length() > 0) {
            output.append(" ");
            output.append("PUBLIC ").append('"').append(docType.getPublicId()).append('"');
        }
        if (docType.getSystemId() != null && docType.getSystemId().length() > 0) {
            output.append(" ");
            output.append('"').append(docType.getSystemId()).append('"');
        }
        output.append(">\n");
    }

    /**
   * Print the start of an HTML element.  If withXmlClose==true, this is an
   * empty element that should have its content
   */
    private static void printStartElement(Element elem, Appendable output, boolean withXmlClose) throws IOException {
        output.append("<").append(elem.getTagName());
        NamedNodeMap attributes = elem.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attr = (Attr) attributes.item(i);
            String attrName = attr.getNodeName();
            output.append(' ').append(attrName);
            if (attr.getNodeValue() != null) {
                output.append("=\"");
                if (attr.getNodeValue().length() != 0) {
                    boolean isUrlAttribute = elem.getNamespaceURI() == null && HtmlSerialization.URL_ATTRIBUTES.contains(attrName);
                    printAttributeValue(attr.getNodeValue(), output, isUrlAttribute);
                }
                output.append('"');
            }
        }
        output.append(withXmlClose ? "/>" : ">");
    }

    private static void printAttributeValue(String text, Appendable output, boolean isUrl) throws IOException {
        int length = text.length();
        for (int j = 0; j < length; j++) {
            char c = text.charAt(j);
            if (c == '"') {
                output.append("&quot;");
            } else if (c == '&' && !isUrl) {
                output.append("&amp;");
            } else {
                output.append(c);
            }
        }
    }
}
