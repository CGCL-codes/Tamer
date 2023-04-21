package org.easystub;

import java.lang.reflect.InvocationTargetException;

public class ThrowableResult implements InvokableResult {

    private final Throwable result;

    public ThrowableResult(Throwable result) {
        this.result = result;
    }

    public Object invoke() {
        throw new ThrowableResultExceptionWrapper(result);
    }
}
