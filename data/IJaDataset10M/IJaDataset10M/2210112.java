package de.perschon.jww.workflows;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import de.perschon.jww.lock.LockManager;

/**
 * A WorkflowFilter is used to give structure to WebApplications. The developer defines the workflows
 * by paths and what session-variables (artifacts) these workflows contain. The Filter determines 
 * whether the user leaves one or more workflows and removes the workflows artifacts from the session
 * if he leaves any or more. Additional if there's a {@link LockManager}, the Filter releases all locks
 * for the user's session and the left workflows.
 */
public interface WorkflowFilter {

    /**
     * The name of the session variable that contains the list of workflows the user is in.
     */
    public static final String KEY_WORKFLOW_VARIABLE = "currentWorkflow";

    /**
     * This method is supposed to be called each time the user sends a {@link HttpServletRequest}.<br/>
     * It determines which workflows the user is in, whether he leaves any of these workflows, if end 
     * what workflows he enters. Based on these information the {@link WorkflowFilter} removes 
     * the artifacts contained in the {@link Workflow}s that are left from the user's session.
     * 
     * @param session the user's session
     * @param uri the requested URI
     */
    public void work(HttpSession session, String uri);
}
