package org.jazzteam.example.jsf.view.bean;

import java.util.ArrayList;
import java.util.List;
import org.jazzteam.example.jsf.exception.ServiceException;
import org.jazzteam.example.jsf.model.Task;
import org.jazzteam.example.jsf.service.ITaskService;
import org.jazzteam.example.jsf.service.impl.InMemoryTaskServiceImpl;

public class BacklogBean extends BaseBean {

    private ITaskService taskService = new InMemoryTaskServiceImpl();

    public List<Task> getAllTasks() {
        List<Task> result = new ArrayList<Task>();
        try {
            result = taskService.getAllTasks();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return result;
    }
}
