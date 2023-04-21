package edu.vt.eng.swat.workflow.db.base.dao;

import java.util.List;
import edu.vt.eng.swat.workflow.db.base.entity.Task;

/**
 * DAo interface for the <code>Task</code> entity class.
 * 
 * @author Dmitry Churbanov (dmitry.churbanov@gmail.com)
 *
 */
public interface TaskDao {

    Task findById(Long id);

    Task findByName(String name);

    Task save(Task task);

    List<Task> getEnabledTasks();
}
