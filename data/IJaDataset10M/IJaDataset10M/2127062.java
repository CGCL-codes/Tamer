package listo.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import listo.client.model.Task;
import listo.client.model.Tasks;
import listo.client.model.operations.DeleteTasksOp;
import listo.utils.logging.Log;
import listo.utils.types.DateTime;
import java.util.concurrent.TimeUnit;

@Singleton
public class CleanupRunner implements Runnable {

    private final ContextManager contextManager;

    private final Preferences preferences;

    private final Log log;

    @Inject
    protected CleanupRunner(ContextManager contextManager, Preferences preferences, AppConfig appConfig, Log log, Executors executors) {
        this.contextManager = contextManager;
        this.preferences = preferences;
        this.log = log;
        log.debug("Starting cleanup runner");
        executors.getCleanupExecutor().scheduleWithFixedDelay(this, appConfig.getAutoRemoverInitial(), appConfig.getAutoRemover(), TimeUnit.SECONDS);
    }

    public void run() {
        log.debug("Starting cleanup");
        long now = new DateTime().getTicks();
        Tasks tasksToRemove = new Tasks();
        for (Task task : contextManager.getCurrentContext().getTasks()) {
            DateTime lastUpdated = task.getLastUpdated();
            if (lastUpdated == null) continue;
            if (task.isCompleted()) {
                int time = (int) ((now - lastUpdated.getTicks()) / (24 * 60 * 60 * 1000));
                if (time >= preferences.getAutoRemoveCompletedDays()) {
                    tasksToRemove.add(task);
                    continue;
                }
            }
            if (!task.hasFolders()) {
                int time = (int) ((now - lastUpdated.getTicks()) / (24 * 60 * 60 * 1000));
                if (time >= preferences.getAutoRemoveDeletedDays()) {
                    tasksToRemove.add(task);
                }
            }
        }
        if (tasksToRemove.isEmpty()) {
            log.debug("No expired tasks found");
            return;
        }
        log.debug("Removing %s expired tasks", tasksToRemove.size());
        DeleteTasksOp op = new DeleteTasksOp();
        op.setTasks(tasksToRemove);
        contextManager.addAndRun(op);
    }
}
