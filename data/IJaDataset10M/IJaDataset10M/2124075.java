package com.lts.util;

import java.lang.reflect.Method;

/**
 * Execute a method in a separate thread.
 * 
 * <P>
 * This class allows clients to create a new thread and then execute a particular method
 * in that thread.  This is useful when simulating a separate VM.
 * 
 * @author cnh
 */
public class MethodThread implements Runnable {

    protected Method method;

    protected Object[] actualParams;

    protected Object target;

    protected String name;

    public MethodThread(Object target, Method method, Object[] actualParams) {
        this.target = target;
        this.method = method;
        this.actualParams = actualParams;
    }

    public void run() {
        try {
            Object[] allParams = { this.actualParams };
            this.method.invoke(target, allParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        if (null == this.name) return super.toString(); else return this.name;
    }
}
