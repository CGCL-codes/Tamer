package pubweb.worker.bench;

import pubweb.supernode.sched.DynamicParameterContainer;
import pubweb.supernode.sched.StaticParameterContainer;

public class HpcCompPowerEstimator implements CompPowerEstimator {

    public StaticParameterContainer getStaticProcessorParameters() {
        return new StaticParameterContainer(1.0);
    }

    public DynamicParameterContainer estimateComputationPower() {
        return new DynamicParameterContainer(1.0);
    }
}
