package org.sourcejammer.project;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import org.sourcejammer.project.view.NodeInfo;

/**
 * A linked and indexed colletion of Nodes. Each node within
 * a NodeList must have a unique name.
 */
public class NodeList {

    private Vector mvecNodes;

    private Hashtable mhshIndex;

    private Hashtable mhshNames;

    public NodeList() {
        mvecNodes = new Vector();
        mhshIndex = new Hashtable();
        mhshNames = new Hashtable();
    }

    /**
   * Provided to dump contents of NodeList to an array of
   * Nodes. This array can be passed more easily using SOAP.
   */
    public Node[] getContentsAsArray() {
        int iSize = mvecNodes.size();
        Node[] ndArray = new Node[iSize];
        mvecNodes.toArray(ndArray);
        return ndArray;
    }

    /**
   * Provided to set this NodeList using an array of Nodes. Note that
   * calling this method will empty out any previously set nodes from the NodeList.
   *
   * @exception NodeExistsException if the array contains more than one node with
   * the same nodename.
   */
    public void setContentsFromArray(Node[] nodes) throws NodeExistsException {
        mvecNodes = new Vector();
        mhshIndex = new Hashtable();
        mhshNames = new Hashtable();
        for (int i = 0; i < nodes.length; i++) {
            addNode(nodes[i]);
        }
    }

    /**
   * Adds a node to the list.
   *
   * @param node -- the Node to be added.
   *
   * @exception NodeExistsException if a Node with the same name
   * is already in the NodeList.
   */
    public void addNode(Node node) throws NodeExistsException {
        if (mhshIndex.get(node.getNodeName()) == null) {
            Integer intIndex = new Integer(mvecNodes.size());
            mhshIndex.put(node.getNodeName(), intIndex);
            mhshNames.put(intIndex, node.getNodeName());
            mvecNodes.add(node);
        } else {
            throw new NodeExistsException("The NodeList already contains a Node with the name: " + node.getNodeName() + ".");
        }
    }

    /**
   * Returns a Node based on it's index value.
   */
    public Node getNode(int index) throws NodeDoesNotExistException {
        Node ndReturn = (Node) mvecNodes.get(index);
        if (ndReturn == null) {
            throw new NodeDoesNotExistException("The NodeList does not contain a Node at index: " + index + ".");
        }
        return ndReturn;
    }

    /**
   * Returns a Node based on it's unique name.
   */
    public Node getNode(String nodeName) throws NodeDoesNotExistException {
        Integer intIndex = (Integer) mhshIndex.get(nodeName);
        Node ndReturn = null;
        if (intIndex != null) {
            ndReturn = (Node) mvecNodes.get(intIndex.intValue());
        } else {
            throw new NodeDoesNotExistException("The NodeList does not contain a Node with the name: " + nodeName + ".");
        }
        return ndReturn;
    }

    /**
   * Removes a Node from the NodeList based on it's index value.
   *
   * @param index -- index of the Node to be removed.
   * @return the Node that is being removed.
   * @exception NodeDoesNotExistException -- if there is no node
   * at the specified index.
   */
    public Node removeNode(int index) throws NodeDoesNotExistException {
        Node ndRemoved = null;
        Integer intIndex = new Integer(index);
        String name = (String) mhshNames.get(intIndex);
        if (name != null) {
            mhshNames.remove(intIndex);
            mhshIndex.remove(name);
            ndRemoved = (Node) mvecNodes.remove(intIndex.intValue());
            reindex();
        } else {
            throw new NodeDoesNotExistException("The NodeList does not contain a Node at index: " + index + ".");
        }
        return ndRemoved;
    }

    /**
   * Removes a Node from the NodeList based on it's unique name.
   *
   * @param name -- name of the Node to be removed.
   * @return the Node that is being removed.
   * @exception NodeDoesNotExistException -- if there is no node
   * with the specified name.
   */
    public Node removeNode(String nodeName) throws NodeDoesNotExistException {
        Node ndRemoved = null;
        Integer intIndex = (Integer) mhshIndex.get(nodeName);
        if (intIndex != null) {
            ndRemoved = removeNode(intIndex.intValue());
        } else {
            throw new NodeDoesNotExistException("The NodeList does not contain a Node with the name: " + nodeName + ".");
        }
        return ndRemoved;
    }

    private void reindex() {
        Enumeration enmNodes = mvecNodes.elements();
        mhshIndex = new Hashtable();
        mhshNames = new Hashtable();
        int iCounter = 0;
        while (enmNodes.hasMoreElements()) {
            Node nd = (Node) enmNodes.nextElement();
            Integer intIndex = new Integer(iCounter);
            String sName = nd.getNodeName();
            mhshNames.put(intIndex, sName);
            mhshIndex.put(sName, intIndex);
            iCounter++;
        }
    }

    /**
   * Returns a NodeIterator for iterating through the contents of this
   * NodeList.
   */
    public NodeIterator getIterator() {
        NodeIterator oIterator = new NodeListIterator(mvecNodes);
        return oIterator;
    }

    /**
   * Returns the number of nodes in the ilst.
   */
    public int size() {
        return mvecNodes.size();
    }
}
