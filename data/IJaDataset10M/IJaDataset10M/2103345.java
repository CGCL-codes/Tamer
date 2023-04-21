package org.jaqlib.core;

import org.jaqlib.core.reflect.MethodCallRecorder;
import org.jaqlib.core.reflect.MethodInvocation;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 * @param <ResultType>
 */
public class ReflectiveWhereCondition<T, DataSourceType, ResultType> extends AbstractComparableWhereCondition<T, DataSourceType, ResultType> {

    private final MethodCallRecorder invocationRecorder;

    public ReflectiveWhereCondition(Query<T, DataSourceType> query, MethodCallRecorder invocationRecorder) {
        super(query);
        this.invocationRecorder = Assert.notNull(invocationRecorder);
    }

    @Override
    protected MethodInvocation getCurrentMethodInvocation() {
        return invocationRecorder.getCurrentInvocation();
    }

    public void appendLogString(StringBuilder sb) {
        sb.append("element.");
        compare.appendLogString(sb);
    }
}
