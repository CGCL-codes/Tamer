package no.balder.bprules.integration.test;

import no.balder.rules.predicate.Predicate;

public class InsaneAmountPredicate extends AbstractBookingPredicate implements Predicate {

    @Override
    public boolean accept(BookingPredicateVisitor bookingVisitor) {
        return bookingVisitor.visit(this);
    }
}
