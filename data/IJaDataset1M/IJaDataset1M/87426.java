package fr.umlv.jee.hibou.bdd;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import junit.framework.TestCase;
import fr.umlv.jee.hibou.bdd.exception.HibouTechnicalException;
import fr.umlv.jee.hibou.bdd.table.Information;
import fr.umlv.jee.hibou.bdd.table.Meeting;
import fr.umlv.jee.hibou.bdd.table.Task;

/**
 * JUnit Test Class for {@link fr.umlv.jee.hibou.bdd.NotificationManager}
 * @author micka, alex, nak, matt
 */
public class NotificationManagerTest extends TestCase {

    private static NotificationManager notificationManager = new NotificationManager();

    private static ProjectManager projectmanager = new ProjectManager();

    protected void setUp() throws Exception {
        super.setUp();
        try {
            projectmanager.createProject("admin@hibou.org", "hibou", "/test", "true", "test", "test", "test");
        } catch (HibouTechnicalException e) {
            e.printStackTrace();
        }
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        try {
            projectmanager.deleteProject("hibou");
        } catch (HibouTechnicalException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#createInformation(java.lang.String, java.lang.String[], java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws HibouTechnicalException 
	 */
    public void testCreateInformation() throws HibouTechnicalException {
        String[] recipients = new String[] { "rcp1", "rcp2", "rcp3" };
        assertTrue(notificationManager.createInformation("author", recipients, "subject", "message", "hibou"));
        List<Information> list = notificationManager.getProjectInformations("hibou");
        assertNotNull(list);
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#getProjectInformations(java.lang.String)}.
	 * @throws HibouTechnicalException 
	 */
    public void testGetProjectInformations() throws HibouTechnicalException {
        String[] recipients = new String[] { "rcp1", "rcp2", "rcp3" };
        for (int i = 1; i < 5; i++) {
            notificationManager.createInformation("author", recipients, "subject", "message", "hibou");
        }
        List<Information> list = notificationManager.getProjectInformations("hibou");
        assertNotNull(list);
        for (Information i : list) {
            assertNotNull(i.getPostDate());
            assertEquals(i.getAuthor(), "author");
            assertNotNull(recipients);
            assertEquals(i.getSubject(), "subject");
            assertEquals(i.getMessage(), "message");
        }
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#getUserInformationNotifications(java.lang.String, java.lang.String)}.
	 */
    public void testGetUserInformationNotifications() throws HibouTechnicalException {
        String[] recipients = new String[] { "test" };
        notificationManager.createInformation("author", recipients, "subject", "message", "hibou");
        notificationManager.createInformation("author", recipients, "subject", "message", "hibou");
        List<Information> infos = notificationManager.getUserInformationNotifications("hibou", "test");
        assertNotNull(infos);
        assertEquals(infos.size(), 2);
        for (Information i : infos) {
            assertNotNull(i.getPostDate());
            assertEquals(i.getAuthor(), "author");
            assertNotNull(recipients);
            assertEquals(i.getSubject(), "subject");
            assertEquals(i.getMessage(), "message");
        }
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#getInformation(long)}.
	 * @throws HibouTechnicalException 
	 */
    public void testGetInformation() throws HibouTechnicalException {
        String[] recipients = new String[] { "test" };
        notificationManager.createInformation("author", recipients, "subject", "message", "hibou");
        List<Information> infos = notificationManager.getUserInformationNotifications("hibou", "test");
        for (Information i : infos) {
            assertNotNull(notificationManager.getInformation(i.getId()));
        }
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#createMeeting(java.lang.String, java.lang.String[], java.lang.String, java.util.Date, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws HibouTechnicalException 
	 */
    public void testCreateMeeting() throws HibouTechnicalException {
        String[] recipients = new String[] { "rcp1", "rcp2", "rcp3" };
        assertTrue(notificationManager.createMeeting("author", recipients, "subject", new Date(Calendar.getInstance().getTimeInMillis()), "place", "time", "message", "hibou"));
        List<Meeting> list = notificationManager.getProjectMeetings("hibou");
        assertNotNull(list);
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#getProjectMeetings(java.lang.String)}.
	 * @throws HibouTechnicalException 
	 */
    public void testGetProjectMeetings() throws HibouTechnicalException {
        String[] recipients = new String[] { "rcp1", "rcp2", "rcp3" };
        for (int i = 1; i < 5; i++) {
            notificationManager.createMeeting("author", recipients, "subject", new Date(Calendar.getInstance().getTimeInMillis()), "place", "time", "message", "hibou");
        }
        List<Meeting> list = notificationManager.getProjectMeetings("hibou");
        assertNotNull(list);
        for (Meeting i : list) {
            assertNotNull(i.getPostDate());
            assertEquals(i.getAuthor(), "author");
            assertNotNull(i.getRecipients());
            assertNotNull(i.getMeetingDate());
            assertEquals(i.getSubject(), "subject");
            assertEquals(i.getMessage(), "message");
            assertEquals(i.getTime(), "time");
            assertEquals(i.getPlace(), "place");
        }
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#getUserMeetingNotifications(java.lang.String, java.lang.String)}.
	 * @throws HibouTechnicalException 
	 */
    public void testGetUserMeetingNotifications() throws HibouTechnicalException {
        String[] recipients = new String[] { "test" };
        for (int i = 1; i < 5; i++) {
            notificationManager.createMeeting("author", recipients, "subject", new Date(Calendar.getInstance().getTimeInMillis()), "place", "time", "message", "hibou");
        }
        List<Meeting> list = notificationManager.getUserMeetingNotifications("hibou", "test");
        assertNotNull(list);
        assertEquals(list.size(), 4);
        for (Meeting i : list) {
            assertNotNull(i.getPostDate());
            assertEquals(i.getAuthor(), "author");
            assertNotNull(i.getRecipients());
            assertNotNull(i.getMeetingDate());
            assertEquals(i.getSubject(), "subject");
            assertEquals(i.getMessage(), "message");
            assertEquals(i.getTime(), "time");
            assertEquals(i.getPlace(), "place");
        }
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#getMeeting(long)}.
	 * @throws HibouTechnicalException 
	 */
    public void testGetMeeting() throws HibouTechnicalException {
        String[] recipients = new String[] { "test" };
        notificationManager.createMeeting("author", recipients, "subject", new Date(Calendar.getInstance().getTimeInMillis()), "place", "time", "message", "hibou");
        List<Meeting> list = notificationManager.getUserMeetingNotifications("hibou", "test");
        for (Meeting i : list) {
            assertNotNull(notificationManager.getMeeting(i.getId()));
        }
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#createTask(java.lang.String, java.lang.String[], java.lang.String, java.util.Date, java.util.Date, java.lang.String, java.lang.String)}.
	 * @throws HibouTechnicalException 
	 */
    public void testCreateTask() throws HibouTechnicalException {
        String[] recipients = new String[] { "rcp1", "rcp2", "rcp3" };
        assertTrue(notificationManager.createTask("author", recipients, "subject", new Date(Calendar.getInstance().getTimeInMillis()), new Date(Calendar.getInstance().getTimeInMillis()), "description", "hibou"));
        List<Task> list = notificationManager.getProjectTasks("hibou");
        assertNotNull(list);
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#getProjectTasks(java.lang.String)}.
	 * @throws HibouTechnicalException 
	 */
    public void testGetProjectTasks() throws HibouTechnicalException {
        String[] recipients = new String[] { "rcp1", "rcp2", "rcp3" };
        for (int i = 1; i < 5; i++) {
            notificationManager.createTask("author", recipients, "subject", new Date(Calendar.getInstance().getTimeInMillis()), new Date(Calendar.getInstance().getTimeInMillis()), "description", "hibou");
        }
        List<Task> list = notificationManager.getProjectTasks("hibou");
        assertNotNull(list);
        for (Task i : list) {
            assertNotNull(i.getPostDate());
            assertEquals(i.getAuthor(), "author");
            assertNotNull(i.getRecipients());
            assertNotNull(i.getStartTask());
            assertNotNull(i.getEndTask());
            assertEquals(i.getDescription(), "description");
        }
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#getUserTaskNotifications(java.lang.String, java.lang.String)}.
	 * @throws HibouTechnicalException 
	 */
    public void testGetUserTaskNotifications() throws HibouTechnicalException {
        String[] recipients = new String[] { "test" };
        for (int i = 1; i < 5; i++) {
            notificationManager.createTask("author", recipients, "subject", new Date(Calendar.getInstance().getTimeInMillis()), new Date(Calendar.getInstance().getTimeInMillis()), "description", "hibou");
        }
        List<Task> list = notificationManager.getUserTaskNotifications("hibou", "test");
        assertNotNull(list);
        assertEquals(list.size(), 4);
        for (Task i : list) {
            assertNotNull(i.getPostDate());
            assertEquals(i.getAuthor(), "author");
            assertNotNull(i.getRecipients());
            assertNotNull(i.getStartTask());
            assertNotNull(i.getEndTask());
            assertEquals(i.getDescription(), "description");
        }
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#getTask(long)}.
	 * @throws HibouTechnicalException 
	 */
    public void testGetTask() throws HibouTechnicalException {
        String[] recipients = new String[] { "test" };
        notificationManager.createTask("author", recipients, "subject", new Date(Calendar.getInstance().getTimeInMillis()), new Date(Calendar.getInstance().getTimeInMillis()), "description", "hibou");
        List<Task> list = notificationManager.getUserTaskNotifications("hibou", "test");
        for (Task i : list) {
            assertNotNull(notificationManager.getTask(i.getId()));
        }
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#dropInformation(long)}.
	 * @throws HibouTechnicalException 
	 */
    public void testDropInformation() throws HibouTechnicalException {
        String[] recipients = new String[] { "test" };
        notificationManager.createInformation("author", recipients, "subject", "message", "hibou");
        List<Information> infos = notificationManager.getUserInformationNotifications("hibou", "test");
        for (Information i : infos) {
            assertTrue(notificationManager.dropInformation(i.getId()));
        }
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#dropMeeting(long)}.
	 * @throws HibouTechnicalException 
	 */
    public void testDropMeeting() throws HibouTechnicalException {
        String[] recipients = new String[] { "test" };
        notificationManager.createInformation("author", recipients, "subject", "message", "hibou");
        List<Meeting> list = notificationManager.getUserMeetingNotifications("hibou", "test");
        for (Meeting i : list) {
            assertTrue(notificationManager.dropMeeting(i.getId()));
        }
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bdd.NotificationManager#dropTask(long)}.
	 * @throws HibouTechnicalException 
	 */
    public void testDropTask() throws HibouTechnicalException {
        String[] recipients = new String[] { "test" };
        notificationManager.createTask("author", recipients, "subject", new Date(Calendar.getInstance().getTimeInMillis()), new Date(Calendar.getInstance().getTimeInMillis()), "description", "hibou");
        List<Task> list = notificationManager.getUserTaskNotifications("hibou", "test");
        for (Task i : list) {
            assertTrue(notificationManager.dropTask(i.getId()));
        }
    }
}
