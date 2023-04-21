package org.helianto.finance;

import javax.persistence.DiscriminatorValue;

/**
 * Receivable.
 * 
 * @author Mauricio Fernandes de Castro
 */
@javax.persistence.Entity
@DiscriminatorValue("R")
public class Receivable extends CashFlow {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object other) {
        if (other instanceof Receivable) {
            super.equals(other);
        }
        return false;
    }
}
