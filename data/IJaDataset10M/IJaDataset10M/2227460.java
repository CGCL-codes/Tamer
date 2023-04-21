package ops.view.forms.component;

import javax.swing.JComponent;
import ops.view.component.StringField;

public class StringInput extends FormInput {

    private StringField field;

    public StringInput(String label) {
        super(label, true);
        field = new StringField();
    }

    public StringInput(String label, int size) {
        this(label, size, true);
    }

    public StringInput(String label, int size, boolean required) {
        super(label, required);
        field = new StringField(size);
    }

    public StringInput(String label, int size, boolean required, boolean readOnly) {
        this(label, size, required);
        field.setEditable(readOnly);
    }

    @Override
    public Object getInputValue() {
        return field.getText();
    }

    @Override
    public JComponent getInputComponent() {
        return field;
    }

    @Override
    public void setInputValue(Object value) {
        field.setText(value.toString());
    }

    @Override
    public void clearInput() {
        field.setText(null);
    }
}
