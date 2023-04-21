package com.firescrum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.firescrum.dao.TaskDao;
import com.firescrum.model.BacklogItem;
import com.firescrum.model.Task;

@Component
public class TaskService {

    @Autowired
    private TaskDao dao;

    public Task addTaskToBacklogItem(BacklogItem backlogItem, Task task) {
        return dao.save(task);
    }

    public Task updateTask(Task task) {
        return dao.update(task);
    }

    public void deleteTask(Task task) {
        dao.delete(task);
    }
}
