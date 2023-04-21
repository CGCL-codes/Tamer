package controller.simulation;

import model.modeling.*;
import model.simulation.*;
import view.modeling.*;

/**
 * Houses implementation common to SimViewCoordinator and 
 * SimViewCoupledCoordinator.
 */
public class SimViewCoordinatorBase {

    /**
     * Tries to attach the given listener to the given simulator.
     *
     * @param   listener    The listener to attach.
     * @param   simulator   The simulator to which to attach the listener.
     */
    protected void setListenerIntoSimulator(Object listener, coupledSimulator simulator) {
        if (listener == null) return;
        if (simulator instanceof ViewableAtomicSimulator) {
            if (listener instanceof ViewableAtomicSimulator.Listener) {
                ((ViewableAtomicSimulator) simulator).setListener((ViewableAtomicSimulator.Listener) listener);
            }
            if (listener instanceof ViewableAtomicSimulator.TimeScaleKeeper) {
                ((ViewableAtomicSimulator) simulator).setTimeScaleKeeper((ViewableAtomicSimulator.TimeScaleKeeper) listener);
            }
        }
    }

    /**
     * Creates a simulator for the given devs component.
     *
     * @param   devs    The component for which to create a simulator.
     */
    protected coupledSimulator createSimulator(IOBasicDevs devs) {
        if (devs instanceof ViewableAtomic) {
            return new ViewableAtomicSimulator((ViewableAtomic) devs);
        }
        return null;
    }
}
