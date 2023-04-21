package org.opennms.core.tasks;

import org.opennms.core.utils.LogUtils;

/**
 * SyncTask
 *
 * @author brozow
 */
public class SyncTask extends Task {

    public static final String DEFAULT_EXECUTOR = "default";

    public static final String ADMIN_EXECUTOR = "admin";

    private final Runnable m_action;

    private String m_preferredExecutor = DEFAULT_EXECUTOR;

    public SyncTask(DefaultTaskCoordinator coordinator, ContainerTask parent, Runnable action) {
        this(coordinator, parent, action, DEFAULT_EXECUTOR);
    }

    public SyncTask(DefaultTaskCoordinator coordinator, ContainerTask parent, Runnable action, String preferredExecutor) {
        super(coordinator, parent);
        m_action = action;
        m_preferredExecutor = preferredExecutor;
    }

    @Override
    protected void doSubmit() {
        submitRunnable(getRunnable(), getPreferredExecutor());
    }

    /**
     * This is the run method where the 'work' related to the Task gets down.  This method can be overridden
     * or a Runnable can be passed to the task in the constructor.  The Task is not complete until this method
     * finishes
     */
    public void run() {
        if (m_action != null) {
            m_action.run();
        }
    }

    /**
     * This method is used by the TaskCoordinator to create runnable that will run this task
     */
    final Runnable getRunnable() {
        return new Runnable() {

            public void run() {
                try {
                    SyncTask.this.run();
                } catch (Throwable t) {
                    LogUtils.debugf(this, t, "Exception occurred executing task %s", SyncTask.this);
                }
            }

            public String toString() {
                return "Runner for " + SyncTask.this;
            }
        };
    }

    public String getPreferredExecutor() {
        return m_preferredExecutor;
    }

    public void setPreferredExecutor(String preferredExecutor) {
        m_preferredExecutor = preferredExecutor;
    }

    public String toString() {
        return m_action == null ? super.toString() : m_action.toString();
    }
}
