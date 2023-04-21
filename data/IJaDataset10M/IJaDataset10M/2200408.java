package org.jquantlib.instruments;

import org.jquantlib.cashflow.Callability;
import org.jquantlib.time.Date;

/**
 * %callability leaving to the holder the possibility to convert
 * 
 * @author Daniel Kong
 */
public class SoftCallability extends Callability {

    private double trigger;

    public SoftCallability(final Price price, final Date date, double trigger) {
        super(price, Callability.Type.Call, date);
        this.trigger = trigger;
    }

    public double getTrigger() {
        return trigger;
    }
}
