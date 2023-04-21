package net.sourceforge.gda.test;

import junit.framework.TestCase;
import java.util.*;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import net.sourceforge.gda.provider.GenericDataObjectProvider;
import net.sourceforge.gda.provider.GenericDataObjectProviderFactory;
import net.sourceforge.gda.query.GenericSelectQuery;
import net.sourceforge.gda.query.GenericQueryFactory;
import net.sourceforge.gda.query.GenericJDBCSelectQuery;
import net.sourceforge.gda.GenericDataObjectProviderConstants;
import net.sourceforge.gda.pool.GenericDatabaseInfo;
import net.sourceforge.gda.pool.GenericPoolManager;
import net.sourceforge.gda.test.data.IDepartment;
import net.sourceforge.gda.test.data.IEmployee;
import net.sourceforge.gda.utils.sql.DatabaseUtils;
import net.sourceforge.gda.exceptions.GenericDataObjectException;
import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: hasani
 * Date: May 23, 2007
 * Time: 1:36:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractProviderTestCase extends TestCase {

    /**
     *
     * Tests query framework.
     */
    public void testQueryEmployee(GenericDataObjectProvider provider) {
        try {
            IDepartment department = provider.create(IDepartment.class);
            department.setName("Fake Department.");
            assertNotNull(department);
            IEmployee employee = provider.create(IEmployee.class);
            assertNotNull(employee);
            employee.setEmail("fake.employee2@fakeemployees.com");
            employee.setFirstName("Fake First Name2");
            employee.setLastName("Fake Last Name2");
            department.addEmployee(employee);
            provider.save(department);
        } catch (net.sourceforge.gda.exceptions.GenericDataObjectException e) {
            fail(e.getMessage());
        }
        GenericSelectQuery selectQuery = GenericQueryFactory.createGenericSelectQuery();
        selectQuery.addParameter("email", net.sourceforge.gda.query.GenericQueryOperation.EQUAL, "fake.employee2@fakeemployees.com", net.sourceforge.gda.query.GenericParameterOperation.AND);
        try {
            Collection<IEmployee> employeeCollection = provider.executeQuery(selectQuery, IEmployee.class);
            assertFalse(employeeCollection.isEmpty());
        } catch (net.sourceforge.gda.exceptions.GenericDataObjectException e) {
            fail(e.getMessage());
        }
    }

    /**
     *
     * Tests "select *" functionality
     */
    public void testSelectAll(GenericDataObjectProvider provider) {
        try {
            IDepartment department = provider.create(IDepartment.class);
            department.setName("Fake Department2");
            assertNotNull(department);
            IEmployee employee = provider.create(IEmployee.class);
            assertNotNull(employee);
            employee.setEmail("fake.employee3@fakeemployees.com");
            employee.setFirstName("Fake First Name3");
            employee.setLastName("Fake Last Name3");
            IEmployee employee2 = provider.create(IEmployee.class);
            employee2.setEmail("fake.employee4@fakeemployees.com");
            employee2.setFirstName("Fake First Name4");
            employee2.setLastName("Fake Last Name4");
            department.addEmployee(employee);
            department.addEmployee(employee2);
            provider.save(department);
            Collection<IEmployee> employeeList = provider.executeSelectAllQuery(IEmployee.class);
            assertTrue(employeeList.size() > 1);
        } catch (net.sourceforge.gda.exceptions.GenericDataObjectException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Tests the sbility to copy objects from one provider into another.
     */
    public void testCopyingBetweenProviders(String providerID1, String providerID2) {
        String jdbcDriver = "org.hsqldb.jdbcDriver";
        String jdbcURL = "jdbc:hsqldb:mem:database1";
        String jdbcUser = "sa";
        String jdbcPassword = "";
        int minimumConnections = 1;
        int maximumConnections = 1;
        GenericDatabaseInfo databaseInfo = new GenericDatabaseInfo(jdbcURL, jdbcUser, jdbcPassword, jdbcDriver, minimumConnections, maximumConnections);
        DataSource dataSource1 = null;
        try {
            dataSource1 = new GenericPoolManager(databaseInfo);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        String jdbcURL2 = "jdbc:hsqldb:mem:database2";
        GenericDatabaseInfo databaseInfo2 = new GenericDatabaseInfo(jdbcURL2, jdbcUser, jdbcPassword, jdbcDriver, minimumConnections, maximumConnections);
        DataSource dataSource2 = null;
        try {
            dataSource2 = new GenericPoolManager(databaseInfo2);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        Properties providerProperties1 = new Properties();
        providerProperties1.put(GenericDataObjectProviderConstants.DEFAULT_DATASOURCE_ON_STARTUP, dataSource1);
        providerProperties1.put(GenericDataObjectProviderConstants.AUTO_CREATE_TABLES, true);
        GenericDataObjectProvider daoProvider = GenericDataObjectProviderFactory.findProvider(providerID1, providerProperties1, true);
        Properties providerProperties2 = new Properties();
        providerProperties2.put(GenericDataObjectProviderConstants.DEFAULT_DATASOURCE_ON_STARTUP, dataSource2);
        providerProperties2.put(GenericDataObjectProviderConstants.AUTO_CREATE_TABLES, true);
        GenericDataObjectProvider daoProvider2 = GenericDataObjectProviderFactory.findProvider(providerID2, providerProperties2, true);
        try {
            IDepartment department = daoProvider.create(IDepartment.class);
            department.setName("Fake Department2");
            daoProvider.save(department);
            IDepartment departmentCopy = daoProvider2.copy(department);
            departmentCopy.setName("Fake Department3");
            daoProvider2.save(departmentCopy);
            Collection<IDepartment> copyDepartmentCollection = daoProvider2.executeSelectAllQuery(IDepartment.class);
            Collection<IDepartment> departmentCollection = daoProvider.executeSelectAllQuery(IDepartment.class);
            assertEquals(1, copyDepartmentCollection.size());
        } catch (GenericDataObjectException e) {
            fail(e.getMessage());
        } finally {
            try {
                daoProvider.shutdown();
                daoProvider2.shutdown();
            } catch (GenericDataObjectException e) {
                fail(e.getMessage());
            }
        }
    }

    /**
     * Tests the sbility to copy objects from one provider into another.
     */
    public void testCopyingSubgraphBetweenProviders(String providerID1, String providerID2) {
        String jdbcDriver = "org.hsqldb.jdbcDriver";
        String jdbcURL = "jdbc:hsqldb:mem:database10";
        String jdbcUser = "sa";
        String jdbcPassword = "";
        int minimumConnections = 1;
        int maximumConnections = 1;
        GenericDatabaseInfo databaseInfo = new GenericDatabaseInfo(jdbcURL, jdbcUser, jdbcPassword, jdbcDriver, minimumConnections, maximumConnections);
        DataSource dataSource1 = null;
        try {
            dataSource1 = new GenericPoolManager(databaseInfo);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        String jdbcURL2 = "jdbc:hsqldb:mem:database11";
        GenericDatabaseInfo databaseInfo2 = new GenericDatabaseInfo(jdbcURL2, jdbcUser, jdbcPassword, jdbcDriver, minimumConnections, maximumConnections);
        DataSource dataSource2 = null;
        try {
            dataSource2 = new GenericPoolManager(databaseInfo2);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        Properties providerProperties1 = new Properties();
        providerProperties1.put(GenericDataObjectProviderConstants.DEFAULT_DATASOURCE_ON_STARTUP, dataSource1);
        providerProperties1.put(GenericDataObjectProviderConstants.AUTO_CREATE_TABLES, true);
        GenericDataObjectProvider daoProvider = GenericDataObjectProviderFactory.findProvider(providerID1, providerProperties1, true);
        Properties providerProperties2 = new Properties();
        providerProperties2.put(GenericDataObjectProviderConstants.DEFAULT_DATASOURCE_ON_STARTUP, dataSource2);
        providerProperties2.put(GenericDataObjectProviderConstants.AUTO_CREATE_TABLES, true);
        GenericDataObjectProvider daoProvider2 = GenericDataObjectProviderFactory.findProvider(providerID2, providerProperties2, true);
        try {
            IEmployee employee1 = daoProvider.create(IEmployee.class);
            employee1.setFirstName("Employee FirstName1");
            employee1.setLastName("Employee LastName1");
            employee1.setEmail("employee1@employee.com");
            IDepartment department = daoProvider.create(IDepartment.class);
            department.setName("Fake Department2");
            employee1.setDepartment(department);
            daoProvider.save(employee1);
            IEmployee employeeCopy = daoProvider2.copy(employee1, true);
            daoProvider2.save(employeeCopy);
            Collection<IDepartment> copyDepartmentCollection = daoProvider2.executeSelectAllQuery(IDepartment.class);
            Collection<IDepartment> departmentCollection = daoProvider.executeSelectAllQuery(IDepartment.class);
            assertEquals(1, copyDepartmentCollection.size());
        } catch (GenericDataObjectException e) {
            fail(e.getMessage());
        } finally {
            try {
                daoProvider.shutdown();
                daoProvider2.shutdown();
            } catch (GenericDataObjectException e) {
                fail(e.getMessage());
            }
        }
    }

    /**
     *
     * Tests copy object with the same provider.
     */
    public void testCopyingWithSameProvider(String providerID) {
        String jdbcDriver = "org.hsqldb.jdbcDriver";
        String jdbcURL = "jdbc:hsqldb:mem:database3";
        String jdbcUser = "sa";
        String jdbcPassword = "";
        int minimumConnections = 1;
        int maximumConnections = 1;
        GenericDatabaseInfo databaseInfo = new GenericDatabaseInfo(jdbcURL, jdbcUser, jdbcPassword, jdbcDriver, minimumConnections, maximumConnections);
        DataSource dataSource1 = null;
        try {
            dataSource1 = new GenericPoolManager(databaseInfo);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        Properties providerProperties1 = new Properties();
        providerProperties1.put(GenericDataObjectProviderConstants.AUTO_CREATE_TABLES, true);
        providerProperties1.put(GenericDataObjectProviderConstants.DEFAULT_DATASOURCE_ON_STARTUP, dataSource1);
        GenericDataObjectProvider daoProvider = GenericDataObjectProviderFactory.findProvider(providerID, providerProperties1, true);
        try {
            IDepartment department = daoProvider.create(IDepartment.class);
            department.setName("Fake Department9");
            daoProvider.save(department);
            IDepartment departmentCopy = daoProvider.copy(department);
            assertFalse(department.equals(departmentCopy));
            departmentCopy.setName("Fake Department10");
            daoProvider.save(departmentCopy);
            Collection<IDepartment> departmentCollection = daoProvider.executeSelectAllQuery(IDepartment.class);
            assertEquals(2, departmentCollection.size());
        } catch (GenericDataObjectException e) {
            fail(e.getMessage());
        }
    }

    /**
     *
     * Copies an Object that the provider doesn't know about. i.e. anonymous class.
     */
    public void testCopyingFromFakeObjectIntoRealObject(String providerID) {
        String jdbcDriver = "org.hsqldb.jdbcDriver";
        String jdbcURL = "jdbc:hsqldb:mem:database4";
        String jdbcUser = "sa";
        String jdbcPassword = "";
        int minimumConnections = 1;
        int maximumConnections = 1;
        GenericDatabaseInfo databaseInfo = new GenericDatabaseInfo(jdbcURL, jdbcUser, jdbcPassword, jdbcDriver, minimumConnections, maximumConnections);
        DataSource dataSource1 = null;
        try {
            dataSource1 = new GenericPoolManager(databaseInfo);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        Properties providerProperties1 = new Properties();
        providerProperties1.put(GenericDataObjectProviderConstants.AUTO_CREATE_TABLES, true);
        providerProperties1.put(GenericDataObjectProviderConstants.DEFAULT_DATASOURCE_ON_STARTUP, dataSource1);
        GenericDataObjectProvider daoProvider = GenericDataObjectProviderFactory.findProvider(providerID, providerProperties1, true);
        try {
            IDepartment fakeDepartment = new IDepartment() {

                public String getName() {
                    return "Fake Department";
                }

                public void setName(String name) {
                }

                public void addEmployee(IEmployee employee) {
                }

                public void removeEmployee(IEmployee employee) {
                }

                public List<IEmployee> employeeList() {
                    return null;
                }

                public boolean isModified() {
                    return false;
                }
            };
            IDepartment departmentCopy = daoProvider.copy(fakeDepartment);
            departmentCopy.setName("Department1");
            daoProvider.save(departmentCopy);
            Collection<IDepartment> departmentCollection = daoProvider.executeSelectAllQuery(IDepartment.class);
            assertEquals(1, departmentCollection.size());
        } catch (GenericDataObjectException e) {
            fail(e.getMessage());
        }
    }

    /**
     *
     * Attempts to save an anonymous class with a actual provider.
     */
    public void testSaveObjectWithWrongProvider(String providerId) {
        String jdbcDriver = "org.hsqldb.jdbcDriver";
        String jdbcURL = "jdbc:hsqldb:mem:database5";
        String jdbcUser = "sa";
        String jdbcPassword = "";
        int minimumConnections = 1;
        int maximumConnections = 1;
        GenericDatabaseInfo databaseInfo = new GenericDatabaseInfo(jdbcURL, jdbcUser, jdbcPassword, jdbcDriver, minimumConnections, maximumConnections);
        DataSource dataSource1 = null;
        try {
            dataSource1 = new GenericPoolManager(databaseInfo);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        Properties providerProperties1 = new Properties();
        providerProperties1.put(GenericDataObjectProviderConstants.AUTO_CREATE_TABLES, true);
        providerProperties1.put(GenericDataObjectProviderConstants.DEFAULT_DATASOURCE_ON_STARTUP, dataSource1);
        GenericDataObjectProvider daoProvider = GenericDataObjectProviderFactory.findProvider(providerId, providerProperties1, true);
        try {
            IDepartment fakeDepartment = new IDepartment() {

                public String getName() {
                    return "Fake Department";
                }

                public void setName(String name) {
                }

                public void addEmployee(IEmployee employee) {
                }

                public void removeEmployee(IEmployee employee) {
                }

                public List<IEmployee> employeeList() {
                    return null;
                }

                public boolean isModified() {
                    return false;
                }
            };
            daoProvider.save(fakeDepartment);
            fail("We can't save a fake object.");
        } catch (GenericDataObjectException e) {
        }
    }

    /**
     *
     * Tests JDBC select functionality
     */
    public void testJDBCQuery(String providerID) {
        String jdbcDriver = "org.hsqldb.jdbcDriver";
        String jdbcURL = "jdbc:hsqldb:mem:database6";
        String jdbcUser = "sa";
        String jdbcPassword = "";
        int minimumConnections = 1;
        int maximumConnections = 1;
        GenericDatabaseInfo databaseInfo = new GenericDatabaseInfo(jdbcURL, jdbcUser, jdbcPassword, jdbcDriver, minimumConnections, maximumConnections);
        DataSource dataSource1 = null;
        try {
            dataSource1 = new GenericPoolManager(databaseInfo);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        Properties providerProperties1 = new Properties();
        providerProperties1.put(GenericDataObjectProviderConstants.AUTO_CREATE_TABLES, true);
        providerProperties1.put(GenericDataObjectProviderConstants.DEFAULT_DATASOURCE_ON_STARTUP, dataSource1);
        GenericDataObjectProvider cayenneProvider = GenericDataObjectProviderFactory.findProvider(providerID, providerProperties1, true);
        try {
            IDepartment department = cayenneProvider.create(IDepartment.class);
            department.setName("Fake Department.");
            assertNotNull(department);
            IEmployee employee = cayenneProvider.create(IEmployee.class);
            assertNotNull(employee);
            employee.setEmail("fake.employee2@fakeemployees.com");
            employee.setFirstName("Fake First Name2");
            employee.setLastName("Fake Last Name2");
            department.addEmployee(employee);
            cayenneProvider.save(department);
        } catch (GenericDataObjectException e) {
            fail(e.getMessage());
        }
        GenericJDBCSelectQuery selectQuery = GenericQueryFactory.createGenericJDBCSelectQuery("select name from Department where name = 'Fake Department.'");
        try {
            ResultSet rs = cayenneProvider.executeJDBCSelectQuery(selectQuery);
            List<String> departmentNameCollection = new ArrayList<String>();
            while (rs.next()) {
                departmentNameCollection.add(rs.getString(1));
            }
            assertFalse(departmentNameCollection.isEmpty());
            rs.close();
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            try {
                cayenneProvider.shutdown();
            } catch (GenericDataObjectException e) {
                fail(e.getMessage());
            }
        }
    }

    public void testGetAllTableNamesFromConnection(String providerID) {
        String jdbcDriver = "org.hsqldb.jdbcDriver";
        String jdbcURL = "jdbc:hsqldb:mem:database7";
        String jdbcUser = "sa";
        String jdbcPassword = "";
        int minimumConnections = 1;
        int maximumConnections = 1;
        GenericDatabaseInfo databaseInfo = new GenericDatabaseInfo(jdbcURL, jdbcUser, jdbcPassword, jdbcDriver, minimumConnections, maximumConnections);
        DataSource dataSource1 = null;
        try {
            dataSource1 = new GenericPoolManager(databaseInfo);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        Properties providerProperties1 = new Properties();
        providerProperties1.put(GenericDataObjectProviderConstants.AUTO_CREATE_TABLES, true);
        providerProperties1.put(GenericDataObjectProviderConstants.DEFAULT_DATASOURCE_ON_STARTUP, dataSource1);
        GenericDataObjectProvider cayenneProvider = GenericDataObjectProviderFactory.findProvider(providerID, providerProperties1, true);
        try {
            Connection connection = cayenneProvider.getDefaultDataSource().getConnection();
            Collection<String> databaseUpperCaseTableNameCollection = DatabaseUtils.getAllUpperCaseTableNamesFromConnection(connection);
            String[] tableNameArray = { "Department".toUpperCase(), "Employee".toUpperCase() };
            assertTrue(databaseUpperCaseTableNameCollection.containsAll(Arrays.asList(tableNameArray)));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testMessaging(String providerID) {
        String jdbcDriver = "org.hsqldb.jdbcDriver";
        String jdbcURL = "jdbc:hsqldb:mem:database15";
        String jdbcUser = "sa";
        String jdbcPassword = "";
        int minimumConnections = 1;
        int maximumConnections = 1;
        GenericDatabaseInfo databaseInfo = new GenericDatabaseInfo(jdbcURL, jdbcUser, jdbcPassword, jdbcDriver, minimumConnections, maximumConnections);
        DataSource dataSource1 = null;
        try {
            dataSource1 = new GenericPoolManager(databaseInfo);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        Properties providerProperties1 = new Properties();
        providerProperties1.put(GenericDataObjectProviderConstants.AUTO_CREATE_TABLES, true);
        providerProperties1.put(GenericDataObjectProviderConstants.DEFAULT_DATASOURCE_ON_STARTUP, dataSource1);
        GenericDataObjectProvider daoProvider = GenericDataObjectProviderFactory.findProvider(providerID, providerProperties1, true);
        try {
            IDepartment department = daoProvider.create(IDepartment.class);
            department.setName("Fake Department9");
            IEmployee employee = daoProvider.create(IEmployee.class);
            assertNotNull(employee);
            employee.setEmail("fake.employee2@fakeemployees.com");
            employee.setFirstName("Fake First Name2");
            employee.setLastName("Fake Last Name2");
            department.addEmployee(employee);
            daoProvider.save(department);
            Collection<IEmployee> employeeCollection = department.employeeList();
            assertEquals(1, employeeCollection.size());
        } catch (GenericDataObjectException e) {
            fail(e.getMessage());
        }
    }

    public void testMappings(String providerID) {
        GenericDataObjectProvider cayenneProvider = GenericDataObjectProviderFactory.findProvider(providerID, true);
        try {
            String tableName = cayenneProvider.getDatabaseMappingForJavaClass(IDepartment.class);
            assertNotNull(tableName);
            String columnName = cayenneProvider.getDatabaseColumnMappingForJavaClassAndProperty(IDepartment.class, "name");
            assertNotNull(columnName);
            int columnLength = DatabaseUtils.getColumnLength(tableName, columnName, cayenneProvider.getDefaultDataSource().getConnection());
            assertTrue(columnLength > 0);
        } catch (GenericDataObjectException e) {
            fail(e.getMessage());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
}
