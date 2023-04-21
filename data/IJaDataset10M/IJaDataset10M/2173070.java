package org.arcsoft.bbcash;

import java.util.*;
import java.io.Serializable;

/**
 *
 *
 * @author $Author: mrspaceman $
 * @version $Revision: 1.4 $
 * Date $Date: 2001/04/13 07:00:23 $
 */
public class CurrencyInfo implements Serializable {

    private int m_id = 0;

    private String m_name = "";

    private String m_isoCountryCode = "";

    private String m_symbol = "";

    /** constructor
    	@param id - the unique id of the transaction record
    */
    public CurrencyInfo(int anId) {
        m_id = anId;
    }

    /** returns the unique Id of this currency
    */
    public int getId() {
        return m_id;
    }

    /** fetch the date that this transaction should stop being recorded
    */
    public String getName() {
        return m_name;
    }

    /** set the date that this transaction should stop being recorded
    */
    public void setName(String aName) {
        m_name = aName;
    }

    /** fetch the date that this transaction should stop being recorded
    */
    public String getCountry() {
        return m_isoCountryCode;
    }

    /** set the date that this transaction should stop being recorded
    */
    public void setCountry(String aString) {
        m_isoCountryCode = aString;
    }

    /** fetch the date that this transaction should stop being recorded
    */
    public String getSymbol() {
        return m_symbol;
    }

    /** set the date that this transaction should stop being recorded
    */
    public void setSymbol(String aString) {
        m_symbol = aString;
    }
}
