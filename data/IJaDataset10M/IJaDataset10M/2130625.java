package jacky.lanlan.song.jdbc;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import jacky.lanlan.song.TestDomain;
import jacky.lanlan.song.closure.Handler;
import jacky.lanlan.song.junitx.v4.atunit.AtUnit;
import jacky.lanlan.song.junitx.v4.atunit.Mock;
import jacky.lanlan.song.junitx.v4.atunit.InjectMock;
import jacky.lanlan.song.junitx.v4.atunit.Unit;
import jacky.lanlan.song.reflection.ReflectionUtils;

@RunWith(AtUnit.class)
@InjectMock
public class RawJDBCHelperTest {

    @Unit
    private RawJDBCHelper helper = null;

    @Mock
    private Connection con;

    @Mock
    private DataSource ds;

    @Before
    public void init() {
        helper = new RawJDBCHelper();
    }

    @Test
    public void testConnection() throws Exception {
        stub(con.isClosed()).toReturn(true);
        stub(ds.getConnection()).toReturn(con);
        helper.setDataSource(ds);
        Connection conFromHelper = helper.getConnection();
        assertNotNull(conFromHelper);
        assertSame(con, conFromHelper);
        JdbcUtils.closeConnection(conFromHelper);
        assertTrue(conFromHelper.isClosed());
        verify(con).close();
        verify(con).isClosed();
    }

    @Test
    public void testHandleProcedure() throws Exception {
        CallableStatement cs = mock(CallableStatement.class);
        stub(cs.execute()).toReturn(true);
        stub(con.prepareCall("select sysdate as now from dual")).toReturn(cs);
        stub(ds.getConnection()).toReturn(con);
        helper = new RawJDBCHelper();
        helper.setDataSource(ds);
        Map<Integer, Object> reValues = helper.handleProcedure("select sysdate as now from dual", new ProcedureHandler() {

            public void beforeExecute(CallableStatement proc) throws SQLException {
                assertNotNull("应该已经创建好语句", proc);
            }

            public ResultSet afterExecute(CallableStatement proc, Map<Integer, Object> reValues) throws SQLException {
                assertNotNull(proc);
                assertTrue("应该传入一个空Map", reValues != null && reValues.isEmpty());
                reValues.put(1, "aaa");
                reValues.put(2, 111);
                return null;
            }
        });
        assertEquals("应该装载返回值", 2, reValues.size());
        assertEquals("aaa", reValues.get(1));
        assertEquals(111, reValues.get(2));
        verify(cs).close();
        verify(con).close();
    }

    @Test
    public void testHandleDML() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        stub(ps.executeUpdate()).toReturn(3);
        stub(con.prepareStatement("dml sql statement")).toReturn(ps);
        stub(ds.getConnection()).toReturn(con);
        helper = new RawJDBCHelper();
        helper.setDataSource(ds);
        int returnValue = helper.handleDML("dml sql statement", new Handler<PreparedStatement>() {

            public void doWith(PreparedStatement stmt) {
                assertNotNull("应该已经创建好语句", stmt);
            }
        });
        assertEquals(3, returnValue);
        verify(ps).close();
        verify(con).close();
    }

    @Test
    public void testFillDomain() throws Exception {
        IMocksControl ctrl = createNiceControl();
        ctrl.checkOrder(true);
        ResultSet rs = ctrl.createMock(ResultSet.class);
        expect(rs.next()).andReturn(true);
        expect(rs.getString("carno")).andReturn("1234");
        expect(rs.getInt("installno")).andReturn(2);
        expect(rs.getString("apptype")).andReturn("私用").times(0, 1);
        expect(rs.next()).andReturn(true);
        expect(rs.getString("carno")).andReturn("5678");
        expect(rs.getTimestamp("111")).andReturn(new Timestamp(2007));
        replay(rs);
        Map<String, String> map = new HashMap<String, String>();
        map.put("installNum", "installno");
        map.put("emitTime", "111");
        map.put("apptype", null);
        List<TestDomain> list = helper.fillDomain(TestDomain.class, rs, map);
        org.easymock.EasyMock.verify(rs);
        assertTrue("应该有2条记录", list != null && list.size() == 2);
        assertEquals("1234", list.get(0).getCarNo());
        assertEquals(2, list.get(0).getInstallNum());
        assertEquals(new Date(2007), list.get(1).getEmitTime());
        assertNull(list.get(0).getAddr());
        assertNull(list.get(1).getAddr());
    }

    @Test
    public void testReflectDataInRawJDBCHelper() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString("carno")).thenReturn("1234");
        when(rs.getTimestamp("emittime")).thenReturn(new Timestamp(2007));
        when(rs.getInt("installnum")).thenReturn(3);
        Method reflectData = ReflectionUtils.findMethod(RawJDBCHelper.class, "reflectData", Class.class, StringBuilder.class, ResultSet.class, Map.class);
        reflectData.setAccessible(true);
        RawJDBCHelper helper = new RawJDBCHelper();
        TestDomain c = (TestDomain) reflectData.invoke(helper, TestDomain.class, new StringBuilder(), rs, null);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("carNo", "1234");
        map.put("emitTime", new Date(2007));
        map.put("installNum", 3);
        assertEquals("1234", c.getCarNo());
        assertEquals(new Date(2007), c.getEmitTime());
        assertEquals(3, c.getInstallNum());
    }

    @After
    public void onTearDown() throws Exception {
        helper = null;
        con = null;
        ds = null;
    }
}
