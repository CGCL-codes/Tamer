package com.ibm.celldt.environment.core;

/**
 * External status code definition for target generated events.
 * 
 * @author Ricardo M. Matinata, Richard Maciel
 * @since 1.1
 */
public interface ITargetElementStatus {

    /**
	 * The element has been successfully configured and is available (conected)
	 * to the host machine.
	 */
    public static final int STARTED = 0;

    /**
	 * The element is not conected to the host machine
	 */
    public static final int STOPPED = 1;

    /**
	 * The element is in a state that allows applications to be run in it.
	 */
    public static final int RESUMED = 2;

    /**
	 * The element is in a state that does not allow applications to be run in it,
	 * although is conected (live) to the host machine.
	 */
    public static final int PAUSED = 3;
}
