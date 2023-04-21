package ezsudoku.controller;

import ezsudoku.model.Coords;

/**
 * Listens to item candidates change.
 *
 * @author Cedric Chantepie (cchantepie@corsaire.fr)
 */
public interface CandidateListener {

    /**
     * Candidate |candidate| has been added to item at |coords|.
     *
     * @param candidate Candidate value added.
     * @param coords Item coordinates.
     */
    public void candidateAdded(final Integer candidate, final Coords coords);

    /**
     * Candidate |candidate| has been removed to item at |coords|.
     *
     * @param candidate Candidate value removed.
     * @param coords Item coordinates.
     */
    public void candidateRemoved(final Integer candidate, final Coords coords);
}
