package org.exist.xqj;

import org.exist.xquery.value.*;
import org.exist.xquery.XPathException;
import org.exist.xquery.NameTest;
import org.exist.storage.DBBroker;
import org.exist.memtree.MemTreeBuilder;
import org.exist.memtree.NodeImpl;
import org.exist.memtree.InMemoryNodeSet;
import org.exist.memtree.ElementImpl;
import org.exist.memtree.DocumentBuilderReceiver;
import org.exist.dom.QName;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xquery.XQItemType;
import javax.xml.xquery.XQException;
import java.util.Properties;
import java.io.Reader;
import java.io.StringReader;

/**
 * A utility class that provides marshalling services for external variables and methods 
 * to create DOM Nodes from streamed representation.
 * 
 * @author Wolfgang Meier
 *
 */
public class Marshaller {

    public static final String NAMESPACE = "http://exist-db.org/xquery/types/serialized";

    public static final String PREFIX = "sx";

    private static final Properties OUTPUT_PROPERTIES = new Properties();

    private static final String VALUE_ELEMENT = "value";

    private static final String VALUE_ELEMENT_QNAME = PREFIX + ":value";

    private static final QName VALUE_QNAME = new QName(VALUE_ELEMENT, NAMESPACE, PREFIX);

    private static final String SEQ_ELEMENT = "sequence";

    private static final String SEQ_ELEMENT_QNAME = PREFIX + ":sequence";

    private static final String ATTR_TYPE = "type";

    private static final String ATTR_ITEM_TYPE = "item-type";

    public static final QName ROOT_ELEMENT_QNAME = new QName(SEQ_ELEMENT, NAMESPACE, PREFIX);

    /**
     * Marshall a sequence in an xml based string representation
     * @param broker
     * @param seq Sequence to be marshalled
     * @param handler Content handler for building the resulting string
     * @throws XPathException
     * @throws SAXException
     */
    public static void marshall(DBBroker broker, Sequence seq, ContentHandler handler) throws XPathException, SAXException {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", ATTR_ITEM_TYPE, ATTR_ITEM_TYPE, "CDATA", Type.getTypeName(seq.getItemType()));
        handler.startElement(NAMESPACE, SEQ_ELEMENT, SEQ_ELEMENT_QNAME, attrs);
        for (SequenceIterator i = seq.iterate(); i.hasNext(); ) {
            marshallItem(broker, i.nextItem(), handler);
        }
        handler.endElement(NAMESPACE, SEQ_ELEMENT, SEQ_ELEMENT_QNAME);
    }

    /**
     * Marshall the items of a sequence in  an xml based string representation
     * @param broker
     * @param seq Sequence which items are to be marshalled
     * @param start index of first item to be marshalled
     * @param howmany number of items following and including the first to be marshalled
     * @param handler
     * @throws XPathException
     * @throws SAXException
     */
    public static void marshall(DBBroker broker, Sequence seq, int start, int howmany, ContentHandler handler) throws XPathException, SAXException {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", ATTR_ITEM_TYPE, ATTR_ITEM_TYPE, "CDATA", Type.getTypeName(seq.getItemType()));
        handler.startElement(NAMESPACE, SEQ_ELEMENT, SEQ_ELEMENT_QNAME, attrs);
        for (int i = start; i < howmany && i < seq.getItemCount(); i++) {
            marshallItem(broker, seq.itemAt(i), handler);
        }
        handler.endElement(NAMESPACE, SEQ_ELEMENT, SEQ_ELEMENT_QNAME);
    }

    /**
     * Marshall an item in an xml based string representation
     * @param broker
     * @param item Sequence(or Item) to me marshalled
     * @param handler
     * @throws SAXException
     * @throws XPathException
     */
    public static void marshallItem(DBBroker broker, Item item, ContentHandler handler) throws SAXException, XPathException {
        AttributesImpl attrs = new AttributesImpl();
        int type = item.getType();
        if (type == Type.NODE) type = ((NodeValue) item).getNode().getNodeType();
        attrs.addAttribute("", ATTR_TYPE, ATTR_TYPE, "CDATA", Type.getTypeName(type));
        if (Type.subTypeOf(item.getType(), Type.NODE)) {
            handler.startElement(NAMESPACE, VALUE_ELEMENT, VALUE_ELEMENT_QNAME, attrs);
            NodeValue nv = (NodeValue) item;
            nv.toSAX(broker, handler, OUTPUT_PROPERTIES);
            handler.endElement(NAMESPACE, VALUE_ELEMENT, VALUE_ELEMENT_QNAME);
        } else {
            handler.startElement(NAMESPACE, VALUE_ELEMENT, VALUE_ELEMENT_QNAME, attrs);
            String value = item.getStringValue();
            handler.characters(value.toCharArray(), 0, value.length());
            handler.endElement(NAMESPACE, VALUE_ELEMENT, VALUE_ELEMENT_QNAME);
        }
    }

