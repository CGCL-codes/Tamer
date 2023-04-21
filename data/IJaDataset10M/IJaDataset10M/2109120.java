package swm.enterprise.rest.jdodemo.dao.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swm.enterprise.rest.jdodemo.model.Employee;
import swm.enterprise.rest.jdodemo.resources.HelloWorldResource;
import swm.enterprise.rest.jdodemo.util.PMF;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class EmployEntityTest {

    final Logger logger = LoggerFactory.getLogger(EmployEntityTest.class);

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() throws Exception {
        helper.setUp();
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public void testCreate() {
        logger.info("started testCreate");
        PersistenceManager pm = null;
        String title = "manager";
        String lastName = "taewan";
        String firstName = "kim";
        try {
            pm = PMF.get().getPersistenceManager();
            Employee e = new Employee(title, lastName, firstName, new Date());
            Key key = KeyFactory.createKey(Employee.class.getSimpleName(), e.getFirstName() + e.getLastName());
            e.setKey(key);
            pm.makePersistent(e);
            logger.info("employee 객체 생성: {}", e);
        } finally {
            pm.close();
        }
        try {
            pm = PMF.get().getPersistenceManager();
            Key key = KeyFactory.createKey(Employee.class.getSimpleName(), "taewankim");
            Employee retrieveEmp = pm.getObjectById(Employee.class, key);
            Assert.assertEquals(title, retrieveEmp.getTitle());
        } finally {
            pm.close();
        }
    }

    @Test(expected = JDOObjectNotFoundException.class)
    public void testCRUD() {
        logger.info("started testCRUD");
        PersistenceManager pm = null;
        String title = "manager";
        String lastName = "taewan";
        String firstName = "kim";
        try {
            pm = PMF.get().getPersistenceManager();
            Employee e = new Employee(title, lastName, firstName, new Date());
            Key key = KeyFactory.createKey(Employee.class.getSimpleName(), e.getFirstName() + e.getLastName());
            e.setKey(key);
            pm.makePersistent(e);
            logger.info("employee 객체 생성: {}", e);
        } finally {
            pm.close();
        }
        Date now = new Date();
        logger.info("Retrieving Employee");
        try {
            pm = PMF.get().getPersistenceManager();
            Key key = KeyFactory.createKey(Employee.class.getSimpleName(), lastName + firstName);
            Employee retrieveEmp = pm.getObjectById(Employee.class, key);
            Assert.assertEquals(title, retrieveEmp.getTitle());
            retrieveEmp.setHireDate(now);
        } finally {
            pm.close();
        }
        logger.info("Updating Employee");
        try {
            pm = PMF.get().getPersistenceManager();
            Key key = KeyFactory.createKey(Employee.class.getSimpleName(), lastName + firstName);
            Employee retrieveEmp = pm.getObjectById(Employee.class, key);
            Assert.assertEquals(now, retrieveEmp.getHireDate());
        } finally {
            pm.close();
        }
        logger.info("Deleting Employee");
        try {
            pm = PMF.get().getPersistenceManager();
            Key key = KeyFactory.createKey(Employee.class.getSimpleName(), lastName + firstName);
            Employee retrieveEmp = pm.getObjectById(Employee.class, key);
            pm.deletePersistent(retrieveEmp);
        } finally {
            pm.close();
        }
        logger.info("예외발생");
        try {
            pm = PMF.get().getPersistenceManager();
            Key key = KeyFactory.createKey(Employee.class.getSimpleName(), lastName + firstName);
            Employee retrieveEmp = pm.getObjectById(Employee.class, key);
        } finally {
            pm.close();
        }
    }

    @Test
    public void testDetached() {
        logger.info("started testDetached");
        PersistenceManager pm = null;
        String title = "manager";
        String lastName = "taewan";
        String firstName = "kim";
        Employee e = null;
        Employee detachedEmployee = null;
        try {
            pm = PMF.get().getPersistenceManager();
            e = new Employee(title, lastName, firstName, new Date());
            Key key = KeyFactory.createKey(Employee.class.getSimpleName(), e.getFirstName() + e.getLastName());
            e.setKey(key);
            pm.makePersistent(e);
            detachedEmployee = pm.detachCopy(e);
            logger.info("employee 객체 생성: {}", e);
        } finally {
            pm.close();
        }
        Assert.assertNotSame(e, detachedEmployee);
        logger.info("Employee: {}", e);
    }

    @Test
    public void testQuery() {
        List<Employee> employees = new ArrayList<Employee>();
        Employee e = new Employee("Manager", "우중", "김", new Date());
        employees.add(e);
        e = new Employee("Manager", "우중", "김", new Date());
        employees.add(e);
        e = new Employee("Manager", "태진", "진", new Date());
        employees.add(e);
        e = new Employee("Manager", "은진", "천", new Date());
        employees.add(e);
        e = new Employee("Manager", "태완", "김", new Date());
        employees.add(e);
        e = new Employee("Manager", "수열", "양", new Date());
        employees.add(e);
        e = new Employee("Manager", "동현", "조", new Date());
        employees.add(e);
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            pm.makePersistentAll(employees);
        } finally {
            pm.close();
        }
        try {
            pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(Employee.class);
            pm.makePersistentAll(employees);
            List<Employee> results = (List<Employee>) query.execute();
            Assert.assertEquals(employees.size(), results.size());
            logger.info("전체 조회리스트 사이즈 체크");
            for (Employee employee : results) {
                logger.info("Employee {}", employee);
            }
            query = pm.newQuery(Employee.class);
            query.setFilter("firstName == firstNameParam");
            query.declareParameters("String firstNameParam");
            List<Employee> results2 = (List<Employee>) query.execute("태완");
            Assert.assertEquals(1, results2.size());
            logger.info("Employee {}", results2);
        } finally {
            pm.close();
        }
    }
}
