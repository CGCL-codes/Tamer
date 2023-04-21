package com.mycila.math.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Result<T> {

    private static final Object NULL = new Object();

    private Future<T> future;

    private T t;

    Result(Future<T> future) {
        this.future = future;
    }

    @SuppressWarnings({ "unchecked" })
    public T get() {
        try {
            if (t == NULL) return null;
            if (t != null) return t;
            t = future.get();
            future = null;
            if (t == null) t = (T) NULL;
            return t;
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
