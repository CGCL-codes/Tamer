package net.sourceforge.hourglass.framework;

/**
 * A listener that reacts to changes in a ProjectGroup.
 */
public interface ProjectGroupListener {

    /**
   * Indicates that project attributes, such as name or description,
   * have changed.
   */
    void projectAttributesChanged(Project p);

    /**
   * Indicates that projects have been added or removed from the group.
   *
   * @param parent the parent project under which the change occurred.
   */
    void projectsChanged(Project parent);
}
