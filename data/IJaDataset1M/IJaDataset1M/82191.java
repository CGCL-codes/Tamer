package org.homemotion.scheduler.impl;

import org.apache.log4j.Logger;
import org.homemotion.macros.Macro;
import org.homemotion.macros.MacroManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <p>
 * This is just a simple job that says "Hello" to the world.
 * </p>
 * 
 * @author Bill Kratzer
 */
public final class MacroJob implements Job {

    private static Logger LOGGER = Logger.getLogger(MacroJob.class);

    private MacroManager macroManager;

    /**
     * <p>
     * Empty constructor for job initilization
     * </p>
     * <p>
     * Quartz requires a public empty constructor so that the
     * scheduler can instantiate the class whenever it needs.
     * </p>
     */
    public MacroJob(MacroManager macroManager) {
        this.macroManager = macroManager;
    }

    /**
     * <p>
     * Called by the <code>{@link org.quartz.Scheduler}</code> when a
     * <code>{@link org.quartz.Trigger}</code> fires that is associated with
     * the <code>Job</code>.
     * </p>
     * 
     * @throws JobExecutionException
     *             if there is an exception while executing the job.
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long id = (Long) context.get("MacroID");
        Macro macro = macroManager.get(id);
        if (macro == null) {
            throw new JobExecutionException("Macro could not be found, id=" + id);
        }
        LOGGER.info("Starting macro: " + macro);
        try {
            macroManager.executeAsynch(macro);
        } catch (Exception e) {
            throw new JobExecutionException("Error executing macro, id=" + id, e);
        }
    }
}
