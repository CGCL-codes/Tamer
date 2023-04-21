package org.cyberaide.emolst.app;

import java.util.Date;

/**
 * This class exists purely so that axis can serialize the date.
 * 
 * Dates could also simply be stored as longs in each bean where 
 * a date is needed, but other options should be explored more.
 * 
 * Background:  Timestamp was being used for dates in the information 
 * beans, but timestamp is not a pojo so axis2 cannot serialize it to 
 * xml automatically.  A XML schema would have to be made in order for 
 * axis2 to serialize the timestamp.  Making a pojo with a long was just 
 * a quick and dirty solution to act as a place holder. 
 *
 */
public class MolstDate {

    private long milli;

    /**
	 * Creates a new MolstDate with the current date and time
	 */
    public MolstDate() {
        this.milli = new Date().getTime();
    }

    /**
	 * Create a MolstDate with the given milleseconds
	 * @param milli
	 */
    public MolstDate(long milli) {
        this.milli = milli;
    }

    /**
	 * Get the milliseconds
	 * @return The milliseconds
	 */
    public long getMilli() {
        return milli;
    }

    /**
	 * Set the milliseconds for this MolstDate
	 * @param milli
	 */
    public void setMilli(long milli) {
        this.milli = milli;
    }

    /**
	 * Returns a java.util.Date with the same data as this MolstDate
	 * @return the Date
	 */
    public Date getDate() {
        return new Date(milli);
    }
}
