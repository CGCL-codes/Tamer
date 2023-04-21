package org.netbeans.cubeon.tasks.core.internals;

import java.util.ArrayList;
import java.util.List;
import org.netbeans.cubeon.tasks.core.api.TaskRepositoryHandler;
import org.netbeans.cubeon.tasks.spi.repository.TaskRepository;
import org.netbeans.cubeon.tasks.spi.repository.TaskRepositoryType;
import org.openide.util.Lookup;

/**
 *
 * @author Anuradha G
 */
class TaskRepositoryHandlerImpl implements TaskRepositoryHandler {

    public List<TaskRepository> getTaskRepositorys() {
        final List<TaskRepository> taskRepositorys = new ArrayList<TaskRepository>();
        final List<TaskRepositoryType> repositoryTypes = getTaskRepositoryTypes();
        for (TaskRepositoryType taskRepositoryType : repositoryTypes) {
            taskRepositorys.addAll(taskRepositoryType.getRepositorys());
        }
        return taskRepositorys;
    }

    public TaskRepository getTaskRepositoryById(String id) {
        List<TaskRepository> repositorys = getTaskRepositorys();
        for (TaskRepository tr : repositorys) {
            if (tr.getId().equals(id)) {
                return tr;
            }
        }
        return null;
    }

    public List<TaskRepositoryType> getTaskRepositoryTypes() {
        return new ArrayList<TaskRepositoryType>(Lookup.getDefault().lookupAll(TaskRepositoryType.class));
    }
}
