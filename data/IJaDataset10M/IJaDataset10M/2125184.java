package org.jmetis.closures.decorator;

import org.jmetis.kernel.closure.ITernaryPredicate;

/**
 * @author era
 *
 */
public class TernaryNotPredicate<A1, A2, A3> extends TernaryPredicateDecorator<A1, A2, A3> {

    /**
	 * @param component
	 */
    public TernaryNotPredicate(ITernaryPredicate<A1, A2, A3> component) {
        super(component);
    }

    @Override
    public boolean evaluate(A1 firstArgument, A2 secondArgument, A3 thirdArgument) {
        return !super.evaluate(firstArgument, secondArgument, thirdArgument);
    }
}
