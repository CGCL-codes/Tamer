package org.jlense.uiworks.forms;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JTextField;
import org.jlense.uiworks.internal.WorkbenchPlugin;
import org.jlense.util.Assert;

/**
 * A field editor for a string type preference.
 * <p>
 * This class may be used as is, or subclassed as required.
 * </p>
 */
public class StringFieldEditor extends FieldEditor {

    /**
     * Validation strategy constant (value <code>0</code>) indicating that
     * the editor should perform validation after every key stroke.
     *
     * @see #setValidateStrategy
     */
    public static final int VALIDATE_ON_KEY_STROKE = 0;

    /**
     * Validation strategy constant (value <code>1</code>) indicating that
     * the editor should perform validation only when the text Component 
     * loses focus.
     *
     * @see #setValidateStrategy
     */
    public static final int VALIDATE_ON_FOCUS_LOST = 1;

    /**
     * Text limit constant (value <code>-1</code>) indicating unlimited
     * text limit and width.
     */
    public static int UNLIMITED = -1;

    /**
     * Cached valid state.
     */
    private boolean _isValid;

    /**
     * Old text value.
     */
    private String _oldValue;

    /**
     * The text field, or <code>null</code> if none.
     */
    private JTextField _textField = new JTextField();

    private JComponent[][] _components = null;

    /**
     * Width of text field in characters; initially unlimited.
     */
    private int _widthInChars = UNLIMITED;

    /**
     * Text limit of text field in characters; initially unlimited.
     */
    private int _textLimit = UNLIMITED;

    /**
     * The error message, or <code>null</code> if none.
     */
    private String _errorMessage;

    /**
     * Indicates whether the empty string is legal;
     * <code>true</code> by default.
     */
    private boolean _emptyStringAllowed = true;

    /**
     * The validation strategy; 
     * <code>VALIDATE_ON_KEY_STROKE</code> by default.
     */
    private int _validateStrategy = VALIDATE_ON_KEY_STROKE;

    private KeyAdapter _keyAdapter = new KeyAdapter() {

        public void keyPressed(KeyEvent e) {
            if (_validateStrategy == VALIDATE_ON_KEY_STROKE) {
                valueChanged();
            } else clearErrorMessage();
        }
    };

    private FocusAdapter _focusAdapter = new FocusAdapter() {

        public void focusGained(FocusEvent e) {
            refreshValidState();
        }

        public void focusLost(FocusEvent e) {
            if (_validateStrategy == VALIDATE_ON_FOCUS_LOST) valueChanged();
            clearErrorMessage();
        }
    };

    /**
     * Creates a new string field editor 
     */
    protected StringFieldEditor() {
    }

    /**
     * Creates a string field editor.
     * Use the method <code>setTextLimit</code> to limit the text.
     * 
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param width the width of the text input field in characters,
     *  or <code>UNLIMITED</code> for no limit
     * @param parent the parent of the field editor's Component
     */
    public StringFieldEditor(String name, String labelText, int width) {
        _widthInChars = width;
        _isValid = false;
        _errorMessage = WorkbenchPlugin.getDefault().bind("StringFieldEditor.errorMessage");
        init(name, labelText);
    }

    protected void init(String name, String labelText) {
        super.init(name, labelText);
        _textField.addKeyListener(_keyAdapter);
        _textField.addFocusListener(_focusAdapter);
    }

    /**
     * Creates a string field editor of unlimited width.
     * Use the method <code>setTextLimit</code> to limit the text.
     * 
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param parent the parent of the field editor's Component
     */
    public StringFieldEditor(String name, String labelText) {
        this(name, labelText, UNLIMITED);
    }

    /**
     * Checks whether the text input field contains a valid value or not.
     *
     * @return <code>true</code> if the field value is valid,
     *   and <code>false</code> if invalid
     */
    protected boolean checkState() {
        boolean result = false;
        if (_emptyStringAllowed) result = true;
        if (_textField == null) {
            result = false;
        } else {
            String txt = _textField.getText();
            if (txt == null) result = false;
            result = (txt.trim().length() > 0) || _emptyStringAllowed;
            result = result && doCheckState();
            if (result) clearErrorMessage(); else showErrorMessage(_errorMessage);
        }
        return result;
    }

