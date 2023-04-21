package org.apache.mina.statemachine.context;

/**
 * {@link StateContextLookup} implementation which always returns the same
 * {@link StateContext} instance.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class SingletonStateContextLookup implements StateContextLookup {

    private final StateContext context;

    /**
     * Creates a new instance which always returns the same 
     * {@link DefaultStateContext} instance.
     */
    public SingletonStateContextLookup() {
        context = new DefaultStateContext();
    }

    /**
     * Creates a new instance which uses the specified {@link StateContextFactory}
     * to create the single instance.
     * 
     * @param contextFactory the {@link StateContextFactory} to use to create 
     *        the singleton instance.
     */
    public SingletonStateContextLookup(StateContextFactory contextFactory) {
        if (contextFactory == null) {
            throw new IllegalArgumentException("contextFactory");
        }
        context = contextFactory.create();
    }

    public StateContext lookup(Object[] eventArgs) {
        return context;
    }
}
