package fulmine.util.reference;

import static fulmine.util.Utils.nullCheck;

/**
 * Type safe object for holding 2 objects.
 * 
 * @author Ramon Servadei
 * @param <FIRST>
 *            the first type
 * @param <SECOND>
 *            the second type
 */
public class DualValue<FIRST, SECOND> {

    /** Holds the values */
    protected Values values;

    public DualValue(FIRST first, SECOND second) {
        this(new Values(first, second));
    }

    protected DualValue(Values values) {
        nullCheck(values, "No values provided");
        this.values = values;
    }

    /**
     * Get the first object from the constructor
     * 
     * @return the first object this was constructed with
     */
    @SuppressWarnings("unchecked")
    public FIRST getFirst() {
        return (FIRST) this.values.get(0);
    }

    /**
     * Get the second object from the constructor
     * 
     * @return the second object this was constructed with
     */
    @SuppressWarnings("unchecked")
    public SECOND getSecond() {
        return (SECOND) this.values.get(1);
    }

    @Override
    public final String toString() {
        return this.values.toString();
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((values == null) ? 0 : values.hashCode());
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final boolean equals(Object obj) {
        if (is.same(this, obj)) {
            return true;
        }
        if (is.differentClass(this, obj)) {
            return false;
        }
        final DualValue other = (DualValue) obj;
        return is.eq(values, other.values);
    }
}
