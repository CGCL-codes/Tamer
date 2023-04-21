package onepoint.project.modules.project.test;

import onepoint.express.XComponent;
import onepoint.project.OpInitializer;
import onepoint.project.OpProjectSession;
import onepoint.project.modules.project.*;
import onepoint.project.modules.user.OpGroup;
import onepoint.project.modules.user.OpPermission;
import onepoint.project.modules.user.OpPermissionSetFactory;
import onepoint.project.modules.user.OpUser;
import onepoint.project.test.OpBaseMockTestCase;
import onepoint.service.XMessage;
import onepoint.util.XCalendar;
import org.jmock.core.Constraint;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;
import java.sql.Date;
import java.util.*;

public class OpProjectAdministrationServiceMockTest extends OpBaseMockTestCase {

    /**
    * Various predefined project id that will be used in the tests.
    */
    private static final String NONEXISTENT_PROJECT_ID = "OpProjectNode.1206.xid";

    private static final String NONEXISTENT_PORTFOLIO_ID = "OpProjectNode.1456.xid";

    /**
    * The project administrationService object that is being tested.
    */
    protected OpProjectAdministrationService administrationService = null;

    /**
    * A user that acts as the logged on user.
    */
    private OpUser sessionUser = null;

    private OpGroup everyone = null;

    /**
    * Project that is being tested.
    */
    private String projectName;

    private String projectDescription;

    private OpProjectNode project;

    /**
    * Project plan for project
    */
    protected OpProjectPlan projectPlan;

    /**
    * Portfolio that is being tested
    */
    private String portfolioName;

    private String portfolioDescription;

    private OpProjectNode portfolio;

    private OpProjectNode superPortfolio;

    /**
    * Portfolio/Project/Project Template String id
    */
    protected final String PORTFOLIO_ID = "OpProjectNode.1897.xid";

    protected final String PROJECT_ID = "OpProjectNode.1500.xid";

    private final int SESSION_USER_ID_LONG = 666;

    protected final String SESSION_USED_ID = "OpUser." + SESSION_USER_ID_LONG + ".xid";

    private final int EVERYONE_ID_LONG = 222;

    protected final String EVERYONE_GROUP_ID = "OpGroup." + EVERYONE_ID_LONG + ".xid";

    /**
    * HQL
    */
    protected static final String SELECT_PROJECT_BY_NAME = "select project from OpProjectNode project where project.Name = ?";

    protected static final String SELECT_PROJECT_FIRST_ACTIVITY_START = "select min(activity.Start) from OpActivity as activity where activity.ProjectPlan.ID = ?";

    private static final String SELECT_PROJECT_PORTFOLIO_BY_NAME = "select project from OpProjectNode project where project.Name = ?";

    private static final String SELECT_ROOT_PORTFOLIO_BY_NAME = "select portfolio from OpProjectNode as portfolio where portfolio.Name = ? and portfolio.Type = ?";

    private static final String SELECT_PORTFOLIO_ID_BY_PROJECT_ID = "select project.SuperNode.ID from OpProjectNode as project " + "where project.ID in (:projectIds) and project.Type = (:projectType)";

    private static final String SELECT_SUPER_PORTFOLIO_ID_BY_PORTFOLIO_ID = "select portfolio.SuperNode.ID from OpProjectNode as portfolio " + "where portfolio.ID in (:portfolioIds) and portfolio.Type = (:projectType)";

    private static final String SELECT_ACCESSIBLE_PROJECTS = "select project from OpProjectNode as project " + "where project.ID in (:projectIds) and project.SuperNode.ID in (:accessiblePortfolioIds)";

    private static final String SELECT_ACCESSIBLE_PORTFOLIOS = "select portfolio from OpProjectNode as portfolio " + "where portfolio.ID in (:portfolioIds) and portfolio.SuperNode.ID in (:accessibleSuperPortfolioIds)";

    private static final String SELECT_PROJECT_WORK_RECORDS = "select count(workrecord) from OpProjectPlan projectPlan join projectPlan.Activities activity join activity.Assignments assignment join assignment.WorkRecords workrecord where projectPlan.ID = ?";

    /**
    * @see onepoint.project.test.OpBaseMockTestCase#setUp()
    */
    protected void setUp() {
        super.setUp();
        administrationService = getTestedService();
        sessionUser = new OpUser();
        sessionUser.setID(SESSION_USER_ID_LONG);
        sessionUser.setLevel(new Byte(OpUser.MANAGER_USER_LEVEL));
        everyone = new OpGroup();
        everyone.setID(EVERYONE_ID_LONG);
        everyone.setUserAssignments(new HashSet());
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
        superPortfolio = new OpProjectNode();
        superPortfolio.setType(OpProjectNode.PORTFOLIO);
        superPortfolio.setName(portfolioName);
        superPortfolio.setDescription(portfolioDescription);
        superPortfolio.setID(1);
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
        queryResults = new ArrayList();
    }

    protected OpProjectAdministrationService getTestedService() {
        return new OpProjectAdministrationService();
    }

    /**
    * @see onepoint.project.test.OpBaseMockTestCase#invocationMatch(org.jmock.core.Invocation)
    */
    public Object invocationMatch(Invocation invocation) throws IllegalArgumentException {
        String methodName = invocation.invokedMethod.getName();
        if (methodName.equals(GET_OBJECT_METHOD)) {
            String entityId = (String) invocation.parameterValues.get(0);
            if (entityId.equals(PORTFOLIO_ID)) {
                return portfolio;
            } else if (entityId.equals(PROJECT_ID)) {
                return project;
            } else if (entityId.equals(SESSION_USED_ID)) {
                return sessionUser;
            } else if (entityId.equals(EVERYONE_GROUP_ID)) {
                return everyone;
            } else if (entityId.equals(NONEXISTENT_PROJECT_ID) || entityId.equals(NONEXISTENT_PORTFOLIO_ID)) {
                return null;
            }
        } else if (methodName.equals(USER_METHOD)) {
            return sessionUser;
        } else if (methodName.equals(EVERYONE_METHOD)) {
            return everyone;
        } else if (methodName.equals(CHECK_ACCESS_LEVEL_METHOD)) {
            return Boolean.TRUE;
        } else if (methodName.equals(ACCESSIBLE_IDS_METHOD)) {
            Set accessiblePortfolioIds = new HashSet();
            accessiblePortfolioIds.add(new Long(portfolio.getID()));
            return accessiblePortfolioIds;
        }
        throw new IllegalArgumentException("Invalid method name:" + methodName + " for this stub");
    }

