package com.ecs.etrade.daextn;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import com.ecs.etrade.da.Employee;
import com.ecs.etrade.da.EmployeeDAO;

/**
 * @author Alok Ranjan
 *
 */
public class EmployeeDAOExtn extends EmployeeDAO {

    private static final Log log = LogFactory.getLog(EmployeeDAOExtn.class);

    /**
	 * 
	 */
    public EmployeeDAOExtn() {
    }

    public Integer getContactDetailsId(final Employee employee) {
        try {
            List resultList = getHibernateTemplate().executeFind(new HibernateCallback() {

                public Object doInHibernate(Session session) throws HibernateException {
                    String SQL_QUERY = "select contactDetails.contactDetailsId from Employee where employeeCd = :employeeCode ";
                    Query query = session.createQuery(SQL_QUERY);
                    query.setParameter("employeeCode", employee.getEmployeeCd());
                    return query.list();
                }
            });
            if (resultList != null) {
                return (Integer) resultList.get(0);
            } else {
                return null;
            }
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
