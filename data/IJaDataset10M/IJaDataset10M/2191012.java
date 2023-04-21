package binf.ai.search.nodestore;

import java.util.ArrayDeque;

public class Queue implements NodeStore {

    private java.util.Queue<Node> list;

    public Queue() {
        this.list = new ArrayDeque<Node>();
    }

    public void add(Node aNode) {
        list.add(aNode);
    }

    public Node remove() {
        return list.poll();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }
}
