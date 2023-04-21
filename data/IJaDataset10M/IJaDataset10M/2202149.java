package tudu.web.dwr.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import tudu.domain.model.Todo;
import tudu.domain.model.TodoList;
import tudu.domain.model.User;
import tudu.domain.model.comparator.TodoByDescriptionAscComparator;
import tudu.domain.model.comparator.TodoByDescriptionComparator;
import tudu.domain.model.comparator.TodoByDueDateAscComparator;
import tudu.domain.model.comparator.TodoByDueDateComparator;
import tudu.domain.model.comparator.TodoByPriorityAscComparator;
import tudu.security.PermissionDeniedException;
import tudu.service.TodoListsManager;
import tudu.service.TodosManager;
import tudu.service.UserManager;
import tudu.web.dwr.TodosDwr;
import tudu.web.dwr.bean.RemoteTodo;
import tudu.web.dwr.bean.RemoteTodoList;
import uk.ltd.getahead.dwr.WebContextFactory;

/**
 * Implementation of the tudu.service.TodosManager interface.
 * 
 * @author Julien Dubois
 */
public class TodosDwrImpl extends AbstractCommonDwr implements TodosDwr {

    private static final String TODO_LIST_SORT_BY = "TODO_LIST_SORT_BY";

    private static final String SORT_BY_PRIORITY_ASC = "priority_asc";

    private static final String SORT_BY_DESCRIPTION = "description";

    private static final String SORT_BY_DESCRIPTION_ASC = "description_asc";

    private static final String SORT_BY_DUE_DATE = "due_date";

    private static final String SORT_BY_DUE_DATE_ASC = "due_date_asc";

    private final Log log = LogFactory.getLog(TodosDwrImpl.class);

    private UserManager userManager = null;

    private TodosManager todosManager = null;

    private TodoListsManager todoListsManager = null;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setTodosManager(TodosManager todosManager) {
        this.todosManager = todosManager;
    }

    public void setTodoListsManager(TodoListsManager todoListsManager) {
        this.todoListsManager = todoListsManager;
    }

    /**
     * @see tudu.web.dwr.TodosDwr#getCurrentTodoLists()
     */
    public RemoteTodoList[] getCurrentTodoLists() {
        log.debug("Get the current Todo Lists (AJAX menu generation)");
        User user = userManager.getCurrentUser();
        Collection<TodoList> todoLists = user.getTodoLists();
        Collection<RemoteTodoList> remoteTodoLists = new TreeSet<RemoteTodoList>();
        for (TodoList todoList : todoLists) {
            RemoteTodoList remoteTodoList = new RemoteTodoList();
            remoteTodoList.setListId(todoList.getListId());
            remoteTodoList.setName(todoList.getName());
            int completed = 0;
            for (Todo todo : todoList.getTodos()) {
                if (todo.isCompleted()) {
                    completed++;
                }
            }
            remoteTodoList.setDescription(todoList.getName() + " (" + completed + "/" + todoList.getTodos().size() + ")");
            remoteTodoLists.add(remoteTodoList);
        }
        return remoteTodoLists.toArray(new RemoteTodoList[remoteTodoLists.size()]);
    }

    /**
     * @see tudu.web.dwr.TodosDwr#getTodoById(java.lang.String)
     */
    public RemoteTodo getTodoById(String todoId) {
        Todo todo = todosManager.findTodo(todoId);
        RemoteTodo remoteTodo = new RemoteTodo();
        String unescapedDescription = StringEscapeUtils.unescapeHtml(todo.getDescription());
        remoteTodo.setDescription(unescapedDescription);
        remoteTodo.setPriority(todo.getPriority());
        if (todo.getDueDate() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = formatter.format(todo.getDueDate());
            remoteTodo.setDueDate(formattedDate);
        } else {
            remoteTodo.setDueDate("");
        }
        return remoteTodo;
    }

    /**
     * @see tudu.web.dwr.TodosDwr#renderTodos(java.lang.String, java.util.Date)
     */
    @SuppressWarnings("unchecked")
    public String renderTodos(String listId, Date tableDate) {
        log.debug("Render AJAX table");
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        if (listId != null && !listId.equals("")) {
            try {
                TodoList todoList = todoListsManager.findTodoList(listId);
                if (tableDate != null && todoList.getLastUpdate().before(tableDate)) {
                    return "";
                }
                request.setAttribute("todoList", todoList);
                Set<Todo> todos = todoList.getTodos();
                String sorter = (String) request.getSession().getAttribute(TODO_LIST_SORT_BY);
                Set<Todo> sortedTodos;
                String descriptionClass = "sortable";
                String priorityClass = "sortable";
                String dueDateClass = "sortable";
                if (sorter != null) {
                    if (sorter.equals(SORT_BY_DESCRIPTION)) {
                        descriptionClass = "sorted";
                        Comparator comparator = new TodoByDescriptionComparator();
                        sortedTodos = new TreeSet<Todo>(comparator);
                    } else if (sorter.equals(SORT_BY_DESCRIPTION_ASC)) {
                        descriptionClass = "sorted_asc";
                        Comparator comparator = new TodoByDescriptionAscComparator();
                        sortedTodos = new TreeSet<Todo>(comparator);
                    } else if (sorter.equals(SORT_BY_DUE_DATE)) {
                        dueDateClass = "sorted";
                        Comparator comparator = new TodoByDueDateComparator();
                        sortedTodos = new TreeSet<Todo>(comparator);
                    } else if (sorter.equals(SORT_BY_DUE_DATE_ASC)) {
                        dueDateClass = "sorted_asc";
                        Comparator comparator = new TodoByDueDateAscComparator();
                        sortedTodos = new TreeSet<Todo>(comparator);
                    } else if (sorter.equals(SORT_BY_PRIORITY_ASC)) {
                        priorityClass = "sorted_asc";
                        Comparator comparator = new TodoByPriorityAscComparator();
                        sortedTodos = new TreeSet<Todo>(comparator);
                    } else {
                        priorityClass = "sorted";
                        sortedTodos = new TreeSet<Todo>();
                    }
                    sortedTodos.addAll(todos);
                } else {
                    priorityClass = "sorted";
                    sortedTodos = new TreeSet<Todo>(todos);
                }
                request.setAttribute("todos", sortedTodos);
                request.setAttribute("descriptionClass", descriptionClass);
                request.setAttribute("priorityClass", priorityClass);
                request.setAttribute("dueDateClass", dueDateClass);
                int nbCompleted = 0;
                for (Todo todo : todos) {
                    if (todo.isCompleted()) {
                        nbCompleted++;
                    }
                }
                if (todos.size() != 0) {
                    request.setAttribute("completion", nbCompleted * 100 / todos.size());
                } else {
                    request.setAttribute("completion", 100);
                }
            } catch (DataAccessException dae) {
                request.setAttribute("todos", new ArrayList());
            }
        } else {
            request.setAttribute("todos", new ArrayList());
        }
        try {
            return WebContextFactory.get().forwardToString("/WEB-INF/jspf/todos_table.jsp");
        } catch (ServletException e) {
            log.error("ServletException : " + e);
            return "";
        } catch (IOException ioe) {
            log.error("IOException : " + ioe);
            return "";
        }
    }

