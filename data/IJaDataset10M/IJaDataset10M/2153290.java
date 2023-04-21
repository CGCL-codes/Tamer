package org.omegahat.Simulation.MCMC.Proposals;

import org.omegahat.Simulation.MCMC.*;
import org.omegahat.Probability.Distributions.*;
import org.omegahat.Simulation.RandomGenerators.PRNG;

public class HastingsCoupledMixtureProposal implements HastingsCoupledProposal {

    protected HastingsCoupledProposal[] proposals;

    protected double[] proposal_probs;

    protected double[] normalized_probs;

    protected double[] cumulative_probs;

    protected PRNG prng;

    public int numChains() {
        return proposals.length;
    }

    public void setProposal(int which, HastingsCoupledProposal what, double prob) {
        proposals[which] = what;
        proposal_probs[which] = prob;
        normalize();
    }

    public void setProposals(HastingsCoupledProposal[] what, double[] probs) {
        if (what.length != probs.length) throw new RuntimeException("The length of the mixture proportions vector must " + " match the number of proposals.");
        proposals = what;
        proposal_probs = probs;
        normalize();
    }

    public HastingsCoupledProposal[] getProposals() {
        return proposals;
    }

    public double[] getProposalProbs() {
        return proposal_probs;
    }

    protected void normalize() {
        double probs_sum = 0.0;
        for (int i = 0; i < proposal_probs.length; i++) {
            if (proposal_probs[i] < 0.0) throw new RuntimeException("Relative probabilities must be non-negative.");
            probs_sum += proposal_probs[i];
        }
        if (probs_sum <= 0.0) throw new RuntimeException("Relative probabilities must sum to non-zero value.");
        double cumsum = 0.0;
        for (int i = 0; i < proposal_probs.length; i++) {
            normalized_probs[i] = proposal_probs[i] / probs_sum;
            cumsum += normalized_probs[i];
            cumulative_probs[i] = cumsum;
        }
    }

    /** 
 * Convenience method for computing the probability of proposing a move.  
 */
    public double transitionProbability(Object from, Object to, int which, MultiState stateVector) {
        return conditionalPDF(to, from, which, stateVector);
    }

    /** 
 * Convenience method for computing the log probability of proposing a move.  
 */
    public double logTransitionProbability(Object from, Object to, int which, MultiState stateVector) {
        return logConditionalPDF(to, from, which, stateVector);
    }

    public double logConditionalPDF(Object state, Object conditions, int which, MultiState stateVector) {
        return Math.log(conditionalPDF(state, conditions, which, stateVector));
    }

    public double conditionalPDF(Object state, Object conditions, int which, MultiState stateVector) {
        double retval = 0.0;
        double mult;
        double inner;
        int numProposals = proposals.length;
        retval = 0.0;
        for (int i = 0; i < numProposals; i++) {
            retval += normalized_probs[i] * proposals[i].conditionalPDF(state, conditions, which, stateVector);
        }
        return retval;
    }

    public Object generate(Object conditionals, int which, MultiState stateVector) {
        double rand = prng.nextDouble();
        for (int i = 0; i < proposals.length; i++) {
            if (rand < cumulative_probs[i]) return proposals[i].generate(conditionals, which, stateVector);
        }
        throw new RuntimeException("Internal Error." + "Unable to randomize between proposal distributions. " + "Check cumulative_probs[].");
    }

    public HastingsCoupledMixtureProposal(int nProposal, PRNG prng) {
        proposals = new HastingsCoupledProposal[nProposal];
        proposal_probs = new double[nProposal];
        normalized_probs = new double[nProposal];
        cumulative_probs = new double[nProposal];
        this.prng = prng;
    }

    public HastingsCoupledMixtureProposal(HastingsCoupledProposal[] proposalList, double[] proposal_probs, PRNG prng) {
        proposals = proposalList;
        this.proposal_probs = proposal_probs;
        normalized_probs = new double[proposal_probs.length];
        cumulative_probs = new double[proposal_probs.length];
        normalize();
        this.prng = prng;
    }
}
