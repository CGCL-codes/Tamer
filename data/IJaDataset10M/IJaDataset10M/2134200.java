package sf.qof;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import sf.qof.testtools.MockConnectionData;
import sf.qof.testtools.MockConnectionFactory;

public class InClauseBeanTest extends TestCase {

    public interface Queries extends BaseQuery {

        @Query(sql = "select value {%%} from test where x in ({%1.id}) and y = {%2.id}")
        List<String> selectBean1(TestBean[] x, TestBean y) throws SQLException;

        @Query(sql = "select value {%%} from test where x in ({%1.parent.id}) and y = {%2.parent.id}")
        List<String> selectBean2(TestBean[] x, TestBean y) throws SQLException;

        @Query(sql = "select value {%%} from test where x in ({%1.boxedId}) and y = {%2.boxedId}")
        List<String> selectBean3(TestBean[] x, TestBean y) throws SQLException;

        @Query(sql = "select value {%%} from test where x in ({%1.parent.boxedId}) and y = {%2.parent.boxedId}")
        List<String> selectBean4(TestBean[] x, TestBean y) throws SQLException;

        @Delete(sql = "delete from test where x in ({%1.id})")
        void deleteBean1(TestBean[] x) throws SQLException;

        @Delete(sql = "delete from test where x in ({%1.parent.id})")
        void deleteBean2(TestBean[] x) throws SQLException;

        @Delete(sql = "delete from test where x in ({%1.boxedId})")
        void deleteBean3(TestBean[] x) throws SQLException;

        @Delete(sql = "delete from test where x in ({%1.parent.boxedId})")
        void deleteBean4(TestBean[] x) throws SQLException;
    }

    private Connection connection;

    private Queries selectQueries;

    private List<String> log;

    public void setUp() {
        selectQueries = QueryObjectFactory.createQueryObject(Queries.class);
        connection = MockConnectionFactory.getConnection();
        log = ((MockConnectionData) connection).getLog();
        selectQueries.setConnection(connection);
        selectQueries.setFetchSize(99);
    }

