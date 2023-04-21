package at.riemers.zero.base.dao;

import at.riemers.zero.base.model.FunctionLog;
import at.riemers.zero.base.model.UserGroup;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Expression;

/**
 *
 * @author tobias
 */
public class FunctionLogHBDao extends GenericHibernateDAO<FunctionLog, Long> implements FunctionLogDao {

    private static final Logger log = Logger.getLogger(FunctionLogHBDao.class);

    public FunctionLogHBDao() {
    }

    public List<FunctionLog> getLogs(int year, String function, String user, String userGroup) {
        List<FunctionLog> logs = getSession().createCriteria(FunctionLog.class).add(Expression.ilike("function", "%" + function + "%")).createCriteria("user").add(Expression.ilike("username", "%" + user + "%")).list();
        return logs;
    }
}
