package test.core;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import com.ivis.xprocess.core.MessageUtility;
import com.ivis.xprocess.core.Organization;
import com.ivis.xprocess.core.Parameter;
import com.ivis.xprocess.core.Pattern;
import com.ivis.xprocess.core.Person;
import com.ivis.xprocess.core.Portfolio;
import com.ivis.xprocess.core.ProjectResource;
import com.ivis.xprocess.core.Role;
import com.ivis.xprocess.core.RoleType;
import com.ivis.xprocess.core.Xprocess;
import com.ivis.xprocess.core.Xproject;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.core.impl.ParameterImpl;
import com.ivis.xprocess.core.impl.PortfolioImpl;
import com.ivis.xprocess.framework.XchangeElement;
import com.ivis.xprocess.framework.properties.PropertyType;
import com.ivis.xprocess.framework.vcs.exceptions.VCSException;
import com.ivis.xprocess.framework.xml.IPersistenceHelper;
import com.ivis.xprocess.properties.impl.exceptions.PropertyException;
import com.ivis.xprocess.util.Day;
import com.ivis.xprocess.util.DayOffset;
import com.ivis.xprocess.util.License;
import com.ivis.xprocess.util.Day.DayOfWeek;
import com.ivis.xprocess.web.framework.ISessionDatasource;
import com.ivis.xprocess.web.framework.MasterDataSource;
import com.ivis.xprocess.web.spring.XProcessSessionAccess;

public abstract class XprocessSpringContextTest extends AbstractDependencyInjectionSpringContextTests {

    private static final Logger logger = Logger.getLogger(XprocessSpringContextTest.class.getName());

    public static final String CONFIG_PACKAGE = "file:";

    public static final String CONFIG_FILE = "applicationContext.xml";

    protected static final String USER_NAME = "user1";

    protected static final String PASSWORD = "user1";

    protected static final int PROJECT_DURATION = 365;

    protected MasterDataSource master;

    protected ISessionDatasource session;

    protected Portfolio testPortfolio;

    protected Day today = new Day();

    protected IPersistenceHelper ph;

    public Portfolio rootPortfolio;

    public Xprocess simpleProcess;

    public RoleType adminRT;

    public RoleType participantRT;

    public Pattern simpleProjectPattern;

    public Pattern anotherSimpleProjectPattern;

    public Pattern taskPattern;

    public Pattern overheadTaskPattern;

    public Pattern instancePattern;

    public RoleType specifierRT;

    public Organization organization;

    public Person user1;

    public Role participantUser1;

    public ProjectResource projectResourceUser1;

    protected Day nextMonday = today.getNext(DayOfWeek.MONDAY, true);

    public Xtask t1;

    public Xtask t2;

    protected MessageUtility dialog;

    protected boolean answerToDialogQuestion = false;

    private Portfolio childPortfolio;

    protected Xproject project;

    @Override
    protected String[] getConfigLocations() {
        return new String[] { CONFIG_PACKAGE + CONFIG_FILE };
    }

    public abstract String getTestRootDir();

    private String getDir() {
        return getTestRootDir() + "." + this.getName();
    }

