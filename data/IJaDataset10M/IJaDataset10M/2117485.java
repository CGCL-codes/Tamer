package de.ibis.permoto.solver.sim.tech.simEngine1.netStrategies.queuePutStrategies;

import de.ibis.permoto.solver.sim.tech.simEngine1.netStrategies.QueuePutStrategy;
import de.ibis.permoto.solver.sim.tech.simEngine1.queueNet.Job;
import de.ibis.permoto.solver.sim.tech.simEngine1.queueNet.JobInfo;
import de.ibis.permoto.solver.sim.tech.simEngine1.queueNet.JobInfoList;
import de.ibis.permoto.solver.sim.tech.simEngine1.queueNet.NetNode;
import de.ibis.permoto.solver.sim.tech.simEngine1.queueNet.NodeSection;

/**
 * This class implements a specific queue put strategy: all arriving jobs
 * are put at the beginning of the queue.
 * @author Francesco Radaelli
 */
public class HeadStrategy extends QueuePutStrategy {

    /**
     * all arriving jobs are put at the beginning of the queue.
	 * @param job Job to be added to the queue.
     * @param queue Queue.
     * @param sourceSection Job source section.
     * @param sourceNode Job source node.
     * @param callingSection The section which calls this strategy.
     */
    public void put(Job job, JobInfoList queue, byte sourceSection, NetNode sourceNode, NodeSection callingSection) {
        queue.addFirst(new JobInfo(job));
    }
}
