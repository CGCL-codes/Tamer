package org.remus.infomngmnt.task;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.remus.infomngmnt.task.navigation.TaskStateStore;

/**
 * The activator class controls the plug-in life cycle
 */
public class TaskActivator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.remus.infomngmnt.task";

    private static TaskActivator plugin;

    public static final String NODE_NAME_STATUS = "status";

    public static final String NODE_NAME_STARTED = "started";

    public static final String NODE_NAME_COMPLETED_PERCENTAGE = "completedpercentage";

    public static final String NODE_NAME_ASIGNEE = "asignee";

    public static final String NODE_NAME_PRIORITY = "priority";

    public static final String NODE_NAME_DUE_DATE = "duedata";

    public static final String NODE_NAME_NOTIFY = "notify";

    public static final String NODE_NAME_MINUTES_BEFORE_DUE = "minutesbeforedue";

    public static final String NODE_NAME_ESTIMATED_EFFORTS = "estimated";

    public static final String NODE_NAME_WORKED_UNITS = "workunits";

    public static final String NODE_NAME_WORKED_UNIT = "workunit";

    public static final String NODE_NAME_WORKED_UNIT_STARTED = "workunitstart";

    public static final String NODE_NAME_WORKED_UNIT_END = "workunitend";

    public static final String NODE_NAME_WORKED_UNIT_DESCRIPTION = "description";

    public static final String NODE_NAME_COMPLETED = "completed";

    public static final String INFO_TYPE_ID = "TASK";

    private TaskStateStore store;

    /**
	 * The constructor
	 */
    public TaskActivator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        this.store = new TaskStateStore();
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
    public static TaskActivator getDefault() {
        return plugin;
    }

    public TaskStateStore getStore() {
        return this.store;
    }
}
