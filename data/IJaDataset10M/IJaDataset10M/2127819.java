package at.jku.rdfstats.expr;

/**
 * @author dorgon
 *
 */
public class UncomparableRangePoint {

    Object point;

    public UncomparableRangePoint(Object point) {
        this.point = point;
    }

    public Object getValue() {
        return point;
    }

    public String toString() {
        return point.toString();
    }

    @Override
    public int hashCode() {
        return point.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof UncomparableRangePoint && hashCode() == other.hashCode();
    }
}
