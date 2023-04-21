package com.tirsen.hanoi.engine;

/**
 *
 *
 * <!-- $Id: Activity.java,v 1.3 2002/08/13 14:52:40 tirsen Exp $ -->
 * <!-- $Author: tirsen $ -->
 *
 * @author Jon Tirs&acute;n (tirsen@users.sourceforge.net)
 * @version $Revision: 1.3 $
 */
public abstract class Activity extends Step {

    /**
     * Execute this task. You can be assured that all input parameters have been set before
     * the execution of this method. Return <code>WAIT</code> if the step could not complete and is waiting for some
     * external event to continue, <code>CONTINUE</code> to continue execution.
     * @return <code>WAIT</code> or <code>CONTINUE</code>.
     */
    public abstract int run();
}
