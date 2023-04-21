package com.blah.gwtgames.client.common.dlx;

import java.util.Set;

public interface SolvedCallback {

    /**
	 * Called whenever DancingLinks.solve finds
	 * a solution. If the solution is not unique,
	 * this will be called more than once. If you
	 * don't need all possible solutions, you can
	 * call DancingLinks.halt() to stop the
	 * algorithm.
	 * 
	 * @param choices A set containing the choices for this solution.
	 */
    public void solved(Set choices);
}
