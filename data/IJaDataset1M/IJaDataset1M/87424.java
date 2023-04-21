package bpiwowar.argparser.checkers;

/**
 * @author bpiwowar
 * @date Nov 29, 2007
 *
 */
public class DoubleLimit extends Limit {

    private double value;

    public DoubleLimit(Type type, double value) {
        super(type);
        this.value = value;
    }

    /**
	 * Constructs from a double
	 * @param d
	 */
    public DoubleLimit(double d) {
        this(Type.UNKNOWN, d);
    }

    public int compareTo(Object object) {
        if (object instanceof Double) return Double.compare(value, (Double) object); else if (object instanceof Float) return Float.compare((float) value, (Float) object);
        throw new ClassCastException("Cannot compare a double to " + object.getClass());
    }

    @Override
    public String toString() {
        return super.toString() + "  " + value;
    }

    public String getDescription() {
        return super.getDescription() + " " + value;
    }
}
