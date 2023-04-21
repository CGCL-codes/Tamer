package edu.ncsu.csc.csc454.project.wheel;

import java.util.ArrayList;
import java.util.Iterator;
import edu.ncsu.csc.csc454.project.wheel.node.Node;

/**
 * The MVC "model" class.
 * @author Team AW
 * @version 0.1
 */
public class NodeChain {

    private ArrayList<Node> nodes;

    public NodeChain() {
        nodes = new ArrayList<Node>();
    }

    public boolean add(Node n) {
        return nodes.add(n);
    }

    public boolean remove(Node n) {
        return nodes.remove(n);
    }

    public void move(int index, Node n) {
        nodes.remove(n);
        nodes.add(index, n);
    }

    public Node get(int index) {
        return nodes.get(index);
    }

    public int size() {
        return nodes.size();
    }

    public Iterator<Node> getIterator() {
        return nodes.iterator();
    }
}
