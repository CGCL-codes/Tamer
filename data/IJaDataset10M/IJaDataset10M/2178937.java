package org.arcsoft.bbcash;

import java.io.Serializable;

/**
 *
 * @author $Author: mrspaceman $
 * @version $Revision: 1.4 $
 * $Date: 2001/04/13 07:00:23 $
 */
public class PhoneNumber implements Serializable {

    /** the sort code for the intitution (unique to each) */
    private String m_countryCode = "";

    /** the sort code for the intitution (unique to each) */
    private String m_areaCode = "";

    /** the sort code for the intitution (unique to each) */
    private String m_number = "";

    /** constructor
    	@param id - the unique id of the transaction record
    */
    public PhoneNumber() {
    }

    /** fetch the date that this transaction should stop being recorded */
    public String getCountryCode() {
        return m_countryCode;
    }

    /** set the date that this transaction should stop being recorded */
    public void setCountryCode(String aString) {
        m_countryCode = aString;
    }

    /** fetch the date that this transaction should stop being recorded */
    public String getAreaCode() {
        return m_areaCode;
    }

    /** set the date that this transaction should stop being recorded */
    public void setAreaCode(String aString) {
        m_areaCode = aString;
    }

    /** fetch the date that this transaction should stop being recorded */
    public String getNumber() {
        return m_number;
    }

    /** set the date that this transaction should stop being recorded */
    public void setNumber(String aString) {
        m_number = aString;
    }

    public String toString() {
        String tmpString = m_countryCode + " " + m_areaCode + " " + m_number;
        return tmpString;
    }
}
