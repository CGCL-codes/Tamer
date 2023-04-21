package org.antdepo.types.controller;

/**
 * Represents a sequence of Tasks. Conceptually similar to Ant's Sequential task this class
 * is used by <code>Workflow</code> to execute a set of tasks but gets access to the task list.
 * <p/>
 * ControlTier Software Inc.
 * User: alexh
 * Date: Oct 19, 2004
 * Time: 9:09:30 AM
 */
public class TaskSequence extends WorkflowTaskContainer {

    public TaskSequence() {
        super(1);
    }
}
