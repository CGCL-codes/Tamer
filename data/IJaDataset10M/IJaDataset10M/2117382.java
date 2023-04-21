package org.jmetis.servlets.service;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * {@code EmptyNodeList}
 * 
 * @author era
 */
class EmptyNodeList implements NodeList {

    /**
	 * Constructs a new {@code EmptyNodeList} instance.
	 */
    public EmptyNodeList() {
        super();
    }

    public int getLength() {
        return 0;
    }

    public Node item(int index) {
        return null;
    }
}
