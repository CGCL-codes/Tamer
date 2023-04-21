package com.ivis.xprocess.web.tapestry.services;

import org.apache.tapestry.engine.state.ApplicationStateManager;
import com.ivis.xprocess.web.tapestry.context.Visit;

/**
 * Implementation of service app.ApplicationLifecycle. Uses the threaded model.
 *
 */
public class ApplicationLifecycleImpl implements ApplicationLifecycle {

    private boolean _discardSession;

    private ApplicationStateManager _stateManager;

    /**
     * Set the ApplicationStateManager for the application lifecycle.
     *
     * @param stateManager
     */
    public void setStateManager(ApplicationStateManager stateManager) {
        _stateManager = stateManager;
    }

    public void logout() {
        _discardSession = true;
        if (_stateManager.exists("visit")) {
            Visit visit = (Visit) _stateManager.get("visit");
            visit.logout();
        }
    }

    public boolean getDiscardSession() {
        return _discardSession;
    }
}
