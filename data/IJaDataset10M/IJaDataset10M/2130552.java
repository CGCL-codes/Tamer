package net.sf.javadc.tasks.hub;

import net.sf.javadc.interfaces.IHubTask;
import net.sf.javadc.interfaces.ISearchRequestFactory;
import net.sf.javadc.interfaces.IShareManager;
import net.sf.javadc.net.SearchRequestFactory;
import net.sf.javadc.net.ShareManager;

/**
 * @author Timo Westk�mper
 */
public class ISearchTaskTest extends AbstractHubTaskTest {

    ISearchRequestFactory searchRequestFactory = new SearchRequestFactory();

    IShareManager shareManager = new ShareManager(settings, taskManager);

    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Constructor for ISearchTaskTest.
     * 
     * @param arg0
     */
    public ISearchTaskTest(String arg0) {
        super(arg0);
    }

    public IHubTask createTask() {
        return new ISearchTask(settings, shareManager, searchRequestFactory);
    }

    public void runTask() throws Exception {
    }
}
