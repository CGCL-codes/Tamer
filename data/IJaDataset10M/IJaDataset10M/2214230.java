package org.wicketopia.util;

import org.wicketopia.context.Context;
import org.wicketopia.context.ContextPredicate;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class ContextualBoolean {

    private final boolean defaultValue;

    private final List<ContextualCondition> conditions = new LinkedList<ContextualCondition>();

    private final Aggregator aggregator;

    public ContextualBoolean(boolean defaultValue) {
        this.defaultValue = defaultValue;
        this.aggregator = defaultValue ? ContextualBoolean.Aggregator.AND : ContextualBoolean.Aggregator.OR;
    }

    public boolean getValue(Context context) {
        if (conditions.isEmpty()) {
            return defaultValue;
        }
        boolean aggregate = defaultValue;
        for (ContextualCondition condition : conditions) {
            aggregate = aggregator.aggregate(aggregate, condition.predicate.evaluate(context) ? condition.value : !condition.value);
        }
        return aggregate;
    }

    public void setValue(ContextPredicate predicate, boolean value) {
        conditions.add(new ContextualCondition(predicate, value));
    }

    public static enum Aggregator {

        OR {

            @Override
            public boolean aggregate(boolean left, boolean right) {
                return left || right;
            }
        }
        , AND {

            @Override
            public boolean aggregate(boolean left, boolean right) {
                return left && right;
            }
        }
        ;

        public abstract boolean aggregate(boolean left, boolean right);
    }

    private final class ContextualCondition {

        private final ContextPredicate predicate;

        private final boolean value;

        private ContextualCondition(ContextPredicate predicate, boolean value) {
            this.predicate = predicate;
            this.value = value;
        }
    }
}
