package gossipServices.bootstraping.theBootstrapingService;

import gossipServices.basic.nodeDescriptors.ConcreteNodeDescriptor;
import gossipServices.basic.nodeDescriptors.NodeDescriptor;
import gossipServices.basic.nodeDescriptors.comparators.IdSorterer;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

/**
 * Description: 
 *
 */
public class ConcreteLeafSet implements LeafSet {

    public static final NodeDescriptor EMPTY = new ConcreteNodeDescriptor("EMPTY");

    protected List<NodeDescriptor> orderedSet;

    protected NodeDescriptor localNode = null;

    protected int leafSetSize;

    public ConcreteLeafSet(int size, List<NodeDescriptor> initElements, NodeDescriptor localNode) {
        setSizeAndElements(size, initElements);
        this.localNode = localNode;
    }

    private void setSizeAndElements(int size, List<NodeDescriptor> initElements) {
        int actualSize;
        leafSetSize = size;
        orderedSet = new Vector<NodeDescriptor>();
        orderedSet.addAll(initElements);
        actualSize = orderedSet.size();
        if (actualSize < size) {
            for (int i = actualSize; i < size; i++) {
                orderedSet.add(new ConcreteNodeDescriptor(EMPTY));
            }
        }
    }

    /**
	 * 
	 */
    private void removeFarthestElements() {
        int elementsToRemove = orderedSet.size() - leafSetSize;
        dinstanceOrdering();
        for (int i = 0; i < elementsToRemove; i++) {
        }
    }

    /**
	 * 
	 */
    private void dinstanceOrdering() {
    }

    @Override
    public LeafSet head(int firstElements) {
        return new ConcreteLeafSet(firstElements, orderedSet.subList(0, firstElements), localNode);
    }

    @Override
    public NodeDescriptor selectPeer() {
        sort();
        int randIndex = (int) (Math.random() * (size() / 2));
        NodeDescriptor n = head(size() / 2).get(randIndex);
        return n;
    }

    @Override
    public int size() {
        return orderedSet.size();
    }

    @Override
    public NodeDescriptor get(int index) {
        return orderedSet.get(index);
    }

    @Override
    public boolean merge(Queue<NodeDescriptor> mq) {
        boolean ret = true;
        while (!mq.isEmpty()) {
            ret &= orderedSet.add(mq.poll());
        }
        return ret;
    }

    @Override
    public boolean sort() {
        Collections.sort(orderedSet, new IdSorterer());
        return true;
    }

    @Override
    public boolean adjustSuccPrec() {
        boolean ret = false;
        if (localNode == null) {
            System.err.println(Thread.currentThread().getName() + " in " + ConcreteLeafSet.class + ": localNode must be set.");
        } else {
            orderedSet.add(new ConcreteNodeDescriptor(localNode));
            sort();
            ret = true;
        }
        return ret;
    }

    public final NodeDescriptor getLocalNode() {
        return localNode;
    }

    public final void setLocalNode(NodeDescriptor localNode) {
        this.localNode = localNode;
    }

    public String toString() {
        String ret = new String("");
        for (NodeDescriptor n : orderedSet) {
            ret += n + "\n";
        }
        return ret;
    }
}
