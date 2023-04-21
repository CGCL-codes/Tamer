package org.omegahat.Simulation.MCMC;

import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Targets.*;
import org.omegahat.Simulation.MCMC.Listeners.*;

/** 
 *  A full conditional proposal generates the new state using each
 *  component's conditional distribution given all other components.
 */
public interface FullConditionalProposal extends MarkovProposal {
}
