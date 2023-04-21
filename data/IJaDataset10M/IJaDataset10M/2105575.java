package org.vikamine.gui.attribute.workspace;

import java.text.ParseException;
import java.util.Arrays;
import java.util.StringTokenizer;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * A Formatter for intervals of double-values. This class deals with Object of
 * type double[].
 * <p>
 * If you want to store the interval in a different type, you can subclass this
 * class and overwrite stringToValue() and valueToString(). So you still have
 * the DocumentFilter, which is responible for the validity of the input.
 * <p>
 * If you create a new subclass of IntervalFormatter, you should add it in the
 * Method IntervalFormatterFactory#getFormatter().
 * 
 * @author Tobias Vogele
 */
public class IntervalFormatter extends AbstractFormatter {

    private static final long serialVersionUID = 1L;

    /**
     * The DocumentFilter, which checks the validity of the input.
     */
    public class IntervalDocumentFilter extends DocumentFilter {

        /**
	 * Is called, when the user inserts some text. Only valid characters are
	 * allowed.
	 */
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            String valid = getValidString(fb, string, offset, 0);
            if (valid == null || valid.length() == 0) {
                invalidEdit();
                return;
            }
            checkValidity(fb, offset, 0, valid);
            super.insertString(fb, offset, valid, attr);
        }

        /**
	 * Checks the validiy of the input. After the check setEditValid() whith
	 * the right flag is called.
	 * 
	 * @param fb
	 * @param offset
	 * @param i
	 * @param valid
	 */
        private void checkValidity(FilterBypass fb, int offset, int length, String newString) {
            String text;
            try {
                String begin = fb.getDocument().getText(0, offset);
                int afterPos = offset + length;
                String end = fb.getDocument().getText(afterPos, fb.getDocument().getLength() - afterPos);
                text = begin + newString + end;
            } catch (BadLocationException e) {
                e.printStackTrace();
                return;
            }
            StringTokenizer tok = new StringTokenizer(text);
            boolean valid = true;
            while (tok.hasMoreTokens()) {
                try {
                    Double.parseDouble(tok.nextToken());
                } catch (NumberFormatException e) {
                    valid = false;
                }
            }
            setEditValid(valid);
        }

        /**
	 * Trys to correct the String and returns the result.
	 * 
	 * @param fb
	 * @param newString
	 * @param offset
	 * @param length
	 * @return the corrected String, if possible. null, if the string is
	 *         invalid (contains invalid characters)
	 */
        private String getValidString(FilterBypass fb, String newString, int offset, int length) {
            if (newString == null) {
                newString = "";
            }
            boolean beforeSpace = isBeforeSpace(fb, offset);
            boolean afterSpace = isAfterSpace(fb, offset, length);
            return correctInsertionString(newString, beforeSpace, afterSpace);
        }

        /**
	 * Trys to correct the String and returns the result.
	 * 
	 * @param newString
	 * @param beforeSpace
	 *                true, if there is a space before the string
	 * @param afterSpace
	 *                true, if there is a space after the string
	 * @return the corrected String, if possible. null, if the string is
	 *         invalid (contains invalid characters)
	 */
        private String correctInsertionString(String newString, boolean beforeSpace, boolean afterSpace) {
            StringBuffer buffy = new StringBuffer(newString.length());
            boolean startSpace = true;
            int lastCopyPos = -1;
            for (int i = 0; i < newString.length(); i++) {
                char c = newString.charAt(i);
                if (!Character.isDigit(c) && c != ' ' && c != '.' && c != '-') {
                    return null;
                }
                if (c != ' ') {
                    buffy.append(newString.substring(lastCopyPos + 1, i + 1));
                    lastCopyPos = i;
                    startSpace = false;
                } else if (beforeSpace && startSpace) {
                    lastCopyPos = i;
                }
            }
            if (!afterSpace && newString.endsWith(" ") && lastCopyPos < newString.length() - 1) {
                buffy.append(" ");
            }
            return buffy.toString();
        }

        /**
	 * looks, whether there is a space after the position, or the postion is
	 * at the end.
	 * 
	 * @param fb
	 * @param offset
	 * @param length
	 * @return
	 */
        private boolean isAfterSpace(FilterBypass fb, int offset, int length) {
            try {
                int posAfter = offset + length;
                return (posAfter < fb.getDocument().getLength()) && " ".equals(fb.getDocument().getText(posAfter, 1));
            } catch (BadLocationException e) {
                System.err.println("WARNING: This is a programm-error an should not occur!");
                e.printStackTrace();
                return false;
            }
        }

        /**
	 * looks, whether there is a space before the postion, or the position
	 * is at the beginning.
	 * 
	 * @param fb
	 * @param offset
	 * @return
	 */
        private boolean isBeforeSpace(FilterBypass fb, int offset) {
            try {
                return (offset > 0) && (fb.getDocument().getLength() > 1) && " ".equals(fb.getDocument().getText(offset - 1, 1));
            } catch (BadLocationException e) {
                System.err.println("WARNING: This is a programm-error an should not occur!");
                e.printStackTrace();
                return false;
            }
        }

        /**
	 * Is called, when a part of the text is replaced. The validity is
	 * checked and only correct charachter can pass.
	 * 
	 * @see javax.swing.text.DocumentFilter#replace(javax.swing.text.DocumentFilter.FilterBypass,
	 *      int, int, java.lang.String, javax.swing.text.AttributeSet)
	 */
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String valid = getValidString(fb, text, offset, length);
            if (valid == null || (length == 0 && valid.length() == 0)) {
                invalidEdit();
                return;
            }
            if (valid.length() == 0) {
                valid = null;
            }
            checkValidity(fb, offset, length, valid);
            super.replace(fb, offset, length, valid, attrs);
        }

        /**
	 * Is called, when a part of the string is removed. Of course, the
	 * validity is checked.
	 * 
	 * @see javax.swing.text.DocumentFilter#remove(javax.swing.text.DocumentFilter.FilterBypass,
	 *      int, int)
	 */
        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            checkValidity(fb, offset, length, "");
            super.remove(fb, offset, length);
        }
    }

    /**
     * 
     */
    public IntervalFormatter() {
        super();
    }

    /**
     * Converts the text to a double[].
     * 
     * @see javax.swing.JFormattedTextField.AbstractFormatter#stringToValue(java.lang.String)
     */
    @Override
    public Object stringToValue(String text) throws ParseException {
        StringTokenizer tok = new StringTokenizer(text);
        double[] values = new double[tok.countTokens()];
        for (int i = 0; i < values.length; i++) {
            String token = tok.nextToken();
            try {
                values[i] = Double.parseDouble(token);
            } catch (NumberFormatException ex) {
                throw new ParseException("Not a Number: " + token, 0);
            }
        }
        Arrays.sort(values);
        return values;
    }

    /**
     * converts a double[] to a string.
     * 
     * @see javax.swing.JFormattedTextField.AbstractFormatter#valueToString(java.lang.Object)
     */
    @Override
    public String valueToString(Object value) {
        if (value == null) {
            return "";
        }
        double[] values = (double[]) value;
        StringBuffer buffy = new StringBuffer(values.length);
        for (int i = 0; i < values.length; i++) {
            buffy.append(String.valueOf(values[i]));
            buffy.append(" ");
        }
        return buffy.toString();
    }

    @Override
    protected DocumentFilter getDocumentFilter() {
        return new IntervalDocumentFilter();
    }
}
