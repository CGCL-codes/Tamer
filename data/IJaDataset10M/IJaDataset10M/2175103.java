package org.furthurnet.xmlparser;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class DateParamInputGui extends ParamInputGui implements FocusListener {

    private DatePanel inputDate;

    public DateParamInputGui(DateParam dp, AutoPopulator a) {
        super(dp, a);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(2, 0, 1, 0));
        inputDate = new DatePanel(this);
        add(new JLabel(basis.getName() + " :   "), BorderLayout.WEST);
        add(inputDate, BorderLayout.CENTER);
    }

    public String getInputSelection() throws EncodingException {
        try {
            return inputDate.getDateString();
        } catch (EncodingException e) {
            inputDate.requestFocus();
            throw e;
        } catch (Exception e) {
            return "";
        }
    }

    public String validateInput() {
        try {
            if (basis.getRequired() == true) if (getInputSelection().length() == 0) {
                inputDate.requestFocus();
                return basis.getName() + " is missing or invalid.  Please enter a valid date value.  Note that a year must be 4 digits long.";
            }
        } catch (Exception e) {
            inputDate.requestFocus();
        }
        return null;
    }

    public void clearInputSelection() {
        inputDate.clearDate();
    }

    public void setInputSelection(String value) {
        inputDate.setDate(value);
    }

    public void focusLost(FocusEvent e) {
        if (autoPopulator != null) autoPopulator.attributeChanged(basis.getName());
    }

    public void focusGained(FocusEvent e) {
    }

    public void setReadOnly(boolean readOnly) {
        inputDate.setReadOnly(readOnly);
    }
}