    public void testSelectBean1() throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        Map<String, Object> data = new HashMap<String, Object>();
        results.add(data);
        data.put("value", "A");
        data = new HashMap<String, Object>();
        results.add(data);
        data.put("value", "B");
        ((MockConnectionData) connection).setResultSetData(results);
        TestBean bean1 = new TestBean();
        bean1.setId(11);
        TestBean bean2 = new TestBean();
        bean2.setId(22);
        TestBean bean3 = new TestBean();
        bean3.setId(33);
        List<String> list = selectQueries.selectBean1(new TestBean[] { bean1, bean2 }, bean3);
        assertEquals(2, list.size());
        assertEquals(13, log.size());
        int i = 0;
        assertEquals("prepareStatement(select value from test where x in ( ?,? ) and y = ? )", log.get(i++));
        assertEquals("setFetchSize(99)", log.get(i++));
        assertEquals("setInt(1,11)", log.get(i++));
        assertEquals("setInt(2,22)", log.get(i++));
        assertEquals("setInt(3,33)", log.get(i++));
        assertEquals("executeQuery()", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getString(value)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getString(value)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("close()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testSelectBean2() throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        Map<String, Object> data = new HashMap<String, Object>();
        results.add(data);
        data.put("value", "A");
        data = new HashMap<String, Object>();
        results.add(data);
        data.put("value", "B");
        ((MockConnectionData) connection).setResultSetData(results);
        TestBean parent = new TestBean();
        parent.setId(55);
        TestBean bean1 = new TestBean();
        bean1.setParent(parent);
        TestBean bean2 = new TestBean();
        bean2.setParent(parent);
        TestBean bean3 = new TestBean();
        bean3.setParent(parent);
        List<String> list = selectQueries.selectBean2(new TestBean[] { bean1, bean2 }, bean3);
        assertEquals(2, list.size());
        assertEquals(13, log.size());
        int i = 0;
        assertEquals("prepareStatement(select value from test where x in ( ?,? ) and y = ? )", log.get(i++));
        assertEquals("setFetchSize(99)", log.get(i++));
        assertEquals("setInt(1,55)", log.get(i++));
        assertEquals("setInt(2,55)", log.get(i++));
        assertEquals("setInt(3,55)", log.get(i++));
        assertEquals("executeQuery()", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getString(value)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getString(value)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("close()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testSelectBean3() throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        Map<String, Object> data = new HashMap<String, Object>();
        results.add(data);
        data.put("value", "A");
        data = new HashMap<String, Object>();
        results.add(data);
        data.put("value", "B");
        ((MockConnectionData) connection).setResultSetData(results);
        TestBean bean1 = new TestBean();
        bean1.setBoxedId(11);
        TestBean bean2 = new TestBean();
        bean2.setBoxedId(22);
        TestBean bean3 = new TestBean();
        bean3.setBoxedId(33);
        List<String> list = selectQueries.selectBean3(new TestBean[] { bean1, bean2 }, bean3);
        assertEquals(2, list.size());
        assertEquals(13, log.size());
        int i = 0;
        assertEquals("prepareStatement(select value from test where x in ( ?,? ) and y = ? )", log.get(i++));
        assertEquals("setFetchSize(99)", log.get(i++));
        assertEquals("setInt(1,11)", log.get(i++));
        assertEquals("setInt(2,22)", log.get(i++));
        assertEquals("setInt(3,33)", log.get(i++));
        assertEquals("executeQuery()", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getString(value)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getString(value)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("close()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testSelectBean4() throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        Map<String, Object> data = new HashMap<String, Object>();
        results.add(data);
        data.put("value", "A");
        data = new HashMap<String, Object>();
        results.add(data);
        data.put("value", "B");
        ((MockConnectionData) connection).setResultSetData(results);
        TestBean parent = new TestBean();
        parent.setBoxedId(55);
        TestBean bean1 = new TestBean();
        bean1.setParent(parent);
        TestBean bean2 = new TestBean();
        bean2.setParent(parent);
        TestBean bean3 = new TestBean();
        bean3.setParent(parent);
        List<String> list = selectQueries.selectBean4(new TestBean[] { bean1, bean2 }, bean3);
        assertEquals(2, list.size());
        assertEquals(13, log.size());
        int i = 0;
        assertEquals("prepareStatement(select value from test where x in ( ?,? ) and y = ? )", log.get(i++));
        assertEquals("setFetchSize(99)", log.get(i++));
        assertEquals("setInt(1,55)", log.get(i++));
        assertEquals("setInt(2,55)", log.get(i++));
        assertEquals("setInt(3,55)", log.get(i++));
        assertEquals("executeQuery()", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getString(value)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("getString(value)", log.get(i++));
        assertEquals("next()", log.get(i++));
        assertEquals("close()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testDeleteBean1() throws SQLException {
        TestBean bean1 = new TestBean();
        bean1.setId(11);
        TestBean bean2 = new TestBean();
        bean2.setId(22);
        selectQueries.deleteBean1(new TestBean[] { bean1, bean2 });
        assertEquals(5, log.size());
        int i = 0;
        assertEquals("prepareStatement(delete from test where x in ( ?,? ) )", log.get(i++));
        assertEquals("setInt(1,11)", log.get(i++));
        assertEquals("setInt(2,22)", log.get(i++));
        assertEquals("executeUpdate()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testDeleteBean2() throws SQLException {
        TestBean parent = new TestBean();
        parent.setId(55);
        TestBean bean1 = new TestBean();
        bean1.setParent(parent);
        TestBean bean2 = new TestBean();
        bean2.setParent(parent);
        selectQueries.deleteBean2(new TestBean[] { bean1, bean2 });
        assertEquals(5, log.size());
        int i = 0;
        assertEquals("prepareStatement(delete from test where x in ( ?,? ) )", log.get(i++));
        assertEquals("setInt(1,55)", log.get(i++));
        assertEquals("setInt(2,55)", log.get(i++));
        assertEquals("executeUpdate()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testDeleteBean3() throws SQLException {
        TestBean bean1 = new TestBean();
        bean1.setBoxedId(11);
        TestBean bean2 = new TestBean();
        bean2.setBoxedId(22);
        selectQueries.deleteBean3(new TestBean[] { bean1, bean2 });
        assertEquals(5, log.size());
        int i = 0;
        assertEquals("prepareStatement(delete from test where x in ( ?,? ) )", log.get(i++));
        assertEquals("setInt(1,11)", log.get(i++));
        assertEquals("setInt(2,22)", log.get(i++));
        assertEquals("executeUpdate()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }

    public void testDeleteBean4() throws SQLException {
        TestBean parent = new TestBean();
        parent.setBoxedId(55);
        TestBean bean1 = new TestBean();
        bean1.setParent(parent);
        TestBean bean2 = new TestBean();
        bean2.setParent(parent);
        selectQueries.deleteBean4(new TestBean[] { bean1, bean2 });
        assertEquals(5, log.size());
        int i = 0;
        assertEquals("prepareStatement(delete from test where x in ( ?,? ) )", log.get(i++));
        assertEquals("setInt(1,55)", log.get(i++));
        assertEquals("setInt(2,55)", log.get(i++));
        assertEquals("executeUpdate()", log.get(i++));
        assertEquals("close()", log.get(i++));
    }
}
