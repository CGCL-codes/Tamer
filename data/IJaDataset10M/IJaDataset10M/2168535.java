package name.dlazerka.gm.dijkstra;

import name.dlazerka.gm.Vertex;
import java.util.Comparator;
import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class BinaryHeap implements DijkstraQueue {

    private final Comparator<Vertex> comparator;

    public BinaryHeap(Comparator<Vertex> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void addAll(Set<Vertex> vertexSet) {
    }

    public void heapify() {
        throw new UnsupportedOperationException("TODO");
    }

    public Vertex extractMin() {
        throw new UnsupportedOperationException("TODO");
    }

    public void updateDecreased(Vertex v) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("TODO");
    }
}
