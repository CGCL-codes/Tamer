package com.the_eventhorizon.todo.commons.model;

import java.util.Set;

/**
 * Abstraction which provides task tags configured in different plugins.
 * 
 * @author <a href="mailto:prj-todo@the-eventhorizon.com">Pavel Krupets</a>
 */
public interface ITaskTagProvider {

    /**
     * @return cannot be <code>null</code> or empty. Note that it must be unique.
     */
    public String getName();

    public TaskTag[] getTaskTags();

    /**
     * Checks whether provider supports this element.
     * 
     * @param element cannot be <code>null</code>.
     */
    public boolean supportsElement(Object element);

    /**
     * Finds whether given tasks tags exist in element and its children.
     * 
     * @param found cannot be <code>null</code>.
     * 
     * @param element cannot be <code>null</code>.
     * 
     * @param taskTags cannot be <code>null</code> or empty.
     */
    public void findTaskTagsNames(Set<TaskTag> found, Object element, TaskTag[] taskTags);

    /**
     * @param listener must not be <code>null</code>.
     */
    public void addListener(ITaskTagListener listener);

    public void removeListener(ITaskTagListener listener);
}
