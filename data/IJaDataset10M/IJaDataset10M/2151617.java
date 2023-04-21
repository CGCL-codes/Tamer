package crafty.metamodel;

public class MetaParameter extends MetaTypedElement {

    public static final Direction IN = new Direction("in");

    public static final Direction OUT = new Direction("out");

    public static final Direction INOUT = new Direction("inout");

    private Direction direction = IN;

    public MetaParameter() {
    }

    public MetaParameter(String name, MetaClassifier type) {
        super(name, type);
    }

    public MetaParameter(String name, TypeUsage type) {
        super(name, type);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetaParameter)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final MetaParameter parameter = (MetaParameter) o;
        if (!direction.equals(parameter.direction)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + direction.hashCode();
        return result;
    }

    public Object copy() {
        MetaParameter other = (MetaParameter) super.copy();
        other.direction = direction;
        return other;
    }

    public void accept(MetaModelVisitor v) {
        v.visitParameter(this);
    }

    public Direction getDirection() {
        return direction;
    }

    public MetaParameter setDirection(Direction direction) {
        assert (direction != null) : "Direction cannot be null.";
        this.direction = direction;
        return this;
    }

    public static final class Direction {

        private final String value;

        public String toString() {
            return value;
        }

        private Direction(String val) {
            value = val;
        }
    }
}
