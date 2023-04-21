package jung.ext.utils;

import edu.uci.ics.jung.exceptions.FatalException;

public final class Triple {

    private final Object value1;

    private final Object value2;

    private final Object value3;

    public Triple(Object value1, Object value2, Object value3) {
        if (value1 == null || value2 == null || value3 == null) throw new FatalException("A Triple can't hold nulls.");
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    public String toString() {
        return "\nTriple: {" + BasicUtils.deTokenizer(BasicUtils.addToSet(BasicUtils.createSet(value1.toString(), value2.toString()), value3.toString()), ", ") + "}";
    }

    public Object getFirst() {
        return value1;
    }

    public Object getSecond() {
        return value2;
    }

    public Object getThird() {
        return value3;
    }

    public boolean equals(Object o) {
        if (o instanceof Triple) {
            Triple tt = (Triple) o;
            Object first = tt.getFirst();
            Object second = tt.getSecond();
            Object third = tt.getThird();
            return ((first == value1 || first.equals(value1)) && (second == value2 || second.equals(value2)) && (third == value3 || third.equals(value3)));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return value1.hashCode() + value2.hashCode() + value3.hashCode();
    }
}
