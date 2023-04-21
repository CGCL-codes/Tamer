package net.sf.daro.swing.text;

import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.text.InternationalFormatter;
import javax.swing.text.NumberFormatter;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * An extension of {@link NumberFormatter} that converts to/from the empty
 * string. The counterpart of the empty string is the empty value which can be
 * provided by clients.
 * 
 * @author daniel
 * 
 * @see NumberFormatter
 */
public class EmptyNumberFormatter extends NumberFormatter {

    /**
	 * serial version UID
	 */
    private static final long serialVersionUID = 5132327454611527022L;

    /**
	 * empty value
	 */
    private Number emptyValue;

    /**
	 * Creates a new EmptyNumberFormatter that converts the empty string to
	 * <code>null</code>.
	 * 
	 * @see NumberFormatter#NumberFormatter()
	 */
    public EmptyNumberFormatter() {
        this(NumberFormat.getNumberInstance(), null);
    }

    /**
	 * Creates a new EmptyNumberFormatter that converts the empty string to
	 * <code>null</code>.
	 * 
	 * @param format
	 *            the number format
	 * 
	 * @see NumberFormatter#NumberFormatter(NumberFormat)
	 */
    public EmptyNumberFormatter(NumberFormat format) {
        this(format, null);
    }

    /**
	 * Creates a new EmptyNumberFormatter that converts the empty string to the
	 * given empty value.
	 * 
	 * @param emptyValue
	 *            the number value to be used for the empty string
	 */
    public EmptyNumberFormatter(Number emptyValue) {
        this(NumberFormat.getNumberInstance(), emptyValue);
    }

    /**
	 * Creates a new EmptyNumberFormatter that converts the empty string to the
	 * given empty value.
	 * 
	 * @param format
	 *            the number format
	 * @param emptyValue
	 *            the number value to be used for the empty string
	 */
    public EmptyNumberFormatter(NumberFormat format, Number emptyValue) {
        super(format);
        setEmptyValue(emptyValue);
    }

    /**
	 * Returns the number value to be used for the empty string.
	 * 
	 * @return the number value
	 */
    public Number getEmptyValue() {
        return emptyValue;
    }

    /**
	 * Sets the number value to be used for the empty string.
	 * 
	 * @param value
	 *            the number value
	 */
    public void setEmptyValue(Number value) {
        this.emptyValue = value;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see InternationalFormatter#stringToValue(String)
	 */
    @Override
    public Object stringToValue(String text) throws ParseException {
        return StringUtils.isBlank(text) ? emptyValue : super.stringToValue(text);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see InternationalFormatter#valueToString(Object)
	 */
    @Override
    public String valueToString(Object value) throws ParseException {
        return ObjectUtils.equals(value, emptyValue) ? StringUtils.EMPTY : super.valueToString(value);
    }
}
