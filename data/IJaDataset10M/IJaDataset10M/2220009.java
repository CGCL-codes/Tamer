package org.zxframework.property;

import org.zxframework.ZXException;
import org.zxframework.zXType;
import org.zxframework.util.StringUtil;

/**
 * LongProperty - Handles the dataTypes Long and Automatics. 
 * 
 * @author Michael Brewer
 * @author Bertus Dispa
 * @author David Swann
 * 
 * @version 0.0.1
 */
public class LongProperty extends Property {

    /** The underlying variable for storing the value. **/
    private long value = 0;

    /**
	 * Default constructor
	 */
    public LongProperty() {
        super();
    }

    /**
	 * Instant value property for doubles values.
	 * 
	 * @param plngValue The Native type to set the value to
	 * @param pblnIsNull Is the value of the property null ?
	 */
    public LongProperty(Long plngValue, boolean pblnIsNull) {
        if (plngValue != null) {
            this.value = plngValue.longValue();
            isNull = pblnIsNull;
        } else {
            this.value = 0;
            isNull = true;
        }
    }

    /**
     * Instant value property for longs and ints values.
     * 
     * @param plngValue The Native type to set the value to
     * @param pblnIsNull Is the value of the property null ?
     */
    public LongProperty(int plngValue, boolean pblnIsNull) {
        this.value = plngValue;
        isNull = pblnIsNull;
    }

    /**
	 * @param plngValue The Native type to set the value to
	 */
    public LongProperty(long plngValue) {
        this.value = plngValue;
        this.isNull = false;
    }

    /**
	 * Instant value property for longs and ints values.
	 * 
	 * @param plngValue The Native type to set the value to
	 * @param pblnIsNull Is the value of the property null ?
	 */
    public LongProperty(long plngValue, boolean pblnIsNull) {
        this.value = plngValue;
        isNull = pblnIsNull;
    }

    /**
	 * Validate / set long value.
	 * 
	 * @param plngValue The value to set.
	 * @return Returns the return code for this method.
	 * @throws ZXException Thrown if the setValue method failed
	 */
    public zXType.rc setValue(Long plngValue) throws ZXException {
        return setValue(plngValue.longValue());
    }

    /**
	 * Validate / set long value.
	 * 
	 * @param plngValue The value to set.
	 * @return Returns the return code for this method
	 * @throws ZXException Thrown if the setValue method failed
	 */
    public zXType.rc setValue(long plngValue) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("plngValue", plngValue);
        }
        zXType.rc setValue = zXType.rc.rcOK;
        try {
            updateFKLabel(new Long(this.value), new Long(plngValue));
            if (!this.bo.isValidate()) {
                if (this.value != plngValue) {
                    setPersistStatusDirty();
                }
                this.value = plngValue;
                this.isNull = false;
            } else {
                if (this.attribute.getOptions() != null && this.attribute.getOptions().size() > 0 && !this.attribute.isCombobox()) {
                    if (!isValueInOptions(String.valueOf(plngValue))) {
                        getZx().trace.userErrorAdd("Value is not in the option list for " + this.attribute.getLabel().getLabel() + " for attribute " + this.attribute.getName());
                        setValue = zXType.rc.rcError;
                        return setValue;
                    }
                }
                if (checkRange(new Long(plngValue)).pos == zXType.rc.rcError.pos) {
                    setValue = zXType.rc.rcError;
                    return setValue;
                }
                if (this.value != plngValue) {
                    setPersistStatusDirty();
                }
                this.value = plngValue;
                this.isNull = false;
            }
            return setValue;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Set long value", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : plngValue = " + plngValue);
            }
            if (getZx().throwException) throw new ZXException(e);
            setValue = zXType.rc.rcError;
            return setValue;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.exitMethod();
            }
        }
    }

    /** 
	 * Sets the Long Property value from a String. 
	 * 
	 * NOTE : If the string is empty.
	 * 
	 * @see org.zxframework.property.Property#setValue(String)
	 **/
    public zXType.rc setValue(String pstrValue) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrValue", pstrValue);
        }
        zXType.rc setValue = zXType.rc.rcOK;
        try {
            if (!this.isNull && !StringUtil.isEmptyTrim(pstrValue)) {
                setValue = setValue(Long.parseLong(pstrValue.trim()));
            } else {
                this.value = 0;
                setValue = handleNull();
                if (setValue.pos == zXType.rc.rcError.pos) {
                    return setValue;
                }
            }
            return setValue;
        } catch (Exception e) {
            if (getZx().log.isErrorEnabled()) {
                getZx().trace.addError("Failed to : Set long value", e);
                getZx().log.error("Parameter : pstrValue = " + pstrValue);
            }
            if (getZx().throwException) throw new ZXException(e);
            return setValue;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.exitMethod();
            }
        }
    }

    /**
	 * @return Returns the value.
	 */
    public long getValue() {
        return this.value;
    }

    protected void setLongValue(long plngValue) {
        this.value = plngValue;
    }

    /***
	 * Return the current value as formatted string
	 * 
	 * @param pblnTrim Trim the outputted string.
	 * @see org.zxframework.property.Property#formattedValue(boolean)
	 * @throws ZXException If it failes
	 **/
    public String formattedValue(boolean pblnTrim) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pblnTrim", pblnTrim);
        }
        String formattedValue = null;
        try {
            if (this.isNull) {
                formattedValue = "";
                return formattedValue;
            } else if (this.attribute == null) {
                formattedValue = getStringValue();
            } else {
                if (StringUtil.len(this.attribute.getForeignKey()) > 0) {
                    formattedValue = fkformattedValue();
                    return formattedValue;
                }
                if (this.attribute.getOptions() != null && this.attribute.getOptions().size() > 0) {
                    formattedValue = optionValueToLabel();
                } else {
                    if (StringUtil.len(this.attribute.getOutputMask()) > 0) {
                        formattedValue = this.value + "";
                    } else {
                        formattedValue = this.value + "";
                    }
                }
                if (!pblnTrim) {
                    formattedValue = StringUtil.padString(formattedValue, ' ', this.attribute.getOutputLength(), false);
                }
            }
            return formattedValue;
        } catch (Exception e) {
            if (getZx().log.isErrorEnabled()) {
                getZx().trace.addError("Failed To : formattedValue - Return the current value as formatted string", e);
                getZx().log.error("Parameter : pblnTrim = " + pblnTrim);
            }
            if (getZx().throwException) throw new ZXException(e);
            return formattedValue;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(formattedValue);
                getZx().trace.exitMethod();
            }
        }
    }

    /** 
	 * If requested raw: simply convert to string but do not apply any formatting rules.
	 * 
     * @see org.zxframework.property.Property#getStringValue()
	 * @return Returns the raw format string format of this object.
     **/
    public String getStringValue() {
        if (isNull()) {
            return "";
        }
        return String.valueOf(this.value);
    }
}
