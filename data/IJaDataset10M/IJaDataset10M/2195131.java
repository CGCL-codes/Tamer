package com.incendiaryblue.cmslite;

import java.util.*;
import java.io.Serializable;

/**
 * An implementation of Node that remembers its location in a linked subtree.
 * LinkedNodes can be nested arbitrarily such that the node pointed to by a
 * LinkedNode is itself a LinkedNode. This allows a linked tree of arbitrary
 * depth to be formed.
 *
 * <p>Title: LinkedNode</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: syzygy</p>
 * @author Giles Taylor
 * @version 1.0
 */
public class LinkedNode implements Node, Serializable {

    private Node link, node;

    /**
	 * Construct a new LinkedNode at a point in the tree, specifying the point
	 * at which the node's line of ancestry links to a link node. Traversal of
	 * the tree from node to the root occurs by checking to see if the current
	 * node's category is the same as the link's category, and if so branching
	 * into link's line of ancestry.
	 *
	 * @param link The point at which the link is rooted. Often this will be a
	 *             Node of CategoryLink type, but can be a Node of any type
	 * @param node The node within the linked subtree
	 */
    private LinkedNode(Node link, Node node) {
        this.link = link;
        this.node = node;
    }

    /**
	 * Return the Node to which this LinkedNode is pointing.
	 *
	 * @return A Node object
	 */
    public Node getNode() {
        return node;
    }

    public String getName() {
        return node.getName();
    }

    public String getDescription() {
        return node.getDescription();
    }

    public List getChildNodes() {
        List l = new ArrayList();
        for (Iterator i = node.getChildNodes().iterator(); i.hasNext(); ) {
            l.add(linkedNode(link, (Node) i.next()));
        }
        return l;
    }

    public Node getChildNode(String name) {
        for (Iterator i = getChildNodes().iterator(); i.hasNext(); ) {
            Node n = (Node) i.next();
            if (n.getName().equalsIgnoreCase(name)) {
                return n;
            }
        }
        return null;
    }

    public CategoryStub getCategoryStub() {
        return node.getCategoryStub();
    }

    public int getNodeType() {
        return node.getNodeType();
    }

    public Node getParentNode() {
        Node parent = node.getParentNode();
        if (node instanceof LinkedNode) {
            if (parent != null) {
                return linkedNode(link, parent);
            }
        }
        if (parent != null) {
            if (parent.getCategoryStub().equals(link.getCategoryStub())) {
                return link;
            }
        }
        if (node.getCategoryStub().equals(link.getCategoryStub())) {
            return link.getParentNode();
        }
        if (parent == null) {
            return null;
        }
        return linkedNode(link, parent);
    }

    public List getContentItems() {
        List l = new ArrayList();
        String thisNode = this.getNodeDescriptor();
        for (Iterator i = node.getContentItems().iterator(); i.hasNext(); ) {
            Content c = ((ContentReference) i.next()).getContent();
            l.add(thisNode + "," + c.getPrimaryKey());
        }
        return new ContentReferenceList(l);
    }

    public List getNodePath() {
        Node parent = getParentNode();
        List l = new ArrayList();
        if (parent != null) {
            l.addAll(parent.getNodePath());
        }
        l.add(this);
        return l;
    }

    public String getPath() {
        return NodeHelper.getPath(this);
    }

    /**
	 * Get a String descriptor of the node. The descriptor for a LinkedNode will
	 * be in the form <pre>/&gt;LINK_DESCRIPTOR&lt;,&gt;NODE_DESCRIPTOR&lt;</PRE>,
	 * where LINK_DESCRIPTOR and NODE_DESCRIPTOR are the results of calling
	 * getNodeDescriptor() on the defining nodes.
	 *
	 * @return A String representing the node.
	 */
    public String getNodeDescriptor() {
        return "/" + link.getNodeDescriptor() + "," + node.getNodeDescriptor();
    }

    public int countChildNodes() {
        return this.getCategoryStub().countChildNodes();
    }

    public String getProperty(String name) {
        return node.getProperty(name);
    }

    public void setProperty(String name, String value) {
        throw new UnsupportedOperationException("Cannot set a property on a LinkedNode.");
    }

    public boolean equals(Object o) {
        if (!(o instanceof Node)) {
            return false;
        }
        Node n = (Node) o;
        return n.getPath().equals(this.getPath());
    }

    public boolean isLinked() {
        return true;
    }

    /**
	 * Factory method to create a linked node. This method performs checks to
	 * ensure that
	 * @param link
	 * @param node
	 * @return
	 */
    public static Node linkedNode(Node link, Node node) {
        for (Node n = node; n != null; n = n.getParentNode()) {
            if (n.equals(link)) {
                return node;
            }
        }
        return new LinkedNode(link, node);
    }

    public int hashCode() {
        return getNodeDescriptor().hashCode();
    }

    public Properties getProperties() {
        return node.getProperties();
    }
}
