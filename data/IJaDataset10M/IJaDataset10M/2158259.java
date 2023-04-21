package net.sourceforge.iwii.db.dev.ui.common.taskprocessing;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.iwii.db.dev.ui.common.taskprocessing.TaskProcessFactory.TaskProcessIds;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;

/**
 * Abstract class representing cancellable task process.
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 */
public abstract class AbstractCancelabbleTaskProcess implements ITaskProcess, Cancellable {

    private static final Logger logger = Logger.getLogger(AbstractCancelabbleTaskProcess.class.getName());

    private ProgressHandle handler;

    private boolean isIndeterminate;

    private int progressLimit;

    private Map params;

    private TaskProcessIds taskProcessId;

    /**
     * Constructor responsible for creating indeterminated task process
     * 
     * @param taskName task name
     * @param taskProcessId task process id
     */
    public AbstractCancelabbleTaskProcess(TaskProcessIds taskProcessId, String taskName) {
        this.handler = ProgressHandleFactory.createHandle(taskName, this);
        this.isIndeterminate = true;
        this.progressLimit = 0;
        this.params = new HashMap();
        this.taskProcessId = taskProcessId;
    }

    /**
     * Constructor responsible for creating determinated task proces
     * 
     * @param taskName task name
     * @param progressLimit task progress finish value
     * @param taskProcessId task process id
     */
    public AbstractCancelabbleTaskProcess(TaskProcessIds taskProcessId, String taskName, int progressLimit) {
        this.handler = ProgressHandleFactory.createHandle(taskName, this);
        this.isIndeterminate = false;
        this.progressLimit = progressLimit;
        this.params = new HashMap();
        this.taskProcessId = taskProcessId;
    }

    @Override
    public void run() {
        this.handler.start(this.progressLimit);
        try {
            this.performTask();
        } finally {
            this.handler.finish();
        }
    }

    @Override
    public ProgressHandle getProcessHandle() {
        return this.handler;
    }

    @Override
    public boolean cancel() {
        try {
            if (!this.cancelTask()) return false;
        } catch (Exception e) {
            AbstractCancelabbleTaskProcess.logger.log(Level.SEVERE, "", e);
        }
        this.handler.finish();
        return true;
    }

    @Override
    public void setTaskProcessParams(Map params) {
        this.params = params;
    }

    @Override
    public TaskProcessIds getTaskProcessId() {
        return this.taskProcessId;
    }

    /**
     * Gets parameter with given key
     * 
     * @param key parameter key
     * @return parameter value
     */
    protected Object getParameter(Object key) {
        return this.params.get(key);
    }

    /**
     * Method is responsible for realization of task process logic. Need to be implemented in underlining class.
     */
    public abstract void performTask();

    /**
     * Method is responsible for realization of task cancelation logic. Need to be implemented in undelining class.
     * 
     * @return true, if task was canceled succesfuly, false otherwise
     */
    public abstract boolean cancelTask();
}
