package org.hip.vif.forum.servlets;

import org.hip.kernel.servlet.TaskManager;
import org.hip.vif.forum.tasks.TaskManagerImpl;
import org.hip.vif.servlets.VIFRequestHandler;

/**
 * The application's servlet for the forum part.
 *
 * @author Luthiger
 * Created: 11.02.2008
 */
public class VIFForumRequestHandler extends VIFRequestHandler {

    private static final String CONTEXT_CLASSNAME = "org.hip.vif.forum.tasks.VIFForumContext";

    @Override
    protected String getContextClassName() {
        return CONTEXT_CLASSNAME;
    }

    @Override
    protected TaskManager getTaskManager() {
        return TaskManagerImpl.getInstance();
    }
}
