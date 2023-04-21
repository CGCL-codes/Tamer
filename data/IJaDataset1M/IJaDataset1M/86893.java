package isql.expression.aggregate;

/**
 * Returns the minimum value among them added using the add method
 * @author SHZ Oct 31, 2007
 */
public class Max implements IAggregate {

    Object max = null;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void add(Object value) {
        if (max == null || ((Comparable) value).compareTo(max) > 0) max = value;
    }

    public Object getSum() {
        return max;
    }
}
