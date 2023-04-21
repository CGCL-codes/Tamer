package binf.ai.search.nodestore;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityQueueNodeStore implements NodeStore {

    private PriorityQueue<Node> list;

    /**
     * creeert een instantie
     */
    public PriorityQueueNodeStore() {
        this(new Comparator<Node>() {

            public int compare(Node o1, Node o2) {
                return Float.compare(o1.getPathCost(), o2.getPathCost());
            }
        });
    }

    /**
     * creeert een instantie
     * @param comparator de custom Comparator voor deze PriorityQueue
     */
    public PriorityQueueNodeStore(Comparator<Node> comparator) {
        list = new PriorityQueue<Node>(1, comparator);
    }

    public void add(Node aNode) {
        list.offer(aNode);
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
