package aga.jitracker.client.ui.editors;

import java.io.Serializable;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;

public class IntegerEditor extends StringEditor {

    public IntegerEditor() {
        textbox.addFocusHandler(new FocusHandler() {

            public void onFocus(FocusEvent event) {
                validate();
            }
        });
        textbox.addBlurHandler(new BlurHandler() {

            public void onBlur(BlurEvent event) {
                validate();
            }
        });
    }

    public Integer getInteger() {
        String str = getString();
        if (str.length() == 0) return null;
        return Integer.parseInt(str);
    }

    @Override
    public Serializable getValue() {
        return getInteger();
    }

    @Override
    protected String getValidationError() {
        try {
            getInteger();
        } catch (NumberFormatException ex) {
            return "Invalid number format.";
        }
        return null;
    }
}
