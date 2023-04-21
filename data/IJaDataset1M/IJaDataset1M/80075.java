package org.jquantlib.pricingengines.vanilla.finitedifferences;

import org.jquantlib.methods.finitedifferences.AmericanCondition;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

public class FDAmericanCondition<T> extends FDStepConditionEngine {

    public FDAmericanCondition(final GeneralizedBlackScholesProcess process) {
        this(process, 100, 100);
    }

    public FDAmericanCondition(final GeneralizedBlackScholesProcess process, final int timeSteps, final int gridPoints) {
        this(process, timeSteps, gridPoints, false);
    }

    public FDAmericanCondition(final GeneralizedBlackScholesProcess process, final int timeSteps, final int gridPoints, final boolean value) {
        super(process, timeSteps, gridPoints, value);
    }

    @Override
    protected void initializeStepCondition() {
        stepCondition = new AmericanCondition(intrinsicValues.values());
    }
}
