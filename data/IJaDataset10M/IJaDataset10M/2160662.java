package org.omegahat.Simulation.MCMC;

import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Targets.*;
import org.omegahat.Simulation.MCMC.Listeners.*;

/** 
 *  A Markov Chain.
 * 
 *  <p> Markov Chains have a current state and generate a new state
 *  based only on the current state. Its state can only be set at
 *  instantiation.  
 */
public interface MarkovChain {

    /** Get the current state of the Markov Chain. */
    public MCMCState getState();

    /** Generate the next state from the current state. */
    public void step();

    /** 
     * Perform several <code>step</code>s. 
     *
     * @param n how many steps 
     */
    public void iterate(int n);
}
