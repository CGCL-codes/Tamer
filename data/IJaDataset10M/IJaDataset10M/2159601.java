package org.gbt2.instrumentation.criteria;

/**
 * This is an {@link Criterion} describing LoopCoverage.
 * 
 * @author Christoph Müller
 */
public class LoopCoverage extends Criterion {

    /** the String "<b>LoopCoverage</b>" */
    public static final String NAME = "LoopCoverage";

    private static final LoopCoverage instance = new LoopCoverage();

    /**
     * @return The single instance of LoopCoverage;
     */
    public static LoopCoverage getInstance() {
        return instance;
    }

    private LoopCoverage() {
    }

    /**
     * @return the String "<b>LoopCoverage</b>"
     */
    @Override
    public String getName() {
        return NAME;
    }
}
