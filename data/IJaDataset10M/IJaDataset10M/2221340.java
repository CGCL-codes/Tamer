package org.exist.xquery.value;

import org.exist.dom.NodeProxy;
import org.exist.memtree.NodeImpl;
import java.util.Comparator;

/**
 * @author wolf
 */
public class MixedNodeValueComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        NodeValue n1 = (NodeValue) o1;
        NodeValue n2 = (NodeValue) o2;
        if (n1.getImplementationType() == NodeValue.IN_MEMORY_NODE) {
            if (n2.getImplementationType() == NodeValue.IN_MEMORY_NODE) return ((NodeImpl) n1).compareTo((NodeImpl) n2); else return -1;
        } else {
            if (n2.getImplementationType() == NodeValue.PERSISTENT_NODE) return ((NodeProxy) o1).compareTo((NodeProxy) o2); else return 1;
        }
    }
}
