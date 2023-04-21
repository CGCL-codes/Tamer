package org.matsim.core.mobsim.queuesim.events;

import org.matsim.core.mobsim.queuesim.QueueSimulation;

/**
 * @author dgrether
 * @see QueueSimulationInitializedEvent
 */
public class QueueSimulationInitializedEventImpl extends AbstractQueueSimulationEvent implements QueueSimulationInitializedEvent {

    public QueueSimulationInitializedEventImpl(QueueSimulation queuesim) {
        super(queuesim);
    }
}
