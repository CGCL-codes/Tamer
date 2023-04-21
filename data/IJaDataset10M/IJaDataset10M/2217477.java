package com.bluestone;

import com.bluestone.context.IContext;

/**
 * @author <a href="mailto:bluesotne.master@gmail.com">daniel.q</a>
 *
 */
public interface IAction {

    /**
     * The action execution was successful. Show result
     * view to the end user.
     */
    public static final String SUCCESS = "success";

    /**
     * The action execution was a failure.
     * Show an error view, possibly asking the
     * user to retry entering data.
     */
    public static final String ERROR = "error";

    /**
	 * 
	 * @return true if the IAction execute successful.
	 */
    public boolean execute(IContext context);

    /**
	 * 
	 * @return true if the IActon is valid.
	 */
    public boolean isValid(IContext context);

    /**
	 * 
	 * @return the parent of the IAction.
	 */
    public Object getParent();

    /**
	 * 
	 * @return the unique id of the action.
	 */
    public String getName();
}
