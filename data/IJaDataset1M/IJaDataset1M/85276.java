package com.sun.sgs.impl.service.nodemap.policy;

import com.sun.sgs.impl.service.nodemap.NoNodesAvailableException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A very simple round robin assignment policy.
 */
public class RoundRobinPolicy extends AbstractNodePolicy {

    private final AtomicInteger nextNode = new AtomicInteger();

    /**
     * Creates a new instance of RoundRobinPolicy.
     *
     * @param props service properties
     */
    public RoundRobinPolicy(Properties props) {
        super();
    }

    /** {@inheritDoc} */
    public synchronized long chooseNode(long requestingNode) throws NoNodesAvailableException {
        if (availableNodes.size() < 1) {
            throw new NoNodesAvailableException("no live nodes available");
        }
        return availableNodes.get(nextNode.getAndIncrement() % availableNodes.size());
    }
}
