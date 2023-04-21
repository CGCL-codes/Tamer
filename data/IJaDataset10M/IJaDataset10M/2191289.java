package org.eclipse.mylyn.tasks.tests;

import org.eclipse.mylyn.internal.tasks.ui.PersonProposalProviderTest;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Mik Kersten
 */
public class AllTasksTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.eclipse.mylyn.tasks.tests");
        suite.addTestSuite(TaskRepositoryTest.class);
        suite.addTestSuite(TaskRepositorySorterTest.class);
        suite.addTestSuite(TaskDataManagerTest.class);
        suite.addTestSuite(CopyDetailsActionTest.class);
        suite.addTestSuite(TaskListTest.class);
        suite.addTestSuite(ProjectRepositoryAssociationTest.class);
        suite.addTestSuite(TaskList06DataMigrationTest.class);
        suite.addTestSuite(TaskPlanningEditorTest.class);
        suite.addTestSuite(TaskListManagerTest.class);
        suite.addTestSuite(RepositoryTaskSynchronizationTest.class);
        suite.addTestSuite(TaskRepositoryManagerTest.class);
        suite.addTestSuite(TaskRepositoriesExternalizerTest.class);
        suite.addTestSuite(TaskListContentProviderTest.class);
        suite.addTestSuite(TaskListBackupManagerTest.class);
        suite.addTestSuite(TableSorterTest.class);
        suite.addTestSuite(TaskKeyComparatorTest.class);
        suite.addTestSuite(TaskTest.class);
        suite.addTestSuite(TaskListUiTest.class);
        suite.addTestSuite(TaskListDnDTest.class);
        suite.addTestSuite(TaskDataExportTest.class);
        suite.addTestSuite(TaskActivityTest.class);
        suite.addTestSuite(AttachmentJobTest.class);
        suite.addTestSuite(RepositorySettingsPageTest.class);
        suite.addTestSuite(TaskHistoryTest.class);
        suite.addTestSuite(UrlConnectionUtilTest.class);
        suite.addTestSuite(CommentQuoterTest.class);
        suite.addTestSuite(OfflineStorageTest.class);
        suite.addTestSuite(OfflineCachingStorageTest.class);
        suite.addTestSuite(QueryExportImportTest.class);
        suite.addTestSuite(PersonProposalProviderTest.class);
        suite.addTestSuite(org.eclipse.mylyn.tasks.tests.web.NamedPatternTest.class);
        return suite;
    }
}
