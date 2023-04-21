package net.teqlo.components;

import net.teqlo.TeqloException;
import net.teqlo.db.ComponentLookup;
import net.teqlo.db.ExecutorLookup;
import net.teqlo.events.TeqloListener;
import net.teqlo.lifecycle.Lifecycle;
import net.teqlo.runtime.Service;

/**
 * A teqlo component is a factory for executors.
 * 
 * A Component instance is associated per component 1:1 with a TeqloRuntime instance.
 * 
 * A Component is added as a listener to its Runtime, and shd respond to LifecycleCloseEvent from it by closing itself.
 * 
 * A Component constructor MUST look like this:
 * 
 * 	public MyComponent( ComponentLookup cl) {
 * 		super( cl);
 * 		...
 * 	}
 * 
 * @author jthwaites
 *
 */
public interface Component extends Lifecycle, TeqloListener {

    /**
	 * Returns the component lookup for this component
	 * @return ComponentLookup for this component
	 */
    public ComponentLookup getComponentLookup();

    /**
	 * Returns a new executor instance for the given service and executor lookup object
	 * @param service whose table of executors will now include this one
	 * @param executor lookup for the new executor
	 * @return Executor for the given service and executor definition
	 * @throws TeqloException if any error occurs
	 */
    public Executor createExecutor(Service service, ExecutorLookup el) throws TeqloException;
}
