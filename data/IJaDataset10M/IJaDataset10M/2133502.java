package org.jquantlib.instruments.bonds;

import org.jquantlib.cashflow.Callability;
import org.jquantlib.time.Date;

/**
 * %callability leaving to the holder the possibility to convert
 * 
 * @author Daniel Kong
 */
public class SoftCallability extends Callability {

    private final double trigger;

    public SoftCallability(final Price price, final Date date, final double trigger) {
        super(price, Callability.Type.Call, date);
        this.trigger = trigger;
    }

    public double trigger() {
        return trigger;
    }
}
