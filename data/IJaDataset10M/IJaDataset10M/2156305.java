package net.sf.swarmnet.common.message;

import java.util.Properties;

/**
 * @author janschroter
 *
 */
public class TaskFinishedMessage extends BaseNodeMessage {

    public TaskFinishedMessage() {
        super(MessageType.TASK_FINISHED);
    }

    /**
   * Constructor
   *
   * @param type
   * @param properties
   */
    public TaskFinishedMessage(Properties properties) {
        super(MessageType.TASK_FINISHED, properties);
    }

    public TaskDescriptor getTask() {
        return new TaskDescriptor(Long.parseLong(mProperties.getProperty(TaskDescriptor.ID)), mProperties);
    }
}
