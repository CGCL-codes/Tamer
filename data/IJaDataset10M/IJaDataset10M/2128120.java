package com.aptana.ide.debug;

import org.eclipse.debug.core.model.IBreakpoint;

/**
 * IJSDebugController
 */
public interface IJSDebugController {

    /**
	 * resume
	 */
    void resume();

    /**
	 * suspend
	 */
    void suspend();

    /**
	 * terminate
	 */
    void terminate();

    /**
	 * Step into the current JS location
	 */
    void stepInto();

    /**
	 * Step over the current JS location
	 */
    void stepOver();

    /**
	 * The provided breakpoint has been added or removed depending on the <code>added</code> parameter. Updates the
	 * controller for this change.
	 * 
	 * @param breakpoint
	 *            the breakpoint that has been added or removed
	 * @param added
	 *            whether or not the breakpoint has been added
	 */
    void handleBreakpoint(IBreakpoint breakpoint, boolean added);

    /**
	 * Retrieve the stack frames of the Ant build. May occur asynchronously depending on implementation.
	 */
    void getStackFrames();

    /**
	 * Open the debugger to the specified URL.
	 * 
	 * @param url
	 */
    void openUrl(String url);
}
