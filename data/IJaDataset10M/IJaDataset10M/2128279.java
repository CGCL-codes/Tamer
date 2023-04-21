package org.zkoss.mil.impl;

import org.zkoss.lang.Objects;
import org.zkoss.mil.Item;
import org.zkoss.xml.HTMLs;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.ext.client.Inputable;

/**
 * TextField related component.
 * @author henrichen
 */
public abstract class InputElement extends Item {

    protected static final int ANY = 0;

    protected static final int EMAILADDR = 1;

    protected static final int NUMERIC = 2;

    protected static final int PHONENUMBER = 3;

    protected static final int URL = 4;

    protected static final int DECIMAL = 5;

    protected static final int PASSWORD = 0x10000;

    protected static final int UNEDITABLE = 0x20000;

    protected static final int SENSITIVE = 0x40000;

    protected static final int NON_PREDICTIVE = 0x80000;

    protected static final int INITIAL_CAPS_WORD = 0x100000;

    protected static final int INITIAL_CAPS_SENTENCE = 0x200000;

    protected static final int CONSTRAINT_MASK = 0xffff;

    /** The value. */
    private Object _value;

    /** Used by setTextByClient() to disable sending back the value */
    private String _txtByClient;

    private int _maxlength = 32;

    private boolean _readonly;

    /** Returns whether it is readonly.
	 * <p>Default: false.
	 */
    public boolean isReadonly() {
        return _readonly;
    }

    /** Sets whether it is readonly.
	 */
    public void setReadonly(boolean readonly) {
        if (_readonly != readonly) {
            _readonly = readonly;
            smartUpdateConstraints();
        }
    }

    /** Returns the value in the String format.
	 * In most case, you shall use the setValue method instead, e.g.,
	 * {@link org.zkoss.mil.Textbox#getValue}.
	 *
	 * <p>It invokes {@link #coerceToString} to convert the stored value
	 * into a string.
	 *
	 * @exception WrongValueException if user entered a wrong value
	 */
    public String getText() throws WrongValueException {
        return coerceToString(_value);
    }

    /** Sets the value in the String format.
	 * In most case, you shall use the setValue method instead, e.g.,
	 * {@link org.zkoss.mil.Textbox#setValue}.
	 *
	 * <p>It invokes {@link #coerceFromString}.
	 * Derives might override them for type conversion and special
	 * validation.
	 *
	 * @param value the value; If null, it is considered as empty.
	 */
    public void setText(String value) throws WrongValueException {
        final Object val = coerceFromString(value);
        if (!Objects.equals(_value, val)) {
            _value = val;
            final String fmtval = coerceToString(_value);
            if (_txtByClient == null || !Objects.equals(_txtByClient, fmtval)) {
                _txtByClient = null;
                smartUpdate("tx", fmtval);
            }
        } else if (_txtByClient != null) {
            final String fmtval = coerceToString(_value);
            if (!Objects.equals(_txtByClient, fmtval)) {
                _txtByClient = null;
                smartUpdate("tx", fmtval);
            }
        }
    }

    /** Internal type of this InputElement (ANY, EMAILADDR, NUMERIC, PHONENUMBER, 
	 * URL, or DECIMAL). */
    protected abstract int getInternalType();

    /** Coerces the value passed to {@link #setText}.
	 *
	 * <p>Deriving note:<br>
	 * If you want to store the value in other type, say BigDecimal,
	 * you have to override {@link #coerceToString} and {@link #coerceFromString}
	 * to convert between a string and your targeting type.
	 *
	 * <p>Moreover, when {@link org.zkoss.mil.Textbox} is called, it calls this method
	 * with value = null. Derives shall handle this case properly.
	 */
    protected abstract Object coerceFromString(String value) throws WrongValueException;

    /** Coerces the value passed to {@link #setText}.
	 *
	 * <p>Default: convert null to an empty string.
	 *
	 * <p>Deriving note:<br>
	 * If you want to store the value in other type, say BigDecimal,
	 * you have to override {@link #coerceToString} and {@link #coerceFromString}
	 * to convert between a string and your targeting type.
	 */
    protected abstract String coerceToString(Object value);

    /** Returns the maxlength.
	 * <p>Default: 32.
	 */
    public int getMaxlength() {
        return _maxlength;
    }

    /** Sets the maxlength.
	 */
    public void setMaxlength(int maxlength) {
        if (_maxlength != maxlength) {
            _maxlength = maxlength;
            smartUpdate("xs", maxlength);
        }
    }

    /** Returns the type.
	 * <p>Default: text.
	 */
    public String getType() {
        return "text";
    }

    public String getInnerAttrs() {
        final StringBuffer sb = new StringBuffer(64).append(super.getInnerAttrs());
        HTMLs.appendAttribute(sb, "tx", coerceToString(_value));
        HTMLs.appendAttribute(sb, "xs", _maxlength);
        HTMLs.appendAttribute(sb, "cs", getConstraints());
        return sb.toString();
    }

    protected int getConstraints() {
        int constraints = getInternalType();
        if ("password".equals(getType())) {
            constraints |= PASSWORD;
        }
        if (isReadonly()) {
            constraints |= UNEDITABLE;
        }
        return constraints;
    }

    protected void smartUpdateConstraints() {
        smartUpdate("cs", getConstraints());
    }

    public String getOuterAttrs() {
        final StringBuffer sb = new StringBuffer(64).append(super.getOuterAttrs());
        appendAsapAttr(sb, "onChange");
        appendAsapAttr(sb, "onChanging");
        return sb.toString();
    }

    /** Returns the value in the targeting type.
	 *
	 * @exception WrongValueException if the user entered a wrong value
	 * @see #getText
	 */
    protected Object getTargetValue() throws WrongValueException {
        return _value;
    }

    /** Returns the raw value directly with checking whether any
	 * error message not yet fixed. 
	 *
	 * @see #getRawText
	 * @see #getText
	 * @see #setRawValue
	 */
    public Object getRawValue() {
        return _value;
    }

    /** Returns the text directly without checking whether any error
	 * message not yet fixed.
	 *
	 * @see #getRawValue
	 * @see #getText
	 */
    public String getRawText() {
        return coerceToString(_value);
    }

    /** Sets the raw value directly. The caller must make sure the value
	 * is correct (or intend to be incorrect), because this method
	 * doesn't do any validation.
	 *
	 * <p>If you feel confusing with setValue, such as {@link org.zkoss.mil.Textbox#setValue},
	 * it is usually better to use setValue instead. This method
	 * is reserved for developer that really want to set an 'illegal'
	 * value (such as an empty string to a textbox with no-empty contraint).
	 *
	 * <p>Like setValue, the result is returned back to the server
	 * by calling {@link #getText}.
	 *
	 * @see #getRawValue
	 */
    public void setRawValue(Object value) {
        if (!Objects.equals(_value, value)) {
            _value = value;
            smartUpdate("value", coerceToString(_value));
        }
    }

    protected Object newExtraCtrl() {
        return new ExtraCtrl();
    }

    /** A utility class to implement {@link #getExtraCtrl}.
	 * It is used only by component developers.
	 */
    protected class ExtraCtrl implements Inputable {

        public void setTextByClient(String value) throws WrongValueException {
            _txtByClient = value;
            try {
                setText(value);
            } catch (WrongValueException ex) {
                throw ex;
            } finally {
                _txtByClient = null;
            }
        }
    }
}
