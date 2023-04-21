package org.sbml.simulator.math.odes;

import org.apache.commons.math.ode.nonstiff.AdamsMoultonIntegrator;

/**
 * 
 * @author Roland Keller
 * @version $Rev: 16 $
 * @since 1.0
 */
public class AdamsMoultonSolver extends FirstOrderSolver {

    /**
	 * Generated serial version identifier.
	 */
    private static final long serialVersionUID = -2601862472447650296L;

    /**
	 * 
	 */
    public AdamsMoultonSolver() {
        super();
    }

    /**
	 * 
	 * @param adamsSolver
	 */
    public AdamsMoultonSolver(AdamsMoultonSolver adamsSolver) {
        super(adamsSolver);
        this.integrator = adamsSolver.getIntegrator();
    }

    /**
	 * 
	 * @param stepSize
	 */
    public AdamsMoultonSolver(double stepSize) {
        super(stepSize);
    }

    /**
	 * 
	 * @param stepSize
	 * @param nonnegative
	 */
    public AdamsMoultonSolver(double stepSize, boolean nonnegative) {
        super(stepSize, nonnegative);
    }

    @Override
    public AdamsMoultonSolver clone() {
        return new AdamsMoultonSolver(this);
    }

    @Override
    protected void createIntegrator() {
        integrator = new AdamsMoultonIntegrator(5, Math.min(1e-8, Math.min(1.0, getStepSize())), Math.min(1.0, getStepSize()), 0.00001, 0.00001);
    }

    @Override
    public String getName() {
        return "Adams-Moulton solver";
    }
}
