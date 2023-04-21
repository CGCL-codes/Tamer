package org.dllearner.core.owl;

/**
 * Represents datatype quantor restrictions. For instance,
 * \exists salary < 100.000 can be used to specify
 * those objects (persons) which have a salary of below 100.000. 
 * 
 * @author Jens Lehmann
 *
 */
public abstract class DatatypeQuantorRestriction extends QuantorRestriction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7230621629431369625L;

    /**
	 * Creates a <code>DatatypeQuantorRestriction</code> along the 
	 * given property.
	 * @param datatypeProperty The datatype property along which this restriction acts.
	 */
    public DatatypeQuantorRestriction(DatatypeProperty datatypeProperty) {
        super(datatypeProperty);
    }
}
