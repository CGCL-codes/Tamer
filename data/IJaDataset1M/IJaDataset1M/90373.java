package pt.ips.estsetubal.mig.academicCloud.client.components.fields;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * This component is the parent of all the text field components.
 * 
 * @author António Casqueiro
 */
public abstract class AbstractTextField extends AbstractField {

    private TextBox widget;

    /**
	 * Block direct access to the widget.
	 */
    @Override
    protected Widget getWidget() {
        return null;
    }

    /**
	 * Constructor.
	 */
    public AbstractTextField(String label) {
        super(new TextBox(), label);
        this.widget = (TextBox) super.getWidget();
    }

    protected int getRealSize() {
        return getWidgetSize().getSize();
    }

    public String getText() {
        return widget.getText();
    }

    public void setText(String text) {
        widget.setText(text);
    }

    public int getMaxLength() {
        return widget.getMaxLength();
    }

    public void setMaxLength(int maxLength) {
        widget.setMaxLength(maxLength);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        ((TextBox) widget).setReadOnly(readOnly);
    }

    @Override
    public boolean getReadOnly() {
        return ((TextBox) widget).isReadOnly();
    }

    @Override
    protected String checkFieldData() {
        String value = getValueAsString();
        if (getRequired() && value == null) {
            return messages.missingRequiredField(getLabel());
        }
        return null;
    }

    protected String getValueAsString() {
        String value = getText();
        if (value == null || value.trim().length() == 0) {
            value = null;
        } else {
            value = value.trim();
        }
        return value;
    }
}
