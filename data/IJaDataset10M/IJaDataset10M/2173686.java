package org.go;

import org.go.expcetion.GoException;

/**
 * <p>
 * Provides an interface for a class to become a "plugin" to Quartz.
 * </p>
 * 
 * <p>
 * Plugins can do virtually anything you wish, though the most interesting ones
 * will obviously interact with the scheduler in some way - either actively: by
 * invoking actions on the scheduler, or passively: by being a <code>JobListener</code>,
 * <code>TriggerListener</code>, and/or <code>SchedulerListener</code>.
 * </p>
 * 
 * 
 * <p>
 * If you need direct access your plugin, you can have it explicitly put a 
 * reference to itself in the <code>Scheduler</code>'s 
 * <code>SchedulerContext</code> as part of its
 * <code>{@link #initialize(String, Scheduler)}</code> method.
 * </p>
 * 
 * @author hejie
 */
public interface GoPlugin {

    /**
	 * <p>
	 * Called during creation of the <code>Scheduler</code> in order to give
	 * the <code>SchedulerPlugin</code> a chance to initialize.
	 * </p>
	 * 
	 * <p>
	 * At this point, the Scheduler's <code>JobStore</code> is not yet
	 * initialized.
	 * </p>
	 * 
	 * <p>
	 * If you need direct access your plugin, for example during <code>Job</code>
	 * execution, you can have this method explicitly put a 
	 * reference to this plugin in the <code>Scheduler</code>'s 
	 * <code>SchedulerContext</code>.
	 * </p>
	 * 
	 * @param name
	 *          The name by which the plugin is identified.
	 * @param scheduler
	 *          The scheduler to which the plugin is registered.
	 * 
	 * @throws org.go.expcetion.quartz.SchedulerConfigException
	 *           if there is an error initializing.
	 */
    void initialize(String name, Scheduler list) throws GoException;

    /**
	 * <p>
	 * Called in order to inform the <code>SchedulerPlugin</code> that it
	 * should free up all of it's resources because the scheduler is shutting
	 * down.
	 * </p>
	 */
    void shutdown();

    /**
	 * <p>
	 * Called when the associated <code>Scheduler</code> is started, in order
	 * to let the plug-in know it can now make calls into the scheduler if it
	 * needs to.
	 * </p>
	 */
    void start();
}
