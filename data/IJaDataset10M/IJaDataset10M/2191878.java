package dk.simonvogensen.itodo.client.backend;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dk.simonvogensen.itodo.client.ITodoGWTUtils;
import dk.simonvogensen.itodo.client.view.ITodoView;
import dk.simonvogensen.itodo.client.TodoApi;
import dk.simonvogensen.itodo.client.TodoApiAsync;
import dk.simonvogensen.itodo.shared.model.Config;
import dk.simonvogensen.itodo.shared.model.TodoItem;

/**
 * @author $LastChangedBy:$ $LastChangedDate:$
 * @version $Revision:$
 */
public class GearsEnabledAppEngineBackend extends Backend {

    private final TodoApiAsync api = GWT.create(TodoApi.class);

    private ITodoView view;

    private ITodoGWTUtils gwtUtils;

    public GearsEnabledAppEngineBackend(ITodoView view, ITodoGWTUtils gwtUtils) {
        this.view = view;
        view.showAlert("Gears is installed!");
        this.gwtUtils = gwtUtils;
    }

    public void getTodoItemList(int startIndex, int fetchSize, AsyncCallback callback) {
        api.getTodoItemList(startIndex, fetchSize, callback);
    }

    public void createTodo(AsyncCallback callback) {
        api.createTodo(callback);
    }

    private int workerId;

    public void updateTodo(TodoItem item, AsyncCallback callback) {
        api.updateTodo(item, callback);
    }

    public void deleteTodo(TodoItem item, AsyncCallback callback) {
        api.deleteTodo(item, callback);
    }

    public void updateConfig(Config config, AsyncCallback callback) {
        api.updateConfig(config, callback);
    }

    public void getLatestTodoItem(AsyncCallback<TodoItem> callback) {
        api.getLatestTodoItem(callback);
    }

    public void importData(String importString, AsyncCallback<Void> callback) {
    }

    public void init() {
    }
}
