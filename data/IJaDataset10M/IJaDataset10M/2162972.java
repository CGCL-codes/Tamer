package test.java;

import java.util.Date;
import java.text.SimpleDateFormat;
import edu.fullcoll.uportal.portlets.earlyalert.dao.AlertCommentsHome;
import edu.fullcoll.uportal.portlets.earlyalert.dao.AlertsHome;
import edu.fullcoll.uportal.portlets.earlyalert.dao.AssignedAlertsHome;
import edu.fullcoll.uportal.portlets.earlyalert.model.alert.AlertComments;
import edu.fullcoll.uportal.portlets.earlyalert.model.alert.AssignedAlerts;
import static org.hibernate.Hibernate.createClob;
import junit.framework.TestCase;

/**
 * 
 * Test case for 'AlertCommentsHomeTest'
 * @see 
 * @author Brad Rippe (brippe@fullcoll.edu)
 * @version 1.0
 */
public class AlertCommentsHomeTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AlertCommentsHomeTest.class);
    }

    /**
     * Test method for 'edu.fullcoll.uportal.portlets.earlyalert.dao.AlertCommentsHome.persist(AlertComments)'
     */
    public void testPersist() {
        AlertComments ac = new AlertComments();
        AssignedAlerts aa = new AssignedAlerts();
        aa.setAssignByBannerId("00001148");
        aa.setAssignTermCode("200520");
        aa.setAssignCrn("232323");
        aa.setAssignToBannerId("00005520");
        AlertsHome ah = new AlertsHome();
        aa.setAlerts(ah.getAlertByCode("C"));
        aa.setAssignDate(new Date());
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM d, yyyy");
        ac.setCommentTxt(createClob("Comment set from unit testing " + df.format(today)));
        AlertCommentsHome ach = new AlertCommentsHome();
        AssignedAlertsHome aah = new AssignedAlertsHome();
        aah.persist(aa);
        ac.setAssignId(aa.getAssignId());
        ach.persist(ac);
        assertNotNull(ach.findById(ac.getCommentId()));
        ach.delete(ac);
        aah.delete(aa);
    }

    /**
     * Test method for 'edu.fullcoll.uportal.portlets.earlyalert.dao.AlertCommentsHome.save(AlertComments)'
     */
    public void testSave() {
        AlertComments ac = new AlertComments();
        AssignedAlerts aa = new AssignedAlerts();
        aa.setAssignByBannerId("00001148");
        aa.setAssignTermCode("200520");
        aa.setAssignCrn("232323");
        aa.setAssignToBannerId("00005520");
        AlertsHome ah = new AlertsHome();
        aa.setAlerts(ah.getAlertByCode("C"));
        aa.setAssignDate(new Date());
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM d, yyyy");
        ac.setCommentTxt(createClob("Comment set from unit testing " + df.format(today)));
        AlertCommentsHome ach = new AlertCommentsHome();
        AssignedAlertsHome aah = new AssignedAlertsHome();
        aah.persist(aa);
        ac.setAssignId(aa.getAssignId());
        ach.save(ac);
        assertNotNull(ach.findById(ac.getCommentId()));
        ach.delete(ac);
        aah.delete(aa);
    }

    /**
     * Test method for 'edu.fullcoll.uportal.portlets.earlyalert.dao.AlertCommentsHome.attachDirty(AlertComments)'
     */
    public void testAttachDirty() {
        AlertComments ac = new AlertComments();
        AssignedAlerts aa = new AssignedAlerts();
        aa.setAssignByBannerId("00001149");
        aa.setAssignTermCode("200520");
        aa.setAssignCrn("232323");
        aa.setAssignToBannerId("00005520");
        ac.setAssignId(aa.getAssignId());
        AlertsHome ah = new AlertsHome();
        aa.setAlerts(ah.getAlertByCode("C"));
        aa.setAssignDate(new Date());
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM d, yyyy");
        ac.setCommentTxt(createClob("Comment set from unit testing " + df.format(today)));
        AlertCommentsHome ach = new AlertCommentsHome();
        AssignedAlertsHome aah = new AssignedAlertsHome();
        aah.save(aa);
        ac.setAssignId(aa.getAssignId());
        ach.attachDirty(ac);
        ach.delete(ac);
        aah.delete(aa);
    }

    /**
     * Test method for 'edu.fullcoll.uportal.portlets.earlyalert.dao.AlertCommentsHome.delete(AlertComments)'
     */
    public void testDelete() {
        AlertComments ac = new AlertComments();
        AssignedAlerts aa = new AssignedAlerts();
        aa.setAssignByBannerId("00001140");
        aa.setAssignTermCode("200520");
        aa.setAssignCrn("232323");
        aa.setAssignToBannerId("00005520");
        AlertsHome ah = new AlertsHome();
        aa.setAlerts(ah.getAlertByCode("C"));
        aa.setAssignDate(new Date());
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM d, yyyy");
        ac.setCommentTxt(createClob("Comment set from unit testing " + df.format(today)));
        AlertCommentsHome ach = new AlertCommentsHome();
        AssignedAlertsHome aah = new AssignedAlertsHome();
        aah.save(aa);
        ac.setAssignId(aa.getAssignId());
        ach.save(ac);
        ach.delete(ac);
        assertNull(ach.findById(ac.getCommentId()));
        aah.delete(aa);
    }

    public void testFindById() {
        AlertComments ac = new AlertComments();
        AssignedAlerts aa = new AssignedAlerts();
        aa.setAssignByBannerId("00001148");
        aa.setAssignTermCode("200520");
        aa.setAssignCrn("232324");
        aa.setAssignToBannerId("00005520");
        AlertsHome ah = new AlertsHome();
        aa.setAlerts(ah.getAlertByCode("C"));
        aa.setAssignDate(new Date());
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM d, yyyy");
        ac.setCommentTxt(createClob("Comment set from unit testing " + df.format(today)));
        AlertCommentsHome ach = new AlertCommentsHome();
        AssignedAlertsHome aah = new AssignedAlertsHome();
        aah.save(aa);
        ac.setAssignId(aa.getAssignId());
        ach.save(ac);
        AlertComments aa2 = ach.findById(ac.getCommentId());
        assertNotNull(aa2);
        ach.delete(aa2);
        aah.delete(aa);
    }
}
