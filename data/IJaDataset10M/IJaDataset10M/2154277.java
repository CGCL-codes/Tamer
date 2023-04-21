package com.agentfactory.logger.actuator;

import com.agentfactory.logic.agent.Actuator;
import com.agentfactory.logic.lang.FOS;

/**
 *
 * @author  remcollier
 */
public class Warning extends Actuator {

    public boolean act(FOS action) {
        String message = action.argAt(0).toString();
        System.err.println("[WARNING: " + agent.getName() + "] " + message);
        return true;
    }
}
