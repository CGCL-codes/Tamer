package ar.com.temporis.task;

import ar.com.temporis.scrum.model.Task;
import ar.com.temporis.system.domain.User;
import junit.framework.TestCase;

public class TaskServiceTestCase extends TestCase {

    public void test() throws Exception {
        TaskService service = new TaskServiceImpl();
        User user = null;
        Task task = null;
        service.assign(user, task);
    }
}
