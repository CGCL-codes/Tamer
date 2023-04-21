package org.geoforge.guitlc.dialog.edit.textfield;

import javax.swing.event.DocumentListener;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class TfdCheckableSmallFloat extends TfdCheckableSmallAbs {

    public TfdCheckableSmallFloat(DocumentListener lstDocument) {
        super("float value", lstDocument);
    }

    @Override
    public String getWrongFormat() {
        String str = super.getText();
        if (str == null) return null;
        str = str.trim();
        if (str.length() < 1) return null;
        try {
            Float.parseFloat(str);
        } catch (Exception exc) {
            return "not a valid float value: " + str;
        }
        return null;
    }
}