    /**
     * @see tudu.web.dwr.TodosDwr#forceRenderTodos(java.lang.String)
     */
    public String forceRenderTodos(String listId) {
        return this.renderTodos(listId, null);
    }

    /**
     * Sort the List according to the "sorter" passed as parameter.
     * <p>
     * If the provided "sorter" is equals to the current list "sorter", then the user must have
     * clicked again on the sort button : in that case he wants to sort the list the other way
     * around (ascending).
     * </p>
     * 
     * @see tudu.web.dwr.TodosDwr#sortAndRenderTodos(java.lang.String, java.lang.String)
     */
    public String sortAndRenderTodos(String listId, String sorter) {
        log.debug("Sort the AJAX table");
        HttpSession session = WebContextFactory.get().getHttpServletRequest().getSession();
        String currentSorter = (String) session.getAttribute(TODO_LIST_SORT_BY);
        if (currentSorter != null && currentSorter.equals(sorter) && !currentSorter.endsWith("_asc")) {
            sorter += "_asc";
        }
        session.setAttribute(TODO_LIST_SORT_BY, sorter);
        return this.forceRenderTodos(listId);
    }

    /**
     * @see tudu.web.dwr.TodosDwr#addTodo(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public String addTodo(String listId, String description, String priority, String dueDate) {
        log.debug("Execute addTodo action");
        Todo todo = new Todo();
        String escapedDescription = StringEscapeUtils.escapeHtml(description);
        todo.setDescription(escapedDescription);
        int priorityInt = 0;
        try {
            priorityInt = Integer.valueOf(priority);
        } catch (NumberFormatException e) {
        }
        todo.setPriority(priorityInt);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date due = formatter.parse(dueDate);
            todo.setDueDate(due);
        } catch (ParseException e) {
        }
        try {
            todosManager.createTodo(listId, todo);
            return forceRenderTodos(listId);
        } catch (PermissionDeniedException pde) {
            return renderPermissionException();
        }
    }

    /**
     * @see tudu.web.dwr.TodosDwr#reopenTodo(java.lang.String)
     */
    public String reopenTodo(String todoId) {
        log.debug("Execute reopenTodo action");
        try {
            Todo todo = todosManager.reopenTodo(todoId);
            return forceRenderTodos(todo.getTodoList().getListId());
        } catch (PermissionDeniedException pde) {
            return renderPermissionException();
        }
    }

    /**
     * @see tudu.web.dwr.TodosDwr#completeTodo(java.lang.String)
     */
    public String completeTodo(String todoId) {
        log.debug("Execute completeTodo action");
        try {
            Todo todo = todosManager.completeTodo(todoId);
            return forceRenderTodos(todo.getTodoList().getListId());
        } catch (PermissionDeniedException pde) {
            return renderPermissionException();
        }
    }

    /**
     * @see tudu.web.dwr.TodosDwr#editTodo(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public String editTodo(String todoId, String description, String priority, String dueDate) {
        log.debug("Execute editTodo action");
        try {
            Todo todo = todosManager.findTodo(todoId);
            String escapedDescription = StringEscapeUtils.escapeHtml(description);
            todo.setDescription(escapedDescription);
            int priorityInt = 0;
            try {
                priorityInt = Integer.parseInt(priority);
            } catch (NumberFormatException e) {
            }
            todo.setPriority(priorityInt);
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            if (dueDate == null || dueDate.equals("")) {
                todo.setDueDate(null);
            } else {
                try {
                    Date due = formatter.parse(dueDate);
                    todo.setDueDate(due);
                } catch (ParseException e) {
                }
            }
            todosManager.updateTodo(todo);
            return forceRenderTodos(todo.getTodoList().getListId());
        } catch (PermissionDeniedException pde) {
            return renderPermissionException();
        }
    }

    /**
     * @see tudu.web.dwr.TodosDwr#deleteTodo(java.lang.String)
     */
    public String deleteTodo(String todoId) {
        Todo todo = todosManager.findTodo(todoId);
        try {
            String listId = todo.getTodoList().getListId();
            todosManager.deleteTodo(todoId);
            return forceRenderTodos(listId);
        } catch (PermissionDeniedException pde) {
            return renderPermissionException();
        }
    }
}
