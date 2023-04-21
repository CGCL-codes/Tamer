package org.apache.tools.ant.taskdefs;

/**
 * Adds a compenent definition to the current project.
 * used in the current project. Two attributes are needed, the name that identifies
 * this compenent uniquely, and the full name of the class (
 * including the packages) that
 * implements this component.</p>
 * @since Ant 1.8
 * @ant.task category="internal"
 */
public class Componentdef extends Definer {

    /**
     * Default constructor.
     * Creates a new ComponentDef instance.
     * Sets the restrict attribute to true.
     */
    public Componentdef() {
        setRestrict(true);
    }
}
