package it.ge.condam.formatter;

import it.ge.condam.utilita.StringUtils;
import java.text.ParseException;
import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 * @author Leone
 *
 * 02/gen/2011
 */
public class NumberFormatter extends AbstractFormatter {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8497415830076596034L;

    private int maxlength = 0;

    public NumberFormatter(int maxlength) {
        this.maxlength = maxlength;
    }

    @Override
    public Object stringToValue(String s) throws ParseException {
        s = StringUtils.stringToNumberString(s);
        if (s.length() > maxlength) {
            s = s.substring(0, maxlength);
        }
        return s;
    }

    @Override
    public String valueToString(Object o) throws ParseException {
        return (String) o;
    }
}
