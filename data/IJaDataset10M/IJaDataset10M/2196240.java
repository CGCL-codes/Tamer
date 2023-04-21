package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.Expectation;
import com.volantis.testtools.mock.expectations.Report;

/**
 * Base class for all those composite expectations that can only contain one
 * expectation.
 */
public abstract class AbstractSingleCompositeExpectation extends AbstractCompositeExpectationContainer {

    /**
     * The expectation contained.
     */
    private InternalExpectation expectation;

    public void addExpectation(Expectation expectation) {
        if (this.expectation != null) {
            throw new IllegalStateException("Can only contain one expectation");
        }
        this.expectation = (InternalExpectation) expectation;
        this.expectation.makeImmutable();
    }

    protected InternalExpectation getExpectation() {
        return expectation;
    }

    /**
     * Reset all the expectations apart from the specified one.
     *
     * @param alreadyReset The expectation that has already been reset.
     */
    protected void resetAllApartFrom(Expectation alreadyReset) {
        if (expectation != alreadyReset) {
            expectation.reset();
        }
    }

    public void reset() {
        super.reset();
        expectation.reset();
    }

    public void debug(Report report) {
        before(report);
        expectation.debug(report);
        after(report);
    }
}
