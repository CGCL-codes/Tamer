package onepoint.project.modules.project_planning.test;

import onepoint.express.XComponent;
import onepoint.project.OpProjectSession;
import onepoint.project.modules.documents.OpContent;
import onepoint.project.modules.project.*;
import onepoint.project.modules.project.components.OpGanttValidator;
import onepoint.project.modules.project_planning.OpProjectPlanningService;
import onepoint.project.modules.resource.OpResource;
import onepoint.project.modules.user.OpGroup;
import onepoint.project.modules.user.OpLock;
import onepoint.project.modules.user.OpSubject;
import onepoint.project.modules.user.OpUser;
import onepoint.project.test.OpBaseMockTestCase;
import onepoint.service.XMessage;
import onepoint.util.XCalendar;
import org.jmock.core.Constraint;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

/**
 * @author : mihai.costin
 */
public class OpProjectPlanningServiceMockTest extends OpBaseMockTestCase {

    /**
    * The project administrationService object that is being tested.
    */
    private OpProjectPlanningService planningService = null;

    /**
    * A user that acts as the logged on user.
    */
    private OpUser sessionUser = null;

    private OpGroup everyone = null;

    /**
    * A project version.
    */
    private OpProjectPlanVersion projectPlanVersion;

    /**
    * Lock for project
    */
    private OpLock projectLock;

    /**
    * Project that is being tested.
    */
    private String projectName;

    private String projectDescription;

    private OpProjectNode project;

    /**
    * Project plan for project
    */
    private OpProjectPlan projectPlan;

    /**
    * Portfolio that is being tested
    */
    private String portfolioName;

    private String portfolioDescription;

    private OpProjectNode portfolio;

    /**
    * Activity category
    */
    private String categoryName;

    private OpActivityCategory category;

    private OpResource resource;

    private static final int SESSION_USER_ID_LONG = 666;

    private static final int EVERYONE_ID_LONG = 222;

    private static final String CHECKED_IN_PROJECT = "checkedInProject";

    private static final String LOCKED_PROJECT_ID = "lockedProjectId";

    private static final String NONEXISTENT_PROJECT_ID = "OpProjectNode.1206.xid";

    private static final String OK_PROJECT_ID = "okProjectId";

    private static final String RESOURCE_ID = "OpResource.0.xid['50']";

    private static int CONTENT_REF_COUNT;

    private static final String SELECT_MAX_VERSION_NUMBER_FOR_PROJECT = "select max(planVersion.VersionNumber) from OpProjectPlanVersion as planVersion " + "where planVersion.ProjectPlan.ProjectNode.ID = ?";

    private static final String SELECT_PROJECT_RESOURCES = "select assignment.Resource from OpProjectNodeAssignment as assignment " + "where assignment.ProjectNode.ID = ? order by assignment.Resource.Name asc";

    private static final String SELECT_PLAN_VERSION_FOR_PROJECT = "select planVersion from OpProjectPlanVersion as planVersion " + "where planVersion.ProjectPlan.ID = ? and planVersion.VersionNumber = ?";

    private static final String SELECT_ACTIVITY_VERSIONS_FOR_PROJECT_PLAN_VERSION = "select activityVersion.Activity.ID, activityVersion.ID from OpActivityVersion as activityVersion " + "where activityVersion.PlanVersion.ID = ?";

    /**
    * @see onepoint.project.test.OpBaseMockTestCase#setUp()
    */
    protected void setUp() {
        super.setUp();
        planningService = new OpProjectPlanningService();
        sessionUser = new OpUser();
        sessionUser.setID(SESSION_USER_ID_LONG);
        sessionUser.setLevel(new Byte(OpUser.MANAGER_USER_LEVEL));
        everyone = new OpGroup();
        everyone.setID(EVERYONE_ID_LONG);
        everyone.setUserAssignments(new HashSet());
        projectPlanVersion = new OpProjectPlanVersion();
        projectPlan = new OpProjectPlan();
        projectPlan.setStart(XCalendar.today());
        projectPlan.setFinish(XCalendar.today());
        projectPlan.setActivities(new HashSet());
        projectPlan.setActivityAssignments(new HashSet());
        projectPlan.setActivityAttachments(new HashSet());
        projectPlan.setDependencies(new HashSet());
        projectPlan.setDependencies(new HashSet());
        projectPlan.setWorkPeriods(new HashSet());
        projectName = "FirstProject";
        projectDescription = "FirstProjectDescription";
        project = new OpProjectNode();
        project.setType(OpProjectNode.PROJECT);
        project.setID(1);
        project.setName(projectName);
        project.setDescription(projectDescription);
        project.setStart(XCalendar.today());
        project.setFinish(XCalendar.today());
        project.setAssignments(new HashSet());
        project.setGoals(new HashSet());
        project.setToDos(new HashSet());
        project.setPlan(projectPlan);
        projectLock = new OpLock();
        projectLock.setOwner(sessionUser);
        projectLock.setTarget(project);
        portfolioName = "FirstPortfolio";
        portfolioDescription = "FirstPortfolioDescription";
        portfolio = new OpProjectNode();
        portfolio.setType(OpProjectNode.PORTFOLIO);
        portfolio.setName(portfolioName);
        portfolio.setDescription(portfolioDescription);
        Set projectsSet = new HashSet();
        projectsSet.add(project);
        project.setSuperNode(portfolio);
        portfolio.setSubNodes(projectsSet);
        portfolio.setID(3);
        categoryName = "ActivityCategory";
        resource = new OpResource();
        resource.setID(0);
        resource.setName("ResourceName");
        resource.setDescription("ResourceDescription");
        resource.setAvailable((byte) 100);
        resource.setHourlyRate(10);
        queryResults = new ArrayList();
    }

