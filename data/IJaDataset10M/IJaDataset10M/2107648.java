package de.fmannan.addbook.common.fieldtypes;

import java.util.Date;

/**
 * This interface provides access to a <code>Date</code> IContact field as is
 * typically necessary to build generic field editors.
 * 
 * @author fmannan
 * 
 */
public interface IDateField extends IField {

    /**
     * Get the value of the IContact field.
     */
    public Date getValue();

    /**
     * Set the value of the IContact field.
     */
    public void setValue(Date value);
}
