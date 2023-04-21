package org.mbari.jdo.castor;

import junit.framework.JUnit4TestAdapter;
import junit.textui.TestRunner;
import org.junit.Test;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mbari.vars.annotation.model.VarsObjectFactory;
import org.mbari.vars.annotation.model.VideoArchiveSet;
import org.mbari.vars.annotation.model.dao.VideoArchiveSetDAO;
import org.mbari.vars.dao.DAOException;
import org.mbari.vars.dao.ObjectDAO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.CacheManager;
import org.exolab.castor.jdo.PersistenceException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.GregorianCalendar;
import java.sql.*;

/**
 * @author brian
 * @version $Id: $
 * @since Jan 31, 2007 10:07:51 AM PST
 */
public class TestDateStorage {

    private static final Logger log = LoggerFactory.getLogger(TestDateStorage.class);

    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestDateStorage.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    @Test
    public void testDateRoundtrip() {
        log.debug("Running testDateRoundtrip");
        String timeZone = System.getProperty("user.timezone");
        log.debug("user.timezone = " + timeZone);
        String dateString = "1968-09-22 00:00:00 UTC";
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            log.error("ParseException thrown", e);
            Assert.fail("Unable to parse " + dateString);
        }
        log.debug("Source Date = " + dateString + ", Formatted Date = " + formatter.format(date) + " [" + date.getTime() + "]");
        VideoArchiveSet videoArchiveSet = VarsObjectFactory.makeVideoArchiveSet();
        videoArchiveSet.setStartDate(date);
        videoArchiveSet.setEndDate(date);
        log.debug("Inserting VideoArchiveSet into " + ObjectDAO.getUrl());
        try {
            VideoArchiveSetDAO.getInstance().insert(videoArchiveSet);
        } catch (DAOException e) {
            log.error("DAOException thrown", e);
            Assert.fail("Unable to insert into database");
        }
        long id = videoArchiveSet.getId();
        log.debug("Clearing Castor's cache");
        Database db = null;
        try {
            db = ObjectDAO.fetchDatabase();
        } catch (DAOException e) {
            log.debug("DAOException thrown", e);
        }
        CacheManager cacheManager = db.getCacheManager();
        cacheManager.expireCache();
        log.debug("Fetching VideoArchiveSet with id = " + id + " from " + ObjectDAO.getUrl());
        try {
            videoArchiveSet = (VideoArchiveSet) VideoArchiveSetDAO.getInstance().findByPK(id + "");
        } catch (DAOException e) {
            log.debug("DAOException thrown", e);
        }
        Date insertedDate = videoArchiveSet.getStartDate();
        Assert.assertEquals("Date did not survive the round trip!! Inserted: " + formatter.format(date) + " [" + date.getTime() + "], Retrieved: " + insertedDate + " [" + insertedDate.getTime() + "]", date, insertedDate);
        log.debug("Success!! Inserted: " + formatter.format(date) + " [" + date.getTime() + "], Retrieved: " + formatter.format(insertedDate) + " [" + insertedDate.getTime() + "]");
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        String sql = "SELECT StartDTG FROM VideoArchiveSet WHERE id = " + id;
        try {
            Database database = ObjectDAO.fetchDatabase();
            database.begin();
            Connection connection = database.getJdbcConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            Date aDate = resultSet.getTimestamp(1);
            Date bDate = resultSet.getTimestamp(1, calendar);
            database.commit();
            database.close();
            log.debug("SQL retrieved " + formatter.format(aDate) + " [" + aDate.getTime() + "] without a Calendar");
            log.debug("SQL retrieved " + formatter.format(bDate) + " [" + bDate.getTime() + "] with a UTC Calendar");
            Assert.assertEquals("Date fetch via Castor and SQL are not the same", date, bDate);
        } catch (PersistenceException e) {
            log.error("PersistenceException thrown", e);
        } catch (SQLException e) {
            log.error("SQLException thrown", e);
        }
        timeZone = System.getProperty("user.timezone");
        log.debug("user.timezone = " + timeZone);
    }
}
