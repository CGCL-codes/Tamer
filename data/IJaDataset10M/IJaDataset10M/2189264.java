package gov.sns.tools.xml.viewer.ui;

import java.io.Serializable;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.Hashtable;

/**
 * Displays a DOM document in a tree control.
 *
 * @author  Andy Clark, IBM
 * @version
 */
public class DOMTree extends JTree {

    /** Default constructor. */
    public DOMTree() {
        this(null);
    }

    /** Constructs a tree with the specified document. */
    public DOMTree(Document document) {
        super(new Model());
        setRootVisible(false);
        setDocument(document);
    }

    /** Sets the document. */
    public void setDocument(Document document) {
        ((Model) getModel()).setDocument(document);
        expandRow(0);
    }

    /** Returns the document. */
    public Document getDocument() {
        return ((Model) getModel()).getDocument();
    }

    /** get the org.w3c.Node for a MutableTreeNode. */
    public Node getNode(Object treeNode) {
        return ((Model) getModel()).getNode(treeNode);
    }

    /**
     * DOM tree model.
     *
     * @author  Andy Clark, IBM
     * @version
     */
    static class Model extends DefaultTreeModel implements Serializable {

        /** Document. */
        private Document document;

        /** Node Map. */
        private Hashtable<MutableTreeNode, Node> nodeMap = new Hashtable<MutableTreeNode, Node>();

        /** Default constructor. */
        public Model() {
            this(null);
        }

        /** Constructs a model from the specified document. */
        public Model(Document document) {
            super(new DefaultMutableTreeNode());
            setDocument(document);
        }

        /** Sets the document. */
        public synchronized void setDocument(Document document) {
            this.document = document;
            ((DefaultMutableTreeNode) getRoot()).removeAllChildren();
            nodeMap.clear();
            buildTree();
            fireTreeStructureChanged(this, new Object[] { getRoot() }, new int[0], new Object[0]);
        }

        /** Returns the document. */
        public Document getDocument() {
            return document;
        }

        /** get the org.w3c.Node for a MutableTreeNode. */
        public Node getNode(Object treeNode) {
            return nodeMap.get(treeNode);
        }

        /** Builds the tree. */
        private void buildTree() {
            if (document == null) {
                return;
            }
            NodeList nodes = document.getChildNodes();
            int len = (nodes != null) ? nodes.getLength() : 0;
            MutableTreeNode root = (MutableTreeNode) getRoot();
            for (int i = 0; i < len; i++) {
                Node node = nodes.item(i);
                switch(node.getNodeType()) {
                    case Node.DOCUMENT_NODE:
                        {
                            root = insertDocumentNode(node, root);
                            break;
                        }
                    case Node.ELEMENT_NODE:
                        {
                            insertElementNode(node, root);
                            break;
                        }
                    default:
                }
            }
        }

        /** Inserts a node and returns a reference to the new node. */
        private MutableTreeNode insertNode(String what, MutableTreeNode where) {
            MutableTreeNode node = new DefaultMutableTreeNode(what);
            insertNodeInto(node, where, where.getChildCount());
            return node;
        }

        /** Inserts the document node. */
        private MutableTreeNode insertDocumentNode(Node what, MutableTreeNode where) {
            MutableTreeNode treeNode = insertNode("<" + what.getNodeName() + '>', where);
            nodeMap.put(treeNode, what);
            return treeNode;
        }

        /** Inserts an element node. */
        private MutableTreeNode insertElementNode(Node what, MutableTreeNode where) {
            StringBuffer name = new StringBuffer();
            name.append('<');
            name.append(what.getNodeName());
            NamedNodeMap attrs = what.getAttributes();
            int attrCount = (attrs != null) ? attrs.getLength() : 0;
            for (int i = 0; i < attrCount; i++) {
                Node attr = attrs.item(i);
                name.append(' ');
                name.append(attr.getNodeName());
                name.append("=\"");
                name.append(attr.getNodeValue());
                name.append('"');
            }
            name.append('>');
            MutableTreeNode element = insertNode(name.toString(), where);
            nodeMap.put(element, what);
            NodeList children = what.getChildNodes();
            int len = (children != null) ? children.getLength() : 0;
            for (int i = 0; i < len; i++) {
                Node node = children.item(i);
                switch(node.getNodeType()) {
                    case Node.CDATA_SECTION_NODE:
                        {
                            insertCDataSectionNode(node, element);
                            break;
                        }
                    case Node.TEXT_NODE:
                        {
                            insertTextNode(node, element);
                            break;
                        }
                    case Node.ELEMENT_NODE:
                        {
                            insertElementNode(node, element);
                            break;
                        }
                }
            }
            return element;
        }

        /** Inserts a text node. */
        private MutableTreeNode insertTextNode(Node what, MutableTreeNode where) {
            String value = what.getNodeValue().trim();
            if (value.length() > 0) {
                MutableTreeNode treeNode = insertNode(value, where);
                nodeMap.put(treeNode, what);
                return treeNode;
            }
            return null;
        }

        /** Inserts a CData Section Node. */
        private MutableTreeNode insertCDataSectionNode(Node what, MutableTreeNode where) {
            StringBuffer CSectionBfr = new StringBuffer();
            CSectionBfr.append(what.getNodeValue());
            if (CSectionBfr.length() > 0) {
                MutableTreeNode treeNode = insertNode(CSectionBfr.toString(), where);
                nodeMap.put(treeNode, what);
                return treeNode;
            }
            return null;
        }
    }
}
