package org.light.portlets.todolist.service;

import java.util.List;
import org.light.portal.core.service.BaseService;
import org.light.portlets.todolist.model.ToDo;

/**
 * 
 * @author Jianmin Liu
 **/
public interface ToDoListService extends BaseService {

    public List<ToDo> getToDosByUser(long userId);

    public ToDo getToDoById(long id);

    public int getUserToDoCount(long userId);
}
