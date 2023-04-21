package de.uni_trier.st.nevada.view.central;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import de.uni_trier.st.nevada.graphs.Int.Graph;
import de.uni_trier.st.nevada.graphs.Int.Node;

/**
 * NodeTree stores position information for all Nodes in a graph. It provides methods to find all nodes at a point fast.
 * @author reitz
 *
 */
public class NodeTree {

    private class SortNodesX implements Comparator<Node> {

        private Graph graphX;

        public SortNodesX(Graph graph) {
            this.graphX = graph;
        }

        public int compare(Node o1, Node o2) {
            if (o1.getPosition(this.graphX).getX() < o2.getPosition(this.graphX).getX()) return -1;
            if (o1.getPosition(this.graphX).getX() > o2.getPosition(this.graphX).getX()) return 1;
            return 0;
        }

        public boolean equals(Node o1, Node o2) {
            return o1.getPosition(this.graphX).getX() == o2.getPosition(this.graphX).getX();
        }
    }

    private class SortNodesY implements Comparator<Node> {

        private Graph graphY;

        public SortNodesY(Graph graph) {
            this.graphY = graph;
        }

        public int compare(Node o1, Node o2) {
            if (o1.getPosition(this.graphY).getY() < o2.getPosition(this.graphY).getY()) return -1;
            if (o1.getPosition(this.graphY).getY() > o2.getPosition(this.graphY).getY()) return 1;
            return 0;
        }

        public boolean equals(Node o1, Node o2) {
            return o1.getPosition(this.graphY).getY() == o2.getPosition(this.graphY).getY();
        }
    }

    private class SortNodesCompoundLabel implements Comparator<Node> {

        private Graph graphL;

        public SortNodesCompoundLabel(Graph g) {
            this.graphL = g;
        }

        public int compare(Node o1, Node o2) {
            if (o1.getCompoundLabel(this.graphL) == null || o2.getCompoundLabel(this.graphL) == null) return 0;
            return -1 * (o1.getCompoundLabel(this.graphL).compareTo(o2.getCompoundLabel(this.graphL)));
        }

        public boolean equals(Node o1, Node o2) {
            if (o1.getCompoundLabel(this.graphL) == null || o2.getCompoundLabel(this.graphL) == null) return false;
            return o1.getCompoundLabel(this.graphL).equals(o2.getCompoundLabel(this.graphL));
        }
    }

    private NodeTreeElem root;

    private Graph graph;

    private int basicSize;

    /**
	 * Creates a NodeTree
	 * @param graph
	 * @param basicSize size of smalest cluster of nodes
	 */
    public NodeTree(Graph graph, int basicSize) {
        this.graph = graph;
        this.root = new NodeTreeElem(NodeTreeElem.Direction.horizontal);
        this.basicSize = basicSize;
        ArrayList<Node> list = new ArrayList<Node>(graph.getVisibleNodes());
        createTreeH(list, this.root);
    }

    /**
	 * Create a NodeTreeMap for graph
	 * @param graph
	 */
    public NodeTree(Graph graph) {
        this.graph = graph;
        this.root = new NodeTreeElem(NodeTreeElem.Direction.horizontal);
        this.basicSize = 5;
        ArrayList<Node> list = new ArrayList<Node>(graph.getVisibleNodes());
        createTreeH(list, this.root);
    }

    private void createTreeH(ArrayList<Node> list, NodeTreeElem elem) {
        int size = list.size();
        if (list.size() <= this.basicSize) {
            elem.setNodes(new LinkedHashSet<Node>(list));
            return;
        }
        Collections.sort(list, new SortNodesY(this.graph));
        ArrayList<Node> l = new ArrayList<Node>(list.subList(0, list.size() / 2));
        list.removeAll(l);
        Node tempN = list.get(0);
        double b = tempN.getPosition(this.graph).getY();
        double diff;
        LinkedList<Node> tempL = new LinkedList<Node>();
        for (Iterator<Node> it = l.iterator(); it.hasNext(); ) {
            tempN = it.next();
            diff = tempN.getDimension(this.graph).getHeight() / 2.0;
            if (tempN.getPosition(this.graph).getY() + diff > b) tempL.add(tempN);
        }
        for (Iterator<Node> it = list.iterator(); it.hasNext(); ) {
            tempN = it.next();
            diff = tempN.getDimension(this.graph).getHeight() / 2.0;
            if (tempN.getPosition(this.graph).getY() - diff < b) l.add(tempN);
        }
        list.addAll(tempL);
        elem.setBorder(b);
        if (list.size() == size) {
            elem.setNodes(new LinkedHashSet<Node>(list));
            return;
        }
        if (l.size() == size) {
            elem.setNodes(new LinkedHashSet<Node>(l));
            return;
        }
        elem.setLeft(new NodeTreeElem(NodeTreeElem.Direction.vertical));
        elem.setRight(new NodeTreeElem(NodeTreeElem.Direction.vertical));
        createTreeV(l, elem.getLeft());
        createTreeV(list, elem.getRight());
    }

