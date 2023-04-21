package com.potix.zul.html.impl;

import com.potix.lang.Objects;
import com.potix.xml.HTMLs;
import com.potix.xml.XMLs;
import com.potix.zk.ui.UiException;
import com.potix.zk.ui.WrongValueException;
import com.potix.zk.ui.ext.Inputable;
import com.potix.zk.ui.ext.Errorable;
import com.potix.zk.au.AuSelectAll;
import com.potix.zul.html.Constraint;
import com.potix.zul.html.SimpleConstraint;
import com.potix.zul.html.Constrainted;

/**
 * A skeletal implementation of an input box.
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public abstract class InputElement extends XulElement implements Inputable, Errorable, Constrainted {

    /** The value. */
    private Object _value;

    /** Used by {@link #setTextByClient} to disable sending back the value */
    private String _txtByClient;

    /** The error message. Not null if users entered a wrong data (and
	 * not correct it yet).
	 */
    private String _errmsg;

    /** The name. */
    private String _name;

    private int _maxlength, _cols;

    private Constraint _constr;

    private boolean _disabled, _readonly;

    /** Whether this input is validated (Feature 1461209). */
    private boolean _valided;

    /** Returns whether it is disabled.
	 * <p>Default: false.
	 */
    public boolean isDisabled() {
        return _disabled;
    }

    /** Sets whether it is disabled.
	 */
    public void setDisabled(boolean disabled) {
        if (_disabled != disabled) {
            _disabled = disabled;
            smartUpdate("disabled", _disabled);
        }
    }

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
            smartUpdate("readOnly", _readonly);
        }
    }

    /** Returns the name of this component.
	 * <p>Default: null.
	 * <p>Don't use this method if your application is purely based
	 * on ZK's event-driven model.
	 * <p>The name is used only to work with "legacy" Web application that
	 * handles user's request by servlets.
	 * It works only with HTTP/HTML-based browsers. It doesn't work
	 * with other kind of clients.
	 */
    public String getName() {
        return _name;
    }

    /** Sets the name of this component.
	 * <p>Don't use this method if your application is purely based
	 * on ZK's event-driven model.
	 * <p>The name is used only to work with "legacy" Web application that
	 * handles user's request by servlets.
	 * It works only with HTTP/HTML-based browsers. It doesn't work
	 * with other kind of clients.
	 *
	 * @param name the name of this component.
	 */
    public void setName(String name) {
        if (name != null && name.length() == 0) name = null;
        if (!Objects.equals(_name, name)) {
            _name = name;
            smartUpdate("name", _name);
        }
    }

    /** Returns the error message that is caused when user entered
	 * invalid value, or null if no error at all.
	 *
	 * <p>The error message is set when user has entered a wrong value,
	 * or setValue is called with a wrong value.
	 * It is cleared once a correct value is assigned.
	 *
	 * <p>If the error message is set, we say this input is in the error mode.
	 * Any following invocation to {@link #getText} or getValue will throw
	 * any exception.
	 */
    public String getErrorMessage() {
        return _errmsg;
    }

    /** Returns the value in String.
	 *
	 * <p>It invokes {@link #checkUserError} to ensure no user error.
	 *
	 * <p>It invokes {@link #coerceToString} to convert the stored value
	 * into a string.
	 *
	 * <p>It cannot be overriden. Rather, override {@link #checkUserError}
	 * or {@link #coerceToString}.
	 *
	 * @exception WrongValueException if user entered a wrong value
	 */
    protected final String getText() throws WrongValueException {
        checkUserError();
        return coerceToString(_value);
    }

    /** Sets the value.
	 *
	 * <p>It invokes {@link #coerceFromString} fisrt and then {@link #validate}.
	 * Derives might override them for type conversion and special
	 * validation.
	 *
	 * @param value the value; If null, it is considered as empty.
	 */
    protected void setText(String value) throws WrongValueException {
        Object val;
        try {
            val = coerceFromString(value);
            validate(val);
        } catch (WrongValueException ex) {
            smartUpdate("defaultValue", "zk_wrong!~-.zk_pha!6");
            throw ex;
        }
        _errmsg = null;
        _valided = true;
        if (!Objects.equals(_value, val)) {
            _value = val;
            final String fmtval = coerceToString(_value);
            if (_txtByClient == null || !Objects.equals(_txtByClient, fmtval)) smartUpdate("value", fmtval);
        } else if (_txtByClient != null) {
            final String fmtval = coerceToString(_value);
            if (!Objects.equals(_txtByClient, fmtval)) smartUpdate("value", fmtval);
        }
    }

    /** Coerces the value passed to {@link #setText}.
	 *
	 * <p>Deriving note:<br>
	 * If you want to store the value in other type, say BigDecimal,
	 * you have to override {@link #coerceToString} and {@link #coerceFromString}
	 * to convert between a string and your targeting type.
	 *
	 * <p>Moreover, when {@link com.potix.zul.html.Textbox} is called, it calls this method
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

    /** Validates the value returned by {@link #coerceFromString}.
	 * <p>Default: use  {@link #getConstraint}'s {@link Constraint#validate},
	 * if not null.
	 * <p>You rarely need to override this method.
	 */
    protected void validate(Object value) throws WrongValueException {
        final Constraint constr = getConstraint();
        if (constr != null) constr.validate(this, value);
    }

    /** Returns the maxlength.
	 * <p>Default: 0 (non-postive means unlimited).
	 */
    public int getMaxlength() {
        return _maxlength;
    }

    /** Sets the maxlength.
	 */
    public void setMaxlength(int maxlength) {
        if (_maxlength != maxlength) {
            _maxlength = maxlength;
            invalidate(OUTER);
        }
    }

    /** Returns the cols.
	 * <p>Default: 0 (non-positive means the same as browser's default).
	 */
    public int getCols() {
        return _cols;
    }

    /** Sets the cols.
	 */
    public void setCols(int cols) throws WrongValueException {
        if (cols <= 0) throw new WrongValueException("Illegal cols: " + cols);
        if (_cols != cols) {
            _cols = cols;
            smartUpdate("cols", Integer.toString(_cols));
        }
    }

    /** Returns whether it is multiline.
	 * <p>Default: false.
	 */
    public boolean isMultiline() {
        return false;
    }

    /** Returns the type.
	 * <p>Default: text.
	 */
    public String getType() {
        return "text";
    }

    /** Selects the whole text in this input.
	 */
    public void select() {
        response("select", new AuSelectAll(this));
    }

    public void setConstraint(String constr) {
        _constr = SimpleConstraint.getInstance(constr);
    }

    public void setConstraint(Constraint constr) {
        _constr = constr;
    }

    public final Constraint getConstraint() {
        return _constr;
    }

    /** Returns whether to send back the request of the specified event
	 * immediately (ASAP). Returns true if you want the component (on the server)
	 * to process the event immediately.
	 *
	 * <p>Default: Besides super.isAsapRequired(evtnm), it also returns true
	 * if evtnm is "onChange", {@link #getConstraint} is not null,
	 * and {@link Constraint#getValidationScript} is null.
	 */
    protected boolean isAsapRequired(String evtnm) {
        return ("onChange".equals(evtnm) && _constr != null && !_constr.isClientComplete()) || super.isAsapRequired(evtnm);
    }

    public String getInnerAttrs() {
        final StringBuffer sb = new StringBuffer(64).append(super.getInnerAttrs());
        if (isMultiline()) {
            if (_cols > 0) HTMLs.appendAttribute(sb, "cols", _cols);
        } else {
            HTMLs.appendAttribute(sb, "value", coerceToString(_value));
            if (_cols > 0) HTMLs.appendAttribute(sb, "size", _cols);
            if (_maxlength > 0) HTMLs.appendAttribute(sb, "maxlength", _maxlength);
            HTMLs.appendAttribute(sb, "type", "password".equals(getType()) ? "password" : "text");
        }
        HTMLs.appendAttribute(sb, "name", _name);
        if (isDisabled()) HTMLs.appendAttribute(sb, "disabled", "disabled");
        if (isReadonly()) HTMLs.appendAttribute(sb, "readonly", "readonly");
        return sb.toString();
    }

    public String getOuterAttrs() {
        final StringBuffer sb = new StringBuffer(64).append(super.getOuterAttrs());
        if (isAsapRequired("onChange")) HTMLs.appendAttribute(sb, "zk_onChange", true);
        if (isAsapRequired("onChanging")) HTMLs.appendAttribute(sb, "zk_onChanging", true);
        if (isAsapRequired("onFocus")) HTMLs.appendAttribute(sb, "zk_onFocus", true);
        if (isAsapRequired("onBlur")) HTMLs.appendAttribute(sb, "zk_onBlur", true);
        if (_constr != null) {
            HTMLs.appendAttribute(sb, "zk_valid", _constr.getValidationScript());
            HTMLs.appendAttribute(sb, "zk_ermg", _constr.getErrorMessage());
        }
        return sb.toString();
    }

    /** Returns the raw value, which is converted from {@link #getText}.
	 * <p>It invokes {@link #checkUserError} to ensure no user error.
	 * @exception WrongValueException if user entered a wrong value
	 */
    protected Object getRawValue() throws WrongValueException {
        checkUserError();
        return _value;
    }

    /** Sets the row value directly. The caller must make sure the value
	 * is correct (or intend to be incorrect), because this method
	 * doesn't do any validation.
	 *
	 * <p>If you feel confusing with setValue, such as {@link com.potix.zul.html.Textbox#setValue},
	 * it is usually better to use setValue instead. This method
	 * is reserved for developer that really want to set an 'illegal'
	 * value (such as an empty string to a textbox with no-empty contraint).
	 *
	 * <p>Like setValue, the result is returned back to the server
	 * by calling {@link #getText}
	 */
    public void setRawValue(Object value) {
        _errmsg = null;
        if (!Objects.equals(_value, value)) {
            _value = value;
            smartUpdate("value", coerceToString(_value));
        }
    }

    /** Checks whether user entered a wrong value (and not correct it yet).
	 * Since user might enter a wrong value and moves on to other components,
	 * this methid is called when {@link #getText} or {@link #getRawValue} is
	 * called.
	 *
	 * <p>Derives rarely need to access this method if they use only
	 * {@link #getText} and {@link #getRawValue}.
	 */
    protected void checkUserError() throws WrongValueException {
        if (_errmsg != null) throw new WrongValueException(this, _errmsg);
        if (!_valided && _constr != null) setText(coerceToString(_value));
    }

    /** Returns the text for HTML AREA (Internal Use Only).
	 *
	 * <p>Used only for component generation. Not for applications.
	 */
    public final String getAreaText() {
        return XMLs.encodeText(coerceToString(_value));
    }

    public void setTextByClient(String value) throws WrongValueException {
        _txtByClient = value;
        try {
            setText(value);
        } catch (WrongValueException ex) {
            _errmsg = ex.getMessage();
            throw ex;
        } finally {
            _txtByClient = null;
        }
    }

    public void setErrorByClient(String value, String msg) {
        _errmsg = msg != null && msg.length() > 0 ? msg : null;
    }

    /** Not childable. */
    public boolean isChildable() {
        return false;
    }
}
