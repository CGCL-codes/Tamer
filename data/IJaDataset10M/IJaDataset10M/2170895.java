package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.SQLException;
import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import at.rc.tacos.core.db.dao.PeriodsDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Period;

public class PeriodDAOSQLTest extends DBTestBase {

    private PeriodsDAO periodsDao = DaoFactory.SQL.createPeriodsDAO();

    private Period period1;

    private Period period2;

    private Period period3;

    private int id1, id2, id3;

    @Before
    public void setUp() throws SQLException {
        period1 = new Period(1, "06:00 - 14:00", "_HA");
        period2 = new Period(2, "07:00 - 15:00", "_HA");
        period3 = new Period(3, "07:00 - 15:00", "_ZD");
        id1 = periodsDao.addPeriod(period1);
        id2 = periodsDao.addPeriod(period2);
        id3 = periodsDao.addPeriod(period3);
        System.out.println("id1: " + id1);
        System.out.println("id2: " + id2);
        System.out.println("id3: " + id3);
        period1.setPeriodId(id1);
        period2.setPeriodId(id2);
        period3.setPeriodId(id3);
    }

    @After
    public void tearDown() throws SQLException {
        deleteTable(PeriodsDAO.TABLE_NAME);
    }

    @Test
    public void testRemovePeriod() throws SQLException {
        System.out.println("remove 1");
        periodsDao.removePeriod(period1.getPeriodId());
        System.out.println("remove 2");
        Period period = periodsDao.getPeriod(id1);
        System.out.println("vor assert null bei remove period");
        Assert.assertNull(period);
        System.out.println("nach assert null bei remove period");
    }

    @Test
    public void testListPeriods() throws SQLException {
        List<Period> list = periodsDao.getPeriodListByServiceTypeCompetence("_HA");
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testUpdatePeriod() throws SQLException {
        {
            System.out.println("period1 id:::::" + period1.getPeriodId());
            Period period = periodsDao.getPeriod(period1.getPeriodId());
            period.setPeriod("08:00 - 17:00");
            period.setServiceTypeCompetence("_LSD");
            period.setPeriodId(period1.getPeriodId());
            periodsDao.updatePeriod(period);
        }
        {
            Period period = periodsDao.getPeriod(period1.getPeriodId());
            Assert.assertEquals("_LSD", period.getServiceTypeCompetence());
        }
    }
}
