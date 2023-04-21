package astcentric.editor.common.dialog;

/**
 * A form field representing a text input field in a dialog.
 */
public class TextField extends Field {

    private String _value;

    /**
   * Creates an instance for the specified id, optional label, and 
   * optional validator.
   * 
   * @param id ID of this field.
   * @param label Descriptive label of this field or <code>null</code> if 
   *        <code>id</code> should be used as label.
   * @param validator Validator which checks the input. 
   *        Can be <code>null</code>.
   */
    public TextField(String id, String label, Validator validator) {
        super(id, label, validator);
    }

    /**
   * Returns input value.
   */
    public Object getValue() {
        return _value;
    }

    /**
   * Sets the value of this field by mapping any object to a String by
   * using <code>String.valueOf</code>. 
   */
    @Override
    public void setValue(Object value) {
        String stringValue = String.valueOf(value);
        if (stringValue.equals(_value) == false) {
            _value = stringValue;
            fireFieldChanged();
        }
    }
}
