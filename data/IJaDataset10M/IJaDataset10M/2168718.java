package com.matthicks.delegates;

/**
 * RunnableDelegate invokes a Runnable when called.
 * 
 * @author Matt Hicks
 */
public class RunnableDelegate implements Delegate {

    private Runnable r;

    public RunnableDelegate(Runnable r) {
        this.r = r;
    }

    public Object invoke(Object... args) {
        r.run();
        return null;
    }
}
