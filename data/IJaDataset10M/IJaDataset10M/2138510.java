package org.antdepo.tasks.session;

import org.apache.tools.ant.BuildException;

/**
 * @ant.task name="session-existsinstance"
 */
public class SessionExistsInstance extends BaseSessionTask {

    /**
     * implementions should validate their required input
     */
    void validate() {
        if (null == session) {
            throw new BuildException("session attribute not set");
        }
    }

    /**
     * Executes the task
     */
    public void execute() {
        validate();
        storeResult(Boolean.toString(existsSessionInstance(session)));
    }
}
