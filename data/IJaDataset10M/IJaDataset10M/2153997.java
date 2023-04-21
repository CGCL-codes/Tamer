package org.eclipse.albireo.internal;

/**
 * A Runnable that produces a result, but nevertheless can be used as a
 * {@link Runnable}.
 */
public abstract class RunnableWithResult implements Runnable {

    private Object result;

    /**
     * Executes the user-defined code.
     * It should call {@link #setResult} to assign a result.
     */
    public abstract void run();

    /**
     * Returns the result.
     * This method can be called after {@link run()} was executed.
     */
    public Object getResult() {
        return result;
    }

    /**
     * Assigns a result. This method should be called once during
     * {@link #run}.
     */
    protected void setResult(Object result) {
        this.result = result;
    }
}
