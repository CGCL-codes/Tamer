package com.gr.staffpm.tasks.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gr.staffpm.datatypes.Project;
import com.gr.staffpm.datatypes.Task;
import com.gr.staffpm.datatypes.TaskQueue;
import com.gr.staffpm.datatypes.User;
import com.gr.staffpm.tasks.dao.TaskQueueDAO;

/**
 * Default implementation of the {@link TaskQueueService} interface.  This service implements
 * operations related to TaskQueue data.
 */
@Transactional
@Service("taskQueueService")
public class DefaultTaskQueueService implements TaskQueueService {

    private TaskQueueDAO taskQueueDAO;

    @Autowired
    public void setTaskQueueDAO(TaskQueueDAO taskQueueDAO) {
        this.taskQueueDAO = taskQueueDAO;
    }

    @Override
    public TaskQueue getQueuedTask(int id) {
        return taskQueueDAO.getQueuedTask(id);
    }

    @Override
    public TaskQueue getQueuedTaskByTaskId(int id) {
        return taskQueueDAO.getQueuedTaskByTaskId(id);
    }

    @Override
    public List<TaskQueue> getQueuedTasksForProject(Project project) {
        return taskQueueDAO.getQueuedTasksForProject(project);
    }

    @Override
    public List<TaskQueue> getQueuedTasksForManager(User manager) {
        return taskQueueDAO.getQueuedTasksForManager(manager);
    }

    @Override
    public void addTaskToQueue(Task task, User currentUser, User manager, String comment) {
        taskQueueDAO.addTaskToQueue(task, currentUser, manager, comment);
    }

    @Override
    public List<TaskQueue> getQueuedTaskQueuesForManager(User user, int first, int count, String property, boolean ascending) {
        List<TaskQueue> queuedTasks = taskQueueDAO.getQueuedTasksForManager(user, property, ascending);
        if ((first + count) > queuedTasks.size()) {
            queuedTasks = queuedTasks.subList(first, queuedTasks.size() - 1);
        } else queuedTasks = queuedTasks.subList(first, first + count);
        return queuedTasks;
    }

    @Override
    public void removeTaskFromQueue(TaskQueue queuedTask) {
        taskQueueDAO.removeTaskFromQueue(queuedTask);
    }

    @Override
    public void removeTaskFromQueue(Task queuedTask) {
        taskQueueDAO.removeTaskFromQueue(queuedTask);
    }
}
