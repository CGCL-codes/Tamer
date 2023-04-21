package org.spockframework.mock;

import org.spockframework.util.GroovyRuntimeUtil;
import groovy.lang.Closure;

public class CodeArgumentConstraint implements IArgumentConstraint {

    private final Closure code;

    public CodeArgumentConstraint(Closure code) {
        this.code = code;
    }

    public boolean isSatisfiedBy(Object argument) {
        return GroovyRuntimeUtil.isTruthy(code.call(argument));
    }
}
