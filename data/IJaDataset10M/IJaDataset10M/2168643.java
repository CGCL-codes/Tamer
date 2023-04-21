package uk.ac.ebi.rhea.ph;

import uk.ac.ebi.rhea.domain.Compound;

/**
 * Exception thrown when a compound cannot be adjusted because it lacks a
 * chemical structure.
 * @author rafalcan
 *
 */
public class NoStructureException extends NotAdjustableException {

    private static final long serialVersionUID = -6664519539644993954L;

    public NoStructureException(Compound compound, double pH) {
        super(compound, pH);
        reason = "no chemical structure";
    }
}
