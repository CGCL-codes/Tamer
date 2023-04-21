package de.objectcode.time4u.server.web.ws;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.objectcode.time4u.server.api.ITaskService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.filter.TaskFilter;

/**
 * Web-service delegate to the task service implementation.
 * 
 * @author junglas
 */
@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws", endpointInterface = "de.objectcode.time4u.server.api.ITaskService")
@SOAPBinding(style = Style.RPC)
public class TaskServiceWS implements ITaskService {

    private static final Log LOG = LogFactory.getLog(TaskServiceWS.class);

    private final ITaskService m_taskService;

    public TaskServiceWS() throws Exception {
        final InitialContext ctx = new InitialContext();
        m_taskService = (ITaskService) ctx.lookup("time4u-server/TaskService/remote");
    }

    public Task getTask(final String taskId) {
        if (LOG.isInfoEnabled()) {
            LOG.info("getTask: " + taskId);
        }
        return m_taskService.getTask(taskId);
    }

    public FilterResult<Task> getTasks(final TaskFilter filter) {
        if (LOG.isInfoEnabled()) {
            LOG.info("getTasks: " + filter);
        }
        return m_taskService.getTasks(filter);
    }

    public FilterResult<TaskSummary> getTaskSummaries(final TaskFilter filter) {
        if (LOG.isInfoEnabled()) {
            LOG.info("getTaskSummaries: " + filter);
        }
        return m_taskService.getTaskSummaries(filter);
    }

    public TaskSummary getTaskSummary(final String taskId) {
        if (LOG.isInfoEnabled()) {
            LOG.info("getTaskSummary: " + taskId);
        }
        return m_taskService.getTaskSummary(taskId);
    }

    public Task storeTask(final Task task) {
        if (LOG.isInfoEnabled()) {
            LOG.info("storeTask: " + task);
        }
        return m_taskService.storeTask(task);
    }
}
