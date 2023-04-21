package l1j.server.server.taskmanager.tasks;

import l1j.server.server.Shutdown;
import l1j.server.server.taskmanager.Task;
import l1j.server.server.taskmanager.TaskManager.ExecutedTask;

/**
 * @author Layane
 * 
 */
public class TaskShutdown extends Task {

    public static String NAME = "shutdown";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onTimeElapsed(ExecutedTask task) {
        Shutdown handler = new Shutdown(Integer.valueOf(task.getParams()[2]), false);
        handler.start();
    }
}
