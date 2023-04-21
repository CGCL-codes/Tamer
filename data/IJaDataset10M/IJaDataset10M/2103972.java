package il.co.entrypoint.dao.hibernate;

import il.co.entrypoint.model.*;
import il.co.entrypoint.dao.TaskDAO;
import java.util.Collection;
import java.util.Date;

/**
 * Hibernate implementation of interface between data source and application
 * for {@link il.co.entrypoint.model.Task} objects
 *
 * @author Grinfeld Igor, Entrypoint Ltd. igorg@entrypoint.co.il
 * @version 1.0
 */
public class TaskDAOImpl extends ReportDAOImpl<Task> implements TaskDAO {

    public TaskDAOImpl() {
        FIND_BY_USER_QUERY = "from Task " + FIND_BY_USER_QUERY;
        FIND_BY_DATE_USER_QUERY = "from Task " + FIND_BY_DATE_USER_QUERY;
        FIND_BY_DATE_QUERY = "from Task " + FIND_BY_DATE_QUERY;
    }

    public Collection<Task> findByPattern(int pageNum, Date startDate, Date endDate, Report pattern) {
        StringBuffer query = new StringBuffer(FIND_BY_DATE_QUERY);
        Task task = (Task) pattern;
        int duration = task.getDayDuration();
        if (duration >= 0) {
            query.append(" and entity.duration=");
            query.append(duration);
        }
        boolean part = task.isPartOfDay();
        query.append(" and entity.partOfDay=");
        query.append(part);
        Employe emp = task.getEmploye();
        if (emp != null) {
            query.append(" and entity.employe.id=");
            query.append(emp.getId());
        }
        String descr = task.getDescription();
        if (descr != null) {
            query.append(" and entity.description='");
            query.append(descr);
            query.append("' ");
        }
        return super.findByLimitedQuery(query.toString(), pageNum, startDate, endDate);
    }
}
