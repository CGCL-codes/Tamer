package com.aurecon.kwb.events;

import com.aurecon.kwb.interfaces.UserEvent;
import com.aurecon.kwb.interfaces.UserEventListener;

/**
 * @author Developer
 *
 */
public class OpenWorkspaceEvent implements UserEvent {

    /**
	 * Workspace id.
	 */
    private String workspaceId;

    /**
	 * Constructor.
	 * 
	 * @param workspace	{@link String} id
	 */
    public OpenWorkspaceEvent(String workspace) {
        workspaceId = workspace;
    }

    public void execute(UserEventListener controller) {
        controller.openWorkspace(workspaceId);
    }
}