    public static Sequence demarshall(DBBroker broker, Reader reader) throws XMLStreamException, XPathException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        factory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        XMLStreamReader parser = factory.createXMLStreamReader(reader);
        return demarshall(broker, parser);
    }

    public static Sequence demarshall(DBBroker broker, Node n) throws XMLStreamException, XPathException {
        DOMSource source = new DOMSource(n, null);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        factory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        XMLStreamReader parser = factory.createXMLStreamReader(source);
        return demarshall(broker, parser);
    }

    public static Sequence demarshall(DBBroker broker, XMLStreamReader parser) throws XMLStreamException, XPathException {
        int event = parser.next();
        while (event != XMLStreamConstants.START_ELEMENT) event = parser.next();
        if (!NAMESPACE.equals(parser.getNamespaceURI())) throw new XMLStreamException("Root element is not in the correct namespace. Expected: " + NAMESPACE);
        if (!SEQ_ELEMENT.equals(parser.getLocalName())) throw new XMLStreamException("Root element should be a " + SEQ_ELEMENT_QNAME);
        ValueSequence result = new ValueSequence();
        while ((event = parser.next()) != XMLStreamConstants.END_DOCUMENT) {
            switch(event) {
                case XMLStreamConstants.START_ELEMENT:
                    if (NAMESPACE.equals(parser.getNamespaceURI()) && VALUE_ELEMENT.equals(parser.getLocalName())) {
                        String typeName = null;
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            if (ATTR_TYPE.equals(parser.getAttributeLocalName(i))) {
                                typeName = parser.getAttributeValue(i);
                                break;
                            }
                        }
                        if (typeName != null) {
                            int type = Type.getType(typeName);
                            Item item;
                            if (Type.subTypeOf(type, Type.NODE)) item = streamToDOM(type, parser); else item = new StringValue(parser.getElementText()).convertTo(type);
                            result.add(item);
                        }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (NAMESPACE.equals(parser.getNamespaceURI()) && SEQ_ELEMENT.equals(parser.getLocalName())) return result;
                    break;
            }
        }
        return result;
    }

    public static Sequence demarshall(NodeImpl node) throws XMLStreamException, XPathException {
        if (!NAMESPACE.equals(node.getNamespaceURI())) throw new XMLStreamException("Root element is not in the correct namespace. Expected: " + NAMESPACE);
        if (!SEQ_ELEMENT.equals(node.getLocalName())) throw new XMLStreamException("Root element should be a " + SEQ_ELEMENT_QNAME);
        ValueSequence result = new ValueSequence();
        InMemoryNodeSet values = new InMemoryNodeSet();
        node.selectChildren(new NameTest(Type.ELEMENT, VALUE_QNAME), values);
        for (SequenceIterator i = values.iterate(); i.hasNext(); ) {
            ElementImpl child = (ElementImpl) i.nextItem();
            String typeName = child.getAttribute(ATTR_TYPE);
            if (typeName != null) {
                int type = Type.getType(typeName);
                Item item;
                if (Type.subTypeOf(type, Type.NODE)) {
                    item = (Item) child.getFirstChild();
                    if (type == Type.DOCUMENT) {
                        NodeImpl n = (NodeImpl) item;
                        DocumentBuilderReceiver receiver = new DocumentBuilderReceiver();
                        try {
                            receiver.startDocument();
                            n.getDocument().copyTo(n, receiver);
                            receiver.endDocument();
                        } catch (SAXException e) {
                            throw new XPathException("Error while demarshalling node: " + e.getMessage(), e);
                        }
                        item = (Item) receiver.getDocument();
                    }
                } else {
                    StringBuilder data = new StringBuilder();
                    Node txt = child.getFirstChild();
                    while (txt != null) {
                        if (!(txt.getNodeType() == Node.TEXT_NODE || txt.getNodeType() == Node.CDATA_SECTION_NODE)) throw new XMLStreamException("sx:value should only contain text if type is " + typeName);
                        data.append(txt.getNodeValue());
                        txt = txt.getNextSibling();
                    }
                    item = new StringValue(data.toString()).convertTo(type);
                }
                result.add(item);
            }
        }
        return result;
    }

    public static Item streamToDOM(XMLStreamReader parser, XQItemType type) throws XMLStreamException, XQException {
        if (type.getBaseType() == XQItemType.XQITEMKIND_DOCUMENT_ELEMENT || type.getBaseType() == XQItemType.XQITEMKIND_DOCUMENT_SCHEMA_ELEMENT) return streamToDOM(Type.DOCUMENT, parser); else return streamToDOM(Type.ELEMENT, parser);
    }

    /**
     * Creates an Item from a streamed representation
     * @param parser Parser to read xml elements from
     * @return item
     * @throws XMLStreamException
     */
    public static Item streamToDOM(int rootType, XMLStreamReader parser) throws XMLStreamException {
        MemTreeBuilder builder = new MemTreeBuilder();
        builder.startDocument();
        int event;
        boolean finish = false;
        while ((event = parser.next()) != XMLStreamConstants.END_DOCUMENT) {
            switch(event) {
                case XMLStreamConstants.START_ELEMENT:
                    AttributesImpl attribs = new AttributesImpl();
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        javax.xml.namespace.QName qn = parser.getAttributeName(i);
                        attribs.addAttribute(qn.getNamespaceURI(), qn.getLocalPart(), qn.getPrefix() + ':' + qn.getLocalPart(), parser.getAttributeType(i), parser.getAttributeValue(i));
                    }
                    builder.startElement(QName.fromJavaQName(parser.getName()), attribs);
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (NAMESPACE.equals(parser.getNamespaceURI()) && VALUE_ELEMENT.equals(parser.getLocalName())) finish = true; else builder.endElement();
                    break;
                case XMLStreamConstants.CHARACTERS:
                    builder.characters(parser.getText());
                    break;
            }
            if (finish) break;
        }
        builder.endDocument();
        if (rootType == Type.DOCUMENT) return builder.getDocument(); else if (rootType == Type.ELEMENT) return (NodeImpl) builder.getDocument().getDocumentElement(); else return (NodeImpl) builder.getDocument().getFirstChild();
    }

    /**
     * Creates an Item from a streamed representation
     * @param reader
     * @return item
     * @throws XMLStreamException
     */
    public static Item streamToDOM(Reader reader, XQItemType type) throws XMLStreamException, XQException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        factory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        XMLStreamReader parser = factory.createXMLStreamReader(reader);
        return streamToDOM(parser, type);
    }

    /**
     * Creates a node from a string representation
     * @param content
     * @return node
     * @throws XMLStreamException
     */
    public static Node streamToNode(String content) throws XMLStreamException {
        StringReader reader = new StringReader(content);
        return streamToNode(reader);
    }

    /**
     * Creates a node from a streamed representation
     * @param reader
     * @return item
     * @throws XMLStreamException
     */
    public static Node streamToNode(Reader reader) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        factory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        XMLStreamReader parser = factory.createXMLStreamReader(reader);
        MemTreeBuilder builder = new MemTreeBuilder();
        builder.startDocument();
        int event;
        boolean finish = false;
        while ((event = parser.next()) != XMLStreamConstants.END_DOCUMENT) {
            switch(event) {
                case XMLStreamConstants.START_ELEMENT:
                    AttributesImpl attribs = new AttributesImpl();
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        javax.xml.namespace.QName qn = parser.getAttributeName(i);
                        attribs.addAttribute(qn.getNamespaceURI(), qn.getLocalPart(), qn.getPrefix() + ':' + qn.getLocalPart(), parser.getAttributeType(i), parser.getAttributeValue(i));
                    }
                    builder.startElement(QName.fromJavaQName(parser.getName()), attribs);
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (NAMESPACE.equals(parser.getNamespaceURI()) && VALUE_ELEMENT.equals(parser.getLocalName())) finish = true; else builder.endElement();
                    break;
                case XMLStreamConstants.CHARACTERS:
                    builder.characters(parser.getText());
                    break;
            }
            if (finish) break;
        }
        builder.endDocument();
        return builder.getDocument().getDocumentElement();
    }
}