    /**
    * Tests that a new project will be corectly inserted provided that the project data is correct
    */
    public void testInsertNewProject() {
        XMessage request = new XMessage();
        Map projectData = creatProjectData("ProjectName", "ProjectDescription", XCalendar.today(), XCalendar.today(), 0);
        request.setArgument(OpProjectAdministrationService.PROJECT_DATA, projectData);
        XComponent goalsDataSet = new XComponent(XComponent.DATA_SET);
        XComponent goal = createGoalData(false, "Goal One Subject", 123);
        goalsDataSet.addChild(goal);
        request.setArgument(OpProjectAdministrationService.GOALS_SET, goalsDataSet);
        XComponent toDoDataSet = new XComponent(XComponent.DATA_SET);
        XComponent todo = createTodoData(false, "ToDo One Subject", 123, new Date(10000));
        toDoDataSet.addChild(todo);
        request.setArgument(OpProjectAdministrationService.TO_DOS_SET, toDoDataSet);
        Constraint testProjectNode = createProjectNodeConstraint(projectData);
        queryResults.clear();
        expectCreateAdminPermission();
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PROJECT_BY_NAME)).will(methodStub);
        mockQuery.expects(once()).method(SET_STRING_METHOD).with(new Constraint[] { eq(0), eq(projectData.get(OpProjectNode.NAME)) });
        mockBroker.expects(once()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(String.valueOf(PORTFOLIO_ID))).will(methodStub);
        mockSession.expects(once()).method(CHECK_ACCESS_LEVEL_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_TRANSACTION_METHOD).will(methodStub);
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(testProjectNode);
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(isA(OpProjectPlan.class));
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(createGoalConstraint(goalsDataSet));
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(createToDoConstraint(toDoDataSet));
        mockTransaction.expects(once()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        assertNoError(administrationService.insertProject((OpProjectSession) mockSession.proxy(), request));
    }

    protected void expectCreateAdminPermission() {
        if (!OpInitializer.isMultiUser()) {
            mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(OpUser.ADMINISTRATOR_ID_QUERY)).will(methodStub);
            List adminList = new ArrayList();
            adminList.add(new Long(SESSION_USER_ID_LONG));
            mockBroker.expects(once()).method(LIST_METHOD).with(same(query)).will(returnValue(adminList));
            mockBroker.expects(once()).method(GET_OBJECT_METHOD).will(returnValue(sessionUser));
            mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(isA(OpPermission.class));
        }
    }

    /**
    * Creates a goal data component
    *
    * @param complete If the goal was completed
    * @param subject  The subject of this goal
    * @param priority The priority of the goal
    * @return <code>XComponent<code> representing a DATA_ROW with data for a goal.
    */
    private XComponent createGoalData(boolean complete, String subject, int priority) {
        XComponent dataCell;
        XComponent goal = new XComponent(XComponent.DATA_ROW);
        dataCell = new XComponent(XComponent.DATA_CELL);
        dataCell.setBooleanValue(complete);
        goal.addChild(dataCell);
        dataCell = new XComponent(XComponent.DATA_CELL);
        dataCell.setStringValue(subject);
        goal.addChild(dataCell);
        dataCell = new XComponent(XComponent.DATA_CELL);
        dataCell.setIntValue(priority);
        goal.addChild(dataCell);
        return goal;
    }

    /**
    * Creates a to do data component
    *
    * @param complete If the to do was completed
    * @param subject  The subject of this to do
    * @param priority The priority of the to do
    * @param due
    * @return <code>XComponent<code> representing a DATA_ROW with data for a to do.
    */
    private XComponent createTodoData(boolean complete, String subject, int priority, Date due) {
        XComponent dataCell;
        XComponent todo = new XComponent(XComponent.DATA_ROW);
        dataCell = new XComponent(XComponent.DATA_CELL);
        dataCell.setBooleanValue(complete);
        todo.addChild(dataCell);
        dataCell = new XComponent(XComponent.DATA_CELL);
        dataCell.setStringValue(subject);
        todo.addChild(dataCell);
        dataCell = new XComponent(XComponent.DATA_CELL);
        dataCell.setIntValue(priority);
        todo.addChild(dataCell);
        dataCell = new XComponent(XComponent.DATA_CELL);
        dataCell.setDateValue(due);
        todo.addChild(dataCell);
        return todo;
    }

    /**
    * Tests the behavior of insertProject for an invalid project data.
    */
    public void testInsertProjectWrongData() {
        XComponent goals = new XComponent(XComponent.DATA_SET);
        XComponent toDos = new XComponent(XComponent.DATA_SET);
        checkInsertProjectWrongData("", "projectDescription", XCalendar.today(), XCalendar.today(), 0, goals, toDos);
        checkInsertProjectWrongData("projectName", "projectDescription", null, XCalendar.today(), 0, goals, toDos);
        checkInsertProjectWrongData("projectName", "projectDescription", XCalendar.today(), new Date(XCalendar.today().getTime() - XCalendar.MILLIS_PER_WEEK), 0, goals, toDos);
        Date startDate = new Date(XCalendar.today().getTime() + XCalendar.MILLIS_PER_DAY);
        checkInsertProjectWrongData("projectName", "projectDescription", startDate, XCalendar.today(), 0, goals, toDos);
        checkInsertProjectWrongData("projectName", "projectDescription", XCalendar.today(), XCalendar.today(), -5, goals, toDos);
        goals = new XComponent(XComponent.DATA_SET);
        toDos = new XComponent(XComponent.DATA_SET);
        goals.addChild(createGoalData(true, "GoalSubject", -10));
        checkInsertProjectToDosOrGoalsWrongData("projectName", "projectDescription", XCalendar.today(), XCalendar.today(), 0, goals, toDos);
        goals = new XComponent(XComponent.DATA_SET);
        toDos = new XComponent(XComponent.DATA_SET);
        toDos.addChild(createTodoData(true, "ToDoSubject", -1, XCalendar.today()));
        checkInsertProjectToDosOrGoalsWrongData("projectName", "projectDescription", XCalendar.today(), XCalendar.today(), 0, goals, toDos);
    }

    /**
    * Tests the behavior of insertProject for an invalid project data.
    */
    private void checkInsertProjectWrongData(String projectName, String projectDescription, Date startDate, Date endDate, double projectBudget, XComponent goals, XComponent todos) {
        XMessage request = new XMessage();
        Map projectData = creatProjectData(projectName, projectDescription, startDate, endDate, projectBudget);
        request.setArgument(OpProjectAdministrationService.PROJECT_DATA, projectData);
        request.setArgument(OpProjectAdministrationService.GOALS_SET, goals);
        request.setArgument(OpProjectAdministrationService.TO_DOS_SET, todos);
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockSession.expects(never()).method(NEW_BROKER_METHOD);
        mockBroker.expects(never()).method(NEW_TRANSACTION_METHOD);
        mockBroker.expects(never()).method(MAKE_PERSISTENT_METHOD);
        mockTransaction.expects(never()).method(COMMIT_METHOD);
        mockBroker.expects(never()).method(CLOSE_METHOD);
        XMessage result = administrationService.insertProject((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result.getError());
        assertEquals("Error should be the one that was set on new error call", error, result.getError());
    }

    /**
    * Tests the behavior of insertProject for an invalid project data todos and goals.
    */
    private void checkInsertProjectToDosOrGoalsWrongData(String projectName, String projectDescription, Date startDate, Date endDate, double projectBudget, XComponent goals, XComponent todos) {
        XMessage request = new XMessage();
        Map projectData = creatProjectData(projectName, projectDescription, startDate, endDate, projectBudget);
        request.setArgument(OpProjectAdministrationService.PROJECT_DATA, projectData);
        request.setArgument(OpProjectAdministrationService.GOALS_SET, goals);
        request.setArgument(OpProjectAdministrationService.TO_DOS_SET, todos);
        queryResults.clear();
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PROJECT_BY_NAME)).will(methodStub);
        mockQuery.expects(once()).method(SET_STRING_METHOD).with(new Constraint[] { eq(0), eq(projectData.get(OpProjectNode.NAME)) });
        mockBroker.expects(once()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(String.valueOf(PORTFOLIO_ID))).will(methodStub);
        mockSession.expects(once()).method(CHECK_ACCESS_LEVEL_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_TRANSACTION_METHOD).will(methodStub);
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(createProjectNodeConstraint(projectData));
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(isA(OpProjectPlan.class));
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockTransaction.expects(once()).method(ROLLBACK_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        mockTransaction.expects(never()).method(COMMIT_METHOD);
        XMessage result = administrationService.insertProject((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result.getError());
        assertEquals("Error should be the one that was set on new error call", error, result.getError());
    }

    /**
    * Tests the behavior of insertProject for an already existent project data.
    */
    public void testInsertAlreadyExistentProject() {
        XMessage request = new XMessage();
        Map projectData = creatProjectData(projectName, "NewProjectDescription", XCalendar.today(), XCalendar.today(), 0);
        request.setArgument(OpProjectAdministrationService.PROJECT_DATA, projectData);
        XComponent goalsDataSet = new XComponent(XComponent.DATA_SET);
        request.setArgument(OpProjectAdministrationService.GOALS_SET, goalsDataSet);
        XComponent toDoDataSet = new XComponent(XComponent.DATA_SET);
        request.setArgument(OpProjectAdministrationService.TO_DOS_SET, toDoDataSet);
        queryResults.clear();
        queryResults.add(project);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PROJECT_BY_NAME)).will(methodStub);
        mockQuery.expects(once()).method(SET_STRING_METHOD).with(new Constraint[] { eq(0), eq(projectData.get(OpProjectNode.NAME)) });
        mockBroker.expects(once()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockBroker.expects(never()).method(NEW_TRANSACTION_METHOD);
        mockBroker.expects(never()).method(MAKE_PERSISTENT_METHOD);
        mockTransaction.expects(never()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = administrationService.insertProject((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result.getError());
        assertEquals("Error should be the one that was set on new error call", error, result.getError());
    }

    /**
    * Tests the behavior of deleteProjects for an existing project.
    */
    public void testDeleteProject() {
        XMessage request = new XMessage();
        List projectIds = new ArrayList();
        projectIds.add(PROJECT_ID);
        request.setArgument(OpProjectAdministrationService.PROJECT_IDS, projectIds);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_TRANSACTION_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PORTFOLIO_ID_BY_PROJECT_ID)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(new Long(portfolio.getID()));
                return query;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PORTFOLIO_ID_BY_PROJECT_ID);
            }
        });
        mockQuery.expects(atLeastOnce()).method(SET_COLLECTION_METHOD);
        mockQuery.expects(atLeastOnce()).method(SET_BYTE_METHOD).with(new Constraint[] { eq("projectType"), eq(OpProjectNode.PROJECT) });
        mockBroker.expects(once()).method(LIST_METHOD).with(same(query)).will(methodStub);
        mockSession.expects(once()).method(ACCESSIBLE_IDS_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_ACCESSIBLE_PROJECTS)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(project);
                return query;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_ACCESSIBLE_PROJECTS);
            }
        });
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PROJECT_WORK_RECORDS)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(new Integer(0));
                return query;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_WORK_RECORDS);
            }
        });
        mockQuery.expects(once()).method(SET_LONG_METHOD);
        mockBroker.expects(atLeastOnce()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(once()).method(DELETE_OBJECT_METHOD).with(same(project));
        mockTransaction.expects(once()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = administrationService.deleteProjects((OpProjectSession) mockSession.proxy(), request);
        assertNull("No Error message should have been returned", result);
    }

    /**
    * Tests the behavior of deleteProjects for a non accesible project.
    */
    public void testDeleteNonAccesibleProject() {
        XMessage request = new XMessage();
        ArrayList projectIds = new ArrayList();
        projectIds.add(NONEXISTENT_PROJECT_ID);
        request.setArgument(OpProjectAdministrationService.PROJECT_IDS, projectIds);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PORTFOLIO_ID_BY_PROJECT_ID)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(new Long(portfolio.getID()));
                return query;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PORTFOLIO_ID_BY_PROJECT_ID);
            }
        });
        mockQuery.expects(atLeastOnce()).method(SET_COLLECTION_METHOD);
        mockQuery.expects(atLeastOnce()).method(SET_BYTE_METHOD).with(new Constraint[] { eq("projectType"), eq(OpProjectNode.PROJECT) });
        mockBroker.expects(once()).method(LIST_METHOD).with(same(query)).will(methodStub);
        mockSession.expects(once()).method(ACCESSIBLE_IDS_METHOD).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                return new HashSet();
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_SUPER_PORTFOLIO_ID_BY_PORTFOLIO_ID);
            }
        });
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = administrationService.deleteProjects((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result.getError());
        assertEquals("Error should be the one that was set on new error call", error, result.getError());
    }

    public void testMoveProjectNodeOk() {
        XMessage request = new XMessage();
        List projectIds = new ArrayList();
        projectIds.add(PROJECT_ID);
        request.setArgument(OpProjectAdministrationService.PROJECT_IDS, projectIds);
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_ID, PORTFOLIO_ID);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_TRANSACTION_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(PORTFOLIO_ID)).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(PROJECT_ID)).will(methodStub);
        mockSession.expects(atLeastOnce()).method(CHECK_ACCESS_LEVEL_METHOD).will(methodStub);
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(createProjectNodeWithSuperNodeConstraint(project, portfolio));
        mockTransaction.expects(once()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = administrationService.moveProjectNode((OpProjectSession) mockSession.proxy(), request);
        assertNull("No Error message should have been returned", result.getError());
    }

    public void testMoveProjectNodeNoRights() {
        XMessage request = new XMessage();
        List projectIds = new ArrayList();
        projectIds.add(PROJECT_ID);
        request.setArgument(OpProjectAdministrationService.PROJECT_IDS, projectIds);
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_ID, PORTFOLIO_ID);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_TRANSACTION_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(PORTFOLIO_ID)).will(methodStub);
        mockSession.expects(once()).method(CHECK_ACCESS_LEVEL_METHOD).will(returnValue(false));
        mockTransaction.expects(once()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = administrationService.moveProjectNode((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result.getError());
    }

    /**
    * Tests the behavior of updateProject for an existent project and accurate data.
    */
    public void testUpdateProject() {
        XMessage request = new XMessage();
        request.setArgument(OpProjectAdministrationService.PROJECT_ID, PROJECT_ID);
        Map projectData = creatProjectData("NewProjectName", "NewProjectDescription", XCalendar.today(), XCalendar.today(), 0);
        request.setArgument(OpProjectAdministrationService.PROJECT_DATA, projectData);
        XComponent goalsDataSet = new XComponent(XComponent.DATA_SET);
        request.setArgument(OpProjectAdministrationService.GOALS_SET, goalsDataSet);
        XComponent toDoDataSet = new XComponent(XComponent.DATA_SET);
        request.setArgument(OpProjectAdministrationService.TO_DOS_SET, toDoDataSet);
        Constraint testProject = createProjectNodeConstraint(projectData);
        queryResults.clear();
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        expectCreateAdminPermission();
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(PROJECT_ID)).will(methodStub);
        mockSession.expects(once()).method(CHECK_ACCESS_LEVEL_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PROJECT_BY_NAME)).will(methodStub);
        mockQuery.expects(once()).method(SET_STRING_METHOD).with(new Constraint[] { eq(0), eq(projectData.get(OpProjectNode.NAME)) });
        mockBroker.expects(once()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(once()).method(NEW_TRANSACTION_METHOD).will(methodStub);
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(testProject);
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(isA(OpProjectPlan.class));
        mockTransaction.expects(once()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        assertNoError(administrationService.updateProject((OpProjectSession) mockSession.proxy(), request));
    }

    /**
    * Tests the behavior of updateProject for a non existent project name and accurata data.
    */
    public void testUpdateNonExistentProject() {
        XMessage request = new XMessage();
        request.setArgument(OpProjectAdministrationService.PROJECT_ID, NONEXISTENT_PROJECT_ID);
        Map projectData = creatProjectData("NewProjectName", "NewProjectDescription", XCalendar.today(), XCalendar.today(), 0);
        request.setArgument(OpProjectAdministrationService.PROJECT_DATA, projectData);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(NONEXISTENT_PROJECT_ID)).will(methodStub);
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockBroker.expects(never()).method(NEW_TRANSACTION_METHOD);
        mockBroker.expects(never()).method(UPDATE_OBJECT_METHOD);
        mockTransaction.expects(never()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = administrationService.updateProject((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result);
        assertEquals("Error should be the one that was set on new error call", error, result.getError());
    }

    /**
    * Tests the behavior of updateProject with an already existent project.
    */
    public void testUpdateProjectWithExistentData() {
        XMessage request = new XMessage();
        request.setArgument(OpProjectAdministrationService.PROJECT_ID, PROJECT_ID);
        Map projectData = creatProjectData(projectName, "NewProjectDescription", XCalendar.today(), XCalendar.today(), 0);
        request.setArgument(OpProjectAdministrationService.PROJECT_DATA, projectData);
        OpProjectNode existentProject = new OpProjectNode();
        existentProject.setType(OpProjectNode.PROJECT);
        existentProject.setName(projectName);
        existentProject.setDescription(projectDescription);
        existentProject.setID(2);
        queryResults.clear();
        queryResults.add(existentProject);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(PROJECT_ID)).will(methodStub);
        mockSession.expects(once()).method(CHECK_ACCESS_LEVEL_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PROJECT_BY_NAME)).will(methodStub);
        mockQuery.expects(once()).method(SET_STRING_METHOD).with(new Constraint[] { eq(0), eq(projectData.get(OpProjectNode.NAME)) });
        mockBroker.expects(once()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockBroker.expects(never()).method(NEW_TRANSACTION_METHOD);
        mockBroker.expects(never()).method(UPDATE_OBJECT_METHOD);
        mockTransaction.expects(never()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = administrationService.updateProject((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result.getError());
        assertEquals("Error should be the one that was set on new error call", error, result.getError());
    }

    /**
    * Tests the behavior of updateProject for an invalid project data.
    */
    public void testUpdateProjectWrongData() {
        checkUpdateProjectWrongData("", "projectDescription", XCalendar.today(), XCalendar.today(), 0);
        checkUpdateProjectWrongData("projectName", "projectDescription", null, XCalendar.today(), 0);
        Date startDate = new Date(XCalendar.today().getTime() + XCalendar.MILLIS_PER_DAY);
        checkUpdateProjectWrongData("projectName", "projectDescription", startDate, XCalendar.today(), 0);
        checkUpdateProjectWrongData("projectName", "projectDescription", XCalendar.today(), XCalendar.today(), -5);
    }

    /**
    * Tests the behavior of updateProject for an invalid project data.
    */
    private void checkUpdateProjectWrongData(String projectName, String projectDescription, Date startDate, Date endDate, double projectBudget) {
        XMessage request = new XMessage();
        request.setArgument(OpProjectAdministrationService.PROJECT_ID, PROJECT_ID);
        Map projectData = creatProjectData(projectName, projectDescription, startDate, endDate, projectBudget);
        request.setArgument(OpProjectAdministrationService.PROJECT_DATA, projectData);
        XComponent goalsDataSet = new XComponent(XComponent.DATA_SET);
        request.setArgument(OpProjectAdministrationService.GOALS_SET, goalsDataSet);
        XComponent toDoDataSet = new XComponent(XComponent.DATA_SET);
        request.setArgument(OpProjectAdministrationService.TO_DOS_SET, toDoDataSet);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(never()).method(GET_OBJECT_METHOD).with(eq(PROJECT_ID)).will(methodStub);
        mockSession.expects(never()).method(CHECK_ACCESS_LEVEL_METHOD);
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockBroker.expects(never()).method(NEW_TRANSACTION_METHOD);
        mockBroker.expects(never()).method(MAKE_PERSISTENT_METHOD);
        mockTransaction.expects(never()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = administrationService.updateProject((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result.getError());
        assertEquals("Error should be the one that was set on new error call", error, result.getError());
    }

    /**
    * Tests that a new portfolio will be corectly inserted provided that the portfolio data is correct
    */
    public void testInsertNewPortfolio() {
        XMessage request = new XMessage();
        HashMap portfolioData = creatPortfolioData("PortfolioName", "Portfolio Description");
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_DATA, portfolioData);
        Constraint testPortfolio = createPortfolioConstraint(portfolioData);
        queryResults.clear();
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        expectCreateAdminPermission();
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PROJECT_PORTFOLIO_BY_NAME)).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_ROOT_PORTFOLIO_BY_NAME)).will(methodStub);
        mockQuery.expects(once()).method(SET_STRING_METHOD).with(new Constraint[] { eq(0), eq(OpProjectNode.ROOT_PROJECT_PORTFOLIO_NAME) });
        mockQuery.expects(once()).method(SET_BYTE_METHOD).with(new Constraint[] { eq(1), eq(OpProjectNode.PORTFOLIO) });
        mockBroker.expects(once()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(superPortfolio);
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_PORTFOLIO_BY_NAME);
            }
        });
        mockQuery.expects(once()).method(SET_STRING_METHOD).with(new Constraint[] { eq(0), eq(portfolioData.get(OpProjectNode.NAME)) });
        mockBroker.expects(once()).method(ITERATE_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                return queryResults.iterator();
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_PORTFOLIO_BY_NAME);
            }
        });
        mockSession.expects(once()).method(CHECK_ACCESS_LEVEL_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_TRANSACTION_METHOD).will(methodStub);
        mockBroker.expects(once()).method(MAKE_PERSISTENT_METHOD).with(testPortfolio);
        mockTransaction.expects(once()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        assertNoError(administrationService.insertPortfolio((OpProjectSession) mockSession.proxy(), request));
    }

    /**
    * Tests the behavior of insertPortfolio if the portfolio name is missing or is empty.
    */
    public void testInsertPortfolioWrongData() {
        XMessage request = new XMessage();
        HashMap portfolioData = creatPortfolioData(null, "Portfolio Description");
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_DATA, portfolioData);
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockSession.expects(never()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(never()).method(NEW_TRANSACTION_METHOD);
        mockBroker.expects(never()).method(MAKE_PERSISTENT_METHOD);
        mockTransaction.expects(never()).method(COMMIT_METHOD);
        mockBroker.expects(never()).method(CLOSE_METHOD);
        XMessage result = administrationService.insertPortfolio((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result.getError());
        assertEquals("Error should be the one that was set on new error call", error, result.getError());
    }

    /**
    * Tests the behavior of insertPortfolio if the portfolio already exists.
    */
    public void testInsertAlreadyExistentPortfolio() {
        XMessage request = new XMessage();
        HashMap portfolioData = creatPortfolioData("PortfolioName", "Portfolio Description");
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_DATA, portfolioData);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PROJECT_PORTFOLIO_BY_NAME)).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_ROOT_PORTFOLIO_BY_NAME)).will(methodStub);
        mockQuery.expects(once()).method(SET_STRING_METHOD).with(new Constraint[] { eq(0), eq(OpProjectNode.ROOT_PROJECT_PORTFOLIO_NAME) });
        mockQuery.expects(once()).method(SET_BYTE_METHOD).with(new Constraint[] { eq(1), eq(OpProjectNode.PORTFOLIO) });
        mockBroker.expects(once()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(superPortfolio);
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_PORTFOLIO_BY_NAME);
            }
        });
        mockQuery.expects(once()).method(SET_STRING_METHOD).with(new Constraint[] { eq(0), eq(portfolioData.get(OpProjectNode.NAME)) });
        mockBroker.expects(once()).method(ITERATE_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(portfolio);
                return queryResults.iterator();
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_PORTFOLIO_BY_NAME);
            }
        });
        mockSession.expects(once()).method(CHECK_ACCESS_LEVEL_METHOD).will(methodStub);
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockBroker.expects(never()).method(NEW_TRANSACTION_METHOD);
        mockBroker.expects(never()).method(MAKE_PERSISTENT_METHOD);
        mockTransaction.expects(never()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = administrationService.insertPortfolio((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result.getError());
        assertEquals("Error should be the one that was set on new error call", error, result.getError());
    }

    /**
    * Tests the behavior of updatePortfolio for an existent portfolio and accurate data.
    */
    public void testUpdatePortfolio() {
        XMessage request = new XMessage();
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_ID, PORTFOLIO_ID);
        HashMap portfolioData = creatPortfolioData("PortfolionName", "Portfolio Description");
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_DATA, portfolioData);
        Constraint testPortfolio = createPortfolioConstraint(portfolioData);
        queryResults.clear();
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        expectCreateAdminPermission();
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(PORTFOLIO_ID)).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PROJECT_PORTFOLIO_BY_NAME)).will(methodStub);
        mockQuery.expects(once()).method(SET_STRING_METHOD).with(new Constraint[] { eq(0), eq(portfolioData.get(OpProjectNode.NAME)) });
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_ROOT_PORTFOLIO_BY_NAME)).will(methodStub);
        mockQuery.expects(once()).method(SET_STRING_METHOD).with(new Constraint[] { eq(0), eq(OpProjectNode.ROOT_PROJECT_PORTFOLIO_NAME) });
        mockQuery.expects(once()).method(SET_BYTE_METHOD).with(new Constraint[] { eq(1), eq(OpProjectNode.PORTFOLIO) });
        mockBroker.expects(once()).method(LIST_METHOD).with(same(query)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(superPortfolio);
                return queryResults;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_PORTFOLIO_BY_NAME);
            }
        });
        mockBroker.expects(once()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(once()).method(NEW_TRANSACTION_METHOD).will(methodStub);
        mockBroker.expects(once()).method(UPDATE_OBJECT_METHOD).with(testPortfolio);
        mockTransaction.expects(once()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        assertNoError(administrationService.updatePortfolio((OpProjectSession) mockSession.proxy(), request));
    }

    /**
    * Tests the behavior of updatePortfolio for a non existent portfolio name and accurata data.
    */
    public void testUpdateNonExistentPortfolio() {
        XMessage request = new XMessage();
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_ID, NONEXISTENT_PORTFOLIO_ID);
        HashMap portfolioData = creatPortfolioData("PortfolionName", "Portfolio Description");
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_DATA, portfolioData);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(NONEXISTENT_PORTFOLIO_ID)).will(methodStub);
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockBroker.expects(never()).method(NEW_TRANSACTION_METHOD);
        mockBroker.expects(never()).method(UPDATE_OBJECT_METHOD);
        mockTransaction.expects(never()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = administrationService.updatePortfolio((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result.getError());
        assertEquals("Error should be the one that was set on new error call", error, result.getError());
    }

    /**
    * Tests the behavior of updatePortfolio if the portfolio name is missing or is empty.
    */
    public void testUpdatePortfolioWrongData() {
        XMessage request = new XMessage();
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_ID, PORTFOLIO_ID);
        HashMap portfolioData = creatPortfolioData(null, "Portfolio Description");
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_DATA, portfolioData);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockBroker.expects(never()).method(GET_OBJECT_METHOD);
        mockBroker.expects(never()).method(NEW_TRANSACTION_METHOD);
        mockBroker.expects(never()).method(UPDATE_OBJECT_METHOD);
        mockTransaction.expects(never()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = administrationService.updatePortfolio((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result.getError());
        assertEquals("Error should be the one that was set on new error call", error, result.getError());
    }

    /**
    * Tests the behavior of updatePortfolio with an already existing portfolio.
    */
    public void testUpdatePortfolioWithExistentData() {
        XMessage request = new XMessage();
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_ID, PORTFOLIO_ID);
        HashMap portfolioData = creatPortfolioData("PortfolionName", "Portfolio Description");
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_DATA, portfolioData);
        OpProjectNode existentPortfolio = new OpProjectNode();
        existentPortfolio.setType(OpProjectNode.PORTFOLIO);
        existentPortfolio.setName(portfolioName);
        existentPortfolio.setDescription(portfolioDescription);
        existentPortfolio.setID(2);
        queryResults.clear();
        queryResults.add(existentPortfolio);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(GET_OBJECT_METHOD).with(eq(PORTFOLIO_ID)).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PROJECT_PORTFOLIO_BY_NAME)).will(methodStub);
        mockQuery.expects(once()).method(SET_STRING_METHOD).with(new Constraint[] { eq(0), eq(portfolioData.get(OpProjectNode.NAME)) });
        mockBroker.expects(once()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockBroker.expects(never()).method(NEW_TRANSACTION_METHOD);
        mockBroker.expects(never()).method(UPDATE_OBJECT_METHOD);
        mockTransaction.expects(never()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = administrationService.updatePortfolio((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result.getError());
        assertEquals("Error should be the one that was set on new error call", error, result.getError());
    }

    /**
    * Tests the behavior of deletePortfolios for an existing portfolio.
    */
    public void testDeletePortfolio() {
        XMessage request = new XMessage();
        ArrayList portfolioIds = new ArrayList();
        portfolioIds.add(PORTFOLIO_ID);
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_IDS, portfolioIds);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_TRANSACTION_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_SUPER_PORTFOLIO_ID_BY_PORTFOLIO_ID)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(new Long(superPortfolio.getID()));
                return query;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_SUPER_PORTFOLIO_ID_BY_PORTFOLIO_ID);
            }
        });
        mockQuery.expects(atLeastOnce()).method(SET_COLLECTION_METHOD);
        mockQuery.expects(atLeastOnce()).method(SET_BYTE_METHOD).with(new Constraint[] { eq("projectType"), eq(OpProjectNode.PORTFOLIO) });
        mockBroker.expects(once()).method(LIST_METHOD).with(same(query)).will(methodStub);
        mockSession.expects(once()).method(ACCESSIBLE_IDS_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_ACCESSIBLE_PORTFOLIOS)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(portfolio);
                return query;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_ACCESSIBLE_PORTFOLIOS);
            }
        });
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_PROJECT_WORK_RECORDS)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(new Integer(0));
                return query;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_PROJECT_WORK_RECORDS);
            }
        });
        mockQuery.expects(once()).method(SET_LONG_METHOD);
        mockBroker.expects(atLeastOnce()).method(ITERATE_METHOD).with(same(query)).will(methodStub);
        mockBroker.expects(once()).method(DELETE_OBJECT_METHOD).with(isA(OpProjectNode.class));
        mockBroker.expects(once()).method(DELETE_OBJECT_METHOD).with(same(portfolio));
        mockTransaction.expects(once()).method(COMMIT_METHOD);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        assertNoError(administrationService.deletePortfolios((OpProjectSession) mockSession.proxy(), request));
    }

    /**
    * Tests the behavior of deletePortfolios for a non accesible portfolio.
    */
    public void testDeleteNonAccesiblePortfolio() {
        XMessage request = new XMessage();
        ArrayList portfolioIds = new ArrayList();
        portfolioIds.add(NONEXISTENT_PORTFOLIO_ID);
        request.setArgument(OpProjectAdministrationService.PORTFOLIO_IDS, portfolioIds);
        mockSession.expects(once()).method(NEW_BROKER_METHOD).will(methodStub);
        mockBroker.expects(once()).method(NEW_QUERY_METHOD).with(eq(SELECT_SUPER_PORTFOLIO_ID_BY_PORTFOLIO_ID)).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                queryResults.clear();
                queryResults.add(new Long(superPortfolio.getID()));
                return query;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_SUPER_PORTFOLIO_ID_BY_PORTFOLIO_ID);
            }
        });
        mockQuery.expects(atLeastOnce()).method(SET_COLLECTION_METHOD);
        mockQuery.expects(atLeastOnce()).method(SET_BYTE_METHOD).with(new Constraint[] { eq("projectType"), eq(OpProjectNode.PORTFOLIO) });
        mockBroker.expects(once()).method(LIST_METHOD).with(same(query)).will(methodStub);
        mockSession.expects(once()).method(ACCESSIBLE_IDS_METHOD).will(new Stub() {

            public Object invoke(Invocation invocation) throws Throwable {
                return new HashSet();
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Mocks the behaviour of new query for ").append(SELECT_SUPER_PORTFOLIO_ID_BY_PORTFOLIO_ID);
            }
        });
        mockSession.expects(once()).method(NEW_ERROR_METHOD).will(methodStub);
        mockBroker.expects(once()).method(CLOSE_METHOD);
        XMessage result = administrationService.deletePortfolios((OpProjectSession) mockSession.proxy(), request);
        assertNotNull("Error message should have been returned", result.getError());
        assertEquals("Error should be the one that was set on new error call", error, result.getError());
    }

    /**
    * Creates a portfolio data given the properties of the new portfolio
    *
    * @param portfolioName a <code>String </code> representing the name for the portfolio
    * @param description   a <code>String</code>representing the description of the portfolio
    * @return a new <code>XStruct</code> with the portfolio data
    */
    private HashMap creatPortfolioData(String portfolioName, String description) {
        Map portfolioValues = new HashMap();
        portfolioValues.put(OpProjectNode.NAME, portfolioName);
        portfolioValues.put(OpProjectNode.DESCRIPTION, description);
        portfolioValues.put(OpPermissionSetFactory.PERMISSION_SET, new XComponent(XComponent.DATA_SET));
        return transformToXStruct(portfolioValues);
    }

    /**
    * Creates a new constraint (anonymous inner class) that can be used to check if a portfolio is the expected one.
    *
    * @return a new portfolio constraint
    */
    private Constraint createPortfolioConstraint(final HashMap portfolioData) {
        return new Constraint() {

            public boolean eval(Object object) {
                if (!(object instanceof OpProjectNode)) {
                    return false;
                }
                OpProjectNode portfolio = (OpProjectNode) object;
                if (portfolio.getType() != OpProjectNode.PORTFOLIO) {
                    return false;
                }
                if (!portfolioData.get(OpProjectNode.NAME).equals(portfolio.getName())) {
                    return false;
                }
                return portfolioData.get(OpProjectNode.DESCRIPTION).equals(portfolio.getDescription());
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Will check if a portfolio is the expected one");
            }
        };
    }

    /**
    * Creates a project data given the properties of the new project
    *
    * @param projectName   a <code>String</code> representing the project name
    * @param projectName   a <code>String</code> representing the project description
    * @param startDate     a <code>Date</code> representing the start date of the project
    * @param endDate       a <code>Date</code> representing the end date of the project
    * @param projectBudget a <code>double</code> representing the budget of the project
    * @return a new <code>XStruct</code> with the project data
    */
    protected Map creatProjectData(String projectName, String projectDescription, Date startDate, Date endDate, double projectBudget) {
        Map projectValues = new HashMap();
        projectValues.put(OpProjectNode.NAME, projectName);
        projectValues.put(OpProjectNode.DESCRIPTION, projectDescription);
        projectValues.put(OpProjectNode.START, startDate);
        projectValues.put(OpProjectNode.FINISH, endDate);
        projectValues.put(OpProjectNode.BUDGET, new Double(projectBudget));
        projectValues.put("PortfolioID", String.valueOf(PORTFOLIO_ID));
        projectValues.put(OpPermissionSetFactory.PERMISSION_SET, new XComponent(XComponent.DATA_SET));
        return projectValues;
    }

    /**
    * Creates a new constraint (anonymous inner class) that can be used to check if a project is the expected one.
    *
    * @return <code>Constraint</code> representing a project constraint
    */
    protected Constraint createProjectNodeConstraint(final Map projectData) {
        return new Constraint() {

            public boolean eval(Object object) {
                if (!(object instanceof OpProjectNode)) {
                    return false;
                }
                OpProjectNode project = (OpProjectNode) object;
                if (project.getType() != OpProjectNode.PROJECT) {
                    return false;
                }
                if (!projectData.get(OpProjectNode.NAME).equals(project.getName())) {
                    return false;
                }
                if (!projectData.get(OpProjectNode.DESCRIPTION).equals(project.getDescription())) {
                    return false;
                }
                if (!projectData.get(OpProjectNode.START).equals(project.getStart())) {
                    return false;
                }
                if (!projectData.get(OpProjectNode.FINISH).equals(project.getFinish())) {
                    return false;
                }
                return (((Double) projectData.get(OpProjectNode.BUDGET)).doubleValue() == project.getBudget());
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Will check if a project is the expected one");
            }
        };
    }

    /**
    * Creates a new constraint (anonymous inner class) that can be used to check if a project is the expected one.
    *
    * @return <code>Constraint</code> representing a project constraint
    */
    protected Constraint createProjectNodeWithSuperNodeConstraint(final OpProjectNode projectNode, final OpProjectNode portfolio) {
        return new Constraint() {

            public boolean eval(Object object) {
                if (!(object instanceof OpProjectNode)) {
                    return false;
                }
                OpProjectNode project = (OpProjectNode) object;
                if (project.getType() != OpProjectNode.PROJECT) {
                    return false;
                }
                if (project != projectNode) {
                    return false;
                }
                OpProjectNode superNode = project.getSuperNode();
                if (superNode != portfolio) {
                    return false;
                }
                return true;
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Will check if a project has the expected super-node");
            }
        };
    }

    /**
    * Creates a constraint for a OpGoal instance .
    *
    * @param goalDataSet data set with the expected values to be found on the project
    * @return a new Constraint object that will check if a OpGoal instance is the expected one
    */
    private Constraint createGoalConstraint(final XComponent goalDataSet) {
        return new Constraint() {

            public boolean eval(Object object) {
                if (!(object instanceof OpGoal)) {
                    return false;
                }
                OpGoal goal = (OpGoal) object;
                XComponent dataRow = (XComponent) goalDataSet.getChild(0);
                XComponent completedDataCell = (XComponent) dataRow.getChild(0);
                if (!(goal.getCompleted() == completedDataCell.getBooleanValue())) {
                    return false;
                }
                XComponent subjectDataCell = (XComponent) dataRow.getChild(1);
                if (!(goal.getName().equals(subjectDataCell.getStringValue()))) {
                    return false;
                }
                XComponent priorityDataCell = (XComponent) dataRow.getChild(2);
                return (goal.getPriority() == priorityDataCell.getIntValue());
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Check if an toDo is the expected one");
            }
        };
    }

    /**
    * Creates a constraint for a OpToDo instance .
    *
    * @param toDoDataSet data set with the expected values to be found on the project
    * @return a new Constraint object that will check if a OpToDo instance is the expected one
    */
    private Constraint createToDoConstraint(final XComponent toDoDataSet) {
        return new Constraint() {

            public boolean eval(Object object) {
                if (!(object instanceof OpToDo)) {
                    return false;
                }
                OpToDo toDo = (OpToDo) object;
                XComponent dataRow = (XComponent) toDoDataSet.getChild(0);
                XComponent completedDataCell = (XComponent) dataRow.getChild(0);
                if (!(toDo.getCompleted() == completedDataCell.getBooleanValue())) {
                    return false;
                }
                XComponent subjectDataCell = (XComponent) dataRow.getChild(1);
                if (!(toDo.getName().equals(subjectDataCell.getStringValue()))) {
                    return false;
                }
                XComponent priorityDataCell = (XComponent) dataRow.getChild(2);
                if (!(toDo.getPriority() == priorityDataCell.getIntValue())) {
                    return false;
                }
                XComponent dueDataCell = (XComponent) dataRow.getChild(3);
                return toDo.getDue().equals(dueDataCell.getDateValue());
            }

            public StringBuffer describeTo(StringBuffer stringBuffer) {
                return stringBuffer.append("Check if an toDo is the expected one");
            }
        };
    }
}
