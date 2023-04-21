package lazyj.page.tags;

import lazyj.Format;
import lazyj.page.StringFormat;

/**
 * <i>dot</i> displays an integer value as comma separated groups of 3 digits.
 * 
 * @author costing
 * @since 2006-10-13
 * @see DDot
 * @see Size
 */
public class Dot implements StringFormat {

    /**
	 * Integer formatting, split the number into 3-digit groups separated by comma.
	 * 
	 * @param sOption always "dot"
	 * @param s string to be formatted, should be an integer representation
	 * @param sTag ignored
	 * @return formatted string
	 */
    @Override
    public String format(final String sTag, final String sOption, final String s) {
        try {
            return Format.showDottedLong((long) Double.parseDouble(s));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
