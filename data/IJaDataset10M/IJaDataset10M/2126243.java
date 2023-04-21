package org.binarydom.impl;

import java.io.Serializable;
import java.util.Vector;
import org.binarydom.Element;
import org.binarydom.ElementList;

/**
 * @author Mattias
 */
public class ElementListImpl implements ElementList, Serializable {

    private static final long serialVersionUID = -8591760173700772377L;

    private Vector nodes;

    public ElementListImpl(Vector nodes) {
        this.nodes = nodes;
    }

    public int getLength() {
        return nodes.size();
    }

    public Element element(int index) {
        return (Element) nodes.elementAt(index);
    }
}
