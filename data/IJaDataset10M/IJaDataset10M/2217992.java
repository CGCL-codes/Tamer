package org.sepp.utils.xml.datatypes;

/**
 * @author <a href="mailto:stefan.kraxberger@iaik.tugraz.at">Stefan
 *         Kraxberger</a>
 * 
 */
public class XMLNode {

    public static final int W3C_NODE = 1;

    public static final int KXML_NODE = 2;

    private int type;

    private Object node;

    public XMLNode(int type, Object node) {
        this.type = type;
        this.node = node;
    }

    public int getType() {
        return type;
    }

    public Object getNode() {
        return node;
    }
}
