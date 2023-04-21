package moduledefault.classify.c45.rafael.jadti;

/**
 * This class implements the value of a symbolic attribute.
 **/
public class KnownNumericalValue extends NumericalValue {

    /**
     * This attribute value represented as an <code>double</code>.
     **/
    public final double doubleValue;

    /**
     * Creates a new numerical value.
     *
     * @param value This attribute value represented as a <code>double</code>.
     **/
    public KnownNumericalValue(double value) {
        doubleValue = value;
    }

    public boolean isUnknown() {
        return false;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof KnownNumericalValue)) return false; else return ((KnownNumericalValue) o).doubleValue == doubleValue;
    }

    public int hashCode() {
        return (int) doubleValue;
    }

    public String toString() {
        return "" + doubleValue;
    }
}
