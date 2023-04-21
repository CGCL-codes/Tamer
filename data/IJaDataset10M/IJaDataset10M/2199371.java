package freets.tools;

import freets.data.settings.*;
import java.lang.Integer;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * DoubleDocument class extends PlainDocument and overrides the
 * insertString method. The Doubleocument.insertString methods attemps to 
 * parse the string to be inserted. If the Double.parseDouble method throws an
 * exception, the method beeps and returns without inserting the string.
 */
public class DoubleDocument extends PlainDocument {

    /** Name of <code>ResourceBundle</code>. */
    private static String INITIAL_BUNDLE = Settings.PROPERTIES_PATH + "gui/Common";

    /** <code>ResourceBundle</code> for Document. */
    private static ResourceBundle resources = ResourceBundle.getBundle(INITIAL_BUNDLE, Options.defaultOptions.getFreeTsLocale(), freets.tools.FreeTsClassLoader.loader);

    /** Minimum double value. */
    private double minValue;

    /** Maximum double value. */
    private double maxValue;

    /**
     * Constructor.
     *
     * @param minValue minimum <code>double<\code> value.
     * @param maxValue maximum <code>double<code\> value.
     */
    public DoubleDocument(double minValue, double maxValue) {
        super();
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Constructor.
     */
    public DoubleDocument() {
        this(Double.NEGATIVE_INFINITY, Double.MAX_VALUE);
    }

    private void sizeInBounds(String number, String nextNumber) throws NumberFormatException {
        double d = Double.parseDouble(number + nextNumber);
        if ((d < minValue) || (d > maxValue)) throw new NumberFormatException();
    }

    public void insertString(int offset, String s, AttributeSet attributeSet) throws BadLocationException {
        try {
            if ((getLength() == 0) && (s.equals("-") || s.equals("+"))) {
            } else {
                Double.parseDouble(getText(0, getLength()) + s);
                sizeInBounds(getText(0, getLength()), s);
            }
        } catch (NumberFormatException ex) {
            Toolkit.getDefaultToolkit().beep();
            JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, resources.getString("dialog.error.interval") + "[" + minValue + " ... " + maxValue + "]", null, JOptionPane.ERROR_MESSAGE);
            return;
        }
        super.insertString(offset, s, attributeSet);
    }
}
