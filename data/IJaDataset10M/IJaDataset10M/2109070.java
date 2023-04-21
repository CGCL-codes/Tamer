package net.dadajax.model;

import java.util.List;
import net.dadajax.download.Link;

/**
 * @author dadajax
 *
 */
public class TaskFactoryImpl implements TaskFactory {

    private static TaskFactoryImpl taskFactoryImpl;

    private TaskFactoryImpl() {
    }

    public static TaskFactory getInstance() {
        if (taskFactoryImpl != null) {
            return taskFactoryImpl;
        } else {
            taskFactoryImpl = new TaskFactoryImpl();
            return taskFactoryImpl;
        }
    }

    @Override
    public Task getTask(String taskName, String destination, List<Link> links, boolean createOwnDir, boolean unpackAfterFinish, boolean deleteAfterFinish, boolean wasUnpacked, int status) {
        Task task;
        task = new RapidshareTask(taskName, destination, links, createOwnDir, unpackAfterFinish, deleteAfterFinish, wasUnpacked, status);
        return task;
    }
}
