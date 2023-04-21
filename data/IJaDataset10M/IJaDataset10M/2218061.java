package org.eclipse.mylyn.bugzilla.tests.headless;

import junit.framework.TestCase;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.context.tests.support.TestUtil;
import org.eclipse.mylyn.context.tests.support.TestUtil.Credentials;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaCorePlugin;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaReportElement;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaRepositoryConnector;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaRepositoryQuery;
import org.eclipse.mylyn.internal.bugzilla.core.IBugzillaConstants;
import org.eclipse.mylyn.tasks.core.AbstractTask;
import org.eclipse.mylyn.tasks.core.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.QueryHitCollector;
import org.eclipse.mylyn.tasks.core.RepositoryTaskData;
import org.eclipse.mylyn.tasks.core.TaskList;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TaskFactory;

/**
 * Example use of headless API (no ui dependencies)
 * 
 * @author Rob Elves
 * @author Nathan Hapke
 */
public class BugzillaQueryTest extends TestCase {

    private TaskRepository repository;

    private BugzillaRepositoryConnector connector;

    private AbstractTaskDataHandler handler;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        connector = new BugzillaRepositoryConnector();
        connector.init(new TaskList());
        handler = connector.getTaskDataHandler();
        repository = new TaskRepository(BugzillaCorePlugin.REPOSITORY_KIND, IBugzillaConstants.TEST_BUGZILLA_222_URL);
        Credentials credentials = TestUtil.readCredentials();
        repository.setAuthenticationCredentials(credentials.username, credentials.password);
    }

    /**
	 * This is the first test so that the repository credentials are correctly set for the other tests
	 */
    public void testAddCredentials() {
        if (!repository.hasCredentials()) {
            Credentials credentials = TestUtil.readCredentials();
            repository.setAuthenticationCredentials(credentials.username, credentials.password);
            assertTrue(repository.hasCredentials());
        }
    }

    public void testGetBug() throws Exception {
        RepositoryTaskData taskData = handler.getTaskData(repository, "1", new NullProgressMonitor());
        assertNotNull(taskData);
        assertEquals("user@mylar.eclipse.org", taskData.getAssignedTo());
        assertEquals("foo", taskData.getDescription());
        assertEquals("P1", taskData.getAttributeValue(BugzillaReportElement.PRIORITY.getKeyString()));
    }

    @SuppressWarnings("deprecation")
    public void testQueryViaConnector() throws Exception {
        String queryUrlString = repository.getUrl() + "/buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=search-match-test&product=TestProduct&long_desc_type=substring&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&deadlinefrom=&deadlineto=&bug_status=NEW&bug_status=ASSIGNED&bug_status=REOPENED&emailassigned_to1=1&emailtype1=substring&email1=&emailassigned_to2=1&emailreporter2=1&emailcc2=1&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&chfieldfrom=&chfieldto=Now&chfieldvalue=&cmdtype=doit&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0=";
        TaskList taskList = new TaskList();
        QueryHitCollector collector = new QueryHitCollector(new TaskFactory(repository));
        BugzillaRepositoryConnector connector = new BugzillaRepositoryConnector();
        connector.init(taskList);
        BugzillaRepositoryQuery query = new BugzillaRepositoryQuery(repository.getUrl(), queryUrlString, "summary");
        connector.performQuery(query, repository, new NullProgressMonitor(), collector);
        assertEquals(2, collector.getTasks().size());
        for (AbstractTask hit : collector.getTasks()) {
            assertTrue(hit.getSummary().contains("search-match-test"));
        }
    }
}
