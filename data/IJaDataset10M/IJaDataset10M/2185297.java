package org.springframework.binding.value.swing;

import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.AbstractValueModelAdapter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class AsYouTypeTextComponentAdapter extends AbstractValueModelAdapter implements DocumentListener {

    private final JTextComponent control;

    private boolean convertEmptyStringToNull;

    private boolean settingText;

    public AsYouTypeTextComponentAdapter(JTextComponent control, ValueModel valueModel) {
        this(control, valueModel, false);
    }

    public AsYouTypeTextComponentAdapter(JTextComponent control, ValueModel valueModel, boolean convertEmptyStringToNull) {
        super(valueModel);
        Assert.notNull(control);
        this.control = control;
        this.control.getDocument().addDocumentListener(this);
        this.convertEmptyStringToNull = convertEmptyStringToNull;
        initalizeAdaptedValue();
    }

    public void removeUpdate(DocumentEvent e) {
        controlTextValueChanged();
    }

    public void insertUpdate(DocumentEvent e) {
        controlTextValueChanged();
    }

    public void changedUpdate(DocumentEvent e) {
        controlTextValueChanged();
    }

    private void controlTextValueChanged() {
        if (!settingText) {
            if (!StringUtils.hasText(control.getText()) && convertEmptyStringToNull) adaptedValueChanged(null); else adaptedValueChanged(control.getText());
        }
    }

    protected void valueModelValueChanged(Object value) {
        try {
            settingText = true;
            control.setText((String) value);
        } finally {
            settingText = false;
        }
    }
}
