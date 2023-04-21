package org.redwood.business.usermanagement.measures.measure;

/**
 * The PK class.
 * This class is the primary key.
 *
 * @author  C. Hille
 * @version 1.0
 */
public class MeasuresPK implements java.io.Serializable {

    public String rw_id;

    /**
   * Constructor.
   *
   * @param measuresID:  the rw_id of the measures.
   */
    public MeasuresPK(String rw_id) {
        this.rw_id = rw_id;
    }

    /**
   * Constructor.
   */
    public MeasuresPK() {
    }

    /**
   * Returns the string value of the Measures.
   *
   * @return    the value of the Measures rw_id.
   */
    public String toString() {
        return rw_id;
    }

    /**
   * Returns the hashCode of the Measures
   *
   * @return    the value of the Measures rw_id.
   */
    public int hashCode() {
        return rw_id.hashCode();
    }

    /**
   * Check whether the object is equal to this
   * primary key.
   *
   * @return    equal or not.
   */
    public boolean equals(Object other) {
        boolean isEqual = false;
        if (other instanceof MeasuresPK) {
            isEqual = (rw_id == ((MeasuresPK) other).rw_id);
        }
        return isEqual;
    }
}
