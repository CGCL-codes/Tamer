package TestingClasses;

import static junit.framework.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;
import org.junit.Before;
import org.junit.Test;
import businessClasses.Ticket;

public class JUnit4BCTicketTester {

    private Ticket ticket;

    @Before
    public void testSetUp() {
        this.ticket = new Ticket();
    }

    @Test
    public void testSetAndGetId() {
        this.ticket.setId(1);
        assertTrue("Id is wrong!", (this.ticket.getId() == 1));
        this.ticket.setId(0);
        assertTrue("Id is wrong!", (this.ticket.getId() == 0));
        this.ticket.setId(-1);
        assertTrue("Id is wrong!", (this.ticket.getId() == -1));
    }

    @Test
    public void testSetAndGetType() {
        this.ticket.setType("testing");
        assertTrue("Type is wrong!", (this.ticket.getType().equals("testing")));
    }

    @Test
    public void testGetAndSetTime() {
        this.ticket.setTime(34235);
        assertTrue("Time is wrong!", (this.ticket.getTime() == 34235));
        this.ticket.setTime(-34235);
        assertTrue("Time is wrong!", (this.ticket.getTime() == -34235));
    }

    @Test
    public void testGetAndSetChangetime() {
        this.ticket.setChangetime(34235);
        assertTrue("ChangeTime is wrong!", (this.ticket.getChangetime() == 34235));
        this.ticket.setChangetime(-34235);
        assertTrue("ChangeTime is wrong!", (this.ticket.getChangetime() == -34235));
    }

    @Test
    public void testGetAndSetComponent() {
        this.ticket.setComponent("Component");
        assertTrue("Component is wrong!", (this.ticket.getComponent().equals("Component")));
    }

    @Test
    public void testGetAndSetSeverity() {
        this.ticket.setSeverity("Severity");
        assertTrue("Severity is wrong!", (this.ticket.getSeverity().equals("Severity")));
    }

    @Test
    public void testGetAndSetPriority() {
        this.ticket.setPriority("Priority");
        assertTrue("Priority is wrong!", (this.ticket.getPriority().equals("Priority")));
    }

    @Test
    public void testGetAndSetOwner() {
        this.ticket.setOwner("Owner");
        assertTrue("Owner is wrong!", (this.ticket.getOwner().equals("Owner")));
    }

    @Test
    public void testGetAndSetReporter() {
        this.ticket.setReporter("Reporter");
        assertTrue("Reporter is wrong!", (this.ticket.getReporter().equals("Reporter")));
    }

    @Test
    public void testGetAndSetCc() {
        this.ticket.setCc("Cc");
        assertTrue("Cc is wrong!", (this.ticket.getCc().equals("Cc")));
    }

    @Test
    public void testGetAndSetVersion() {
        this.ticket.setVersion("Version");
        assertTrue("Version is wrong!", (this.ticket.getVersion().equals("Version")));
    }

    @Test
    public void testGetAndSetMilestone() {
        this.ticket.setMilestone("Milestone");
        assertTrue("Milestone is wrong!", (this.ticket.getMilestone().equals("Milestone")));
    }

    @Test
    public void testGetAndSetStatus() {
        this.ticket.setStatus("Status");
        assertTrue("Status is wrong!", (this.ticket.getStatus().equals("Status")));
    }

    @Test
    public void testGetAndSetResolution() {
        this.ticket.setResolution("Resolution");
        assertTrue("Resolution is wrong!", (this.ticket.getResolution().equals("Resolution")));
    }

    @Test
    public void testGetAndSetSummary() {
        this.ticket.setSummary("Summary");
        assertTrue("Summary is wrong!", (this.ticket.getSummary().equals("Summary")));
    }

    @Test
    public void testGetAndSetDescription() {
        this.ticket.setDescription("Description");
        assertTrue("Description is wrong!", (this.ticket.getDescription().equals("Description")));
    }

    @Test
    public void testGetAndSetKeywords() {
        this.ticket.setKeywords("keywords");
        assertTrue("Keywords is wrong!", (this.ticket.getKeywords().equals("keywords")));
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(JUnit4BCTicketTester.class);
    }
}
