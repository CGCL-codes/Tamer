package org.trdf.trdf4jena;

/**
 * A trust value to be associated to RDF triples.
 * A trust value represents the trustworthiness of RDF statements. Trust values
 * are either unknown or a number in the interval [-1,1]. A trust value of 1
 * represents absolute belief in the information represented by the statements;
 * -1 represents absolute disbelief; 0 represents the lack of belief/disbelief.
 * Unknown trust values represent unknown trustworthiness.
 *
 * @author Olaf Hartig
 */
public class TrustValue {

    /** the actual value */
    private final Float value;

    public static final TrustValue UnknownTrustValue = new TrustValue();

    /**
	 * Creates an unknown trust value.
	 */
    TrustValue() {
        this.value = null;
    }

    /**
	 * Creates a trust value with the given value.
	 *
	 * @param value the actual value which must be in the interval [-1,1]
	 */
    public TrustValue(float value) throws IllegalArgumentException {
        if ((value < -1.0) || (value > 1.0)) throw new IllegalArgumentException("The given value (" + String.valueOf(value) + ") is not a legal trust value.");
        this.value = new Float(value);
    }

    /**
	 * Returns the actual value.
	 */
    public Float getValue() {
        return value;
    }

    /**
	 * Returns true if this trust value is unknown.
	 */
    public boolean isUnknown() {
        return (value == null);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TrustValue)) return false;
        TrustValue otherTrustValue = (TrustValue) obj;
        if (isUnknown()) return otherTrustValue.isUnknown(); else return value.equals(otherTrustValue.getValue());
    }

    @Override
    public String toString() {
        if (isUnknown()) return "unknown trust value"; else return "trust value " + value.toString();
    }
}
