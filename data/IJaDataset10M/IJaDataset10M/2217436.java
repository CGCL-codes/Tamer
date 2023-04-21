package edu.udo.scaffoldhunter.gui.util;

import edu.udo.scaffoldhunter.model.db.DatabaseException;

/**
 * Abstract implementation of {@link DBFunction}, which allows using an argument
 * when creating an anonymous inner class. This version is meant to be used when
 * you do not want to return anything, the return value forwarded by the
 * <code>CallDBManager</code> methods will always be <code>null</code>.
 * <p>
 * To use an argument use the argument type as type variable and call the
 * constructor with the argument. You can then access the argument from
 * <code>call</code> as <code>arg</code>.
 * 
 * @param <T>
 *            type of the function argument
 * 
 * @author Henning Garus
 */
public abstract class VoidUnaryDBFunction<T> implements DBFunction<Void> {

    private final T arg;

    /**
     * Creates a new db function, the constructor argument can be accessed as
     * <code>arg</code> in the <code>call</code> as <code>arg</code>
     * 
     * @param arg
     *            argument passed to <code>call(T)</code>
     */
    public VoidUnaryDBFunction(T arg) {
        this.arg = arg;
    }

    /**
     * 
     * @param arg
     *            argument given to the constructor of this db function
     * @throws DatabaseException
     * 
     * @see #call()
     */
    public abstract void call(T arg) throws DatabaseException;

    @Override
    public final Void call() throws DatabaseException {
        call(arg);
        return null;
    }
}
