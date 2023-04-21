package viewer.selection.rectangular;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class Latitude implements Comparable<Latitude> {

    private double value;

    /**
	 * @param value
	 *            the value of this longitude
	 */
    public Latitude(double value) {
        this.value = value;
    }

    @Override
    public int compareTo(Latitude other) {
        if (value < other.value) {
            return 1;
        }
        if (value > other.value) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    /**
	 * @return the double value.
	 */
    public double value() {
        return value;
    }
}
