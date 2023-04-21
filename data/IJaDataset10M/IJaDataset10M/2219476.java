package cz.jAlgorithm.structures.heap;

/**
 *
 * @author Pavel Micka
 */
public interface HeapIface<ENTITY extends Comparable> {

    /**
     * Insert element into heap
     * @param e element
     */
    void insert(ENTITY e);

    /**
     * Delete and return entity with highest priority
     * @return entity with highest priority, @null if heap is empty
     */
    ENTITY returnTop();
}
