package org.omegahat.Simulation.MCMC;

import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Targets.*;
import org.omegahat.Simulation.MCMC.Listeners.*;
import java.lang.reflect.Array;

/**
 * MCMCState that can hold several individual states.  Its purpose is
 * to allow a multi-chain sampler to easily store and retrieve the
 * states of individual chains.  
*/
public class DetailedMultiState extends MultiState {

    ProposalDetails proposalDetails;

    ProposalDetails proposalDetails() {
        return proposalDetails;
    }

    ProposalDetails proposalDetails(ProposalDetails proposalDetails) {
        return this.proposalDetails = proposalDetails;
    }

    public DetailedMultiState(int size) {
        super(size);
    }

    public DetailedMultiState(int size, Object oneValue) {
        super(size, oneValue);
    }

    public DetailedMultiState() {
        super();
    }

    public String toString() {
        String retval = super.toString();
        if (proposalDetails != null) retval += "Proposal Details:\n " + proposalDetails + "\n";
        return retval;
    }
}
