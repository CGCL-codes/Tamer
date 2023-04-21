package com.ewansilver.raindrop;

import java.util.Map;

/**
 * A QueueAdmissionsController is responsible for checking to see if specified
 * Tasks can be added to a queue.
 * 
 * A controller is only used if isActive is true. The active status can be
 * toggled in real time.
 * 
 * @author Ewan Silver
 */
public interface QueueAdmissionsController {

    /**
	 * Initialise the QueueAdmissionsController.
	 * 
	 * @param parameters a Map of parameters using name/value pairs.
	 */
    public void initialise(Map<String, String> parameters);

    /**
	 * Checks to see if the supplied Task is able to be accepted into the Queue.
	 * 
	 * @return true if the task is acceptable. False otherwise.
	 */
    public boolean isAcceptable(Object aTask);

    /**
	 * Checks to see if the controller is active and therefore should be checked
	 * to see if it will allow the enqueued task through.
	 * 
	 * @return true if the controller is active.
	 */
    public boolean isActive();

    /**
	 * Sets the active status of the controller.
	 * 
	 * @param aStatus
	 *            the new status of the controller.
	 */
    public void setActive(boolean aStatus);
}