    /**
     * Hook for subclasses to do specific state checks.
     * <p>
     * The default implementation of this framework method does
     * nothing and returns <code>true</code>.  Subclasses should 
     * override this method to specific state checks.
     * </p>
     *
     * @return <code>true</code> if the field value is valid,
     *   and <code>false</code> if invalid
     */
    protected boolean doCheckState() {
        return true;
    }

    protected void doLoad() {
        String value = getDialogSettings().getString(getDialogSettingName());
        _textField.setText(value);
        _oldValue = value;
    }

    protected void doLoadDefault() {
    }

    protected void doStore() {
        getDialogSettings().put(getDialogSettingName(), _textField.getText());
    }

    /**
     * Returns the error message that will be displayed when and if 
     * an error occurs.
     *
     * @return the error message, or <code>null</code> if none
     */
    public String getErrorMessage() {
        return _errorMessage;
    }

    /**
     * Returns the field editor's value.
     *
     * @return the current value
     */
    public String getStringValue() {
        if (_textField != null) return _textField.getText(); else return getDialogSettings().getString(getDialogSettingName());
    }

    public JComponent[][] getComponents() {
        if (_components == null) {
            _components = new JComponent[1][];
            _components[0] = new JComponent[] { getLabelComponent(), _textField };
        }
        return _components;
    }

    /**
     * Returns whether an empty string is a valid value.
     *
     * @return <code>true</code> if an empty string is a valid value, and
     *  <code>false</code> if an empty string is invalid
     * @see #setEmptyStringAllowed
     */
    public boolean isEmptyStringAllowed() {
        return _emptyStringAllowed;
    }

    public boolean isValid() {
        return _isValid;
    }

    protected void refreshValidState() {
        _isValid = checkState();
    }

    /**
     * Sets whether the empty string is a valid value or not.
     *
     * @param b <code>true</code> if the empty string is allowed,
     *  and <code>false</code> if it is considered invalid
     */
    public void setEmptyStringAllowed(boolean b) {
        _emptyStringAllowed = b;
    }

    /**
     * Sets the error message that will be displayed when and if 
     * an error occurs.
     *
     * @param message the error message
     */
    public void setErrorMessage(String message) {
        _errorMessage = message;
    }

    public void setFocus() {
        if (_textField != null) {
            _textField.requestFocus();
        }
    }

    /**
     * Sets this field editor's value.
     *
     * @param value the new value, or <code>null</code> meaning the empty string
     */
    public void setStringValue(String value) {
        if (_textField != null) {
            if (value == null) value = "";
            _oldValue = _textField.getText();
            if (!_oldValue.equals(value)) {
                _textField.setText(value);
                valueChanged();
            }
        }
    }

    /**
     * Sets the strategy for validating the text.
     *
     * @param value either <code>VALIDATE_ON_KEY_STROKE</code> to perform
     *  on the fly checking (the default), or <code>VALIDATE_ON_FOCUS_LOST</code> to
     *  perform validation only after the text has been typed in
     */
    public void setValidateStrategy(int value) {
        Assert.isTrue(value == VALIDATE_ON_FOCUS_LOST || value == VALIDATE_ON_KEY_STROKE);
        _validateStrategy = value;
    }

    /**
     * Shows the error message set via <code>setErrorMessage</code>.
     */
    public void showErrorMessage() {
        showErrorMessage(_errorMessage);
    }

    /**
     * Informs this field editor's listener, if it has one, about a change
     * to the value (<code>VALUE</code> property) provided that the old and
     * new values are different.
     * <p>
     * This hook is <em>not</em> called when the text is initialized 
     * (or reset to the default value) from the preference store.
     * </p>
     */
    protected void valueChanged() {
        setPresentsDefaultValue(false);
        boolean oldState = _isValid;
        refreshValidState();
        if (_isValid != oldState) fireStateChanged(IS_VALID, oldState, _isValid);
        String newValue = _textField.getText();
        if (!newValue.equals(_oldValue)) {
            fireValueChanged(VALUE, _oldValue, newValue);
            _oldValue = newValue;
        }
    }
}