    /**
    * @see onepoint.project.test.OpBaseMockTestCase#invocationMatch(org.jmock.core.Invocation)
    */
    public Object invocationMatch(Invocation invocation) throws IllegalArgumentException {
        String methodName = invocation.invokedMethod.getName();
        if (methodName.equals(GET_OBJECT_METHOD)) {
            String entityId = (String) invocation.parameterValues.get(0);
            if (entityId.equals(LOCKED_PROJECT_ID)) {
                OpUser lockOwner = new OpUser();
                lockOwner.setID(sessionUser.getID() + 1);
                projectLock.setOwner(lockOwner);
                Set locks = new HashSet();
                locks.add(projectLock);
                project.setLocks(locks);
                return project;
            } else if (entityId.equals(NONEXISTENT_PROJECT_ID)) {
                return null;
            } else if (entityId.equals(OK_PROJECT_ID)) {
                Set locks = new HashSet();
                locks.add(projectLock);
                project.setLocks(locks);
                return project;
            } else if (entityId.equals(CHECKED_IN_PROJECT)) {
                Set locks = new HashSet();
                project.setLocks(locks);
                return project;
            } else if (entityId.equals(categoryName)) {
                category = new OpActivityCategory();
                return category;
            } else if (entityId.equals(RESOURCE_ID)) {
                return resource;
            }
        } else if (methodName.equals(USER_METHOD)) {
            return sessionUser;
        } else if (methodName.equals(GET_USER_ID_METHOD)) {
            return new Long(sessionUser.getID());
        } else if (methodName.equals(CHECK_ACCESS_LEVEL_METHOD)) {
            return Boolean.TRUE;
        }
        throw new IllegalArgumentException("Invalid method name:" + methodName + " for this stub");
    }

    /**
    * Tests the behaviour of edit action if project is locked
    */
    public void testEditActivitiesProjectLocked() {
        XMessage request = createProjectRequest(LOCKED_PROJECT_ID, null);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).will(methodStub);
        mockSession.expects(once()).method(CHECK_ACCESS_LEVEL_METHOD).will(methodStub);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = planningService.editActivities((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result);
        assertNotNull("Error message should have been returned", result.getError());
    }

