package org.redwood.business.etl.logfile;

import java.io.Serializable;

/**
 * The PK class.
 * This class is the primary key.
 *
 * @author M. Lehnert
 * @version 1.0
 */
public class LogFilePK implements Serializable {

    public long rw_id;

    /**
   * Constructor.
   *
   * @param     ID  the rw_id of the
   */
    public LogFilePK(long rw_id) {
        this.rw_id = rw_id;
    }

    /**
   * Constructor.
   */
    public LogFilePK() {
    }

    /**
   * Returns the hashCode of the subject.
   *
   * @return    the value of the  rw_id.
   */
    public int hashCode() {
        return new java.lang.Long(rw_id).hashCode();
    }

    /**
   * Check whether the object is equal to this
   * primary key.
   *
   * @return    equal or not.
   */
    public boolean equals(Object other) {
        boolean isEqual = false;
        if (other instanceof LogFilePK) {
            isEqual = (rw_id == ((LogFilePK) other).rw_id);
        }
        return isEqual;
    }

    /**
   * Returns the string value of the subject.
   *
   * @return    the value of the  rw_id.
   */
    public String toString() {
        return String.valueOf(this.rw_id);
    }
}
