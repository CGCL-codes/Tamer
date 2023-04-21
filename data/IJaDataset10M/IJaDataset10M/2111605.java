package org.xlim.sic.ig.tools;

import java.text.ParseException;
import javax.swing.JFormattedTextField;

/**
 * Converts a Double to String and inversely for the UI.
 * @author aveneau
 */
public class MyFormatterDoubleFactory extends JFormattedTextField.AbstractFormatterFactory {

    private class MyFormatter extends JFormattedTextField.AbstractFormatter {

        public static final long serialVersionUID = 0x9834128L;

        public MyFormatter() {
            super();
        }

        public Object stringToValue(String text) throws ParseException {
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException e) {
                throw new ParseException(e.getMessage(), 0);
            }
        }

        public String valueToString(Object value) throws ParseException {
            try {
                return value.toString();
            } catch (Exception e) {
                throw new ParseException(e.getMessage(), 0);
            }
        }
    }

    public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
        return new MyFormatter();
    }
}
