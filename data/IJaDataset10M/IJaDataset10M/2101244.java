package org.openrtk.idl.epp0604;

/**
 * Class that contains the generic, non-object specific EPP result data.</p>
 * Used in epp_Result.  Contains data for the results to the Status command.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0604/epp_ResultData.java,v 1.1 2003/03/21 15:54:26 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/21 15:54:26 $<br>
 * @see org.openrtk.idl.epp0604.epp_Result
 * @see org.openrtk.idl.epp0604.epp_Status
 */
public class epp_ResultData implements org.omg.CORBA.portable.IDLEntity {

    /**
   * Placeholder for the result data to the EPP Status command
   * @see #setStatus(org.openrtk.idl.epp0604.epp_StatusResultData)
   * @see #getStatus()
   */
    public org.openrtk.idl.epp0604.epp_StatusResultData m_status = null;

    /**
   * Empty constructor
   */
    public epp_ResultData() {
    }

    /**
   * The constructor with initializing variables.
   * @param _m_status The result data for the status command
   */
    public epp_ResultData(org.openrtk.idl.epp0604.epp_StatusResultData _m_status) {
        m_status = _m_status;
    }

    /**
   * Accessor method for the status result data
   * @param value The status result data
   * @see #m_status
   */
    public void setStatus(org.openrtk.idl.epp0604.epp_StatusResultData value) {
        m_status = value;
    }

    /**
   * Accessor method for the status result data
   * @return The status result data
   * @see #m_status
   */
    public org.openrtk.idl.epp0604.epp_StatusResultData getStatus() {
        return m_status;
    }

    /**
   * Converts this class into a string.
   * Typically used to view the object in debug output.
   * @return The string representation of this object instance
   */
    public String toString() {
        return this.getClass().getName() + ": { m_status [" + m_status + "] }";
    }
}
