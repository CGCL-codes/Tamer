package org.quantumleaphealth.model.patient;

import java.util.HashMap;
import org.quantumleaphealth.ontology.ByteShortCharacteristicHolder;
import org.quantumleaphealth.ontology.CharacteristicCode;

/**
 * Holds non-<code>null</code> year-month characteristics.
 * @author Tom Bechtold
 * @version 2008-05-22
 */
public class YearMonthHistory extends HashMap<CharacteristicCode, YearMonth> implements ByteShortCharacteristicHolder {

    /**
     * Calls superclass' default constructor.
     */
    public YearMonthHistory() {
        super();
    }

    /**
     * Sets a year value for a year-month characteristic.
     * This method does nothing if <code>characteristicCode</code> is <code>null</code>
     * @param characteristicCode the code of the characteristic to set the year value for
     * @param yearValue the year to set to the characteristic
     *        or <code>null</code> to remove any value
     * @see org.quantumleaphealth.ontology.ShortCharacteristicHolder#setShort(org.quantumleaphealth.ontology.CharacteristicCode, java.lang.Short)
     */
    public void setShort(CharacteristicCode characteristicCode, Short yearValue) {
        if (characteristicCode == null) return;
        YearMonth yearMonth = get(characteristicCode);
        if (yearValue == null) {
            if (yearMonth == null) return;
            if (yearMonth.getMonth() <= 0) remove(characteristicCode); else yearMonth.setYear(yearValue);
            return;
        }
        if (yearMonth == null) {
            yearMonth = new YearMonth();
            put(characteristicCode, yearMonth);
        }
        yearMonth.setYear(yearValue);
    }

    /**
     * Returns the year value that is set in a characteristic
     * @param characteristicCode the code of the characteristic
     * @return the year that is set in a characteristic
     *         or <code>null</code> if no year is set
     *         or <code>characteristicCode</code> is <code>null</code>
     * @see org.quantumleaphealth.ontology.StringCharacteristicHolder#getString(org.quantumleaphealth.ontology.CharacteristicCode)
     */
    public Short getShort(CharacteristicCode characteristicCode) {
        YearMonth yearMonth = (characteristicCode == null) ? null : get(characteristicCode);
        return yearMonth == null ? null : yearMonth.getYear();
    }

    /**
     * Sets a month value for a year-month characteristic.
     * This method does nothing if <code>characteristicCode</code> is <code>null</code>
     * @param characteristicCode the code of the characteristic to set the month value for
     * @param monthValue the month to set to the characteristic
     *        or <code>0</code> to remove any value
     * @see org.quantumleaphealth.ontology.ByteShortCharacteristicHolder#setByte(org.quantumleaphealth.ontology.CharacteristicCode, byte)
     */
    public void setByte(CharacteristicCode characteristicCode, byte monthValue) {
        if (characteristicCode == null) return;
        YearMonth yearMonth = get(characteristicCode);
        if (monthValue <= 0) {
            if (yearMonth == null) return;
            if (yearMonth.getYear() == null) remove(characteristicCode); else yearMonth.setMonth(monthValue);
            return;
        }
        if (yearMonth == null) {
            yearMonth = new YearMonth();
            put(characteristicCode, yearMonth);
        }
        yearMonth.setMonth(monthValue);
    }

    /**
     * Returns the month value that is set in a characteristic
     * @param characteristicCode the code of the characteristic
     * @return the year that is set in a characteristic
     *         or <code>0</code> if no month is set
     *         or <code>characteristicCode</code> is <code>null</code>
     * @see org.quantumleaphealth.ontology.ByteShortCharacteristicHolder#getByte(org.quantumleaphealth.ontology.CharacteristicCode)
     */
    public byte getByte(CharacteristicCode characteristicCode) {
        YearMonth yearMonth = (characteristicCode == null) ? null : get(characteristicCode);
        return yearMonth == null ? 0 : yearMonth.getMonth();
    }

    /**
     * Version UID for serialization
     */
    private static final long serialVersionUID = 6778813766941544723L;
}
