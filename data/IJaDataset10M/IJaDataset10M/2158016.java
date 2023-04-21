package org.vesuf.model.uml.behavior.statemachines;

import org.vesuf.model.uml.foundation.core.*;
import org.vesuf.runtime.uml.behavior.statemachines.*;
import org.vesuf.runtime.uml.foundation.core.*;

/**
 *  A SimpleState is a state that does not have substates.
 */
public class SimpleState extends State implements ISimpleState {

    /**
	 *  Create a new simple state.
	 */
    public SimpleState(String name, Namespace namespace, CompositeState container) {
        super(name, namespace, container);
    }

    /**
	 *  Create a new instance.
	 *  @param state The modelelement.
	 *  @param runtime The runtime.
	 *  @param parent The parent.
	 *  @return The new instance.
	 */
    public StateVertexInstance create(IRuntime runtime, CompositeStateInstance parent) {
        return new SimpleStateInstance(this, runtime, parent);
    }
}
