package simtools.ui;

import java.awt.Toolkit;
import javax.swing.JTextField;

/**
 * @author nbrodu
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class NumberField extends JTextField {

    private Toolkit toolkit;

    public NumberField(int columns) {
        super(columns);
        toolkit = Toolkit.getDefaultToolkit();
    }

    public NumberField(double value, int columns) {
        this(columns);
        setValue(value);
    }

    public NumberField(long value, int columns) {
        this(columns);
        setValue(value);
    }

    public NumberField(double value) {
        toolkit = Toolkit.getDefaultToolkit();
        setValue(value);
    }

    public NumberField(long value) {
        toolkit = Toolkit.getDefaultToolkit();
        setValue(value);
    }

    public boolean isEmpty() {
        return getText().trim().equals("");
    }

    public void setValue(double value) {
        setText(String.valueOf(value));
    }

    public void setValue(long value) {
        setText(String.valueOf(value));
    }

    public double getDoubleValue() throws NumberFormatException {
        double retVal = 0;
        try {
            retVal = Double.parseDouble(getText());
        } catch (NumberFormatException e) {
            selectAll();
            requestFocus();
            toolkit.beep();
            throw e;
        }
        return retVal;
    }

    public long getLongValue() throws NumberFormatException {
        long retVal = 0;
        try {
            retVal = Long.decode(getText()).longValue();
        } catch (NumberFormatException e) {
            selectAll();
            requestFocus();
            toolkit.beep();
            throw e;
        }
        return retVal;
    }
}
