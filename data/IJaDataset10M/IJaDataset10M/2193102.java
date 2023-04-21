package tests.integertypes.date;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import junit.framework.TestCase;
import oracle.domain.custom.DateTypes;
import org.apache.log4j.Logger;
import tests.SqlMapSingleton;
import com.ibatis.sqlmap.client.SqlMapClient;

public class DateTests extends TestCase {

    private static Logger logger = Logger.getLogger(DateTests.class);

    private SqlMapClient sqlMap = SqlMapSingleton.getInstance().getSqlMap();

    public DateTests(final String txt) {
        super(txt);
    }

    public void testNull() throws SQLException {
        storeAndRetrieve(null);
    }

    public void testIni2006() throws SQLException {
        storeAndRetrieve(2006, 0, 1, 0, 0, 0);
    }

    public void testFin2006() throws SQLException {
        storeAndRetrieve(2006, 11, 31, 23, 59, 59);
    }

    public void testOtra() throws SQLException {
        storeAndRetrieve(1967, 10, 17, 23, 15, 0);
    }

    private void storeAndRetrieve(final int ano, final int mes, final int dia, final int hora, final int min, final int seg) throws SQLException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, ano);
        c.set(Calendar.MONTH, mes);
        c.set(Calendar.DAY_OF_MONTH, dia);
        c.set(Calendar.HOUR_OF_DAY, hora);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, seg);
        c.set(Calendar.MILLISECOND, 0);
        storeAndRetrieve(c.getTime());
    }

    private void storeAndRetrieve(final Date hora) throws SQLException {
        try {
            sqlMap.startTransaction();
            logger.debug("I will update with value: " + hora);
            DateTypes ity = new DateTypes(1);
            ity.setFecha(hora);
            int rowsUpdated = sqlMap.update("updateDateTypes", ity);
            logger.debug("hice UPDATE. Ok.  rowsUpdated=" + rowsUpdated);
            assertEquals(rowsUpdated, 1);
            sqlMap.commitTransaction();
        } finally {
            sqlMap.endTransaction();
        }
        logger.debug("ok, so far.");
        try {
            sqlMap.startTransaction();
            logger.debug("I will select for value: " + hora);
            DateTypes ity = (DateTypes) sqlMap.queryForObject("selectDateTypes", new DateTypes(1));
            logger.debug("ity,getFecha()=" + ity.getFecha());
            assertEquals(ity.getFecha(), hora);
            sqlMap.commitTransaction();
        } finally {
            sqlMap.endTransaction();
        }
    }
}
