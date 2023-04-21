package org.horen.ui.editors.filters;

import org.horen.task.Task;
import org.horen.ui.editors.filters.decides.FilterDecide;
import org.horen.ui.editors.filters.decides.IDecide;

/**
 * Contains all necessary information to filter a viewer in a task
 * editor.
 * 
 * @author Steffen
 */
public class Filter {

    public static final Filter DEFAULT = new Filter();

    private Task m_RootTask = null;

    private boolean m_ShowFinished = false;

    private FilterDecide m_Decide = null;

    /**
	 * Default constructor.
	 */
    public Filter() {
    }

    /**
	 * @return The task intended to use as root in the editor or null to
	 * show all tasks.
	 */
    public Task getRootTask() {
        return m_RootTask;
    }

    /**
	 * Sets the root task for the filter.
	 * @see Filter#getRootTask()
	 */
    public void setRootTask(Task rootTask) {
        m_RootTask = rootTask;
    }

    /**
	 * @return <code>true</code> if also finished tasks should be shown or
	 * <code>false</code> otherwise.
	 */
    public boolean showFinished() {
        return m_ShowFinished;
    }

    /**
	 * Sets whether finished tasks should be shown. (default is 
	 * <code>false</code>).
	 */
    public void setShowFinished(boolean showFinished) {
        m_ShowFinished = showFinished;
    }

    /**
	 * @return The decide for the filter or null if no filter is set
	 * @see IDecide
	 */
    public FilterDecide getDecide() {
        return m_Decide;
    }

    /**
	 * Sets the filter decide.
	 */
    public void setDecide(FilterDecide decide) {
        m_Decide = decide;
    }
}
