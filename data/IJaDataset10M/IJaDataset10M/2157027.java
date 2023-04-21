package net.sf.javadc.tasks.hub;

import java.util.Iterator;
import junit.framework.TestCase;
import net.sf.javadc.config.AdvancedSettings;
import net.sf.javadc.config.Settings;
import net.sf.javadc.config.UserInfo;
import net.sf.javadc.interfaces.IHub;
import net.sf.javadc.interfaces.IHubFactory;
import net.sf.javadc.interfaces.IHubManager;
import net.sf.javadc.interfaces.IHubTask;
import net.sf.javadc.interfaces.IHubTaskFactory;
import net.sf.javadc.interfaces.ISettings;
import net.sf.javadc.interfaces.ITaskManager;
import net.sf.javadc.mockups.BaseHubTaskFactory;
import net.sf.javadc.net.hub.HostInfo;
import net.sf.javadc.net.hub.HubFactory;
import net.sf.javadc.net.hub.HubManager;
import net.sf.javadc.util.TaskManager;
import org.apache.log4j.Category;

/**
 * @author Timo Westk�mper
 */
public abstract class AbstractHubTaskTest extends TestCase {

    protected static final Category logger = Category.getInstance(AbstractHubTaskTest.class);

    protected final ITaskManager taskManager = new TaskManager();

    protected final IHubManager hubManager = new HubManager();

    protected final IHubTaskFactory hubTaskFactory = new BaseHubTaskFactory();

    protected final ISettings settings = new Settings();

    protected final IHubFactory hubFactory = new HubFactory(taskManager, hubManager, hubTaskFactory, settings);

    protected IHub hub;

    protected String cmdData = new String();

    protected IHubTask task;

    protected void setUp() throws Exception {
        super.setUp();
        HostInfo host = new HostInfo("ilotalo.no-ip.org:411");
        hub = hubFactory.createHub(host);
        settings.setUserInfo(new UserInfo());
        settings.setAdvancedSettings(new AdvancedSettings());
        task = createTask();
        task.setHub(hub);
        task.setCmdData(cmdData);
    }

    /**
     * Constructor for HubTaskTest.
     * 
     * @param arg0
     */
    public AbstractHubTaskTest(String arg0) {
        super(arg0);
    }

    /**
     * Template method to create the Task instance
     * 
     * @return
     */
    public abstract IHubTask createTask();

    /**
     * Template method to be overwritten by subclasses
     * 
     * @throws Exception
     */
    public abstract void runTask() throws Exception;

    /**
     * @throws Exception
     */
    public void testRunTaskActiveNoExceptions() throws Exception {
        noExceptions(true);
    }

    /**
     * @throws Exception
     */
    public void testRunTaskPassiveNoExceptions() throws Exception {
        noExceptions(false);
    }

    /**
     * @throws Exception
     */
    public void noExceptions(boolean active) throws Exception {
        if (active) settings.setActive(true); else settings.setActive(false);
        runTask();
        assertTrue("task was empty", task != null);
        int counter = 0;
        for (Iterator i = task.getExceptions().iterator(); i.hasNext(); ) {
            Exception e = (Exception) i.next();
            System.out.println(++counter + " " + e);
        }
        assertTrue("There were " + task.getExceptions().size() + " exceptions during the task invocation.", task.getExceptions().isEmpty());
    }
}
