package sereneSamples;

import org.w3c.dom.ls.LSResourceResolver;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Entity;
import org.w3c.dom.Notation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Comment;
import org.w3c.dom.NamedNodeMap;

class NodeWriter {

    void write(Node node) {
        final Node top = node;
        while (node != null) {
            beginNode(node);
            Node next = node.getFirstChild();
            while (next == null) {
                finishNode(node);
                if (top == node) {
                    break;
                }
                next = node.getNextSibling();
                if (next == null) {
                    node = node.getParentNode();
                    if (node == null || top == node) {
                        if (node != null) {
                            finishNode(node);
                        }
                        next = null;
                        break;
                    }
                }
            }
            node = next;
        }
    }

    void beginNode(Node node) {
        switch(node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                handleDocument((Document) node);
                break;
            case Node.ELEMENT_NODE:
                handleStartElement(node);
                break;
            case Node.TEXT_NODE:
                String value = node.getNodeValue();
                System.out.println(value);
                break;
            case Node.CDATA_SECTION_NODE:
                value = node.getNodeValue();
                System.out.println(value);
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                handleProcessingInstruction((ProcessingInstruction) node);
                break;
            case Node.COMMENT_NODE:
                System.out.println("<!--" + node.getNodeValue() + "-->");
                break;
            case Node.DOCUMENT_TYPE_NODE:
                break;
            default:
                break;
        }
    }

    void finishNode(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            String qName = node.getNodeName();
            if (qName == null) qName = "";
            System.out.println("</" + qName + ">");
        }
    }

    void handleDocument(Document document) {
        String version = document.getXmlVersion();
        String encoding = document.getXmlEncoding();
        boolean standalone = document.getXmlStandalone();
        System.out.println("<?xml version=\"" + version + "\" encoding=\"" + encoding + "\" standalone=\"" + standalone + "\"?>");
    }

    void handleStartElement(Node element) {
        String qName = element.getNodeName();
        if (qName == null) qName = "";
        String tag = "<" + qName;
        NamedNodeMap attrs = element.getAttributes();
        final int attrCount2 = attrs.getLength();
        for (int i = 0; i < attrCount2; ++i) {
            Attr attr = (Attr) attrs.item(i);
            String attrQName = attr.getNodeName();
            if (attrQName == null) attrQName = "";
            String value = attr.getValue();
            if (value == null) value = "";
            tag += " " + attrQName + "=\"" + value + "\"";
        }
        tag += ">";
        System.out.println(tag);
    }

    void handleProcessingInstruction(ProcessingInstruction pi) {
        String target = pi.getTarget();
        String data = pi.getData();
        System.out.println("<?" + target + " " + data + "?>");
    }
}
