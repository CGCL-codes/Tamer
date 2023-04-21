package com.mycila.event;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface FutureListener<T> {

    void onResponse(T value);

    void onError(Throwable t);
}
