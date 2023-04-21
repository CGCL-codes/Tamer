package org.eclipse.mylyn.bugzilla.tests;

import junit.framework.TestCase;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaCorePlugin;
import org.eclipse.mylyn.internal.bugzilla.core.IBugzillaConstants;
import org.eclipse.mylyn.internal.bugzilla.ui.search.BugzillaSearchPage;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.TaskRepositoryManager;
import org.eclipse.mylyn.tasks.ui.TasksUiPlugin;
import org.eclipse.swt.widgets.Shell;

/**
 * Test the bugzilla search dialog.
 * 
 * @author Jeff Pound
 */
public class BugzillaSearchDialogTest extends TestCase {

    private TaskRepositoryManager manager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        manager = TasksUiPlugin.getRepositoryManager();
        assertNotNull(manager);
        manager.clearRepositories(TasksUiPlugin.getDefault().getRepositoriesFilePath());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (manager != null) {
            manager.clearRepositories(TasksUiPlugin.getDefault().getRepositoriesFilePath());
        }
    }

    /**
	 * Test that the search dialog is initialized properly with the given repository.
	 * 
	 * @throws Exception
	 */
    public void testSearchDialogInit() throws Exception {
        TaskRepository repo = new TaskRepository(BugzillaCorePlugin.REPOSITORY_KIND, IBugzillaConstants.TEST_BUGZILLA_222_URL, IBugzillaConstants.BugzillaServerVersion.SERVER_222.toString());
        manager.addRepository(repo, TasksUiPlugin.getDefault().getRepositoriesFilePath());
        BugzillaSearchPage page = new BugzillaSearchPage(repo);
        Shell shell = BugzillaTestPlugin.getDefault().getWorkbench().getDisplay().getShells()[0];
        page.createControl(shell);
        page.setVisible(true);
        assertFalse(page.getProductCount() == 0);
    }
}
