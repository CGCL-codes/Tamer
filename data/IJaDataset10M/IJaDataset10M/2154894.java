package org.eclipse.mylyn.internal.tasks.core;

import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;

/**
 * Task repository filter to build list of repositories with required capabilities.
 * 
 * @author Eugene Kleshov
 * @since 2.0
 */
public interface TaskRepositoryFilter {

    public static TaskRepositoryFilter ALL = new TaskRepositoryFilter() {

        public boolean accept(TaskRepository repository, AbstractRepositoryConnector connector) {
            return true;
        }
    };

    public static TaskRepositoryFilter CAN_QUERY = new TaskRepositoryFilter() {

        public boolean accept(TaskRepository repository, AbstractRepositoryConnector connector) {
            return !(connector instanceof LocalRepositoryConnector);
        }
    };

    public static TaskRepositoryFilter CAN_CREATE_NEW_TASK = new TaskRepositoryFilter() {

        public boolean accept(TaskRepository repository, AbstractRepositoryConnector connector) {
            return connector.canCreateNewTask(repository);
        }
    };

    public static TaskRepositoryFilter CAN_CREATE_TASK_FROM_KEY = new TaskRepositoryFilter() {

        public boolean accept(TaskRepository repository, AbstractRepositoryConnector connector) {
            return connector.canCreateTaskFromKey(repository);
        }
    };

    public static TaskRepositoryFilter IS_USER_MANAGED = new TaskRepositoryFilter() {

        public boolean accept(TaskRepository repository, AbstractRepositoryConnector connector) {
            return connector.isUserManaged();
        }
    };

    boolean accept(TaskRepository repository, AbstractRepositoryConnector connector);
}
