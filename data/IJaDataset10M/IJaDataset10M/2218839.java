package org.gwtoolbox.widget.client.form.editor;

import com.google.gwt.user.client.ui.PasswordTextBox;

/**
 * @author Uri Boness
 */
public class PasswordEditor extends AbstractFocusWidgetEditor<String, PasswordTextBox> {

    public PasswordEditor() {
        this(new PasswordTextBox());
    }

    public PasswordEditor(PasswordTextBox textBox) {
        this(textBox, "", true);
    }

    public PasswordEditor(String defaultText) {
        this(defaultText, true);
    }

    public PasswordEditor(boolean enabled) {
        this("", enabled);
    }

    public PasswordEditor(String defaultText, boolean enabled) {
        this(new PasswordTextBox(), defaultText, enabled);
    }

    protected PasswordEditor(PasswordTextBox textBox, String defaultText, boolean enabled) {
        super(textBox, defaultText, enabled);
        getWidget().setStylePrimaryName("PasswordEditor");
    }

    public String doGetValue() {
        return getWidget().getText();
    }

    public void doSetValue(String text) {
        getWidget().setText(text);
    }

    protected String getNullValue() {
        return "";
    }
}
