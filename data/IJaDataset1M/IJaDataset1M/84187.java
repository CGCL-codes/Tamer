package uk.co.weft.dealersys2;

import javax.servlet.*;
import uk.co.weft.dbutil.*;
import uk.co.weft.htform.*;

public class DimensionWidget extends IntegerWidget {

    /** the minimum dimension accepted by this widget */
    private int min = 0;

    /** the maximum dimension accepted by this widget */
    private int max = 0;

    /**
	 * This constructor is only any use if you are subclassing, as min and max
	 * will both be zero
	 */
    public DimensionWidget(String name, String prompt, String help) {
        super(name, prompt, help);
    }

    /**
	 * This is the constructor to use if you are using this class directly.
	 */
    public DimensionWidget(String name, String prompt, String help, int min, int max) {
        super(name, prompt, help);
        this.min = min;
        this.max = max;
    }

    /**
	 * get my minimum value. Intended to be over-ridden where range needs to
	 * be computed from the context
	 */
    protected int getMin(Context context) throws DataStoreException {
        return min;
    }

    /**
	 * get my minimum value. Intended to be over-ridden where range needs to
	 * be computed from the context
	 */
    protected int getMax(Context context) throws DataStoreException {
        return max;
    }

    /**
	 * If I have a value, check that it's within my range
	 */
    protected void preProcess(Context context) throws DataStoreException, ServletException {
        super.preProcess(context);
        Integer value = context.getValueAsInteger(name);
        if (value != null) {
            int v = value.intValue();
            int l = getMin(context);
            int m = getMax(context);
            if ((v < l) || (v > m)) {
                context.put(name, null);
                throw new DataFormatException(name + ": value " + v + " is out of range; " + "it must be between " + l + " and " + m);
            }
        }
    }
}
