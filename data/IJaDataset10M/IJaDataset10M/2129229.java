package org.jaqlib.core;

import org.jaqlib.core.reflect.MethodInvocation;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <ResultType>
 */
public class IsNull<T, ResultType> extends ReflectiveCompare<T, ResultType> {

    public IsNull(MethodInvocation invocation) {
        super(invocation, null);
    }

    @Override
    public boolean evaluate(T element) {
        if (!invocationPresent()) {
            return (element == null);
        }
        if (element == null) {
            return false;
        }
        return doEvaluate(getActualValue(element));
    }

    @Override
    protected boolean doEvaluate(ResultType actual) {
        return actual == null;
    }
}
