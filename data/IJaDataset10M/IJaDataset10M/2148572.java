package au.com.kelpie.fgfp.util;

import java.text.FieldPosition;
import au.com.kelpie.fgfp.Messages;

/**
 * Format a Latitude
 */
public class LatFormat extends LatLongFormat {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2021425344356567043L;

    /**
	 * 
	 */
    public LatFormat() {
        super();
    }

    @Override
    public StringBuffer format(final double number, final StringBuffer toAppendTo, final FieldPosition pos) {
        return super.formatAbsolute(number, toAppendTo, pos).append(number > 0 ? Messages.getString("LatFormat.0") : Messages.getString("LatFormat.1"));
    }
}
