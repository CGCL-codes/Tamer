package org.ncsa.foodlog.test.hibernate;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.ncsa.foodlog.data.Daily;
import org.ncsa.foodlog.data.hibernate.HibernateUtil;
import org.ncsa.foodlog.data.profiles.Profile;
import org.ncsa.foodlog.data.profiles.ProfileFolder;
import org.ncsa.foodlog.data.profiles.ProfileFolderHibernate;

public class TestQueries extends TestCase {

    Session session;

    private Logger log = Logger.getLogger(TestQueries.class);

    protected void setUp() throws Exception {
        URL xmlUrl = getClass().getClassLoader().getResource("org/ncsa/foodlog/test/hibernate/full.xml");
        File xmlFile = new File(xmlUrl.getPath());
        IDataSet data = new FlatXmlDataSet(xmlFile);
        CreateNetworkSchema.create();
        session = HibernateUtil.currentSession();
        IDatabaseConnection connection = new DatabaseConnection(session.connection());
        DatabaseOperation.CLEAN_INSERT.execute(connection, data);
    }

    public void testGetProfileFolders() {
        List results = session.createQuery("from ProfileFolderHibernate").list();
        assertTrue(results.size() == 3);
        dumpProfileSourceResults(results);
        results = session.getNamedQuery("profilefolder.getProfileFolders").list();
        assertTrue(results.size() == 3);
        dumpProfileSourceResults(results);
    }

    public void testFindProfileFolder() {
        List results = session.createQuery("from ProfileFolderHibernate folder where folder.name like :name").setString("name", "hmr test folder").list();
        assertTrue(results.size() == 1);
        dumpProfileSourceResults(results);
        results = session.getNamedQuery("profilefolder.findProfileFolder").setString("name", "hmr test folder").list();
        assertTrue(results.size() == 1);
        dumpProfileSourceResults(results);
    }

    public void testFindProfile() {
        List results = session.createQuery("from Profile profile where profile.tag.name like :tag").setString("tag", "Peaches").list();
        assertTrue(results.size() == 1);
        dumpProfileResults(results);
        results = session.getNamedQuery("profile.findProfileByTag").setString("tag", "Profile Ice Cream").list();
        assertTrue(results.size() == 1);
        dumpProfileResults(results);
    }

    public void testGetProfiles() {
        List results = session.createQuery("from Profile").list();
        assertTrue(results.size() == 11);
        dumpProfileResults(results);
        results = session.getNamedQuery("profile.getProfiles").list();
        assertTrue(results.size() == 11);
        dumpProfileResults(results);
    }

    public Profile findProfile(String tag) {
        List results = session.getNamedQuery("profile.findProfileByTag").setString("tag", tag).list();
        if (results.size() == 0) {
            log.error(tag + " not found in profiles database");
            return null;
        }
        if (results.size() > 1) {
            log.error(tag + " returned multiple profiles");
        }
        return (Profile) results.get(0);
    }

    public void testGetDaily() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 12);
        cal.set(Calendar.MONTH, Calendar.JUNE);
        cal.set(Calendar.YEAR, 2005);
        Date date = cal.getTime();
        List results = session.createQuery("from Daily daily where daily.date = :date").setParameter("date", date).list();
        assertTrue(results.size() == 1);
        dumpDailyResults(results);
        results = session.getNamedQuery("daily.getDaily").setParameter("date", date).list();
        assertTrue(results.size() == 1);
        dumpDailyResults(results);
    }

    public void testTimeSlice() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 12);
        cal.set(Calendar.MONTH, Calendar.JUNE);
        cal.set(Calendar.YEAR, 2005);
        Date startDate = cal.getTime();
        cal.add(Calendar.DATE, 30);
        Date endDate = cal.getTime();
        List results = session.createQuery("from Daily daily where daily.date >= :startdate and daily.date <= :enddate order by daily.date").setParameter("startdate", startDate).setParameter("enddate", endDate).list();
        assertTrue(results.size() > 1);
        dumpDailyResults(results);
        results = session.getNamedQuery("daily.timeSlice").setParameter("startdate", startDate).setParameter("enddate", endDate).list();
        assertTrue(results.size() > 1);
        dumpDailyResults(results);
    }

    public void testLatestWeight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 28);
        cal.set(Calendar.MONTH, Calendar.AUGUST);
        cal.set(Calendar.YEAR, 2005);
        Date older = cal.getTime();
        cal.add(Calendar.DATE, 2);
        Date newer = cal.getTime();
        List results = session.createQuery("from Daily daily where daily.weight > 0 order by daily.date desc").list();
        assertTrue(results.size() > 1);
        Daily newest = (Daily) results.get(0);
        assertTrue(older.before(newest.getDate()));
        assertTrue(newer.after(newest.getDate()));
        results = session.getNamedQuery("daily.latestWeight").list();
        newest = (Daily) results.get(0);
        log.debug("Latest: " + newest.dump());
        assertTrue(older.before(newest.getDate()));
        assertTrue(newer.after(newest.getDate()));
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 12);
        cal.set(Calendar.MONTH, Calendar.JUNE);
        cal.set(Calendar.YEAR, 2005);
        Date startDate = cal.getTime();
        cal.add(Calendar.DATE, 30);
        Date endDate = cal.getTime();
        results = session.createQuery("from Daily daily where daily.date >= :startdate and daily.date <= :enddate and daily.weight > 0 order by daily.date desc").setParameter("startdate", startDate).setParameter("enddate", endDate).list();
        cal.set(Calendar.DAY_OF_MONTH, 10);
        cal.set(Calendar.MONTH, Calendar.JULY);
        cal.set(Calendar.YEAR, 2005);
        older = cal.getTime();
        log.debug("Older " + Daily.df.format(older));
        cal.add(Calendar.DATE, 2);
        newer = cal.getTime();
        log.debug("Newer " + Daily.df.format(newer));
        newest = (Daily) results.get(0);
        log.debug("Latest: " + newest.dump());
        assertTrue(older.before(newest.getDate()));
        assertTrue(newer.after(newest.getDate()));
        results = session.getNamedQuery("daily.latestWeightWithWindow").setParameter("startdate", startDate).setParameter("enddate", endDate).list();
        log.debug("Newer " + Daily.df.format(newer));
        newest = (Daily) results.get(0);
    }

    private void dumpDailyResults(List results) {
        for (Iterator i = results.iterator(); i.hasNext(); ) {
            Daily d = (Daily) i.next();
            assertNotNull(d);
            log.info("Queried Daily " + (Hibernate.isInitialized(d) ? " is initialized " : " is NOT initialized ") + " whose records" + (Hibernate.isInitialized(d.getRecords()) ? " are initialized " : " are NOT initialized ") + "(" + d.dump() + ")");
            assertTrue(d.getRecords().size() > 0);
        }
    }

    private void dumpProfileResults(List results) {
        for (Iterator i = results.iterator(); i.hasNext(); ) {
            Profile p = (Profile) i.next();
            assertNotNull(p);
            log.info("Queried Profile " + (Hibernate.isInitialized(p) ? " is initialized " : " is NOT initialized ") + " whose records" + "(" + p.dump() + ")");
        }
    }

    private void dumpProfileSourceResults(List results) {
        for (Iterator i = results.iterator(); i.hasNext(); ) {
            ProfileFolder s = (ProfileFolderHibernate) i.next();
            assertNotNull(s);
            log.info("Queried Profile " + (Hibernate.isInitialized(s) ? " is initialized " : " is NOT initialized ") + " whose records" + "(" + s.dump() + ")");
        }
    }

    protected void tearDown() throws Exception {
        HibernateUtil.closeSession();
    }
}
