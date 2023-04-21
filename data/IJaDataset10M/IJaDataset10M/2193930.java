package net.sourceforge.javautil.groovy.util;

import groovy.lang.Closure;

/**
 * This allows one to easily setup proxying to a closure allowing alternative of property resolution. It setups the
 * original closures delegate as the wrapper and assigns DELEGATE_FIRST resolve strategy. All of the call() methods are
 * handed off to the original.
 * 
 * @author elponderador
 */
public abstract class ClosureWrapper extends Closure {

    protected Closure original;

    public ClosureWrapper(Closure original) {
        this(original.getOwner(), original.getThisObject(), original);
    }

    public ClosureWrapper(Object owner, Object thisObject, Closure original) {
        this(owner, thisObject, original, Closure.DELEGATE_FIRST);
    }

    public ClosureWrapper(Object owner, Object thisObject, Closure original, int resolveStategy) {
        super(owner, thisObject);
        this.original = original;
        this.original.setDelegate(this);
        this.original.setResolveStrategy(resolveStategy);
    }

    @Override
    public Object call() {
        return original.call();
    }

    @Override
    public Object call(Object arguments) {
        return original.call(arguments);
    }

    @Override
    public Object call(Object[] args) {
        return original.call(args);
    }

    @Override
    public int getDirective() {
        return original.getDirective();
    }

    @Override
    public int getMaximumNumberOfParameters() {
        return original.getMaximumNumberOfParameters();
    }

    @Override
    public Class[] getParameterTypes() {
        return original.getParameterTypes();
    }

    @Override
    public boolean isCase(Object candidate) {
        return original.isCase(candidate);
    }

    @Override
    public final Object getProperty(String property) {
        return this.resolve(property);
    }

    @Override
    public final void setProperty(String property, Object newValue) {
        this.assign(property, newValue);
    }

    /**
	 * Sub classes need to override this to specify how property resolution will be handled.
	 * 
	 * @param name The name of the property that needs to be resolved.
	 * @return The value of the property, or null if no such property is available.
	 */
    public abstract Object resolve(String name);

    /**
	 * Sub classes need to override this to specify how property assignment will be handled.
	 * 
	 * @param name The name of the property to be assigned.
	 * @param value The value to associate with the name.
	 */
    public abstract void assign(String name, Object value);
}
