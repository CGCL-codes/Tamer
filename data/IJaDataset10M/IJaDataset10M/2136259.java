package cn.hrmzone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jbpm.api.Configuration;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.IdentityService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
import org.jbpm.api.task.Task;
import org.junit.Before;
import org.junit.Test;

/**
 * @author hrmzone.cn
 *
 */
public class TaskDemoTest {

    private ProcessEngine pe;

    private RepositoryService rs;

    private ExecutionService es;

    private ProcessInstance pi;

    private TaskService ts;

    private IdentityService is;

    private String deploymentId;

    Map<String, String> map;

    @Before
    public void init() {
        pe = Configuration.getProcessEngine();
        rs = pe.getRepositoryService();
        deploymentId = rs.createDeployment().addResourceFromClasspath("resource/task.jpdl.xml").deploy();
        map = new HashMap<String, String>();
        map.put("hr", "employee");
        es = pe.getExecutionService();
        ts = pe.getTaskService();
    }

    @Test
    public void testAssignee() {
        pi = es.startProcessInstanceByKey("task");
        System.out.println("Pi's on first:" + pi.isActive("first"));
        pi = es.signalExecutionById(pi.getId());
        System.out.println("Pi.s on hrmzone:" + pi.isActive("hrmzone"));
        List<Task> list = ts.findPersonalTasks("jobs");
        for (Task t : list) {
            System.out.println("Task name:" + t.getActivityName());
            System.out.println("Task's assignee:" + t.getAssignee());
            System.out.println("id:" + t.getId());
            System.out.println("Ex's id:" + t.getExecutionId());
        }
        System.out.println("Pi's id:" + pi.getId());
        pi = es.signalExecutionById(pi.getId(), "to desktophrm", map);
        System.out.println("Pi.s on desktophrm:" + pi.isActive("desktophrm"));
        List<Task> lt = ts.findPersonalTasks("employee");
        for (Task t : lt) {
            System.out.println("Task name:" + t.getActivityName());
            System.out.println("Task's assignee:" + t.getAssignee());
            System.out.println("id:" + t.getId());
            System.out.println("Ex's id:" + t.getExecutionId());
        }
        System.out.println("Pi's id:" + pi.getId());
        pi = es.signalExecutionById(pi.getId(), "to end");
        System.out.println("Pi's end:" + pi.isEnded());
    }
}
