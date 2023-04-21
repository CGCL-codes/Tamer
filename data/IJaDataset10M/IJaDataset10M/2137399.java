package gossipServices.aggregation;

/**
 * Description: 
 * Provides an implementation of the interface 
 * SingleValueDouble and contains a unique value
 * of type double.
 *
 */
public class SingleValueContainer implements SingleValueDouble {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private double value;

    public SingleValueContainer() {
    }

    public SingleValueContainer(Double v) {
        value = v;
    }

    @Override
    public final double getValue() {
        return value;
    }

    @Override
    public final void setValue(double val) {
        value = val;
    }

    public String toString() {
        String ret = new String("");
        ret += value;
        return ret;
    }
}
