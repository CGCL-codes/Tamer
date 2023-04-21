package org.jpox.samples.versioned;

import java.util.Date;

/**
 * Subclass of a trade, to test inheritance of versioned objects.
 * @version $Revision: 1.1 $
 */
public class Trade5Sub extends Trade5Base {

    public Trade5Sub(long id, String person, double value, Date date) {
        super(id, person, value, date);
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
