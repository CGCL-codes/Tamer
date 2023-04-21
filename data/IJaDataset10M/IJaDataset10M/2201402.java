package sf.qof;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import sf.qof.exception.ValidationException;
import sf.qof.testtools.MockConnectionData;
import sf.qof.testtools.MockConnectionFactory;

public class AdapterSqlTest extends TestCase {

    public interface AdapterQueries extends BaseQuery {

        @Query(sql = "select id {%%.id}, first_name {name-gen%%.name@1}, last_name {name-gen%%.name@2} from test")
        TestBean2 selectBeanGen() throws SQLException;

        @Query(sql = "select id {%%.id}, first_name {name-dyn%%.name@1}, last_name {name-dyn%%.name@2} from test")
        TestBean2 selectBeanDyn() throws SQLException;

        @Insert(sql = "insert into test values ({%1.id}, {name-gen%1.name@1}, {name-gen%1.name@2})")
        void insertBeanGen(TestBean2 bean) throws SQLException;

        @Insert(sql = "insert into test values ({%1.id}, {name-dyn%1.name@1}, {name-dyn%1.name@2})")
        void insertBeanDyn(TestBean2 bean) throws SQLException;

        @Query(sql = "select first_name {name-gen%%@1}, last_name {name-gen%%@2} from test")
        Name selectNameGen() throws SQLException;

        @Query(sql = "select first_name {name-gen%%*@1}, last_name {name-gen%%*@2}, dummy {%%} from test")
        Map<Name, String> selectNameGenMap() throws SQLException;

        @Query(sql = "select first_name {name-dyn%%*@1}, last_name {name-dyn%%*@2}, dummy {%%} from test")
        Map<Name, String> selectNameDynMap() throws SQLException;

        @Call(sql = "{ call proc({name-gen%1.name@1,name-gen%%@1}, {name-gen%1.name@2,name-gen%%@2}) }")
        Name callGen(TestBean2 bean) throws SQLException;

        @Call(sql = "{ call proc({name-dyn%1.name@1,name-dyn%%@1}, {name-dyn%1.name@2,name-dyn%%@2}) }")
        Name callDyn(TestBean2 bean) throws SQLException;
    }

    Connection connection;

    AdapterQueries adapterQueries;

    List<String> log;

    public void setUp() {
        QueryObjectFactory.unregisterMapper("name-gen");
        QueryObjectFactory.unregisterMapper("name-dyn");
        QueryObjectFactory.registerMapper("name-gen", new GeneratorNameAdapter());
        QueryObjectFactory.registerMapper("name-dyn", new DynamicNameAdapter());
        adapterQueries = QueryObjectFactory.createQueryObject(AdapterQueries.class);
        connection = MockConnectionFactory.getConnection();
        log = ((MockConnectionData) connection).getLog();
        adapterQueries.setConnection(connection);
        adapterQueries.setFetchSize(99);
    }

    public void tearDown() {
        QueryObjectFactory.unregisterMapper("name-gen");
        QueryObjectFactory.unregisterMapper("name-dyn");
    }

