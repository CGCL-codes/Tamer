package org.yawlfoundation.yawl.resourcing.allocators;

import org.yawlfoundation.yawl.resourcing.resource.Participant;
import org.yawlfoundation.yawl.resourcing.WorkQueue;
import java.util.Set;

/**
 * Performs allocation based on work queue length. This is the default allocator.
 *
 *  Create Date: 23/08/2007. Last Date: 12/11/2007
 *
 *  @author Michael Adams (BPM Group, QUT Australia)
 *  @version 2.0
 */
public class ShortestQueue extends AbstractAllocator {

    public ShortestQueue() {
        super();
        setName(this.getClass().getSimpleName());
        setDisplayName("Shortest Queue");
        setDescription("The Shortest-Queue allocator chooses from a distribution set " + "the participant with the shortest Allocated queue (i.e. the " + "least number of workitems in the Allocated queue).");
    }

    /**
     * selects the Participant with the least number of workitems in the 'allocated'
     * workqueue, or the first participant with an empty queue 
     * @param resSet the set of Participants to compare
     * @return the Participant with the shortest queue
     */
    public Participant performAllocation(Set<Participant> resSet) {
        int shortest = 10000;
        int qSize;
        Participant result = null;
        for (Participant p : resSet) {
            if (p.getWorkQueues() != null) qSize = p.getWorkQueues().getQueueSize(WorkQueue.ALLOCATED); else qSize = 0;
            if (qSize < shortest) {
                shortest = qSize;
                result = p;
                if (qSize == 0) break;
            }
        }
        return result;
    }

    public Set getParamKeys() {
        return null;
    }
}
