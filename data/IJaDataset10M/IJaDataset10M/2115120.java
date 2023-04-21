package org.matsim.core.mobsim.queuesim.events;

import org.matsim.core.mobsim.queuesim.QueueSimulation;

/**
 * @author dgrether
 */
public class QueueSimulationBeforeSimStepEventImpl extends AbstractQueueSimulationEvent implements QueueSimulationBeforeSimStepEvent {

    private double time;

    public QueueSimulationBeforeSimStepEventImpl(QueueSimulation queuesim, double time) {
        super(queuesim);
        this.time = time;
    }

    /**
	 * @see org.matsim.core.mobsim.queuesim.events.QueueSimulationBeforeSimStepEvent#getSimulationTime()
	 */
    public double getSimulationTime() {
        return this.time;
    }
}