    public void onSetUp() {
        System.out.println("XprocessWebTest: onSetUp");
        logger.info("onSetUp by Spring logger");
        logger.setLevel(java.util.logging.Level.ALL);
        logger.severe("onSetUp by java logger");
        try {
            getMasterAndSession();
            ph = master.getDataSource().getPersistenceHelper();
        } catch (Exception e) {
            throw new RuntimeException("TestMaster failed", e);
        }
        setupLicensedUser();
        getRootProcessReferences(ph);
        assertsOnSimpleProcess();
        try {
            setupTestPortfolio();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog = new MessageUtility() {

            public void informUser(String title, String message) {
                System.out.println("Title: " + title);
                System.out.println("Message: " + message);
            }

            public void warnUser(String title, String message) {
                System.err.println("Title: " + title);
                System.err.println("Message: " + message);
            }

            public boolean askUser(String title, String question) {
                return answerToDialogQuestion;
            }
        };
    }

    private void assertsOnSimpleProcess() {
        assertEquals(1, simpleProcess.getCategoryTypes().size());
        assertEquals(1, simpleProcess.getGatewayTypes().size());
        assertEquals(14, simpleProcess.getParameterizedActions().size());
        assertEquals(44, simpleProcess.getContainedElements().size());
        assertEquals(0, DayOffset.getInstance().getOffset());
    }

    private void setupLicensedUser() {
        organization = ((Portfolio) master.getDataSource().getRoot()).createOrganization("Test Organisation");
        user1 = organization.createPerson("user1");
        user1.setLicenseId(License.getLicense().getCheckSum());
    }

    @Override
    protected void onTearDown() throws Exception {
        System.out.println("XprocessWebTest: onTearDown");
        super.onTearDown();
        Thread.yield();
    }

    private void setupTestPortfolio() throws Exception {
        Portfolio root = session.getRoot();
        for (Portfolio p : root.getPortfolios()) {
            System.out.println("deleting portfolio: " + p.getLabel() + " : " + p.getUuid());
            p.delete();
        }
        ph.getDataSource().getVcsProvider().update();
        testPortfolio = root.createPortfolio(getDir());
        assertNotNull(testPortfolio);
    }

    public void saveAddCommit() throws VCSException {
        master.getDataSource().getPersistenceHelper();
        ph.saveAndAddDirtyElements();
        ph.getDataSource().getVcsProvider().commit();
        Thread.yield();
    }

    private void setupTestProject() throws Exception {
        if (testPortfolio == null) {
            System.out.println("calling setupTestPortfolio");
            setupTestPortfolio();
        }
        Pattern simpleProjectPattern = PortfolioImpl.getProjectPattern(ph);
        Pattern instancePattern = simpleProjectPattern.instantiate(childPortfolio, null, new Parameter[] { getNameParameter(getDir()), getProjectStartParameter(today.addDays(1)), getProjectDurationParameter(PROJECT_DURATION) });
        assertNull(instancePattern.getInstantiationException());
        project = (Xproject) instancePattern.getPrototypes().iterator().next();
        project.setName("PRoject");
        assertNotNull(project);
    }

    private void getMasterAndSession() throws Exception {
        master = XProcessSessionAccess.getMasterDS();
        if (!(master.isInitialized() && master.isStatusOK())) {
            String msg = master.getMessage();
            System.out.println("Error initialization MasterDS:" + msg);
        }
        assertTrue(master.isInitialized());
        assertTrue(master.isStatusOK());
        assertNotNull(master);
        System.out.println("getting session DS for " + master.getRepositoryUser());
        session = master.getSessionDS(master.getRepositoryUser(), master.getRepositoryPassword());
        assertNotNull(session);
    }

    protected Parameter getNameParameter(String name) {
        try {
            return ParameterImpl.createInstance(PortfolioImpl.NAME_PARAMETER, PropertyType.STRING, name, 1, ph);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }
    }

    protected Parameter getSizeParameter(double size) {
        try {
            return ParameterImpl.createInstance(PortfolioImpl.SIZE_PARAMETER, PropertyType.FLOAT, size, 3, ph);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }
    }

    protected Parameter getHomePageParameter(String description) {
        try {
            return ParameterImpl.createInstance(PortfolioImpl.HOME_PARAMETER, PropertyType.STRING, (description != null) ? description : "", 2, ph);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }
    }

    protected Parameter getDescriptionParameter(String name) {
        try {
            return ParameterImpl.createInstance(PortfolioImpl.DESCRIPTION_PARAMETER, PropertyType.STRING, name, 2, ph);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }
    }

    protected Parameter getStartParameter(Day day) {
        try {
            return ParameterImpl.createInstance(PortfolioImpl.STARTDATE_PARAMETER, PropertyType.DAY, day, 3, ph);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }
    }

    protected Parameter getOverheadStartParameter(Day day) {
        try {
            return ParameterImpl.createInstance(PortfolioImpl.OVERHEAD_STARTDATE_PARAMETER, PropertyType.DAY, day, 3, ph);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }
    }

    protected Parameter getProjectStartParameter(Day day) {
        try {
            return ParameterImpl.createInstance(PortfolioImpl.PROJECT_STARTDATE_PARAMETER, PropertyType.DAY, day, 3, ph);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }
    }

    protected Parameter getProjectDurationParameter(int duration) {
        try {
            return ParameterImpl.createInstance(PortfolioImpl.DURATION_PARAMETER, PropertyType.INTEGER, duration, 4, ph);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }
    }

    protected Parameter getOverheadEndParameter(Day day) {
        try {
            return ParameterImpl.createInstance(PortfolioImpl.OVERHEAD_ENDDATE_PARAMETER, PropertyType.DAY, day, 4, ph);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }
    }

    protected Xtask setupT1InSimpleProject() throws Exception {
        return setupTaskInSimpleProject("Task 1", 960);
    }

    protected Xtask setupT2InSimpleProject() throws Exception {
        return setupTaskInSimpleProject("Task 2", 960);
    }

    protected Xtask setupTaskInSimpleProject(String taskName, int estimate) throws Exception {
        if (project == null) {
            setupTestProject();
        }
        instancePattern = taskPattern.instantiate(project, null, new Parameter[] { getNameParameter(taskName), getSizeParameter(2.0) });
        assertNull(instancePattern.getInstantiationException());
        t1 = (Xtask) instancePattern.getPrototypes().iterator().next();
        t1.setMostLikely(estimate);
        t1.setBest(estimate);
        t1.setWorst(estimate);
        return t1;
    }

    protected void getRootProcessReferences(IPersistenceHelper ph) {
        simpleProcess = PortfolioImpl.getRootProcess(ph);
        participantRT = PortfolioImpl.getParticipantRoletype(ph);
        simpleProjectPattern = PortfolioImpl.getProjectPattern(ph);
        anotherSimpleProjectPattern = PortfolioImpl.getProjectPattern(ph);
        taskPattern = PortfolioImpl.getTaskPattern(ph);
        overheadTaskPattern = PortfolioImpl.getOverheadTaskPattern(ph);
        rootPortfolio = (Portfolio) ph.getDataSource().getRoot();
    }

    protected Xproject makeSimpleProject(Portfolio testPortfolio) {
        return makeSimpleProject(testPortfolio, null, null, null, null);
    }

    protected Xproject makeSimpleProject(Portfolio testPortfolio, String name) {
        return makeSimpleProject(testPortfolio, name, null, null, null);
    }

    protected Xproject makeSimpleProject(Portfolio testPortfolio, String name, String description, Day start, Integer duration) {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        final Set<Parameter> projectParameters = simpleProjectPattern.getParameterGroups().iterator().next().getParameters();
        for (Parameter parameter : projectParameters) {
            if ((name == null) && parameter.getName().equals(PortfolioImpl.NAME_PARAMETER)) {
                name = getDir();
            } else if ((description == null) && parameter.getName().equals(PortfolioImpl.DESCRIPTION_PARAMETER)) {
                description = (String) parameter.getDefaultObjectValue(project);
            } else if ((start == null) && parameter.getName().equals(PortfolioImpl.PROJECT_STARTDATE_PARAMETER)) {
                start = (Day) parameter.getDefaultObjectValue(project);
            } else if ((duration == null) && parameter.getName().equals(PortfolioImpl.DURATION_PARAMETER)) {
                duration = (Integer) parameter.getDefaultObjectValue(project);
            }
        }
        parameters.add(getNameParameter(name));
        parameters.add(getHomePageParameter(description));
        parameters.add(getProjectStartParameter(start));
        parameters.add(getProjectDurationParameter(duration));
        instancePattern = simpleProjectPattern.instantiate(testPortfolio, dialog, (Parameter[]) parameters.toArray(new Parameter[parameters.size()]));
        assertNull(instancePattern.getInstantiationException());
        Xproject proj = (Xproject) instancePattern.getPrototypes().iterator().next();
        return proj;
    }

    protected Xtask makeTask(XchangeElement parent, String name) {
        return makeTask(parent, name, null, null, null, null, null);
    }

    protected Xtask makeTask(XchangeElement parent, String name, String description, Double size, String importance, String roleType, String assignTo) {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        final Set<Parameter> taskParameters = taskPattern.getParameterGroups().iterator().next().getParameters();
        for (Parameter parameter : taskParameters) {
            if ((name == null) && parameter.getName().equals(PortfolioImpl.NAME_PARAMETER)) {
                name = (String) parameter.getDefaultObjectValue(parent);
            } else if ((description == null) && parameter.getName().equals(PortfolioImpl.DESCRIPTION_PARAMETER)) {
                description = (String) parameter.getDefaultObjectValue(parent);
            } else if ((size == null) && parameter.getName().equals(PortfolioImpl.SIZE_PARAMETER)) {
                size = (Double) parameter.getDefaultObjectValue(parent);
            } else if ((importance == null) && parameter.getName().equals(PortfolioImpl.IMPORTANCE_HIGH_MEDIUM_LOW)) {
                importance = (String) parameter.getDefaultObjectValue(parent);
            } else if ((roleType == null) && parameter.getName().equals(PortfolioImpl.REQUIRED_ROLE_TYPE_PARAMETER)) {
                roleType = (String) parameter.getDefaultObjectValue(parent);
            } else if ((assignTo == null) && parameter.getName().equals(PortfolioImpl.ASSIGN_TO_ACCOUNT_NAME_PARAMETER)) {
                assignTo = (String) parameter.getDefaultObjectValue(parent);
            }
        }
        parameters.add(getNameParameter(name));
        parameters.add(getDescriptionParameter(description));
        parameters.add(getSizeParameter(size));
        parameters.add(getImportanceParameter(importance));
        parameters.add(getRoleTypeParameter(roleType));
        parameters.add(getAssignToParameter(assignTo));
        instancePattern = taskPattern.instantiate(parent, dialog, (Parameter[]) parameters.toArray(new Parameter[parameters.size()]));
        assertNull(instancePattern.getInstantiationException());
        return (Xtask) instancePattern.getPrototypes().iterator().next();
    }

    protected Parameter getImportanceParameter(String name) {
        try {
            return ParameterImpl.createInstance(PortfolioImpl.IMPORTANCE_HIGH_MEDIUM_LOW, PropertyType.STRING, name, 4, ph);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }
    }

    protected Parameter getRoleTypeParameter(String name) {
        try {
            return ParameterImpl.createInstance(PortfolioImpl.REQUIRED_ROLE_TYPE_PARAMETER, PropertyType.STRING, name, 5, ph);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }
    }

    protected Parameter getAssignToParameter(String name) {
        try {
            return ParameterImpl.createInstance(PortfolioImpl.ASSIGN_TO_ACCOUNT_NAME_PARAMETER, PropertyType.STRING, name, 6, ph);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }
    }
}
