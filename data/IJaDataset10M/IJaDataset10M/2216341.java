package joelib2.util.types;

/**
 * Two integer values.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.2 $, $Date: 2005/02/17 16:48:42 $
 */
public class BasicDouble implements java.io.Serializable, DoubleValue {

    private static final long serialVersionUID = 1L;

    /**
     *  Description of the Field
     */
    public double doubleValue;

    /**
     *  Constructor for the IntInt object
     */
    public BasicDouble() {
    }

    /**
     *  Constructor for the IntInt object
     *
     * @param  _i1  Description of the Parameter
     */
    public BasicDouble(double initDouble) {
        doubleValue = initDouble;
    }

    public boolean equals(Object otherObj) {
        boolean isEqual = false;
        if (otherObj instanceof BasicInt) {
            BasicInt other = (BasicInt) otherObj;
            if ((other.intValue == this.doubleValue)) {
                isEqual = true;
            }
        }
        return isEqual;
    }

    /**
     * @return Returns the intValue.
     */
    public double getDoubleValue() {
        return doubleValue;
    }

    public int hashCode() {
        long bits = Double.doubleToLongBits(doubleValue);
        int dh = (int) (bits ^ (bits >>> 32));
        return dh;
    }

    /**
     * @param intValue The intValue to set.
     */
    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(10);
        sb.append(doubleValue);
        return sb.toString();
    }
}