    public void testSelectBeanGen() throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        Map<String, Object> data = new HashMap<String, Object>();
        results.add(data);
        data.put("id", new Integer(1));
        data.put("first_name", "John");
        data.put("last_name", "Smith");
        ((MockConnectionData) connection).setResultSetData(results);
        TestBean2 bean = adapterQueries.selectBeanGen();
        assertNotNull(bean);
        assertEquals(1, bean.getId());
        assertEquals("John", bean.getName().getFirstName());
        assertEquals("Smith", bean.getName().getLastName());
        int i = 0;
        assertEquals(10, log.size());
        assertEquals("prepareStatement(select id , first_name , last_name from test )", log.get(i++));
        assertEquals("setFetchSize(2)", log.get(i++));
        assertEquals("executeQuery()", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getInt(id)", log.get(i++));
        assertEquals("getString(first_name)", log.get(i++));
        assertEquals("getString(last_name)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("close()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testSelectBeanDyn() throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        Map<String, Object> data = new HashMap<String, Object>();
        results.add(data);
        data.put("id", new Integer(1));
        data.put("first_name", "John");
        data.put("last_name", "Smith");
        ((MockConnectionData) connection).setResultSetData(results);
        TestBean2 bean = adapterQueries.selectBeanDyn();
        assertNotNull(bean);
        assertEquals(1, bean.getId());
        assertEquals("John", bean.getName().getFirstName());
        assertEquals("Smith", bean.getName().getLastName());
        int i = 0;
        assertEquals(10, log.size());
        assertEquals("prepareStatement(select id , first_name , last_name from test )", log.get(i++));
        assertEquals("setFetchSize(2)", log.get(i++));
        assertEquals("executeQuery()", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getInt(id)", log.get(i++));
        assertEquals("getString(first_name)", log.get(i++));
        assertEquals("getString(last_name)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("close()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testInsertBeanGen() throws SQLException {
        TestBean2 bean = new TestBean2();
        bean.setId(11);
        bean.setName(new Name("John", "Smith"));
        adapterQueries.insertBeanGen(bean);
        int i = 0;
        assertEquals(6, log.size());
        assertEquals("prepareStatement(insert into test values ( ? , ? , ? ) )", log.get(i++));
        assertEquals("setInt(1,11)", log.get(i++));
        assertEquals("setString(2,John)", log.get(i++));
        assertEquals("setString(3,Smith)", log.get(i++));
        assertEquals("executeUpdate()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testInsertBeanDyn() throws SQLException {
        TestBean2 bean = new TestBean2();
        bean.setId(11);
        bean.setName(new Name("John", "Smith"));
        adapterQueries.insertBeanDyn(bean);
        int i = 0;
        assertEquals(6, log.size());
        assertEquals("prepareStatement(insert into test values ( ? , ? , ? ) )", log.get(i++));
        assertEquals("setInt(1,11)", log.get(i++));
        assertEquals("setString(2,John)", log.get(i++));
        assertEquals("setString(3,Smith)", log.get(i++));
        assertEquals("executeUpdate()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public interface AdapterQueriesFail extends BaseQuery {

        @Query(sql = "select id {%%.id}, first_name {name-gen%%.name@1}, last_name {name-gen%%.name@2} from test")
        TestBean2 selectBeanGen() throws SQLException;
    }

    public void testCreationFails() {
        QueryObjectFactory.unregisterMapper("name-gen");
        try {
            QueryObjectFactory.createQueryObject(AdapterQueriesFail.class);
            fail("Expected exception");
        } catch (ValidationException e) {
            assertEquals("No mapping found for type name-gen or class sf.qof.Name", e.getMessage());
        }
    }

    public void testSelectGen() throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        Map<String, Object> data = new HashMap<String, Object>();
        results.add(data);
        data.put("first_name", "John");
        data.put("last_name", "Smith");
        ((MockConnectionData) connection).setResultSetData(results);
        Name name = adapterQueries.selectNameGen();
        assertNotNull(name);
        assertEquals("John", name.getFirstName());
        assertEquals("Smith", name.getLastName());
        int i = 0;
        assertEquals(9, log.size());
        assertEquals("prepareStatement(select first_name , last_name from test )", log.get(i++));
        assertEquals("setFetchSize(2)", log.get(i++));
        assertEquals("executeQuery()", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getString(first_name)", log.get(i++));
        assertEquals("getString(last_name)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("close()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testSelectNameGenMap() throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        Map<String, Object> data = new HashMap<String, Object>();
        results.add(data);
        data.put("first_name", "John");
        data.put("last_name", "Smith");
        data.put("dummy", "abc");
        data = new HashMap<String, Object>();
        results.add(data);
        data.put("first_name", "Alfred");
        data.put("last_name", "Tester");
        data.put("dummy", "xyz");
        ((MockConnectionData) connection).setResultSetData(results);
        Map<Name, String> nameMap = adapterQueries.selectNameGenMap();
        assertNotNull(nameMap);
        String dummy = nameMap.get(new Name("John", "Smith"));
        assertNotNull(dummy);
        assertEquals("abc", dummy);
        dummy = nameMap.get(new Name("Alfred", "Tester"));
        assertNotNull(dummy);
        assertEquals("xyz", dummy);
        int i = 0;
        assertEquals(14, log.size());
        assertEquals("prepareStatement(select first_name , last_name , dummy from test )", log.get(i++));
        assertEquals("setFetchSize(99)", log.get(i++));
        assertEquals("executeQuery()", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getString(dummy)", log.get(i++));
        assertEquals("getString(first_name)", log.get(i++));
        assertEquals("getString(last_name)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getString(dummy)", log.get(i++));
        assertEquals("getString(first_name)", log.get(i++));
        assertEquals("getString(last_name)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("close()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testSelectNameDynMap() throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        Map<String, Object> data = new HashMap<String, Object>();
        results.add(data);
        data.put("first_name", "John");
        data.put("last_name", "Smith");
        data.put("dummy", "abc");
        data = new HashMap<String, Object>();
        results.add(data);
        data.put("first_name", "Alfred");
        data.put("last_name", "Tester");
        data.put("dummy", "xyz");
        ((MockConnectionData) connection).setResultSetData(results);
        Map<Name, String> nameMap = adapterQueries.selectNameDynMap();
        assertNotNull(nameMap);
        String dummy = nameMap.get(new Name("John", "Smith"));
        assertNotNull(dummy);
        assertEquals("abc", dummy);
        dummy = nameMap.get(new Name("Alfred", "Tester"));
        assertNotNull(dummy);
        assertEquals("xyz", dummy);
        int i = 0;
        assertEquals(14, log.size());
        assertEquals("prepareStatement(select first_name , last_name , dummy from test )", log.get(i++));
        assertEquals("setFetchSize(99)", log.get(i++));
        assertEquals("executeQuery()", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getString(dummy)", log.get(i++));
        assertEquals("getString(first_name)", log.get(i++));
        assertEquals("getString(last_name)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getString(dummy)", log.get(i++));
        assertEquals("getString(first_name)", log.get(i++));
        assertEquals("getString(last_name)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("close()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testCallGen() throws SQLException {
        List<Object> results = new ArrayList<Object>();
        results.add("Joe");
        results.add("Tester");
        ((MockConnectionData) connection).setResultData(results);
        TestBean2 bean = new TestBean2();
        bean.setId(11);
        bean.setName(new Name("John", "Smith"));
        Name name = adapterQueries.callGen(bean);
        assertNotNull(name);
        assertEquals("Joe", name.getFirstName());
        assertEquals("Tester", name.getLastName());
        int i = 0;
        assertEquals(9, log.size());
        assertEquals("prepareCall({  call proc( ? , ? )  })", log.get(i++));
        assertEquals("setString(1,John)", log.get(i++));
        assertEquals("setString(2,Smith)", log.get(i++));
        assertEquals("registerOutParameter(1,12)", log.get(i++));
        assertEquals("registerOutParameter(2,12)", log.get(i++));
        assertEquals("execute()", log.get(i++));
        assertEquals("getString(1)", log.get(i++));
        assertEquals("getString(2)", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testCallDyn() throws SQLException {
        List<Object> results = new ArrayList<Object>();
        results.add("Joe");
        results.add("Tester");
        ((MockConnectionData) connection).setResultData(results);
        TestBean2 bean = new TestBean2();
        bean.setId(11);
        bean.setName(new Name("John", "Smith"));
        Name name = adapterQueries.callDyn(bean);
        assertNotNull(name);
        assertEquals("Joe", name.getFirstName());
        assertEquals("Tester", name.getLastName());
        int i = 0;
        assertEquals(9, log.size());
        assertEquals("prepareCall({  call proc( ? , ? )  })", log.get(i++));
        assertEquals("setString(1,John)", log.get(i++));
        assertEquals("setString(2,Smith)", log.get(i++));
        assertEquals("registerOutParameter(1,12)", log.get(i++));
        assertEquals("registerOutParameter(2,12)", log.get(i++));
        assertEquals("execute()", log.get(i++));
        assertEquals("getString(1)", log.get(i++));
        assertEquals("getString(2)", log.get(i++));
        assertEquals("close()", log.get(i++));
    }
}
