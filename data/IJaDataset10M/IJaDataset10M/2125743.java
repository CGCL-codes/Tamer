package org.gbt2.instrumentation.criteria;

/**
 * This is an {@link Criterion} describing ConditionCoverage.
 * 
 * @author Christoph Müller
 */
public class ConditionCoverage extends Criterion {

    /** the String "<b>ConditionCoverage</b>" */
    public static final String NAME = "ConditionCoverage";

    private static final ConditionCoverage instance = new ConditionCoverage();

    /**
     * @return The single instance of ConditionCoverage;
     */
    public static ConditionCoverage getInstance() {
        return instance;
    }

    private ConditionCoverage() {
    }

    /**
     * @return the String "<b>ConditionCoverage</b>"
     */
    @Override
    public String getName() {
        return NAME;
    }
}
