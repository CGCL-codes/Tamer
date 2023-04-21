package com.vscorp.ui.swing.table.objectSource;

import java.awt.AWTError;
import java.lang.reflect.Field;

/**
 * Represents a time interval column for an ObjectSource.
 * The column's value is derived via
 * reflection from the object's attribute name. Reflection is used to eliminate
 * the need for a sub-class for each field of the object.
 * <p>
 */
public class ReflectedTimeIntervalColumn extends TimeIntervalColumn {

    /** The reflected field to use as a value */
    private Field mField;

    /**
     * Construct a new column configured by the specified arguments. This
     * constructor form is common to all of the "reflected" column types and is
     * here to support the EditableSortableTableColumn.createColumn factory
     * method.
     * The column is editable. A single click is
     * required to start editing. Column is sortable, but is not sorted
     * after construction. Default heading alignment is HEADER_CENTER. Heading text,
     * set by setHeaderValue, is an empty string.
     *
     * @param anObjectClass the class of the object returned from the ObjectSource
     * @param anAttributeName a String representing the name of the object's attribute
     * @param someArgs an array of Strings used to configure this specific column.
     *  It may be null or empty to use the default settings. These arguments are
     *  ignored by this specific class.
     *
     * @throws AWTError (unchecked exception) if the field doesn't exist on
     *  anObjectClass.
     */
    public ReflectedTimeIntervalColumn(Class anObjectClass, String anAttributeName, String[] someArgs) {
        this("", true, anObjectClass, anAttributeName);
    }

    /**
     * Construct a new column. Column is editable. A single click is
     * required to start editing. Column is sortable by default, but is not sorted
     * after construction. Default heading alignment is HEADER_CENTER. Heading text,
     * set by setHeaderValue, is an empty string. This method
     * fails fast by throwing an AWTError if anAttributeName does not exist.
     * This usually happens due to a programming error, and shouldn't normally
     * happen at runtime.
     *
     * @param anObjectClass the class of the object returned from the ObjectSource
     * @param anAttributeName a String representing the name of the object's attribute
     *
     * @throws AWTError (unchecked exception) if the field doesn't exist on
     *  anObjectClass.
     */
    public ReflectedTimeIntervalColumn(Class anObjectClass, String anAttributeName) {
        this("", true, anObjectClass, anAttributeName);
    }

    /**
     * Construct a new column. Column is optionally editable. This method
     * fails fast by throwing an AWTError if anAttributeName does not exist.
     * This usually happens due to a programming error, and shouldn't normally
     * happen at runtime.
     *
     * @param aColumnTitle the columns heading, if any. This may be null.
     * @param anEditingFlag if true, the column can be edited, otherwise it cannot.
     * @param anObjectClass the class of the object returned from the ObjectSource
     * @param anAttributeName a String representing the name of the object's attribute
     *
     * @throws AWTError (unchecked exception) if the field doesn't exist on
     *  anObjectClass.
     */
    public ReflectedTimeIntervalColumn(String aColumnTitle, boolean anEditingFlag, Class anObjectClass, String anAttributeName) {
        super(aColumnTitle, anEditingFlag);
        setFieldName(anAttributeName);
        try {
            mField = anObjectClass.getDeclaredField(anAttributeName);
            mField.setAccessible(true);
        } catch (Exception e) {
            throw new AWTError(e.toString());
        }
    }

    /**
     * Get the column's value from the specified object.
     *
     * @param anObject an Object returned from an ObjectSource
     *
     * @return The value, or null if the value was not found.
     */
    protected Long getColumnValue(Object anObject) {
        try {
            Object obj = mField.get(anObject);
            if (obj == null || obj instanceof Long) {
                return (Long) obj;
            } else if (obj instanceof Number) {
                return new Long(((Number) obj).longValue());
            }
            String string = obj.toString();
            Long result = parseInterval(string);
            if (result == null) {
                return new Long(0);
            }
            return result;
        } catch (Exception e) {
            throw new AWTError("Cannot convert field " + mField.getName() + " into a Long: " + e);
        }
    }

    /**
     * Set the column's value on the specified object.
     *
     * @param anObject an Object returned from an ObjectSource
     * @param aValue the value to be set.
     *
     * @return true if the value was set, or false if the value was not set because
     *  it is invalid.
     */
    protected boolean setColumnValue(Object anObject, Long aValue) {
        try {
            if (mField.getType() == String.class) {
                mField.set(anObject, formatInterval(aValue));
            } else {
                mField.setLong(anObject, aValue.longValue());
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
