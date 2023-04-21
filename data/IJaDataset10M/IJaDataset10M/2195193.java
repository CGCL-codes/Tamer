package org.xmlsh.util;

import javanet.staxutils.XMLEventStreamWriter;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import net.sf.saxon.om.Axis;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NamePool;
import net.sf.saxon.om.NamespaceBinding;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.tree.iter.AxisIterator;
import net.sf.saxon.type.Type;

public class StAXUtils {

    public static void copy(NodeInfo node, XMLStreamWriter writer) throws XMLStreamException {
        switch(node.getNodeKind()) {
            case Type.ELEMENT:
                {
                    String prefix = node.getPrefix();
                    String localName = node.getLocalPart();
                    String namespaceURI = node.getURI();
                    writer.writeStartElement(prefix, localName, namespaceURI);
                    NamespaceBinding[] nsb = node.getDeclaredNamespaces(null);
                    if (nsb != null) {
                        for (NamespaceBinding ns : nsb) {
                            NamePool np = node.getNamePool();
                            String ns_prefix = ns.getPrefix();
                            String ns_uri = ns.getURI();
                            writer.writeNamespace(ns_prefix, ns_uri);
                        }
                    }
                    AxisIterator iter = node.iterateAxis(Axis.ATTRIBUTE);
                    Item item;
                    while ((item = iter.next()) != null) {
                        NodeInfo attr = (NodeInfo) item;
                        copy(attr, writer);
                    }
                    iter = node.iterateAxis(Axis.CHILD);
                    while ((item = iter.next()) != null) {
                        NodeInfo child = (NodeInfo) item;
                        copy(child, writer);
                    }
                    writer.writeEndElement();
                }
                break;
            case Type.ATTRIBUTE:
                writer.writeAttribute(node.getPrefix(), node.getURI(), node.getLocalPart(), node.getStringValue());
                break;
            case Type.TEXT:
            case Type.WHITESPACE_TEXT:
                writer.writeCharacters(node.getStringValue());
                break;
            case Type.PROCESSING_INSTRUCTION:
                writer.writeProcessingInstruction(node.getDisplayName(), node.getStringValue());
                break;
            case Type.COMMENT:
                writer.writeComment(node.getStringValue());
                break;
            case Type.DOCUMENT:
                {
                    AxisIterator iter = node.iterateAxis(Axis.CHILD);
                    Item item;
                    while ((item = iter.next()) != null) {
                        NodeInfo child = (NodeInfo) item;
                        copy(child, writer);
                    }
                }
                break;
            case Type.NAMESPACE:
                {
                    String prefix = node.getDisplayName();
                    if (Util.isBlank(prefix)) break;
                    String uri = node.getStringValue();
                    if (Util.isBlank(uri)) break;
                    writer.writeNamespace(prefix, uri);
                }
                break;
        }
    }

    public static void copy(NodeInfo node, XMLEventWriter writer) throws XMLStreamException {
        copy(node, (XMLStreamWriter) new XMLEventStreamWriter(writer));
    }

    private static String eventTypes[] = { "UNKNOWN", "START_ELEMENT", "END_ELEMENT", "PROCESSING_INSTRUCTION", "CHARACTERS", "COMMENT", "SPACE", "START_DOCUMENT", "END_DOCUMENT", "ENTITY_REFERENCE", "ATTRIBUTE", "DTD", "CDATA", "NAMESPACE", "NOTATION_DECLARATION", "ENTITY_DECLARATION" };

    public static String getEventTypeName(int type) {
        if (type >= 0 && type <= eventTypes.length) return eventTypes[type];
        return "";
    }

    public static int getEventTypeByName(String typeName) {
        for (int i = 1; i < eventTypes.length; i++) if (eventTypes[i].equals(typeName)) return i;
        return -1;
    }

    public static boolean matchesQName(javax.xml.namespace.QName name, QName qname) {
        return Util.isEqual(name.getNamespaceURI(), qname.getNamespaceURI()) && Util.isEqual(name.getLocalPart(), qname.getLocalName());
    }

    public static javax.xml.namespace.QName getQName(QName name) {
        return new javax.xml.namespace.QName(name.getNamespaceURI(), name.getLocalName(), name.getPrefix());
    }
}
