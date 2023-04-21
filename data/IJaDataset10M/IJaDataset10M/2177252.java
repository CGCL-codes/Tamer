package org.happy.commons.patterns.impl;

import org.happy.commons.patterns.Passivable_1x0;

/**
 * this class is an implementation of Passiveable
 * The implementation is based on a counter which 
 * will be incremented if you call setPassive(true) and decremented  call setPassive(false).
 * isPassive returns only false if the counter==0 
 * @author Andreas Hollmann
 *
 */
public class Passivable_1x0Impl implements Passivable_1x0 {

    int counter = 0;

    @Override
    public boolean isPassive() {
        return 0 < counter;
    }

    @Override
    public void setPassive(boolean passive) {
        if (passive) {
            this.counter++;
        } else {
            this.counter--;
        }
        if (this.counter < 0) {
            this.counter = 0;
            throw new IllegalStateException("The counter in Passivable_1x0Impl can't be smaller as zero!");
        }
    }
}
