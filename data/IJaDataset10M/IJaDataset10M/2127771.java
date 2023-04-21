package uk.ac.ebi.rhea.domain;

/**
 * Exception thrown when a formula is missing.
 * @author rafalcan
 * @since 2.0
 */
public class MissingFormulaException extends FormulaException {

    private static final long serialVersionUID = 7594282027126281029L;

    /**
     * Constructor for missing formulae.
     * @param rp the compound lacking formulae.
     */
    public MissingFormulaException(Compound compound) {
        super(compound);
    }
}
