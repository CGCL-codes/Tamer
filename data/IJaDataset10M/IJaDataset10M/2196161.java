package org.vardb.util.tasks;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.vardb.util.tasks.dao.CTask;

@Transactional(readOnly = false)
public interface ITaskService {

    CTask getTask(String id);

    List<CTask> getTasks(String user_id);

    int getActiveCount();

    void execute(CTaskExecutor task);

    String queueTask(ITaskParams params);

    void failTask(String id, String message);

    void cancelTask(String id);

    void updateTask(String id, String message);

    void updateTask(String id, String message, CTask.Status status);

    void updateRedirect(String id, String redirect);

    void onTaskComplete(String id);

    void onTaskCancelled(String id);

    void onException(String id, Exception e);
}
