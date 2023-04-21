package pl.ehotelik.portal.service.spa;

import pl.ehotelik.portal.domain.spa.Task;
import pl.ehotelik.portal.exception.ServiceException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mkr
 * Date: Aug 25, 2010
 * Time: 9:21:57 PM
 * This is a representation of TaskService object.
 */
public interface TaskService {

    public Long saveTask(final Task task) throws ServiceException;

    public Task loadTask(final long id) throws ServiceException;

    public List<Task> loadAllTask() throws ServiceException;

    public void deleteTask(final Task task) throws ServiceException;
}
