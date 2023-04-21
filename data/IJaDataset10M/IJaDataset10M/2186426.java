package com.sun.jini.reggie;

import com.sun.jini.start.LifeCycle;

/**
 * Class for starting transient lookup services.
 * 
 * @author Sun Microsystems, Inc.
 */
class TransientRegistrarImpl extends RegistrarImpl {

    /**
	 * Constructs a TransientRegistrarImpl based on a configuration obtained
	 * using the provided arguments. If lifeCycle is non-null, then its
	 * unregister method is invoked during service shutdown.
	 */
    TransientRegistrarImpl(String[] configArgs, LifeCycle lifeCycle) throws Exception {
        super(configArgs, null, false, lifeCycle);
    }
}
