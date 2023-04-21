package com.volantis.devrep.repository.accessors;

import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import java.util.Set;
import java.util.HashSet;

/**
 * This filter is used to delete specifically identified nodes and their
 * children from the SAX event sequence.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class DeletionFilter extends XMLFilterImpl {

    /**
     * Used to track the current child nesting level (indicates that deletion
     * filtering is active).
     */
    private int nesting = 0;

    /**
     * Defines the set of nodes to be deleted.
     */
    private Set nodeNames = new HashSet();

    /**
     * Data object class for identifying a node that should be filtered.
     */
    public static class NodeIdentifier {

        public final String nodeName;

        public final String namespace;

        /**
         * Construct a new NodeIdentifier
         * @param nodeName the name of the node - cannot be null
         * @param namespace the namespace of the node - cannot be null
         * @throws IllegalArgumentException if either argument null
         */
        public NodeIdentifier(String nodeName, String namespace) {
            if (nodeName == null) {
                throw new IllegalArgumentException("Cannot be null: nodeName");
            }
            if (namespace == null) {
                throw new IllegalArgumentException("Cannot be null: namespace");
            }
            this.nodeName = nodeName;
            this.namespace = namespace;
        }

        public boolean equals(Object o) {
            boolean equals = o != null && o.getClass().equals(getClass());
            if (equals) {
                NodeIdentifier ni = (NodeIdentifier) o;
                equals = nodeName.equals(ni.nodeName) && namespace.equals(ni.namespace);
            }
            return equals;
        }

        public int hashCode() {
            return nodeName.hashCode() + namespace.hashCode();
        }
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param nodeNames the set of names identifying the nodes to be deleted
     */
    public DeletionFilter(NodeIdentifier[] nodeNames) {
        updateNodeNames(nodeNames);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param nodeNames the set of names identifying the nodes to be deleted
     * @param xmlReader the parent XMLReader for this filter
     */
    public DeletionFilter(NodeIdentifier[] nodeNames, XMLReader xmlReader) {
        super(xmlReader);
        updateNodeNames(nodeNames);
    }

    /**
     * Update the node names set.
     * @param nodeNames the array of node names.
     */
    private void updateNodeNames(NodeIdentifier[] nodeNames) {
        for (int i = 0; i < nodeNames.length; i++) {
            this.nodeNames.add(nodeNames[i]);
        }
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
        if (nesting > 0 || (nodeNames.contains(new NodeIdentifier(localName, namespaceURI)))) {
            nesting++;
        } else {
            super.startElement(namespaceURI, localName, qName, attributes);
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (nesting > 0 || (nodeNames.contains(new NodeIdentifier(localName, namespaceURI)))) {
            nesting--;
        } else {
            super.endElement(namespaceURI, localName, qName);
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        if (nesting > 0) {
        } else {
            super.characters(ch, start, length);
        }
    }
}
