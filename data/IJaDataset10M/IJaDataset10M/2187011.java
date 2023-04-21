package cardwall.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CardWallServiceAsync {

    void getTaskList(AsyncCallback async);

    void createTask(AsyncCallback async);

    void updateTask(Task task, AsyncCallback async);
}
