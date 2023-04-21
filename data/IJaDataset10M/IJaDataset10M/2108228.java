package net.sourceforge.xconf.toolbox.hibernate3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import org.joda.time.LocalTime;
import net.sourceforge.xconf.toolbox.mockcontrol.MockTestCase;

public class LocalTimeTypeTest extends MockTestCase {

    private final String[] names = { "test" };

    private LocalTimeType type;

    private PreparedStatement stmt;

    private ResultSet result;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        type = new LocalTimeType();
        stmt = (PreparedStatement) createMock(PreparedStatement.class);
        result = (ResultSet) createMock(ResultSet.class);
    }

    public void testNullSafeGetNull() throws Exception {
        control(result).expectAndReturn(result.getString("test"), null);
        replay();
        assertNull(type.nullSafeGet(result, names, null));
        verify();
    }

    public void testNullSafeGetValid() throws Exception {
        LocalTime time = new LocalTime(10, 45);
        control(result).expectAndReturn(result.getString("test"), "10:45");
        replay();
        assertEquals(time, type.nullSafeGet(result, names, null));
        verify();
    }

    public void testNullSafeSetNull() throws Exception {
        stmt.setNull(1, Types.VARCHAR);
        replay();
        type.nullSafeSet(stmt, null, 1);
        verify();
    }

    public void testNullSafeSetValid() throws Exception {
        LocalTime time = new LocalTime(10, 45);
        stmt.setString(1, "10:45");
        replay();
        type.nullSafeSet(stmt, time, 1);
        verify();
    }
}
