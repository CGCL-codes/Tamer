package il.co.entrypoint.dao;

import il.co.entrypoint.model.Task;

/**
 * Interface between data source and application, used to save, delete and
 * retrieve {@link il.co.entrypoint.model.Task} objects
 *
 * @author Grinfeld Igor, Entrypoint Ltd. igorg@entrypoint.co.il
 * @version 1.0
 */
public interface TaskDAO extends ReportDAO<Task> {
}
