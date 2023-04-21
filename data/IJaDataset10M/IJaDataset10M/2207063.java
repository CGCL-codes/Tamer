package tests.service;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import com.ivis.xprocess.core.Assignment;
import com.ivis.xprocess.core.Person;
import com.ivis.xprocess.core.ProjectResource;
import com.ivis.xprocess.core.Role;
import com.ivis.xprocess.core.RoleType;
import com.ivis.xprocess.core.Xproject;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.core.impl.PortfolioImpl;
import com.ivis.xprocess.web.dao.DailyRecordDAO;
import com.ivis.xprocess.web.elements.PlannerRow;
import com.ivis.xprocess.web.service.OrganizationService;
import com.ivis.xprocess.web.service.PersonService;
import com.ivis.xprocess.web.service.PortfolioService;
import com.ivis.xprocess.web.service.ProjectService;
import com.ivis.xprocess.web.service.WebSessionService;
import com.ivis.xprocess.web.utils.DailyRecordBundle;
import com.ivis.xprocess.web.utils.PropertyBundle;

public class TestBookingTime extends WebTestCase {

    public void testBookingTime() throws RemoteException {
        String projectUuid = PortfolioService.createProject(sessionToken, testPortfolio.getUuid(), "Test Project 1");
        System.out.println("Project UUID: " + projectUuid);
        assertNotNull(projectUuid);
        Xproject project = (Xproject) sessionDatasource.findElement(projectUuid);
        assertNotNull(project);
        String personUuid = OrganizationService.createPerson(sessionToken, testOrganization.getUuid(), "testperson");
        Person person = (Person) sessionDatasource.findElement(personUuid);
        assertNotNull(person);
        ProjectService.addPersonToProject(sessionToken, projectUuid, personUuid);
        RoleType participantRoleType = PortfolioImpl.getParticipantRoletype(sessionDatasource.getDataSource().getPersistenceHelper());
        Role role = person.getRole(participantRoleType);
        assertNotNull(role);
        project = (Xproject) sessionDatasource.findElement(projectUuid);
        ProjectResource projectResource = project.getProjectResource(person);
        assertNotNull(projectResource);
        String taskUuid = ProjectService.createTask(sessionToken, projectUuid, "Analysis");
        Xtask task = (Xtask) sessionDatasource.findElement(taskUuid);
        assertNotNull(task);
        PropertyBundle propertyBundle = new PropertyBundle();
        propertyBundle.setUuid(taskUuid);
        propertyBundle.setPropertyNames(new String[] { "SIZE", "BEST", "MOST_LIKELY", "WORST" });
        propertyBundle.setPropertyTypes(new String[] { "FLOAT", "INT", "INT", "INT" });
        propertyBundle.setPropertyValues(new Object[] { new Float(2), new Integer(240), new Integer(480), new Integer(960) });
        WebSessionService.saveProperties(sessionToken, propertyBundle);
        task = (Xtask) sessionDatasource.findElement(taskUuid);
        assertEquals(2.0, task.getSize());
        assertEquals(240, task.getBest());
        assertEquals(480, task.getMostLikely());
        assertEquals(960, task.getWorst());
        sessionDatasource.requestReschedule(project.getUuid());
        pause(5000);
        task = (Xtask) sessionDatasource.findElement(taskUuid);
        Set<Assignment> allAssignmentsForPerson = task.getAllAssignmentsForPerson(person);
        System.out.println("The no. of assignments on task = " + allAssignmentsForPerson.size());
        pause(1000);
        long dateAsMilliseconds = Calendar.getInstance().getTimeInMillis();
        PlannerRow[] plannerRows = PersonService.getPlannerData(sessionToken, projectUuid, personUuid, dateAsMilliseconds, dateAsMilliseconds);
        assertEquals(1, plannerRows.length);
        DailyRecordBundle dailyRecordBundle = new DailyRecordBundle();
        dailyRecordBundle.setRoleUuids(new String[] { plannerRows[0].getRoleRef() });
        dailyRecordBundle.setRequiredResourceUuids(new String[] { plannerRows[0].getRequiredResourceRef() });
        dailyRecordBundle.setDaysToBookTimeAgainst(new long[] { dateAsMilliseconds });
        dailyRecordBundle.setTimeToBookInMinutes(new int[] { 420 });
        boolean bookingWasSuccessful = PersonService.bookTime(sessionToken, dailyRecordBundle);
        assertTrue(bookingWasSuccessful);
        plannerRows = PersonService.getPlannerData(sessionToken, projectUuid, personUuid, dateAsMilliseconds, dateAsMilliseconds);
        assertEquals(1, plannerRows.length);
        DailyRecordDAO dailyRecordDAO = getDailyRecordDAO(plannerRows[0], dateAsMilliseconds);
        assertNotNull(dailyRecordDAO);
        assertEquals(420, dailyRecordDAO.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateAsMilliseconds);
        cal.roll(Calendar.DAY_OF_MONTH, false);
        cal.roll(Calendar.DAY_OF_MONTH, false);
        long twoDaysAgoAsMilliseconds = cal.getTimeInMillis();
        System.out.println("Two days ago = " + cal.getTime().toString());
        System.out.println("(A Date)Two days ago = " + new Date(twoDaysAgoAsMilliseconds).toString());
        dailyRecordDAO = getDailyRecordDAO(plannerRows[0], twoDaysAgoAsMilliseconds);
        assertNull(dailyRecordDAO);
        dailyRecordBundle = new DailyRecordBundle();
        dailyRecordBundle.setRoleUuids(new String[] { plannerRows[0].getRoleRef() });
        dailyRecordBundle.setRequiredResourceUuids(new String[] { plannerRows[0].getRequiredResourceRef() });
        dailyRecordBundle.setDaysToBookTimeAgainst(new long[] { twoDaysAgoAsMilliseconds });
        dailyRecordBundle.setTimeToBookInMinutes(new int[] { 120 });
        bookingWasSuccessful = PersonService.bookTime(sessionToken, dailyRecordBundle);
        assertTrue(bookingWasSuccessful);
        plannerRows = PersonService.getPlannerData(sessionToken, projectUuid, personUuid, twoDaysAgoAsMilliseconds, twoDaysAgoAsMilliseconds);
        assertEquals(1, plannerRows.length);
        dailyRecordDAO = getDailyRecordDAO(plannerRows[0], twoDaysAgoAsMilliseconds);
        assertNotNull(dailyRecordDAO);
        assertEquals(120, dailyRecordDAO.getTime());
    }

    private DailyRecordDAO getDailyRecordDAO(PlannerRow plannerRow, long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        String dayName = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        if (dayName.equals("Mon")) {
            return plannerRow.getMondayDailyRecord();
        }
        if (dayName.equals("Tue")) {
            return plannerRow.getTuesdayDailyRecord();
        }
        if (dayName.equals("Wed")) {
            return plannerRow.getWednesdayDailyRecord();
        }
        if (dayName.equals("Thu")) {
            return plannerRow.getThursdayDailyRecord();
        }
        if (dayName.equals("Fri")) {
            return plannerRow.getFridayDailyRecord();
        }
        if (dayName.equals("Sat")) {
            return plannerRow.getSaturdayDailyRecord();
        }
        if (dayName.equals("Sun")) {
            return plannerRow.getSundayDailyRecord();
        }
        fail("Unable to determine which day to return: " + dayName);
        return null;
    }
}
