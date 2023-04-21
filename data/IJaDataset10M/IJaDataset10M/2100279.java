package org.matsim.core.mobsim.framework.events;

import org.matsim.core.mobsim.framework.Simulation;

/**
 * Default implementation of {@link SimulationAfterSimStepEvent}.
 * 
 * @author mrieser
 */
public class SimulationAfterSimStepEventImpl<T extends Simulation> extends AbstractSimulationEvent<T> implements SimulationAfterSimStepEvent<T> {

    private final double simTime;

    public SimulationAfterSimStepEventImpl(final T queuesim, final double simTime) {
        super(queuesim);
        this.simTime = simTime;
    }

    public double getSimulationTime() {
        return this.simTime;
    }
}
