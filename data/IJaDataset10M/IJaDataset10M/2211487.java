package jaco.swing;

import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * {@link AdvancedTextField}
 * 
 * @version 1.0.5, June 28, 2011
 * @author Cristian Sulea ( http://cristiansulea.entrust.ro )
 */
public class AdvancedTextField extends JTextField {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private FocusListener listenerSelectAllOnFocus;

    private int maximumCharacters = Integer.MAX_VALUE;

    private String validCharacters;

    private String invalidCharacters;

    private DecimalFormat nf;

    private String decimalSeparator;

    private boolean isNegativeAllowed;

    private boolean isCapsOn;

    private boolean isMandatory;

    public AdvancedTextField() {
        _init(null);
    }

    public AdvancedTextField(String text) {
        _init(text);
    }

    private void _init(String text) {
        setNumeric(false);
        setNegativeAllowed(true);
        setDocument(new SmartTextFieldDocument());
        listenerSelectAllOnFocus = new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                select(0, getText().length());
            }
        };
        setSelectAllOnFocus(false);
        if (text != null) {
            setText(text);
        }
        updateUI();
    }

    public void setSelectAllOnFocus(boolean b) {
        if (b) {
            addFocusListener(listenerSelectAllOnFocus);
        } else {
            removeFocusListener(listenerSelectAllOnFocus);
        }
    }

    @Override
    public String getText() {
        return super.getText();
    }

    @Override
    public void setText(String t) {
        if (t == null || t.length() == 0) {
            super.setText("");
            return;
        }
        if (isNumeric()) {
            boolean ok = true;
            try {
                String t2 = nf.format(nf.parse(t));
                ok = t2.length() == t.length();
            } catch (ParseException e) {
                ok = false;
            }
            if (!ok) {
                throw new IllegalArgumentException("Wrong text passed to a numeric field: '" + t + "'.");
            }
        }
        super.setText(t);
        setCaretPosition(getText().length());
    }

    public void setText(int i) {
        checkIfIsNumeric();
        setText(nf.format(i));
    }

    public void setText(double d) {
        checkIfIsNumeric();
        setText(nf.format(d));
    }

    public int getTextAsInteger() throws ParseException {
        checkIfIsNumeric();
        return nf.parse(getText()).intValue();
    }

    public int getTextAsInteger(int defaultValue) {
        checkIfIsNumeric();
        try {
            return getTextAsInteger();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double getTextAsDouble() throws ParseException {
        checkIfIsNumeric();
        return nf.parse(getText()).doubleValue();
    }

    public double getTextAsDouble(double defaultValue) {
        checkIfIsNumeric();
        try {
            return getTextAsDouble();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int getMaximumCharacters() {
        return maximumCharacters;
    }

    public void setMaximumCharacters(int i) {
        this.maximumCharacters = i;
    }

    public String getValidCharacters() {
        return validCharacters;
    }

    public void setValidCharacters(String validCharacters) {
        this.validCharacters = validCharacters;
    }

    public String getInvalidCharacters() {
        return invalidCharacters;
    }

    public void setInvalidCharacters(String invalidCharacters) {
        this.invalidCharacters = invalidCharacters;
    }

    public boolean isNumeric() {
        return nf != null;
    }

    public void setNumeric(boolean b) {
        if (b) {
            nf = (DecimalFormat) NumberFormat.getNumberInstance();
            nf.setGroupingUsed(false);
            nf.setMaximumIntegerDigits(Integer.toString(Integer.MAX_VALUE).length());
            nf.setMaximumFractionDigits(2);
            decimalSeparator = String.valueOf(nf.getDecimalFormatSymbols().getDecimalSeparator());
            setHorizontalAlignment(TRAILING);
        } else {
            nf = null;
            decimalSeparator = null;
            setHorizontalAlignment(LEADING);
        }
    }

    public void setNumericInteger(boolean b) {
        if (b) {
            setNumeric(true);
            setValidCharacters("-0123456789");
        } else {
            setNumeric(false);
            setValidCharacters(null);
        }
    }

    public int getMaximumIntegerDigits() {
        checkIfIsNumeric();
        return nf.getMaximumIntegerDigits();
    }

    public void setMaximumIntegerDigits(int i) {
        checkIfIsNumeric();
        nf.setMaximumIntegerDigits(i);
    }

    public int getMaximumFractionDigits() {
        checkIfIsNumeric();
        return nf.getMaximumFractionDigits();
    }

    public void setMaximumFractionDigits(int i) {
        checkIfIsNumeric();
        nf.setMaximumFractionDigits(i);
    }

    public boolean isNegativeAllowed() {
        return isNegativeAllowed;
    }

    public void setNegativeAllowed(boolean b) {
        this.isNegativeAllowed = b;
    }

    public boolean isCapsOn() {
        return isCapsOn;
    }

    public void setCapsOn(boolean isCapsOn) {
        this.isCapsOn = isCapsOn;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
        repaint();
    }

    private void checkIfIsNumeric() {
        if (!isNumeric()) {
            throw new NotNumericException();
        }
    }

    private class SmartTextFieldDocument extends PlainDocument {

        /** serialVersionUID */
        private static final long serialVersionUID = -5232988617803490587L;

        @Override
        public void insertString(int offset, String str, AttributeSet as) throws BadLocationException {
            if (str != null) {
                if (validCharacters != null) {
                    for (int i = 0; i < str.length(); i++) {
                        if (validCharacters.indexOf(str.charAt(i)) == -1) {
                            Toolkit.getDefaultToolkit().beep();
                            return;
                        }
                    }
                }
                if (invalidCharacters != null) {
                    for (int i = 0; i < str.length(); i++) {
                        if (invalidCharacters.indexOf(str.charAt(i)) != -1) {
                            Toolkit.getDefaultToolkit().beep();
                            return;
                        }
                    }
                }
                if (getLength() + str.length() > getMaximumCharacters()) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                String sign = "";
                if (isNumeric()) {
                    if (str.startsWith("-")) {
                        sign = "-";
                        if (str.length() > 1) {
                            str = str.substring(1);
                        } else {
                            str = "";
                        }
                        if (offset != 0 || !isNegativeAllowed()) {
                            Toolkit.getDefaultToolkit().beep();
                            return;
                        }
                    }
                    if (str.contains(decimalSeparator) && nf.getMaximumFractionDigits() == 0) {
                        Toolkit.getDefaultToolkit().beep();
                        return;
                    }
                    String str2 = new StringBuilder(getText(0, getLength())).insert(offset, str).toString();
                    if (str2.length() > 0) {
                        str2 = sign + str2;
                        int count = 0;
                        int idx = 0;
                        while ((idx = str2.indexOf(decimalSeparator, idx)) != -1) {
                            count++;
                            idx += decimalSeparator.length();
                        }
                        if (count > 1) {
                            Toolkit.getDefaultToolkit().beep();
                            return;
                        }
                        int x = str2.endsWith(decimalSeparator) ? 1 : 0;
                        try {
                            if (str2.length() - x != nf.format(nf.parse(str2)).length()) {
                                Toolkit.getDefaultToolkit().beep();
                                return;
                            }
                        } catch (Exception e) {
                            Toolkit.getDefaultToolkit().beep();
                            return;
                        }
                    }
                }
                if (isCapsOn()) {
                    str = str.toUpperCase();
                }
                super.insertString(offset, sign + str, as);
            }
        }
    }

    private class NotNumericException extends RuntimeException {

        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        public NotNumericException() {
            super("Field is not numeric.");
        }
    }
}
