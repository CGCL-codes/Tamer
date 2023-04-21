package de.peathal.math;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class provides access to the standard matrix operations.
 * Its implementation based on LinkedLists, so it is only good for
 * 'thin' occupied matrices.
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public class ThinMatrixMap<T> {

    /**
     * Here we put all the nodes in.
     */
    protected List<TMNode> nodes;

    /** All header nodes imagine the all have y = -1.*/
    protected Map<Integer, HNode> headerNodes;

    /** Creates a new instance of ThinMatrix */
    public ThinMatrixMap() {
        this(10);
    }

    /** Creates a new instance of ThinMatrix.
     * The matrix starts from 0,0. Hint: x increases horizontal. y vertical.
     *
     * @param initialNoOfXNodes specfies how much space you need at least
     * to manage this matrix.
     */
    public ThinMatrixMap(int initialNoOfXNodes) {
        nodes = new ArrayList<TMNode>(initialNoOfXNodes * 2);
        headerNodes = new TreeMap<Integer, HNode>();
        headerNodes.put(0, new HNode(0, END));
    }

    public void set(int x, int y, T value) {
        HNode xNode = new HNode(x, END);
        HNode oldNode = headerNodes.put(x, xNode);
        if (oldNode != null) xNode.columnLink = oldNode.columnLink;
        TMNode pointerNode, tmNextNode;
        int i;
        if (xNode.columnLink != END) {
            pointerNode = nodes.get(xNode.columnLink);
            while (pointerNode.y != y) {
                i = pointerNode.bottomLink;
                if (i == END || y < (tmNextNode = nodes.get(i)).y && pointerNode.y < y) {
                    pointerNode.bottomLink = nodes.size();
                    pointerNode = new TMNode(x, UNDEFINED, null, i);
                    nodes.add(pointerNode);
                    break;
                }
                pointerNode = tmNextNode;
            }
        } else {
            xNode.columnLink = nodes.size();
            pointerNode = new TMNode(x, UNDEFINED, null, END);
            nodes.add(pointerNode);
        }
        pointerNode.value = value;
        pointerNode.y = y;
    }

    public T get(int x, int y) {
        int i;
        HNode xNode = headerNodes.get(x);
        TMNode tmNextNode;
        if (xNode != null) {
            TMNode pointerNode = null;
            if (xNode.columnLink != END) {
                pointerNode = nodes.get(xNode.columnLink);
                while (pointerNode.y != y) {
                    i = pointerNode.bottomLink;
                    if (i == END || y < (tmNextNode = nodes.get(i)).y && pointerNode.y < y) {
                        pointerNode = null;
                        break;
                    }
                    pointerNode = tmNextNode;
                }
            }
            if (pointerNode != null) {
                return pointerNode.value;
            } else {
                return null;
            }
        } else return null;
    }

    /**
     * This method returns an iterator over a constant y => One row.
     */
    public Iterator getRowIterator(int y) {
        return new RowIterator();
    }

    static int END = -1;

    static int UNDEFINED = -2;

    protected class TMNode {

        int bottomLink;

        int x;

        int y;

        T value;

        public TMNode(int x, int y, T value, int bottomLink) {
            this.x = x;
            this.y = y;
            this.value = value;
            this.bottomLink = bottomLink;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(50);
            sb.append("[");
            sb.append("x:");
            sb.append(x);
            sb.append("\t y:");
            sb.append(y);
            sb.append("\t value:");
            sb.append(value.toString());
            sb.append("]");
            return sb.toString();
        }
    }

    protected class HNode {

        int columnLink;

        int xOrY;

        public HNode(int xOrY, int columnLink) {
            this.columnLink = columnLink;
            this.xOrY = xOrY;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(50);
            sb.append("[");
            sb.append("xOry:");
            sb.append(xOrY);
            sb.append("\t columnLink:");
            sb.append(columnLink);
            sb.append("]");
            return sb.toString();
        }
    }

    protected class RowIterator implements Iterator {

        public boolean hasNext() {
            return false;
        }

        public Object next() {
            return null;
        }

        public void remove() {
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        HNode nextNode;
        TMNode pointerNode;
        for (HNode yNode : headerNodes.values()) {
            if (yNode.columnLink != END) {
                pointerNode = nodes.get(yNode.columnLink);
                sb.append(pointerNode.toString());
                sb.append("\n");
                while (pointerNode.bottomLink != END) {
                    pointerNode = nodes.get(pointerNode.bottomLink);
                    sb.append(pointerNode.toString());
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }
}
