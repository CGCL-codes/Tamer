package net.ontopia.topicmaps.query.impl.basic;

import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.impl.utils.PredicateSignature;
import net.ontopia.topicmaps.query.impl.utils.PredicateDrivenCostEstimator;

/**
 * INTERNAL: Implements the '>=' predicate.
 */
public class GreaterThanEqualsPredicate implements BasicPredicateIF {

    public String getName() {
        return ">=";
    }

    public String getSignature() {
        return ".! .!";
    }

    public int getCost(boolean[] boundparams) {
        return PredicateDrivenCostEstimator.getComparisonPredicateCost(boundparams);
    }

    public QueryMatches satisfy(QueryMatches matches, Object[] arguments) throws InvalidQueryException {
        PredicateSignature sign = PredicateSignature.getSignature(this);
        sign.verifyBound(matches, arguments, this);
        int colix1 = matches.getIndex(arguments[0]);
        int colix2 = matches.getIndex(arguments[1]);
        QueryMatches result = new QueryMatches(matches);
        for (int ix = 0; ix <= matches.last; ix++) {
            Object value1 = matches.data[ix][colix1];
            Object value2 = matches.data[ix][colix2];
            if (PredicateUtils.compare(value1, value2) < 0) continue;
            if (result.last + 1 == result.size) result.increaseCapacity();
            result.last++;
            result.data[result.last] = matches.data[ix];
        }
        return result;
    }
}
