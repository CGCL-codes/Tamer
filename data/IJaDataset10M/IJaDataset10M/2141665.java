package swm.enterprise.rest.jdodemo.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.jdo.support.JdoDaoSupport;
import org.springframework.stereotype.Component;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import swm.enterprise.rest.jdodemo.model.Employee;

public class EmployeeDAOJDOImpl extends JdoDaoSupport implements EmployeeDAO {

    @Override
    public Employee load(String firstName, String lastName) throws DataAccessException {
        Key key = KeyFactory.createKey(Employee.class.getSimpleName(), firstName + lastName);
        Employee e = (Employee) getJdoTemplate().getObjectById(Employee.class, key);
        return e;
    }

    @Override
    public void store(Employee employee) throws DataAccessException {
        getJdoTemplate().makePersistent(employee);
    }

    @Override
    public void delete(Employee employee) throws DataAccessException {
        if (employee == null || employee.getKey() == null) {
            throw new RuntimeException("Employee is not persistent");
        } else {
            getJdoTemplate().deletePersistent(employee);
        }
    }
}
