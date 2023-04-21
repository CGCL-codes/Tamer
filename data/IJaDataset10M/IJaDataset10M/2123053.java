package org.dllearner.core.owl;

/**
 * A datatype property assertion.
 * 
 * @author Jens Lehmann
 *
 */
public abstract class DatatypePropertyAssertion extends PropertyAssertion {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7202070934971240534L;

    protected DatatypeProperty datatypeProperty;

    protected Individual individual;

    public DatatypePropertyAssertion(DatatypeProperty datatypeProperty, Individual individual) {
        this.datatypeProperty = datatypeProperty;
        this.individual = individual;
    }

    public int getLength() {
        return 3;
    }

    /**
	 * @return the individual
	 */
    public Individual getIndividual() {
        return individual;
    }

    /**
	 * @return the datatypeProperty
	 */
    public DatatypeProperty getDatatypeProperty() {
        return datatypeProperty;
    }
}
