package playground.dressler.ea_flow;

import playground.dressler.control.Debug;
import playground.dressler.control.FlowCalculationSettings;

public class BreadCrumbOutOfSource implements BreadCrumb {

    private final int t;

    public BreadCrumbOutOfSource(int t) {
        this.t = t;
    }

    /**
	 * Method returning a String representation of the StepEdge
	 */
    @Override
    public String toString() {
        String s;
        s = "Out of source @ " + t;
        return s;
    }

    @Override
    public PathStep createPathStepForward(VirtualNode arrival, FlowCalculationSettings settings) {
        if (Debug.GLOBAL && Debug.STEP_CHECKS) {
            if (!(arrival instanceof VirtualSource)) {
                throw new RuntimeException("Can only step out of source in a source!");
            }
        }
        return new StepSourceFlow(arrival.getRealNode(), t, false);
    }

    @Override
    public PathStep createPathStepReverse(VirtualNode start, FlowCalculationSettings settings) {
        if (Debug.GLOBAL && Debug.STEP_CHECKS) {
            if (!(start instanceof VirtualSource)) {
                throw new RuntimeException("Can only step out of source in a source!");
            }
        }
        return new StepSourceFlow(start.getRealNode(), t, true);
    }
}

;
