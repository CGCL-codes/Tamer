package org.modyna.modyna.simulation.integration;

/**
 * The interface has to be implemented by all classes which provide functions
 * that should be integrated with the Integrator class.
 * 
 * @author Dr. Rupert Rebentisch
 */
public interface Integratable {

    /**
	 * Caculate a time step within the integration scheme
	 * 
	 * @param yAtT
	 *            Vector of the values of the levels at the end of the
	 *            preceeding time step. Initial values of levels at the start of
	 *            the integration.
	 * @param t
	 *            value of time at the integration step
	 * @return resulting vector of level values at the end of time step
	 */
    DoubleVector calculateDerivatives(DoubleVector yAtT, double t);

    /**
	 * The calculateDerivatives method gets the vector of the levels of the set
	 * of ODEs at time t as an input. It returns the derivatives of these levels
	 * (i.e. the left hand sides) of the set of ODEs. This is the input that the
	 * Runge-Kutta scheme needs for integration. allQuantities is the pointer to
	 * an Object that stores the levels as well as the auxilliaries in one
	 * common DoubleVector. This is an out parameter, and it is passed in as an
	 * array (reference to the object), because allQuantities is allocated in
	 * the class that implements Integratable. This is not an elegant way to do
	 * this. Returning all auxilliaries by a different method would be more
	 * straightforward. However this would mean that the state of the class
	 * implementing Integratable would have to be preserved. A different option
	 * is to recalculate the auxiliaries, in a different method call.
	 * Recalculating the auxilliaries however is to costful.
	 * 
	 */
    DoubleVector calculateDerivatives(DoubleVector yAtT, double t, DoubleVector[] allQuantities);

    /**
	 * Retrieve names of the model quantities in the order of calculation, i.e.
	 * in the order in which they are stored in the Sample.
	 * 
	 * @return vector of strings containing the quantity names
	 */
    java.util.Vector getQuantityNames();
}
