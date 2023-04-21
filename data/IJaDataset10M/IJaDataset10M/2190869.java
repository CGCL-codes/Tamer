package tudu.web.ws.impl;

import tudu.domain.model.Todo;
import tudu.domain.model.TodoList;
import tudu.domain.model.User;
import tudu.service.TodoListsManager;
import tudu.service.UserManager;
import tudu.web.ws.TuduListsWebService;
import tudu.web.ws.bean.WsTodo;
import tudu.web.ws.bean.WsTodoList;

/**
 * Implementation of the Tudu Lists Web Sercice.
 * 
 * @author Julien Dubois
 */
public class TuduListsWebServiceImpl implements TuduListsWebService {

    private TodoListsManager todoListsManager = null;

    private UserManager userManager = null;

    public void setTodoListsManager(TodoListsManager todoListsManager) {
        this.todoListsManager = todoListsManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * @see tudu.web.ws.TuduListsWebService#getAllTodoLists()
     */
    public WsTodoList[] getAllTodoLists() {
        User user = userManager.getCurrentUser();
        WsTodoList[] wsTodoLists = new WsTodoList[user.getTodoLists().size()];
        int i = 0;
        for (TodoList todoList : user.getTodoLists()) {
            WsTodoList wsTodoList = new WsTodoList();
            wsTodoList.setListId(todoList.getListId());
            wsTodoList.setName(todoList.getName());
            wsTodoLists[i++] = wsTodoList;
        }
        return wsTodoLists;
    }

    /**
     * @see tudu.web.ws.TuduListsWebService#getTodosByTodoList(java.lang.String)
     */
    public WsTodo[] getTodosByTodoList(String listId) {
        TodoList todoList = todoListsManager.findTodoList(listId);
        WsTodo[] wsTodos = new WsTodo[todoList.getTodos().size()];
        int i = 0;
        for (Todo todo : todoList.getTodos()) {
            WsTodo wsTodo = new WsTodo();
            wsTodo.setTodoId(todo.getTodoId());
            wsTodo.setPriority(todo.getPriority());
            wsTodo.setDescription(todo.getDescription());
            wsTodo.setCompleted(todo.isCompleted());
            wsTodos[i++] = wsTodo;
        }
        return wsTodos;
    }
}