    /**
    * Tests if a project is brought ok in edit mode.
    */
    public void testEditActivities() throws IOException {
        XMessage request = createProjectRequest(CHECKED_IN_PROJECT, null);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_TRANSACTION_METHOD).will(methodStub);
        mockSession.expects(once()).method(CHECK_ACCESS_LEVEL_METHOD).will(methodStub);
        mockSession.expects(once()).method(USER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(createLockConstraint(sessionUser, project));
        mockTransaction.expects(once()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        assertNoError(planningService.editActivities((OpProjectSession) mockSession.proxy(), request));
    }

    /**
    * Tests if a given set of activities are saved ok, provided that the
    */
    public void testSaveActivities() {
        project.setPlan(projectPlan);
        XComponent activitiesDataSet = new XComponent(XComponent.DATA_SET);
        String activityOneName = "Activity 1";
        XComponent activity = newActivity(activityOneName);
        OpGanttValidator.addWorkPhaseStart(activity, OpGanttValidator.getStart(activity));
        OpGanttValidator.addWorkPhaseFinish(activity, OpGanttValidator.getEnd(activity));
        activitiesDataSet.addChild(activity);
        XMessage request = createProjectRequest(OK_PROJECT_ID, activitiesDataSet);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(OK_PROJECT_ID)).will(methodStub);
        mockSession.expects(once()).method(GET_USER_ID_METHOD).will(methodStub);
        projectPlanVersion.setProjectPlan(projectPlan);
        projectPlanVersion.setVersionNumber(OpProjectAdministrationService.WORKING_VERSION_NUMBER);
        projectPlanVersion.setStart(XCalendar.today());
        projectPlanVersion.setFinish(XCalendar.today());
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PLAN_VERSION_FOR_PROJECT)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.add(projectPlanVersion);
                return query;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PLAN_VERSION_FOR_PROJECT);
            }
        });
        mockQuery.expects(atLeastOnce()).method(SET_INTEGER_METHOD);
        mockQuery.expects(atLeastOnce()).method(SET_LONG_METHOD);
        mockBroker.expects(atLeastOnce()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_ACTIVITY_VERSIONS_FOR_PROJECT_PLAN_VERSION)).will(methodStub);
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(isA(OpActivityVersion.class));
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PROJECT_RESOURCES)).will(methodStub);
        mockBroker.expects(atLeastOnce()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_RESOURCES);
            }
        });
        mockBroker.expects(once()).method(NEW_TRANSACTION_METHOD).will(methodStub);
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(isA(OpWorkPeriodVersion.class));
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(same(projectPlanVersion));
        mockTransaction.expects(once()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = planningService.saveActivities((OpProjectSession) mockSession.proxy(), request);
        assertNull("No error message should have been returned", result);
    }

    /**
    * Tests the behaviour of save method a checked in project
    */
    public void testSaveActivitiesLokedBySomeoneElse() {
        XComponent activitiesDataSet = new XComponent(XComponent.DATA_SET);
        String activityOneName = "Activity 1";
        XComponent activity = newActivity(activityOneName);
        activitiesDataSet.addChild(activity);
        XMessage request = createProjectRequest(LOCKED_PROJECT_ID, activitiesDataSet);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).will(methodStub);
        mockSession.expects(once()).method(GET_USER_ID_METHOD).will(methodStub);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = planningService.saveActivities((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result);
        assertNotNull("Error message should have been returned", result.getError());
    }

    /**
    * Tests the behaviour of save method a checked in project
    */
    public void testSaveActivitiesCheckedIn() {
        XComponent activitiesDataSet = new XComponent(XComponent.DATA_SET);
        String activityOneName = "Activity 1";
        XComponent activity = newActivity(activityOneName);
        activitiesDataSet.addChild(activity);
        XMessage request = createProjectRequest(CHECKED_IN_PROJECT, activitiesDataSet);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(CHECKED_IN_PROJECT)).will(methodStub);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = planningService.saveActivities((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result);
        assertNotNull("Error message should have been returned", result.getError());
    }

    /**
    * Tests the checkIn action for a new version of the project with one activity.
    */
    public void testCheckInExistingActivity() {
        project.setPlan(projectPlan);
        OpActivity activityOne = addActivityToProject(project, "ExistingActivityOne");
        activityOne.setType((byte) 2);
        activityOne.setComplete((double) 10);
        XComponent activitiesDataSet = new XComponent(XComponent.DATA_SET);
        String activityName = "Activity 1";
        XComponent activity = newActivity(activityName);
        activity.setStringValue(activityOne.locator());
        OpGanttValidator.setType(activity, (byte) 1);
        OpGanttValidator.setCategory(activity, categoryName);
        OpGanttValidator.setComplete(activity, (byte) 100);
        OpGanttValidator.setStart(activity, XCalendar.today());
        OpGanttValidator.setEnd(activity, new Date(XCalendar.today().getTime() + XCalendar.MILLIS_PER_WEEK * 2));
        OpGanttValidator.setDuration(activity, OpGanttValidator.getEnd(activity).getTime() - OpGanttValidator.getStart(activity).getTime());
        OpGanttValidator.setBaseEffort(activity, 1234);
        OpGanttValidator.setBasePersonnelCosts(activity, 1);
        OpGanttValidator.setBaseExternalCosts(activity, 2);
        OpGanttValidator.setBaseMaterialCosts(activity, 3);
        OpGanttValidator.setBaseTravelCosts(activity, 4);
        OpGanttValidator.setBaseMiscellaneousCosts(activity, 5);
        OpGanttValidator.setDescription(activity, "Tested Activity Descrition");
        OpGanttValidator.setAttributes(activity, (byte) 11);
        activitiesDataSet.addChild(activity);
        XMessage request = createProjectRequest(OK_PROJECT_ID, activitiesDataSet);
        expectPreProjectCheckIn();
        queryResults.clear();
        queryResults.add(new Integer(1));
        mockBroker.expects(atLeastOnce()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(atLeastOnce()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_RESOURCES);
            }
        });
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(isA(OpActivityVersion.class));
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(createActivityConstraint(activity, new ArrayList()));
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(categoryName)).will(methodStub);
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(same(projectPlan));
        expectPostProjectCheckIn();
        XMessage response = planningService.checkInActivities((OpProjectSession) mockSession.proxy(), request);
        assertNull("No errors should have been found on the response, because the project has valid data!", response);
    }

    /**
    * Tests the checkIn action for a new version of the project with one activity.
    */
    public void testCheckInNewActivity() {
        project.setPlan(projectPlan);
        XComponent activitiesDataSet = new XComponent(XComponent.DATA_SET);
        String activityOneName = "Activity 1";
        XComponent activity = newActivity(activityOneName);
        OpGanttValidator.setType(activity, (byte) 1);
        categoryName = "Activity Category";
        OpGanttValidator.setCategory(activity, categoryName);
        OpGanttValidator.setComplete(activity, (double) 100);
        OpGanttValidator.setStart(activity, XCalendar.today());
        OpGanttValidator.setEnd(activity, new Date(XCalendar.today().getTime() + XCalendar.MILLIS_PER_WEEK * 2));
        OpGanttValidator.setDuration(activity, OpGanttValidator.getEnd(activity).getTime() - OpGanttValidator.getStart(activity).getTime());
        OpGanttValidator.setBaseEffort(activity, 1234);
        OpGanttValidator.setBasePersonnelCosts(activity, 1);
        OpGanttValidator.setBaseExternalCosts(activity, 2);
        OpGanttValidator.setBaseMaterialCosts(activity, 3);
        OpGanttValidator.setBaseTravelCosts(activity, 4);
        OpGanttValidator.setBaseMiscellaneousCosts(activity, 5);
        OpGanttValidator.setDescription(activity, "Tested Activity Descrition");
        OpGanttValidator.setAttributes(activity, (byte) 11);
        activitiesDataSet.addChild(activity);
        XMessage request = createProjectRequest(OK_PROJECT_ID, activitiesDataSet);
        expectPreProjectCheckIn();
        expectPersistentActivities(activitiesDataSet);
        queryResults.clear();
        queryResults.add(new Integer(1));
        mockBroker.expects(atLeastOnce()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(atLeastOnce()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_RESOURCES);
            }
        });
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(categoryName)).will(methodStub);
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(same(projectPlan));
        expectPostProjectCheckIn();
        XMessage response = planningService.checkInActivities((OpProjectSession) mockSession.proxy(), request);
        assertNull("No errors should have been found on the response, because the project has valid data!", response);
        assertEquals("Project Name should not have been changed", projectName, project.getName());
        assertEquals("Project Assignments should not have been changed", 0, project.getAssignments().size());
        assertEquals("Project Description should not have been changed", projectDescription, project.getDescription());
        assertEquals("Project WorkBreaks should not have been changed", 0, project.getPlan().getWorkPeriods().size());
    }

    /**
    * Tests if an attachment for an activity is persisted at checkIn time for a project.
    */
    public void testCheckInAttachment() {
        project.setPlan(projectPlan);
        projectPlan.setProjectNode(project);
        XComponent activitiesDataSet = new XComponent(XComponent.DATA_SET);
        String activityName = "Activity 1";
        XComponent activity = newActivity(activityName);
        OpGanttValidator.addWorkPhaseStart(activity, OpGanttValidator.getStart(activity));
        OpGanttValidator.addWorkPhaseFinish(activity, OpGanttValidator.getEnd(activity));
        activitiesDataSet.addChild(activity);
        ArrayList attachmentsArray = new ArrayList();
        ArrayList attachment = new ArrayList();
        attachment.add("f");
        attachment.add("Attachment Name");
        String attName = "Attached File";
        attachment.add(attName);
        String attLocation = "location";
        attachment.add(attLocation);
        attachment.add("0");
        byte[] attContent = new byte[] { 1, 2, 3, 5, 6 };
        attachment.add(attContent);
        attachmentsArray.add(attachment);
        OpGanttValidator.setAttachments(activity, attachmentsArray);
        CONTENT_REF_COUNT = 1;
        XMessage request = createProjectRequest(OK_PROJECT_ID, activitiesDataSet);
        expectPreProjectCheckIn();
        List activitiesList = expectPersistentActivities(activitiesDataSet);
        queryResults.clear();
        queryResults.add(new Integer(1));
        mockBroker.expects(atLeastOnce()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(atLeastOnce()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_RESOURCES);
            }
        });
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(isA(OpContent.class));
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(isA(OpWorkPeriod.class));
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(createAttachmentConstraint(activitiesList, activityName, attContent, attName, attLocation));
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(isA(OpContent.class));
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(same(projectPlan));
        expectPostProjectCheckIn();
        XMessage response = planningService.checkInActivities((OpProjectSession) mockSession.proxy(), request);
        assertNull("No errors should have been found on the response, because the project has valid data!", response);
    }

    /**
    * Tests if a work phase on an existing activity is checked in ok.
    */
    public void testCheckInExistingActivityWorkPhase() {
        project.setPlan(projectPlan);
        OpActivity activityOne = addActivityToProject(project, "ExistingActivityOne");
        activityOne.setComplete(33);
        XComponent activitiesSet = new XComponent(XComponent.DATA_SET);
        String activityName = "ExistingActivityOne";
        XComponent activity = newActivity(activityName);
        activity.setStringValue(activityOne.locator());
        activitiesSet.addChild(activity);
        Calendar calendar = XCalendar.getDefaultCalendar().getCalendar();
        calendar.set(2006, 8, 4);
        Date activityStart = new Date(calendar.getTime().getTime());
        Date activityEnd = new Date(activityStart.getTime() + XCalendar.MILLIS_PER_WEEK * 10);
        OpGanttValidator.setStart(activity, activityStart);
        OpGanttValidator.setEnd(activity, activityEnd);
        double baseEffort = 10 * XCalendar.getDefaultCalendar().getWorkHoursPerDay();
        OpGanttValidator.setDuration(activity, baseEffort);
        OpGanttValidator.setBaseEffort(activity, baseEffort);
        ArrayList workPhaseBegin = new ArrayList();
        ArrayList workPhaseEnd = new ArrayList();
        workPhaseBegin.add(activityStart);
        workPhaseEnd.add(activityEnd);
        OpGanttValidator.setWorkPhaseStarts(activity, workPhaseBegin);
        OpGanttValidator.setWorkPhaseFinishes(activity, workPhaseEnd);
        ArrayList workPhaseEffortsArray = new ArrayList();
        workPhaseEffortsArray.add(new Double(baseEffort));
        OpGanttValidator.setWorkPhaseBaseEfforts(activity, workPhaseEffortsArray);
        XMessage request = createProjectRequest(OK_PROJECT_ID, activitiesSet);
        expectPreProjectCheckIn();
        List activitiesList = new ArrayList();
        activitiesList.add(activityOne);
        queryResults.clear();
        queryResults.add(new Integer(1));
        mockBroker.expects(atLeastOnce()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(atLeastOnce()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_RESOURCES);
            }
        });
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(isA(OpActivityVersion.class));
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(same(activityOne));
        mockBroker.expects(atLeastOnce()).method(MAKE_PERSISTENT_METHOD).with(createWorkPhaseConstraint(activitiesList, activityName));
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(same(projectPlan));
        expectPostProjectCheckIn();
        XMessage response = planningService.checkInActivities((OpProjectSession) mockSession.proxy(), request);
        assertNull("No errors should have been found on the response, because the project has valid data!", response);
    }

    /**
    * Tests if an work phase of a new activity is checked in ok.
    */
    public void testCheckInNewActivityWorkPhase() {
        project.setPlan(projectPlan);
        XComponent activitiesDataSet = new XComponent(XComponent.DATA_SET);
        String activityName = "Activity 1";
        XComponent activity = newActivity(activityName);
        activitiesDataSet.addChild(activity);
        Calendar calendar = XCalendar.getDefaultCalendar().getCalendar();
        calendar.set(2006, 8, 4);
        Date activityStart = new Date(calendar.getTime().getTime());
        Date activityEnd = new Date(activityStart.getTime() + XCalendar.MILLIS_PER_WEEK * 2);
        OpGanttValidator.setStart(activity, activityStart);
        OpGanttValidator.setEnd(activity, activityEnd);
        ArrayList workPhaseBegin = new ArrayList();
        ArrayList workPhaseEnd = new ArrayList();
        workPhaseBegin.add(activityStart);
        workPhaseEnd.add(activityEnd);
        OpGanttValidator.setWorkPhaseStarts(activity, workPhaseBegin);
        OpGanttValidator.setWorkPhaseFinishes(activity, workPhaseEnd);
        int workPhaseEffort = 10;
        ArrayList workPhaseEffortsArray = new ArrayList();
        workPhaseEffortsArray.add(new Double(workPhaseEffort));
        OpGanttValidator.setWorkPhaseBaseEfforts(activity, workPhaseEffortsArray);
        XMessage request = createProjectRequest(OK_PROJECT_ID, activitiesDataSet);
        expectPreProjectCheckIn();
        List activitiesList = expectPersistentActivities(activitiesDataSet);
        queryResults.clear();
        queryResults.add(new Integer(1));
        mockBroker.expects(atLeastOnce()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(atLeastOnce()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_RESOURCES);
            }
        });
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(createWorkPhaseConstraint(activitiesList, activityName));
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(same(projectPlan));
        expectPostProjectCheckIn();
        XMessage response = planningService.checkInActivities((OpProjectSession) mockSession.proxy(), request);
        assertNull("No errors should have been found on the response, because the project has valid data!", response);
    }

    /**
    * Tests if an assignment given by the resources attached to a new project's activity is persisted ok.
    */
    public void testCheckInNewActivityAssignment() {
        project.setPlan(projectPlan);
        XComponent activitiesDataSet = new XComponent(XComponent.DATA_SET);
        String activityName = "Activity 1";
        XComponent activity = newActivity(activityName);
        OpGanttValidator.addWorkPhaseStart(activity, OpGanttValidator.getStart(activity));
        OpGanttValidator.addWorkPhaseFinish(activity, OpGanttValidator.getEnd(activity));
        activitiesDataSet.addChild(activity);
        ArrayList resources = new ArrayList();
        resources.add(RESOURCE_ID);
        OpGanttValidator.setResources(activity, resources);
        OpGanttValidator.setBaseEffort(activity, 10);
        int resourceBaseEffort = 10;
        OpGanttValidator.setBaseEffort(activity, resourceBaseEffort);
        ArrayList resourcesEffortArray = new ArrayList();
        resourcesEffortArray.add(new Double(resourceBaseEffort));
        OpGanttValidator.setResourceBaseEfforts(activity, resourcesEffortArray);
        XMessage request = createProjectRequest(OK_PROJECT_ID, activitiesDataSet);
        expectPreProjectCheckIn();
        queryResults.clear();
        queryResults.add(new Integer(1));
        mockBroker.expects(atLeastOnce()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(atLeastOnce()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(resource);
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_RESOURCES);
            }
        });
        List activitiesList = expectPersistentActivities(activitiesDataSet);
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(createAssignmentConstraint(activitiesList, activityName, resourceBaseEffort, resourceBaseEffort * resource.getHourlyRate()));
        mockBroker.expects(atLeastOnce()).method(MAKE_PERSISTENT_METHOD).with(isA(OpWorkPeriod.class));
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(same(projectPlan));
        expectPostProjectCheckIn();
        XMessage response = planningService.checkInActivities((OpProjectSession) mockSession.proxy(), request);
        assertNull("No errors should have been found on the response, because the project has valid data!", response);
    }

    /**
    * Tests if an assignment given by the resources attached to an existing project's activity is persisted ok.
    */
    public void testCheckInExistingActivityAssignment() {
        project.setPlan(projectPlan);
        OpActivity activityOne = addActivityToProject(project, "ExistingActivityOne");
        HashSet assigns = new HashSet();
        assigns.add(new OpAssignment());
        activityOne.setAssignments(assigns);
        activityOne.setComplete(43);
        XComponent activitiesDataSet = new XComponent(XComponent.DATA_SET);
        String activityName = "ExistingActivityOne";
        XComponent activity = newActivity(activityName);
        OpGanttValidator.addWorkPhaseStart(activity, OpGanttValidator.getStart(activity));
        OpGanttValidator.addWorkPhaseFinish(activity, OpGanttValidator.getEnd(activity));
        activity.setStringValue(activityOne.locator());
        OpGanttValidator.setStart(activity, XCalendar.today());
        OpGanttValidator.setEnd(activity, new Date(XCalendar.today().getTime() + XCalendar.MILLIS_PER_WEEK * 10));
        activitiesDataSet.addChild(activity);
        ArrayList resources = new ArrayList();
        resources.add(RESOURCE_ID);
        OpGanttValidator.setResources(activity, resources);
        int resourceBaseEffort = 10;
        OpGanttValidator.setBaseEffort(activity, resourceBaseEffort);
        ArrayList resourcesEffortArray = new ArrayList();
        resourcesEffortArray.add(new Double(resourceBaseEffort));
        OpGanttValidator.setResourceBaseEfforts(activity, resourcesEffortArray);
        XMessage request = createProjectRequest(OK_PROJECT_ID, activitiesDataSet);
        expectPreProjectCheckIn();
        queryResults.clear();
        queryResults.add(new Integer(1));
        mockBroker.expects(atLeastOnce()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(atLeastOnce()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(resource);
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_RESOURCES);
            }
        });
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(isA(OpActivityVersion.class));
        mockBroker.expects(atLeastOnce()).method(MAKE_PERSISTENT_METHOD).with(isA(OpWorkPeriod.class));
        List activitiesList = new ArrayList();
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(createActivityConstraint(activity, activitiesList));
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(createAssignmentConstraint(activitiesList, activityName, resourceBaseEffort, resourceBaseEffort * resource.getHourlyRate()));
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(same(projectPlan));
        expectPostProjectCheckIn();
        XMessage response = planningService.checkInActivities((OpProjectSession) mockSession.proxy(), request);
        assertNull("No errors should have been found on the response, because the project has valid data!", response);
    }

    /**
    * Tests if a dependency between two new activities is persisted at check in time for a project.
    */
    public void testCheckInNewDependency() {
        project.setPlan(projectPlan);
        XComponent activitiesDataSet = new XComponent(XComponent.DATA_SET);
        String activityOneName = "Activity 1";
        XComponent activityOne = newActivity(activityOneName);
        OpGanttValidator.addWorkPhaseStart(activityOne, OpGanttValidator.getStart(activityOne));
        OpGanttValidator.addWorkPhaseFinish(activityOne, OpGanttValidator.getEnd(activityOne));
        String activityTwoName = "Activity 2";
        XComponent activityTwo = newActivity(activityTwoName);
        OpGanttValidator.addWorkPhaseStart(activityTwo, OpGanttValidator.getStart(activityTwo));
        OpGanttValidator.addWorkPhaseFinish(activityTwo, OpGanttValidator.getEnd(activityTwo));
        activitiesDataSet.addChild(activityOne);
        activitiesDataSet.addChild(activityTwo);
        ArrayList succ = new ArrayList();
        succ.add(new Integer(1));
        OpGanttValidator.setSuccessors(activityOne, succ);
        ArrayList pred = new ArrayList();
        pred.add(new Integer(0));
        OpGanttValidator.setPredecessors(activityTwo, pred);
        XMessage request = createProjectRequest(OK_PROJECT_ID, activitiesDataSet);
        expectPreProjectCheckIn();
        List activitiesList = expectPersistentActivities(activitiesDataSet);
        queryResults.clear();
        queryResults.add(new Integer(1));
        mockBroker.expects(atLeastOnce()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(atLeastOnce()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_RESOURCES);
            }
        });
        mockBroker.expects(atLeastOnce()).method(MAKE_PERSISTENT_METHOD).with(isA(OpWorkPeriod.class));
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(createDependencyConstraint(project, activitiesList, activityOneName, activityTwoName));
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(same(projectPlan));
        expectPostProjectCheckIn();
        XMessage response = planningService.checkInActivities((OpProjectSession) mockSession.proxy(), request);
        assertNull("No errors should have been found on the response, because the project has valid data!", response);
    }

    /**
    * Tests if a new dependency between two existing activities is persisted
    */
    public void testCheckInExistingActivityDependency() {
        project.setPlan(projectPlan);
        OpActivity activityOne = addActivityToProject(project, "ExistingActivityOne");
        OpActivity activityTwo = addActivityToProject(project, "ExistingActivityTwo");
        activityTwo.setStart(new Date(XCalendar.today().getTime() + XCalendar.MILLIS_PER_WEEK * 11));
        activityTwo.setFinish(new Date(XCalendar.today().getTime() + XCalendar.MILLIS_PER_WEEK * 20));
        OpDependency dependencyOne = new OpDependency();
        dependencyOne.setProjectPlan(projectPlan);
        dependencyOne.setSuccessorActivity(activityTwo);
        dependencyOne.setPredecessorActivity(activityOne);
        Set dependeciesSet = new HashSet();
        dependeciesSet.add(dependencyOne);
        projectPlan.setDependencies(dependeciesSet);
        XComponent activitiesDataSet = new XComponent(XComponent.DATA_SET);
        String activityOneName = "ExistingActivityOne";
        XComponent activityOneReq = newActivity(activityOneName);
        activityOneReq.setStringValue(activityOne.locator());
        OpGanttValidator.setStart(activityOneReq, XCalendar.today());
        OpGanttValidator.setEnd(activityOneReq, new Date(XCalendar.today().getTime() + XCalendar.MILLIS_PER_WEEK * 5));
        OpGanttValidator.addWorkPhaseStart(activityOneReq, OpGanttValidator.getStart(activityOneReq));
        OpGanttValidator.addWorkPhaseFinish(activityOneReq, OpGanttValidator.getEnd(activityOneReq));
        String activityTwoName = "ExistingActivityTwo";
        XComponent activityTwoReq = newActivity(activityTwoName);
        activityTwoReq.setStringValue(activityTwo.locator());
        OpGanttValidator.setStart(activityTwoReq, new Date(XCalendar.today().getTime() + XCalendar.MILLIS_PER_WEEK * 6));
        OpGanttValidator.setEnd(activityTwoReq, new Date(XCalendar.today().getTime() + XCalendar.MILLIS_PER_WEEK * 7));
        OpGanttValidator.addWorkPhaseStart(activityTwoReq, OpGanttValidator.getStart(activityOneReq));
        OpGanttValidator.addWorkPhaseFinish(activityTwoReq, OpGanttValidator.getEnd(activityOneReq));
        ArrayList succ = new ArrayList();
        succ.add(new Integer(1));
        OpGanttValidator.setSuccessors(activityOneReq, succ);
        ArrayList pred = new ArrayList();
        pred.add(new Integer(0));
        OpGanttValidator.setPredecessors(activityTwoReq, pred);
        activitiesDataSet.addChild(activityOneReq);
        activitiesDataSet.addChild(activityTwoReq);
        XMessage request = createProjectRequest(OK_PROJECT_ID, activitiesDataSet);
        expectPreProjectCheckIn();
        queryResults.clear();
        queryResults.add(new Integer(1));
        mockBroker.expects(atLeastOnce()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(atLeastOnce()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_RESOURCES);
            }
        });
        mockBroker.expects(atLeastOnce()).method(MAKE_PERSISTENT_METHOD).with(isA(OpActivityVersion.class));
        mockBroker.expects(atLeastOnce()).method(MAKE_PERSISTENT_METHOD).with(isA(OpWorkPeriod.class));
        mockBroker.expects(atLeastOnce()).method(MAKE_PERSISTENT_METHOD).with(isA(OpDependencyVersion.class));
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(same(activityOne));
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(same(activityTwo));
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(same(projectPlan));
        expectPostProjectCheckIn();
        XMessage response = planningService.checkInActivities((OpProjectSession) mockSession.proxy(), request);
        assertNull("No errors should have been found on the response, because the project has valid data!", response);
    }

    /**
    * Tests the "happy-flow" of project check in action. No activities are associated with this project
    */
    public void testCheckInNoActivities() {
        XComponent activities = new XComponent(XComponent.DATA_SET);
        XMessage request = createProjectRequest(OK_PROJECT_ID, activities);
        project.setPlan(projectPlan);
        expectPreProjectCheckIn();
        queryResults.clear();
        queryResults.add(null);
        mockBroker.expects(atLeastOnce()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(createProjectPlanConstraint(project));
        mockBroker.expects(atLeastOnce()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_RESOURCES);
            }
        });
        expectPostProjectCheckIn();
        XMessage response = planningService.checkInActivities((OpProjectSession) mockSession.proxy(), request);
        assertNull("No errors should have been found on the response, because the project has valid data!", response);
    }

    /**
    * Tests that an already checked in project can't be checked in again.
    */
    public void testCheckInCheckedInProject() {
        XMessage request = createProjectRequest(CHECKED_IN_PROJECT, null);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).will(methodStub);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        mockBroker.expects(never()).method(NEW_TRANSACTION_METHOD);
        XMessage response = planningService.checkInActivities((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("There should be errors on the response, because the project is already checked in !", response);
        assertNotNull("There should be errors on the response, because the project is already checked in !", response.getError());
    }

    /**
    * Tests that an error is returned if a non-exitent project is being checked in.
    *
    * @throws Exception if anything fails.
    */
    public void testCheckInNotExistentProject() throws Exception {
        XMessage request = createProjectRequest(NONEXISTENT_PROJECT_ID, null);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).will(methodStub);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage response = planningService.checkInActivities((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("There should be errors on the response because the project does not exist !", response);
        assertNotNull("Error message should have been returned because the project does not exist !", response.getError());
    }

    /**
    * Tests that the project administrationService cannot be checked in if it is locked by someone else than the user
    * trying to do the checkin.
    *
    * @throws Exception if anything fails.
    */
    public void testCheckInLockedProject() throws Exception {
        XMessage request = createProjectRequest(LOCKED_PROJECT_ID, null);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).will(methodStub);
        mockSession.expects(once()).method(GET_USER_ID_METHOD).will(methodStub);
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage response = planningService.checkInActivities((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("There should be errors on the response, because the project is locked by someone else !", response);
        assertNotNull("There should be errors on the response, because the project is locked by someone else !", response.getError());
        assertEquals("Error should be the one that was set on new error call", error, response.getError());
    }

    /**
    * Creates a new "request" containing the input parameters that will be received by a project administrationService.
    *
    * @param projectId   a <code>String</code> representing the id of a project.
    * @param activitySet a <code>String</code> representing the id of a <code>XComponent(DATA_SET)</code>
    * @return a <code>XMessage</code> instance, representing the request that is made by the client to the project administrationService.
    */
    private XMessage createProjectRequest(String projectId, XComponent activitySet) {
        XMessage result = new XMessage();
        result.setArgument(OpProjectAdministrationService.PROJECT_ID, projectId);
        result.setArgument(OpProjectPlanningService.ACTIVITY_SET, activitySet);
        return result;
    }

    /**
    * Adds a new activity to the XProjectPlna of the <code>project</code> with default values - may be changed in tests.
    *
    * @param project a <code>XProject</code>
    * @param name    a <code>String</code> representoing the activity's name
    * @return the new created <code>OpActivity</code>.
    */
    private OpActivity addActivityToProject(OpProjectNode project, String name) {
        HashSet activities = (HashSet) project.getPlan().getActivities();
        OpActivity activity = new OpActivity();
        activity.setName(name);
        activity.setDescription("Activity Description");
        activity.setAssignments(new HashSet());
        activity.setAttachments(new HashSet());
        activity.setPermissions(new HashSet());
        activity.setProjectPlan(project.getPlan());
        activity.setWorkPeriods(new HashSet());
        activity.setSequence(project.getPlan().getActivities().size());
        activity.setID(project.getPlan().getActivities().size() + 1);
        activity.setStart(XCalendar.today());
        activity.setFinish(new Date(XCalendar.today().getTime() + XCalendar.MILLIS_PER_WEEK * 10));
        activity.setAttributes(0);
        byte priority = 0;
        activity.setPriority(priority);
        activities.add(activity);
        return activity;
    }

    /**
    * Creates a new assignment constrait to check if an assignment is the expected one.
    *
    * @param activityList a <code>List</code> of <code>OpActivity</code> from project
    * @param activityName
    * @return a new Constraint
    */
    private Constraint createAssignmentConstraint(final List activityList, final String activityName, final double baseEffort, final double baseCost) {
        return new Constraint() {

            public boolean eval(Object object) {
                if (!(object instanceof OpAssignment)) {
                    return false;
                }
                OpAssignment assignment = (OpAssignment) object;
                if (!(assignment.getActivity().equals(getActivityByName(activityList, activityName)))) {
                    return false;
                }
                if (assignment.getBaseEffort() != baseEffort) {
                    return false;
                }
                if (assignment.getBaseCosts() != baseCost) {
                    return false;
                }
                return assignment.getResource() == resource;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Checks if an assignment is the expected one");
            }
        };
    }

    /**
    * Creates a set of expectencies for all the activities in a given data set that would become persistent.
    *
    * @param dataSet <code>XComponent</code> that represents the data set with the activities
    * @return a collector <code>List</code> that will hold at run time all the <code>OpActivity<code> that were created
    */
    private List expectPersistentActivities(XComponent dataSet) {
        List activitiesList = new ArrayList();
        for (int i = 0; i < dataSet.getChildCount(); i++) {
            XComponent activity = (XComponent) dataSet.getChild(i);
            mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(createActivityConstraint(activity, activitiesList)).will(methodStub);
        }
        return activitiesList;
    }

    /**
    * Creates common expectencies in test case expectencies.
    */
    private void expectPostProjectCheckIn() {
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PLAN_VERSION_FOR_PROJECT)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                return query;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PLAN_VERSION_FOR_PROJECT);
            }
        });
        mockQuery.expects(atLeastOnce()).method(SET_INTEGER_METHOD);
        mockBroker.expects(once()).method(DELETE_OBJECT_METHOD).with(same(projectLock));
        mockTransaction.expects(once()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
    }

    /**
    * Creates common expectencies in test case expectencies.
    */
    private void expectPreProjectCheckIn() {
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).will(methodStub);
        mockSession.expects(once()).method(GET_USER_ID_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_TRANSACTION_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_MAX_VERSION_NUMBER_FOR_PROJECT)).will(methodStub);
        mockQuery.expects(atLeastOnce()).method(SET_LONG_METHOD);
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(isA(OpProjectPlanVersion.class));
        mockBroker.expects(atLeastOnce()).method(NEW_QUERY_METHOD).with(eq(SELECT_PROJECT_RESOURCES)).will(methodStub);
    }

    /**
    * Creates a new constraint for an attachment in order to see if an attachment is the expected one.
    *
    * @return true if the constraint is respected
    */
    private Constraint createAttachmentConstraint(final List activities, final String activityName, final byte[] blob, final String name, final String location) {
        return new Constraint() {

            public boolean eval(Object object) {
                if (!(object instanceof OpAttachment)) {
                    return false;
                }
                OpAttachment att = (OpAttachment) object;
                if (!(att.getContent().getBytes().length == blob.length)) {
                    return false;
                }
                if (att.getContent().getRefCount() != CONTENT_REF_COUNT) {
                    return false;
                }
                if (!att.getLocation().equals(location)) {
                    return false;
                }
                if (!att.getName().equals(name)) {
                    return false;
                }
                return att.getActivity().equals(getActivityByName(activities, activityName));
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Checks if an attachment is the expected one");
            }
        };
    }

    /**
    * Creates a new work phase constraint in order to test if a work phase is the expected one at run time.
    *
    * @param activitiesList <code>List</code> of <code>OpActivity</code> from the project
    * @param activityName   the activity name for wich is the work phase
    * @return a new work break Constraint
    */
    private Constraint createWorkPhaseConstraint(final List activitiesList, final String activityName) {
        return new Constraint() {

            public boolean eval(Object object) {
                if (!(object instanceof OpWorkPeriod)) {
                    return false;
                }
                OpWorkPeriod workPeriod = (OpWorkPeriod) object;
                OpActivity expectedActivity = getActivityByName(activitiesList, activityName);
                if (!workPeriod.getActivity().equals(expectedActivity)) {
                    return false;
                }
                Double expectedEffort = new Double(expectedActivity.getBaseEffort() / (expectedActivity.getDuration() / XCalendar.getDefaultCalendar().getWorkHoursPerDay()));
                return new Double(workPeriod.getBaseEffort()).equals(expectedEffort);
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Checks if a work period is the expected one.");
            }
        };
    }

    /**
    * Creates a new constraint for a lock.
    *
    * @param user the user that is expected to own the lock
    * @return e new constraint for xlock objects.
    */
    private Constraint createLockConstraint(final OpSubject user, final Object target) {
        return new Constraint() {

            public boolean eval(Object object) {
                if (!(object instanceof OpLock)) {
                    return false;
                }
                OpLock lock = (OpLock) object;
                if (lock.getOwner() != user) {
                    return false;
                }
                return (lock.getTarget() == target);
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("checks if a lock entity is the expected one");
            }
        };
    }

    /**
    * Creates a new dependency constraint
    *
    * @param project    the expected project to be found on the dependency
    * @param activities the list off all project activities
    * @param predName   the name of the predecessor for the dependency
    * @param succName   the name of the successor for the dependency
    * @return true if the constraint is respected
    */
    private Constraint createDependencyConstraint(final OpProjectNode project, final List activities, final String predName, final String succName) {
        return new Constraint() {

            public boolean eval(Object object) {
                if (!(object instanceof OpDependency)) {
                    return false;
                }
                OpDependency dep = (OpDependency) object;
                OpActivity pred = getActivityByName(activities, predName);
                OpActivity succ = getActivityByName(activities, succName);
                if (!dep.getPredecessorActivity().equals(pred)) {
                    return false;
                }
                if (!dep.getSuccessorActivity().equals(succ)) {
                    return false;
                }
                return (dep.getProjectPlan() == project.getPlan());
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Checks if a dependency is the expected one");
            }
        };
    }

    /**
    * Creates a constraint for an activity.
    *
    * @param activityDataRow data set with the expected values to be found on the activity
    * @return a new Constraint object that will check if an activity is the expected one
    */
    private Constraint createActivityConstraint(final XComponent activityDataRow, final List persistedActivities) {
        return new Constraint() {

            public boolean eval(Object object) {
                if (!(object instanceof OpActivity)) {
                    return false;
                }
                OpActivity activity = (OpActivity) object;
                if (persistedActivities != null) {
                    persistedActivities.add(activity);
                }
                if (!activity.getName().equals(OpGanttValidator.getName(activityDataRow))) {
                    return false;
                }
                if (!activity.getDescription().equals(OpGanttValidator.getDescription(activityDataRow))) {
                    return false;
                }
                if (activity.getOutlineLevel() != activityDataRow.getOutlineLevel()) {
                    return false;
                }
                if (!activity.getStart().equals(OpGanttValidator.getStart(activityDataRow))) {
                    return false;
                }
                if (!activity.getFinish().equals(OpGanttValidator.getEnd(activityDataRow))) {
                    return false;
                }
                if (activity.getDuration() != OpGanttValidator.getDuration(activityDataRow)) {
                    return false;
                }
                if (activity.getBaseEffort() != OpGanttValidator.getBaseEffort(activityDataRow)) {
                    return false;
                }
                if (activity.getCategory() != category) {
                    return false;
                }
                if (activity.getBaseExternalCosts() != OpGanttValidator.getBaseExternalCosts(activityDataRow)) {
                    return false;
                }
                if (activity.getBaseMaterialCosts() != OpGanttValidator.getBaseMaterialCosts(activityDataRow)) {
                    return false;
                }
                if (activity.getBaseMiscellaneousCosts() != OpGanttValidator.getBaseMiscellaneousCosts(activityDataRow)) {
                    return false;
                }
                if (activity.getBasePersonnelCosts() != OpGanttValidator.getBasePersonnelCosts(activityDataRow)) {
                    return false;
                }
                if (activity.getBaseTravelCosts() != OpGanttValidator.getBaseTravelCosts(activityDataRow)) {
                    return false;
                }
                return activity.getType() == OpGanttValidator.getType(activityDataRow);
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Check if an activity is the expected one");
            }
        };
    }

    /**
    * Creates a new activity
    *
    * @return an <code>XComponent.DATA_ROW</code> represeting activity
    */
    private XComponent newActivity(String name) {
        XComponent data_row = new XComponent(XComponent.DATA_ROW);
        XComponent data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setStringValue(name);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setByteValue(OpGanttValidator.STANDARD);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setDoubleValue(0);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        Date start = XCalendar.today();
        data_cell.setDateValue(start);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setDateValue(start);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setDoubleValue(0);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setDoubleValue(0);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setListValue(new ArrayList());
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setListValue(new ArrayList());
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setListValue(new ArrayList());
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setDoubleValue(0);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setDoubleValue(0);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setDoubleValue(0);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setDoubleValue(0);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setDoubleValue(0);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setStringValue("Activity Description");
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setListValue(new ArrayList());
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setIntValue(0);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setListValue(new ArrayList());
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setListValue(new ArrayList());
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setListValue(new ArrayList());
        data_cell.setEnabled(true);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setByteValue((byte) 0);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(false);
        data_cell.setValue(new HashMap());
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(false);
        data_cell.setDoubleValue(0);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(true);
        data_cell.setListValue(new ArrayList());
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(false);
        data_cell.setStringValue(null);
        data_row.addChild(data_cell);
        data_cell = new XComponent(XComponent.DATA_CELL);
        data_cell.setEnabled(false);
        data_cell.setStringValue(null);
        data_row.addChild(data_cell);
        return data_row;
    }

    /**
    * Finds a <code>OpActivity</code> in the given <code>List</code> by name.
    *
    * @param activities   the <code>List</code> of activities where the search is made
    * @param activityName <code>String</code> the activity name
    * @return the found <code>OpActivity</code>
    * @throws IllegalArgumentException if the activityName is not found in the <code>List</code>
    */
    private OpActivity getActivityByName(List activities, String activityName) {
        OpActivity activity = new OpActivity();
        for (int i = 0; i < activities.size(); i++) {
            if (((OpActivity) activities.get(i)).getName().equals(activityName)) {
                activity = (OpActivity) activities.get(i);
            }
        }
        if (activity == null) {
            throw new IllegalArgumentException("No such activity was found in the given List!");
        }
        return activity;
    }

    /**
    * Creates a new constraint (anonymous inner class) that can be used to check if a project plan  is the expected one.
    *
    * @return a new project plan constraint
    */
    private Constraint createProjectPlanConstraint(final OpProjectNode project) {
        return new Constraint() {

            public boolean eval(Object object) {
                if (!(object instanceof OpProjectPlan)) {
                    return false;
                }
                OpProjectPlan projectPlan = (OpProjectPlan) object;
                if (project.getPlan() != projectPlan) {
                    return false;
                }
                if (!project.getStart().equals(projectPlan.getStart())) {
                    return false;
                }
                return (project.getFinish().equals(projectPlan.getFinish()));
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Will check if a project plan is the expected one");
            }
        };
    }
}
