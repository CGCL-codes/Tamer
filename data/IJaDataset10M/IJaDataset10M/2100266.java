package gumbo.swt.util;

import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;

/**
 * Utilities related to SWT GUI widgets.
 * @author jonb
 */
public class SwtGuiUtils {

    SwtGuiUtils() {
    }

    /**
	 * Sets the value of a spinner.  Handles decimal point.
	 * @param spinner Temp exposed widget.  Never null.
	 * @param value The value.
	 */
    public static void setValue(Spinner spinner, double value) {
        if (spinner == null) throw new IllegalArgumentException();
        int digits = spinner.getDigits();
        spinner.setSelection((int) (Math.pow(10, digits) * value));
    }

    /**
	 * Sets the value of a scale.  Handles decimal point.
	 * @param spinner Temp exposed widget.  Never null.
	 * @param digits Number of decimal places. >=0.
	 * @param value The value.
	 */
    public static void setValue(Scale scale, int digits, double value) {
        if (scale == null) throw new IllegalArgumentException();
        if (digits < 0) throw new IllegalArgumentException("digits must be >=0.");
        scale.setSelection((int) (Math.pow(10, digits) * value));
    }

    /**
	 * Gets the value of a spinner.  Handles decimal point.
	 * @param spinner Temp output widget.  Never null.
	 * @return The value.
	 */
    public static double getValue(Spinner spinner) {
        if (spinner == null) throw new IllegalArgumentException();
        int rawValue = spinner.getSelection();
        int digits = spinner.getDigits();
        return rawValue / Math.pow(10, digits);
    }

    /**
	 * Gets the value of a spinner.  Handles decimal point.
	 * @param scale Temp output widget.  Never null.
	 * @param digits Number of decimal places. >=0.
	 * @return The value.
	 */
    public static double getValue(Scale scale, int digits) {
        if (scale == null) throw new IllegalArgumentException();
        if (digits < 0) throw new IllegalArgumentException("digits must be >=0.");
        int rawValue = scale.getSelection();
        return rawValue / Math.pow(10, digits);
    }
}
