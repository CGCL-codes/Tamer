package imtek.optsuite.analysis.zernike;

/**
 * @author Alexander Bieber <fleque@users.sourceforge.net>
 *
 */
public class DataPointDescriptor {

    private PolarCoordinate polarCoordinate;

    private double value;

    private double index;

    /**
	 * @return the index
	 */
    public double getIndex() {
        return index;
    }

    /**
	 * @param index the index to set
	 */
    public void setIndex(double index) {
        this.index = index;
    }

    /**
	 * @return the polarCoordinate
	 */
    public PolarCoordinate getPolarCoordinate() {
        return polarCoordinate;
    }

    /**
	 * @param polarCoordinate the polarCoordinate to set
	 */
    public void setPolarCoordinate(PolarCoordinate polarCoordinate) {
        this.polarCoordinate = polarCoordinate;
    }

    /**
	 * @return the value
	 */
    public double getValue() {
        return value;
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(double value) {
        this.value = value;
    }
}
