package jgcp.service;

import jgcp.common.TaskStatus;
import jgcp.message.Message;

/**
 * 
 * @Date 31/05/2009
 * @author Jie Zhao (288654)
 * @version 1.0
 */
public abstract class AbstractService implements CommonService {

    public TaskStatus getTaskStatus(int workerId, int taskId) {
        return null;
    }

    public void sendMessage(int workerId, jgcp.message.Message message) throws Exception {
    }

    ;

    public void sendMessage(Message message) throws Exception {
    }
}
