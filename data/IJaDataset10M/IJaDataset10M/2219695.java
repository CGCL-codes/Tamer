package org.xenon.xmlPath.util;

import org.xenon.xmlPath.nodeForParsing.NodeText;

/**
 *
 * @author  ThomasK
 * @version
 */
public class NodeTextArray {

    private int[] array = new int[300];

    private int count = 0;

    private NodeTextIterator internIterator = new NodeTextIterator(this);

    public void add(NodeText in) {
        array[count] = in.getNumber();
        count++;
    }

    public NodeText get(int i) {
        return null;
    }

    public int size() {
        return count;
    }

    public NodeTextIterator iterator() {
        internIterator.set(0);
        return internIterator;
    }

    /** Creates new NodeTextArray */
    public NodeTextArray() {
    }
}
