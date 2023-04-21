package net.sourceforge.clownfish.mojo.glassfish.event;

import javax.enterprise.deploy.spi.status.ProgressEvent;
import net.sourceforge.clownfish.core.ModuleStartable;
import net.sourceforge.clownfish.core.TaskProgress;
import org.apache.commons.logging.Log;

/**
 * DeployedProgressListener is a progress listener class that calls
 * {@link ModuleStartable} to auto-start the deployed artifact and notifies
 * {@link TaskProgress} if the operation succeeded or failed.
 * 
 * @author lyeung
 */
public class DeployedProgressListener extends DefaultProgressListener {

    /**
     * Module startable.
     */
    private ModuleStartable moduleStartable;

    /**
     * Autostart.
     */
    private boolean autoStart;

    /**
     * Constructor.
     * 
     * @param log log
     * @param moduleStartable module startable
     * @param taskProgress task progress
     * @param autoStart auto start
     * @param verbose verbose
     */
    public DeployedProgressListener(Log log, ModuleStartable moduleStartable, TaskProgress taskProgress, boolean autoStart, boolean verbose) {
        super(log, taskProgress, verbose);
        this.moduleStartable = moduleStartable;
        this.autoStart = autoStart;
    }

    /**
     * Handle progress event.
     * 
     * @param event event
     */
    @Override
    public void handleProgressEvent(ProgressEvent event) {
        if (event.getDeploymentStatus().isCompleted()) {
            if (autoStart) {
                if (isVerbose() && getLog().isInfoEnabled()) {
                    getLog().info("Autostarting artifact");
                }
                if (!moduleStartable.startModule()) {
                    failTask();
                }
            } else {
                completeTask();
            }
        } else if (event.getDeploymentStatus().isFailed()) {
            failTask();
        }
    }

    /**
     * Fail task.
     */
    private void failTask() {
        getTaskProgress().setCompleted(false);
    }

    /**
     * Complete task.
     */
    private void completeTask() {
        getTaskProgress().setCompleted(true);
    }
}
