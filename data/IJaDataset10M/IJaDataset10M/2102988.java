package org.jazzteam.bpe.dao.employee;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jazzteam.bpe.dao.GenericDAO;
import org.jazzteam.bpe.model.employee.Employee;
import org.jazzteam.bpe.model.employee.PersonalInfo;
import org.jazzteam.bpe.util.Log4jUtil;

/**
 * This class realize storage/access operations for employee.
 * 
 * @author skars
 * @version $Rev: $
 */
public class EmployeeDAO extends GenericDAO<Employee> implements IEmployeeDAO {

    /** Events logger. */
    private static final Logger logger = Logger.getLogger(EmployeeDAO.class);

    /** Name of query for getting employee by personal information. */
    private static final String QEURY_NAME_GET_BY_PERSONAL_INFO = "Employee.getByPersonalInfo";

    /** Name of employee name parameter in named query. */
    private static final String QUERY_PARAMETER_NAME = "n";

    /** Name of employee second name parameter in named query. */
    private static final String QUERY_PARAMETER_SECOND_NAME = "sn";

    /**
	 * Constructs employee DAO.
	 */
    public EmployeeDAO() {
        super(Employee.class);
    }

    @Override
    public Employee get(PersonalInfo personalInfo) throws IllegalArgumentException {
        if (personalInfo == null) {
            throw new IllegalArgumentException("PersonalInfo is null.");
        }
        Employee employee = null;
        try {
            Session session = hibernateUtil.getSession();
            Query query = session.getNamedQuery(QEURY_NAME_GET_BY_PERSONAL_INFO);
            query.setParameter(QUERY_PARAMETER_NAME, personalInfo.getName());
            query.setParameter(QUERY_PARAMETER_SECOND_NAME, personalInfo.getSecondName());
            employee = (Employee) query.uniqueResult();
        } catch (HibernateException e) {
            Log4jUtil.debug(logger, "Can't get employee by personal info " + personalInfo, e);
        }
        return employee;
    }
}
