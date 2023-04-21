package org.qedeq.kernel.bo.logic.proof.common;

/**
 * Can create a {@link ProofFinder}.
 *
 * @author  Michael Meyling
 */
public interface ProofFinderFactory {

    /**
     * Create a {@link ProofFinder}.
     *
     * @return  Instance that can create formal proofs for propositions.
     */
    public ProofFinder createProofFinder();
}
