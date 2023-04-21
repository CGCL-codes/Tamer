package org.proteored.miapeapi.interfaces.ge;

import org.proteored.miapeapi.cv.ge.DirectDetectionAgentName;
import org.proteored.miapeapi.interfaces.Substance;

public interface DirectDetectionAgent extends Substance {

    /**
	 * Gets the agent name used in the direct detection. It should be one of the
	 * possible values from {link {@link DirectDetectionAgentName}
	 */
    public String getName();
}
