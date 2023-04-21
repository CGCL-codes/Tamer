package org.netbeans.cubeon.tasks.spi.query;

import org.netbeans.cubeon.tasks.spi.task.TaskElement;
import org.netbeans.cubeon.tasks.spi.repository.TaskRepository;
import org.netbeans.cubeon.tasks.spi.*;
import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author Anuradha G
 */
public interface TaskQuery {

    /**
     *
     * @return
     */
    String getId();

    /**
     *
     * @return
     */
    String getName();

    /**
     *
     * @return
     */
    String getDescription();

    /**
     *
     * @return
     */
    TaskRepository getTaskRepository();

    /**
     *
     * @return
     */
    Lookup getLookup();

    /**
     *
     */
    void synchronize();

    /**
     *
     * @return
     */
    List<TaskElement> getTaskElements();

    Notifier<TaskQueryEventAdapter> getNotifier();
}
