package isql.expression.aggregate;

import isql.expression.AggregateType;

/**
 * @author SHZ Oct 31, 2007
 *
 */
public class AggregateFactory {

    public static IAggregate createAggregate(AggregateType type) {
        switch(type) {
            case SUM:
                return new Sum();
            case AVERAGE:
                return new Average();
            case MIN:
                return new Min();
            case MAX:
                return new Max();
            case VAR:
                return new Variance();
            case STD:
                return new StandardDeviation();
            default:
                throw new AssertionError("Aggregate type " + type + " not recognized.");
        }
    }
}
