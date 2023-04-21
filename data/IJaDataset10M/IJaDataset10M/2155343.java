package gov.lanl.Utility;

import nu.xom.*;
import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Displays a DOM document in a tree control, supports editing
 * for text/cdata types and updates their changes in the 
 * associated DOM Document
 *
 * @author  Andy Clark, IBM
 * @author  Torsten Staab, LANL
 * @author Jim George, Dave Forslund
 * @version $Revision: 3346 $ $Date: 2006-03-27 19:50:02 -0500 (Mon, 27 Mar 2006) $
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
        getModel().addTreeModelListener(new TreeModelListener() {

            public void treeNodesChanged(TreeModelEvent e) {
                DefaultMutableTreeNode treeNode;
                treeNode = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());
                try {
                    int index = e.getChildIndices()[0];
                    treeNode = (DefaultMutableTreeNode) (treeNode.getChildAt(index));
                } catch (NullPointerException exc) {
                }
                ((Model) getModel()).updateTextNode(treeNode, (String) treeNode.getUserObject());
            }

            public void treeNodesInserted(TreeModelEvent e) {
            }

            public void treeNodesRemoved(TreeModelEvent e) {
            }

            public void treeStructureChanged(TreeModelEvent e) {
            }
        });
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
     * Sets all text nodes to a specified value. Method by Torsten.
     *
     * @param newValue the new value for the text nodes
     */
    public void setTextNodes(String newValue) {
        ((Model) getModel()).setTextNodes(newValue);
    }

    /**
     * Returns all leaf nodes that have some contents.
     *
     * @return  a vector containing the found leaf nodes in form of
     *          <code>DefaultMutableTreeNode</code> objects
     */
    public Vector findNonEmptyLeaves() {
        return ((Model) getModel()).findNonEmptyLeaves();
    }

    /**
     * DOM tree model.
     *
     * @author  Andy Clark, IBM
     * @version 1.0
     */
    static class Model extends DefaultTreeModel implements Serializable {

        /** Document. */
        private Document document;

        /** Node Map. */
        private Hashtable nodeMap = new Hashtable();

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
            return (Node) nodeMap.get(treeNode);
        }

        /**
         * Sets all text nodes to a specified value. Method by Torsten.
         *
         * @param newValue the new value for the text nodes
         */
        public void setTextNodes(String newValue) {
            DefaultMutableTreeNode aTreeNode;
            Node aNode;
            DefaultMutableTreeNode theRootNode = (DefaultMutableTreeNode) getRoot();
            if (theRootNode == null) return;
            Enumeration aNodeEnum = theRootNode.depthFirstEnumeration();
            for (; aNodeEnum.hasMoreElements(); ) {
                aTreeNode = (DefaultMutableTreeNode) aNodeEnum.nextElement();
                if (aTreeNode.isLeaf()) {
                    aNode = getNode(aTreeNode);
                    if (aNode instanceof Text) {
                        aTreeNode.setUserObject(newValue);
                        ((Text) aNode).setValue(newValue);
                        nodeMap.remove(aTreeNode);
                        nodeMap.put(aTreeNode, aNode);
                    }
                }
            }
            nodeChanged(theRootNode);
        }

        /**
         * Updates the value of a DOM node. Method by Torsten.
         *
         * @param theTreeNode  a reference to the text node to be updated
         * @param newValue     the new value for the text nodes
         */
        public void updateTextNode(DefaultMutableTreeNode theTreeNode, String newValue) {
            Node theDOMNode;
            if (theTreeNode == null) {
                return;
            }
            if (theTreeNode.isLeaf()) {
                theDOMNode = getNode(theTreeNode);
                if (theDOMNode instanceof Text) {
                    theTreeNode.setUserObject(newValue);
                    ((Text) theDOMNode).setValue(newValue);
                    nodeMap.remove(theTreeNode);
                    nodeMap.put(theTreeNode, theDOMNode);
                }
            }
        }

        /**
         * Returns all leaf nodes that have some contents.
         *
         * @return  a vector containing the found leaf nodes in form of
         *          <code>DefaultMutableTreeNode</code> objects
         */
        public Vector findNonEmptyLeaves() {
            DefaultMutableTreeNode aTreeNode;
            Node aNode;
            Vector leafVector = new Vector();
            DefaultMutableTreeNode theRootNode = (DefaultMutableTreeNode) getRoot();
            if (theRootNode == null) return leafVector;
            Enumeration aNodeEnum = theRootNode.depthFirstEnumeration();
            String leafContents;
            for (; aNodeEnum.hasMoreElements(); ) {
                aTreeNode = (DefaultMutableTreeNode) aNodeEnum.nextElement();
                if (aTreeNode.isLeaf()) {
                    aNode = getNode(aTreeNode);
                    if (aNode instanceof Text) {
                        leafContents = (String) aTreeNode.getUserObject();
                        if (leafContents.trim().length() > 0) leafVector.addElement(aTreeNode);
                    }
                }
            }
            return leafVector;
        }

        /** Builds the tree. */
        private void buildTree() {
            if (document == null) {
                return;
            }
            Node el = document.getRootElement();
            int len = el.getChildCount();
            MutableTreeNode root = (MutableTreeNode) getRoot();
            for (int i = 0; i < len; i++) {
                Node node = el.getChild(i);
                if (node instanceof Document) root = insertDocumentNode(node, root);
                if (node instanceof Element) insertElementNode((Element) node, root);
            }
        }

        /** Inserts a node and returns a reference to the new node. */
        private MutableTreeNode insertNode(String what, MutableTreeNode where) {
            MutableTreeNode node = new DefaultMutableTreeNode(what);
            insertNodeInto(node, where, where.getChildCount());
            return node;
        }

        /** Inserts the document node.
         *
         */
        private MutableTreeNode insertDocumentNode(Node what, MutableTreeNode where) {
            MutableTreeNode treeNode = insertNode(((Document) what).getRootElement().getLocalName(), where);
            nodeMap.put(treeNode, what);
            return treeNode;
        }

        /** Inserts an element node. */
        private MutableTreeNode insertElementNode(Node what, MutableTreeNode where) {
            Element el = (Element) what;
            StringBuffer name = new StringBuffer();
            name.append('<');
            name.append(el.getLocalName());
            int attrCount = el.getAttributeCount();
            for (int i = 0; i < attrCount; i++) {
                Attribute attr = el.getAttribute(i);
                name.append(' ');
                name.append(attr.getLocalName());
                name.append("=\"");
                name.append(attr.getValue());
                name.append('"');
            }
            name.append('>');
            MutableTreeNode element = insertNode(name.toString(), where);
            nodeMap.put(element, what);
            int len = what.getChildCount();
            for (int i = 0; i < len; i++) {
                Node node = what.getChild(i);
                if (node instanceof Text) insertTextNode((Text) node, element); else insertElementNode((Element) node, element);
            }
            if (len == 0) insertTextNode((Text) what, element);
            return element;
        }

        /** Inserts a text node. */
        private MutableTreeNode insertTextNode(Node what, MutableTreeNode where) {
            String value = what.getValue().trim();
            if (value.length() == 0) value = "";
            MutableTreeNode treeNode = insertNode(value, where);
            nodeMap.put(treeNode, what);
            return treeNode;
        }
    }
}
