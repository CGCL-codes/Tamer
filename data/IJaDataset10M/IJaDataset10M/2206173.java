package xxl.core.collections.queues;

import java.util.List;
import xxl.core.collections.sweepAreas.DefaultMemoryManageableSA.EveryQth;
import xxl.core.cursors.Cursors;
import xxl.core.cursors.filters.Filter;
import xxl.core.cursors.filters.Remover;

public class MemoryManageableListQueue<E> extends AbstractMemoryManageableQueue<E> {

    public MemoryManageableListQueue(int objectSize) {
        super(new ListQueue<E>(), objectSize);
    }

    public void handleOverflow() {
        if (getCurrentMemUsage() <= assignedMemSize) return;
        int realNoOfObjects = size();
        int maxAllowedNoOfObjects = 4 * assignedMemSize / (5 * objectSize);
        int overage = realNoOfObjects - maxAllowedNoOfObjects;
        List<E> list = ((ListQueue<E>) queue).list;
        Cursors.consume(new Remover(new Filter<E>(list.iterator(), new EveryQth<E>(realNoOfObjects, overage))));
        ((ListQueue<E>) queue).size = list.size();
    }
}
