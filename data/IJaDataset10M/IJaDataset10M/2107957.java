package jgcp.worker.service;

import java.net.Socket;
import jgcp.common.TaskStatus;
import jgcp.common.network.Connection;
import jgcp.message.Message;
import jgcp.service.AbstractService;
import jgcp.worker.process.TaskList;
import jgcp.worker.process.TaskQueue;

/**
 * 
 * @Date 28/05/2009
 * @author Jie Zhao (288654)
 * @version 1.0
 */
public class WorkerService extends AbstractService {

    private static WorkerService instance = null;

    public static WorkerService getInstance() {
        return instance;
    }

    public static void initInstance(Connection c) {
        if (instance == null) {
            instance = new WorkerService(c);
        }
    }

    Connection con;

    private WorkerService() {
    }

    private WorkerService(Connection c) {
        con = c;
    }

    public void cancelTask(int taskId) {
        TaskList.getInstance().cancel(taskId);
        TaskQueue.getInstance().cancelTask(taskId);
    }

    public TaskStatus getStatus(int taskId) {
        TaskStatus status = TaskStatus.UNKNOWN;
        status = TaskList.getInstance().getStatus(taskId);
        if (status == TaskStatus.UNKNOWN) {
            status = TaskQueue.getInstance().getStatus(taskId);
        }
        return status;
    }

    @Override
    public void sendMessage(Message message) throws Exception {
        con.writeData(message.toByteArray());
    }
}
