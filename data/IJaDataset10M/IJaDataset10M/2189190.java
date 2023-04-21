package org.redwood.business.usermanagement.reportmeasures.reportclassificationmeasure;

/**
 * The PK class.
 * This class is the primary key.
 *
 * @author  Gerrit Franke
 * @version 1.0
 */
public class ReportClassificationMeasurePK implements java.io.Serializable {

    public String rw_id;

    /**
   * Constructor.
   *
   * @param     subjectId  the rw_id of the subject.
   */
    public ReportClassificationMeasurePK(String rw_id) {
        this.rw_id = rw_id;
    }

    /**
   * Constructor.
   */
    public ReportClassificationMeasurePK() {
    }

    /**
   * Returns the string value of the subject.
   *
   * @return    the value of the subject rw_id.
   */
    public String toString() {
        return rw_id;
    }

    /**
   * Returns the hashCode of the subject.
   *
   * @return    the value of the subject rw_id.
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
        if (other instanceof ReportClassificationMeasurePK) {
            isEqual = (rw_id == ((ReportClassificationMeasurePK) other).rw_id);
        }
        return isEqual;
    }
}
