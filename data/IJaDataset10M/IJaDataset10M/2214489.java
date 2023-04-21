package org.jpox.samples.versioned;

import java.util.Date;

/**
 * Subclass of subclass of trade, to test inheritance of versioned objects.
 * @version $Revision: 1.1 $
 */
public class Trade5SubSub extends Trade5Sub {

    String name;

    public Trade5SubSub(long id, String person, double value, Date date) {
        super(id, person, value, date);
    }

    public void setName(String name) {
        this.name = name;
    }
}
