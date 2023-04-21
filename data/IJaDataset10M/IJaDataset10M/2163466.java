package com.potix.zul.html.impl;

import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import com.potix.lang.Objects;
import com.potix.util.Locales;
import com.potix.zk.ui.WrongValueException;

/**
 * An input box that supports format.
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public abstract class FormatInputElement extends InputElement {

    private String _format;

    /** Returns the format.
	 * <p>Default: null (used what is defined in the format sheet).
	 */
    public String getFormat() {
        return _format;
    }

    /** Sets the format.
	 */
    public void setFormat(String format) throws WrongValueException {
        if (!Objects.equals(_format, format)) {
            final String old = _format;
            _format = format;
            smartUpdate("zk_fmt", getFormat());
            try {
                smartUpdate("value", getText());
            } catch (WrongValueException ex) {
            }
        }
    }

    /** Formats a number (Integer, BigDecimal...) into a string.
	 * If null, an empty string is returned.
	 * A utility to assist the handling of numeric data.
	 * @see #toNumberOnly
	 */
    protected String formatNumber(Object value) {
        if (value == null) return "";
        final String fmt = getFormat();
        if (fmt == null) {
            return value.toString();
        } else {
            final DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locales.getCurrent());
            df.applyPattern(fmt);
            return df.format(value);
        }
    }

    /** Filters out non digit characters, such comma and whitespace,
	 * from the specified value.
	 * It is used to parse a string to numeric data.
	 * @see #formatNumber
	 */
    protected String toNumberOnly(String val) {
        if (val == null) return val;
        final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locales.getCurrent());
        final char GROUPING = symbols.getGroupingSeparator(), DECIMAL = symbols.getDecimalSeparator(), PERCENT = symbols.getPercent(), MINUS = symbols.getMinusSign();
        final String fmt = getFormat();
        StringBuffer sb = null;
        for (int j = 0, len = val.length(); j < len; ++j) {
            final char cc = val.charAt(j);
            final boolean ignore = (cc < '0' || cc > '9') && cc != DECIMAL && cc != MINUS && cc != '+' && cc != PERCENT && (Character.isWhitespace(cc) || cc == GROUPING || (fmt != null && fmt.indexOf(cc) >= 0));
            if (ignore) {
                if (sb == null) sb = new StringBuffer(len).append(val.substring(0, j));
            } else {
                final char c2 = cc == MINUS ? '-' : cc == DECIMAL ? '.' : cc == PERCENT ? '%' : cc;
                if (cc != c2) {
                    if (sb == null) sb = new StringBuffer(len);
                    sb.append(c2);
                } else if (sb != null) {
                    sb.append(c2);
                }
            }
        }
        return sb != null ? sb.toString() : val;
    }

    public String getOuterAttrs() {
        final String attrs = super.getOuterAttrs();
        final String fmt = getFormat();
        return fmt != null && fmt.length() != 0 ? attrs + " zk_fmt=\"" + fmt + '"' : attrs;
    }

    protected boolean isAsapRequired(String evtnm) {
        return ("onChange".equals(evtnm) && getFormat() != null) || super.isAsapRequired(evtnm);
    }
}
