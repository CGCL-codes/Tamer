package fr.lig.sigma.astral.operators.aggregation;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 *
 */
public class AggregateFunctionFactory {

    private static HashMap<String, Class> repo = new HashMap<String, Class>();

    static {
        AggregateFunction f;
        f = new AvgAggregate();
        repo.put(f.toString(), f.getClass());
        f = new CountAggregate();
        repo.put(f.toString(), f.getClass());
        f = new MinAggregate();
        repo.put(f.toString(), f.getClass());
        f = new MaxAggregate();
        repo.put(f.toString(), f.getClass());
        f = new CountDistinctAggregate();
        repo.put(f.toString(), f.getClass());
        f = new FirstAggregate();
        repo.put(f.toString(), f.getClass());
        f = new LastAggregate();
        repo.put(f.toString(), f.getClass());
    }

    public static AggregateFunction get(String c) {
        try {
            return (AggregateFunction) repo.get(c).getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create aggregate function instance?");
        }
    }
}
