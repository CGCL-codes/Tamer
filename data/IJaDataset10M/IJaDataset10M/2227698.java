package unit.dao;

import java.util.Calendar;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import uk.co.q3c.deplan.dao.BaseCalendarDao;
import uk.co.q3c.deplan.dao.BaseCalendarDao_db4o;
import uk.co.q3c.deplan.dao.DatabaseConnection;
import uk.co.q3c.deplan.domain.resource.BaseCalendar;
import uk.co.q3c.deplan.util.Q3Calendar;
import util.TestUtils;

/**
 * {@link BaseCalendarDao_db4o}
 * 
 * @author DSowerby 18 Nov 2008
 * 
 */
public class BaseCalendarDao_db4o_UT {

    BaseCalendarDao baseCalendarDao;

    protected final transient Logger logger = Logger.getLogger(getClass().getName());

    DatabaseConnection dbc;

    @BeforeMethod
    public void beforeMethod() {
        dbc = TestUtils.dbc(this, true);
        baseCalendarDao = dbc.getBaseCalendarDao();
        baseCalendarDao.deleteAll();
    }

    @AfterMethod
    public void afterMethod() {
        dbc.delete();
        baseCalendarDao = null;
        dbc = null;
    }

    @Test
    public void testSaveDelete() {
        Calendar cal = new Q3Calendar();
        cal.set(2008, 4, 1);
        BaseCalendar baseCalendar;
        baseCalendar = new BaseCalendar();
        Assert.assertEquals(baseCalendar.getWorkingTime(cal), 450);
        baseCalendar.addAdjustment(2008, 4, 1, true, 0);
        Assert.assertEquals(baseCalendar.getWorkingTime(cal), 0);
        baseCalendarDao.save(baseCalendar);
        dbc.commit();
        BaseCalendar baseCalendar1 = baseCalendarDao.find();
        Assert.assertEquals(baseCalendar1.getWorkingTime(cal), 0);
        baseCalendarDao.discard(baseCalendar);
        dbc.commit();
        Assert.assertNull(baseCalendarDao.find());
        baseCalendar = new BaseCalendar();
        baseCalendarDao.save(baseCalendar);
        dbc.commit();
        Assert.assertNotNull(baseCalendarDao.find());
        baseCalendarDao.deleteAll();
        dbc.commit();
        Assert.assertNull(baseCalendarDao.find());
    }
}
