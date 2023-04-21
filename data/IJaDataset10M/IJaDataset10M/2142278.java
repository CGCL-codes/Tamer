package com.javaeedev.lightweight.mvc;

/**
 * Used to sort interceptors.
 * 
 * @author Xuefeng
 */
class InterceptorOrder implements java.lang.Comparable<InterceptorOrder> {

    public final Interceptor interceptor;

    public final int order;

    public InterceptorOrder(Interceptor interceptor, int order) {
        this.interceptor = interceptor;
        this.order = order;
    }

    public int compareTo(InterceptorOrder o) {
        if (order > o.order) return 1;
        if (order < o.order) return (-1);
        return interceptor.getClass().getName().compareTo(o.interceptor.getClass().getName());
    }
}