    private void createTreeV(ArrayList<Node> list, NodeTreeElem elem) {
        int size = list.size();
        if (list.size() <= this.basicSize) {
            elem.setNodes(new LinkedHashSet<Node>(list));
            return;
        }
        Collections.sort(list, new SortNodesX(this.graph));
        ArrayList<Node> l = new ArrayList<Node>(list.subList(0, list.size() / 2));
        list.removeAll(l);
        Node tempN = list.get(0);
        double b = tempN.getPosition(this.graph).getX();
        double diff;
        LinkedList<Node> tempL = new LinkedList<Node>();
        for (Iterator<Node> it = l.iterator(); it.hasNext(); ) {
            tempN = it.next();
            diff = tempN.getDimension(this.graph).getWidth() / 2.0;
            if (tempN.getPosition(this.graph).getX() + diff > b) tempL.add(tempN);
        }
        for (Iterator<Node> it = list.iterator(); it.hasNext(); ) {
            tempN = it.next();
            diff = tempN.getDimension(this.graph).getWidth() / 2.0;
            if (tempN.getPosition(this.graph).getX() - diff < b) l.add(tempN);
        }
        list.addAll(tempL);
        elem.setBorder(b);
        if (list.size() == size) {
            elem.setNodes(new LinkedHashSet<Node>(list));
            return;
        }
        if (l.size() == size) {
            elem.setNodes(new LinkedHashSet<Node>(l));
            return;
        }
        elem.setLeft(new NodeTreeElem(NodeTreeElem.Direction.horizontal));
        elem.setRight(new NodeTreeElem(NodeTreeElem.Direction.horizontal));
        createTreeH(l, elem.getLeft());
        createTreeH(list, elem.getRight());
    }

    /**
	 * Searches Node next to p. If there are two nodes at equal distance a reace condition determins which is picked.
	 * @param p
	 * @return a node close to p
	 */
    public Node getNode(Point2D p) {
        return getNodeRec(p, this.root);
    }

    private Node getNodeRec(Point2D p, NodeTreeElem e) {
        if (e.size() != 0) {
            for (final Node n : e.getNodes()) {
                if (n.getBounds(this.graph).contains(p)) {
                    return n;
                }
            }
            return null;
        }
        if (e.getDirection() == NodeTreeElem.Direction.horizontal) {
            if (p.getY() < e.getBorder()) return getNodeRec(p, e.getLeft());
            return getNodeRec(p, e.getRight());
        }
        if (e.getDirection() == NodeTreeElem.Direction.vertical) {
            if (p.getX() < e.getBorder()) return getNodeRec(p, e.getLeft());
            return getNodeRec(p, e.getRight());
        }
        return null;
    }

    public Node getNodeIt(Point2D p) {
        NodeTreeElem temp = this.root;
        boolean dir = true;
        if (temp.size() == 0 && temp.getLeft() == null && temp.getRight() == null) {
            return null;
        }
        while (temp.size() == 0) {
            if (dir) {
                if (p.getY() < temp.getBorder()) temp = temp.getLeft(); else temp = temp.getRight();
            } else {
                if (p.getX() < temp.getBorder()) temp = temp.getLeft(); else temp = temp.getRight();
            }
            dir = !dir;
        }
        LinkedList<Node> result = new LinkedList<Node>();
        for (final Node n : temp.getNodes()) {
            if (n.getBounds(this.graph).contains(p)) {
                result.add(n);
            }
        }
        if (result.size() == 1) return result.get(0);
        if (result.size() > 1) {
            Collections.sort(result, new SortNodesCompoundLabel(this.graph));
            return result.get(0);
        }
        return null;
    }

    /**
	 * Searches all Nodes in rectangle r
	 * @param r
	 * @return Returns all nodes in the given rectangle.
	 */
    public Set<Node> getNodesInRect(Rectangle2D r) {
        Set<Node> result = new HashSet<Node>();
        Node temp;
        for (Iterator<Node> it = this.graph.getVisibleNodes().iterator(); it.hasNext(); ) {
            temp = it.next();
            if (r.contains(temp.getBounds(this.graph))) result.add(temp);
        }
        return result;
    }

    /**
	 * Delete a Node from tree.
	 * @param node
	 */
    public void deleteNode(Node node) {
        this.root.deleteNode(node, this.graph);
    }

    /**
	 * Adds a Node to tree.
	 * @param node
	 */
    public void addNode(Node node) {
        this.root.addNode(node, this.graph);
    }
}
