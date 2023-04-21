package net.sourceforge.puxx.core.constants;

import net.sourceforge.puxx.core.math.BoundedSet;
import net.sourceforge.puxx.core.math.Interval;

/**
 * A goal constants defines goal's characteristics.
 * 
 * @author Xavier Detant <xavier.detant@gmail.com>
 * @version 0.1 Creation of the type
 */
public class GoalConstants extends AbstractPuxxGameConstant<Double> {

    /** The CHARGE. */
    public static final GoalConstants CHARGE = new GoalConstants(10.0, Interval.POSITIVES_STAR);

    /** The MASS. */
    public static final GoalConstants MASS = new GoalConstants(10.0, Interval.POSITIVES_STAR);

    /** The RADIUS. */
    public static final GoalConstants RADIUS = new GoalConstants(10.0, Interval.POSITIVES_STAR);

    /**
     * Instantiates a new goal constants.
     * 
     * @param defaultValue the default value
     * @param definitionInterval the definition interval
     */
    protected GoalConstants(final Double defaultValue, final BoundedSet<Double> definitionInterval) {
        super(defaultValue, definitionInterval);
    }
}
