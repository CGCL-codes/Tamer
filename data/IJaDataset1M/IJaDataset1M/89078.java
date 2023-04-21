package org.hip.kernel.servlet.impl;

import org.hip.kernel.exc.VException;
import org.hip.kernel.servlet.Context;
import org.hip.kernel.servlet.Task;

/**
 * 	Baseclass of all tasks.
 *
 *	@author Benno Luthiger
 *  @see org.hip.kernel.servlet.Task
 */
public abstract class AbstractTask implements Task {

    Context context = null;

    /**
	 * Returns the context set for this task.
	 * 
	 * @return org.hip.kernel.servlet.Context The tasks context
	 * @see org.hip.kernel.servlet.Context
	 */
    public Context getContext() {
        return context;
    }

    /**
	 * Runs this task
	 */
    public abstract void run() throws VException;

    /**
	 * Sets the context of this Task
	 *
	 * @param inContext org.hip.kernel.servlet.Context The context for this task
	 */
    public void setContext(Context inContext) {
        context = inContext;
    }
}
