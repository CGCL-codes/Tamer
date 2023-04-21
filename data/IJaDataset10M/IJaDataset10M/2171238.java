package tests.datetimetypes;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import sybase.domain.custom.DateTypesDO;
import sybase.domain.gen.DateTypesPK;
import tests.SqlMapSingleton;
import com.ibatis.sqlmap.client.SqlMapClient;

public class DateTests extends TestCase {

    private static Logger logger = Logger.getLogger(DateTests.class);

    private SqlMapClient sqlMap = SqlMapSingleton.getInstance().getSqlMap();

    private static final DateTypesPK PK1 = new DateTypesPK(new Integer(1));

    public DateTests(final String txt) {
        super(txt);
    }

    public void testNull() throws SQLException {
        storeAndRetrieve(null);
    }

    public void test0() throws SQLException {
        storeAndRetrieve(2000, 1, 1);
    }

    public void test1() throws SQLException {
        storeAndRetrieve(1900, 1, 1);
    }

    public void test2() throws SQLException {
        storeAndRetrieve(2100, 1, 1);
    }

    public void test3() throws SQLException {
        storeAndRetrieve(2000, 12, 31);
    }

    public void test4() throws SQLException {
        storeAndRetrieve(2004, 2, 29);
    }

    public void test5() throws SQLException {
        storeAndRetrieve(2010, 1, 1);
    }

    public void test6() throws SQLException {
        storeAndRetrieve(2010, 12, 31);
    }

    public void testMax() throws SQLException {
        storeAndRetrieve(9999, 12, 31);
    }

    public void testMin() throws SQLException {
        storeAndRetrieve(1753, 1, 1);
    }

    private void storeAndRetrieve(final Timestamp store) throws SQLException {
        storeAndRetrieve(store, store);
    }

    private void storeAndRetrieve(final int year, final int month, final int day) throws SQLException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Timestamp ts = new Timestamp(c.getTimeInMillis());
        storeAndRetrieve(ts, ts);
    }

    private void storeAndRetrieve(final Timestamp store, final Timestamp expected) throws SQLException {
        try {
            sqlMap.startTransaction();
            logger.debug("I will update with value: " + store);
            DateTypesDO t = new DateTypesDO(PK1.getId());
            t.setToday(store);
            int rowsUpdated = sqlMap.update("updateDateTypes", t);
            assertEquals(1, rowsUpdated);
            sqlMap.commitTransaction();
        } finally {
            sqlMap.endTransaction();
        }
        try {
            sqlMap.startTransaction();
            logger.debug("I will select for value: " + store);
            DateTypesDO t = (DateTypesDO) sqlMap.queryForObject("selectDateTypes", PK1);
            logger.debug("t=" + t);
            assertNotNull(t);
            assertEquals(expected, t.getToday());
            sqlMap.commitTransaction();
        } finally {
            sqlMap.endTransaction();
        }
    }
}
