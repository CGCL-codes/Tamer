package de.uni_muenster.cs.sev.lethal.factories;

import de.uni_muenster.cs.sev.lethal.states.NamedState;

/**
 * Provides some methods to create a new state, which is either entirely fresh or
 * created from an arbitrary object, which is used to identify it.
 * 
 * @author Martin, Irene, Philipp
 */
public abstract class StateFactory {

    private static StateFactory instance = null;

    /**
	 * Initially sets a non-standard state factory.
	 * @param factory the state factory to use.
	 */
    public static void init(StateFactory factory) {
        assert (instance == null);
        instance = factory;
    }

    /**
	 * Returns the state factory instance.
	 * @return the state factory instance
	 */
    public static StateFactory getStateFactory() {
        if (instance == null) instance = new StdStateFactory();
        return instance;
    }

    /**
	 * Creates a new named state from the given name. This method must be implemented by subclasses.<br>
	 * Make sure, that the name does not change afterwards.
	 * 
	 * @param <T> type of name
	 * @param name name of the new state. <br> 
	 * WARNING: Once the name is stored in the StateFactory, it must not be changed!
	 * @return a fresh state with that name
	 */
    public abstract <T> NamedState<T> makeState(T name);

    /**
	 * Creates a new state out of hot air.
	 * 
	 * @return fresh state
	 */
    public abstract NamedState<?> makeState();
}
