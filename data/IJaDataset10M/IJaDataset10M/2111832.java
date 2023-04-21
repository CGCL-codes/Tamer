package com.tacitknowledge.util.migration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.MockControl;
import com.tacitknowledge.util.migration.jdbc.DistributedJdbcMigrationLauncher;
import com.tacitknowledge.util.migration.jdbc.DistributedJdbcMigrationLauncherFactory;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncher;
import com.tacitknowledge.util.migration.jdbc.TestDataSourceMigrationContext;
import com.tacitknowledge.util.migration.jdbc.TestDistributedJdbcMigrationLauncherFactory;
import com.tacitknowledge.util.migration.tasks.normal.TestMigrationTask2;

/**
 * Test the distributed launcher factory
 * 
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class DistributedJdbcMigrationLauncherFactoryTest extends MigrationListenerTestBase {

    /** Class logger */
    private static Log log = LogFactory.getLog(DistributedJdbcMigrationLauncherFactoryTest.class);

    /** The launcher we're testing */
    private DistributedJdbcMigrationLauncher launcher = null;

    /** A MigrationContext for us */
    private TestMigrationContext context = null;

    /**
     * constructor that takes a name
     *
     * @param name of the test to run
     */
    public DistributedJdbcMigrationLauncherFactoryTest(String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        log.debug("setting up " + this.getClass().getName());
        System.getProperties().setProperty("migration.factory", "com.tacitknowledge.util.migration.jdbc.TestJdbcMigrationLauncherFactory");
        DistributedJdbcMigrationLauncherFactory factory = new TestDistributedJdbcMigrationLauncherFactory();
        launcher = (DistributedJdbcMigrationLauncher) factory.createMigrationLauncher("orchestration");
        launcher.getMigrationProcess().addListener(this);
        context = new TestMigrationContext();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * For the given launchers, set all it's context's patch info stores as mocks
     * that report the given patch level.  This method is a helper to get this
     * test to pass the DisitributedMigrationProcess::validateControlledSystems() test.
     * @param launchers Collection of JDBCMigrationLaunchers
     * @param levelToReport the patch level the mock should report
     * @throws MigrationException 
     */
    protected void setReportedPatchLevel(Collection launchers, int levelToReport) throws MigrationException {
        for (Iterator launchersIterator = launchers.iterator(); launchersIterator.hasNext(); ) {
            JdbcMigrationLauncher launcher = (JdbcMigrationLauncher) launchersIterator.next();
            for (Iterator it = launcher.getContexts().keySet().iterator(); it.hasNext(); ) {
                MigrationContext ctx = (MigrationContext) it.next();
                MockControl patchInfoStoreControl = MockControl.createControl(PatchInfoStore.class);
                PatchInfoStore patchInfoStore = (PatchInfoStore) patchInfoStoreControl.getMock();
                patchInfoStore.getPatchLevel();
                patchInfoStoreControl.setReturnValue(levelToReport);
                patchInfoStoreControl.replay();
                launcher.getContexts().put(ctx, patchInfoStore);
            }
        }
    }

    /**
     * Test the configuration of the launchers versus a known property file
     */
    public void testDistributedLauncherConfiguration() {
        HashMap controlledSystems = ((DistributedMigrationProcess) launcher.getMigrationProcess()).getControlledSystems();
        assertEquals(3, controlledSystems.size());
    }

    /**
     * Make sure that the task loading works correctly
     * 
     * @exception MigrationException if anything goes wrong
     */
    public void testDistributedMigrationTaskLoading() throws MigrationException {
        DistributedMigrationProcess process = (DistributedMigrationProcess) launcher.getMigrationProcess();
        assertEquals(7, process.getMigrationTasks().size());
        assertEquals(7, process.getMigrationTasksWithLaunchers().size());
    }

    /**
     * Ensure that overlapping tasks even among sub-launchers are detected
     * 
     * @exception Exception if anything goes wrong
     */
    public void testDistributedMigrationTaskValidation() throws Exception {
        MigrationProcess process = launcher.getMigrationProcess();
        process.validateTasks(process.getMigrationTasks());
        TestMigrationTask2.setPatchLevelOverride(new Integer(3));
        try {
            process.validateTasks(process.getMigrationTasks());
            fail("We should have thrown an exception - " + "there were overlapping tasks among sub-launchers");
        } catch (MigrationException me) {
        } finally {
            TestMigrationTask2.reset();
        }
    }

    /**
     * Ensure that read-only mode actually works
     * 
     * @exception Exception if anything goes wrong
     */
    public void testDistributedReadOnlyMode() throws Exception {
        int currentPatchLevel = 3;
        DistributedMigrationProcess process = (DistributedMigrationProcess) launcher.getMigrationProcess();
        process.validateTasks(process.getMigrationTasks());
        HashMap controlledSystems = process.getControlledSystems();
        setReportedPatchLevel(controlledSystems.values(), currentPatchLevel);
        process.setReadOnly(true);
        try {
            process.doMigrations(currentPatchLevel, context);
            fail("There should have been an exception - unapplied patches + read-only don't work");
        } catch (MigrationException me) {
            log.debug("got exception: " + me.getMessage());
        }
        currentPatchLevel = 8;
        setReportedPatchLevel(controlledSystems.values(), currentPatchLevel);
        int patches = process.doMigrations(currentPatchLevel, context);
        assertEquals(0, patches);
        assertEquals(0, getMigrationStartedCount());
        assertEquals(0, getMigrationSuccessCount());
    }

    /**
     * Make sure we get notified of patch application
     * 
     * @exception Exception if anything goes wrong
     */
    public void testDistributedMigrationEvents() throws Exception {
        assertEquals(5, launcher.getMigrationProcess().getListeners().size());
        HashMap controlledSystems = ((DistributedMigrationProcess) launcher.getMigrationProcess()).getControlledSystems();
        for (Iterator controlledSystemIter = controlledSystems.keySet().iterator(); controlledSystemIter.hasNext(); ) {
            String controlledSystemName = (String) controlledSystemIter.next();
            JdbcMigrationLauncher subLauncher = (JdbcMigrationLauncher) controlledSystems.get(controlledSystemName);
            MigrationProcess subProcess = subLauncher.getMigrationProcess();
            assertEquals(1, subProcess.getListeners().size());
        }
        DistributedMigrationProcess process = (DistributedMigrationProcess) launcher.getMigrationProcess();
        int currentPatchlevel = 3;
        setReportedPatchLevel(process.getControlledSystems().values(), currentPatchlevel);
        int patches = process.doMigrations(currentPatchlevel, context);
        assertEquals(4, patches);
        assertEquals(4, getMigrationStartedCount());
        assertEquals(4, getMigrationSuccessCount());
    }

    /**
     * Make sure we the right patches go in the right spot
     * 
     * @exception Exception if anything goes wrong
     */
    public void testDistributedMigrationContextTargetting() throws Exception {
        int currentPatchLevel = 3;
        HashMap controlledSystems = ((DistributedMigrationProcess) launcher.getMigrationProcess()).getControlledSystems();
        setReportedPatchLevel(controlledSystems.values(), currentPatchLevel);
        MigrationProcess process = launcher.getMigrationProcess();
        process.doMigrations(currentPatchLevel, context);
        JdbcMigrationLauncher ordersLauncher = (JdbcMigrationLauncher) controlledSystems.get("orders");
        TestDataSourceMigrationContext ordersContext = (TestDataSourceMigrationContext) ordersLauncher.getContexts().keySet().iterator().next();
        assertEquals("orders", ordersContext.getSystemName());
        assertTrue(ordersContext.hasExecuted("TestTask1"));
        assertTrue(ordersContext.hasExecuted("TestTask2"));
        assertTrue(ordersContext.hasExecuted("TestTask3"));
        assertTrue(ordersContext.hasExecuted("TestTask4"));
        JdbcMigrationLauncher coreLauncher = (JdbcMigrationLauncher) controlledSystems.get("core");
        TestDataSourceMigrationContext coreContext = (TestDataSourceMigrationContext) coreLauncher.getContexts().keySet().iterator().next();
        assertEquals(3, coreLauncher.getMigrationProcess().getMigrationTasks().size());
        assertEquals("core", coreContext.getSystemName());
        assertFalse(coreContext.hasExecuted("patch0001_first_patch"));
        assertFalse(coreContext.hasExecuted("patch0002_second_patch"));
        assertFalse(coreContext.hasExecuted("patch0003_third_patch"));
    }

    /**
     * Get the MigrationContext to use during testing
     * 
     * @return TestMigrationContext object
     */
    public TestMigrationContext getContext() {
        return context;
    }

    /**
     * Set the MigrationContext to use for testing
     * 
     * @param context a TestMigrationContext object to use for testing
     */
    public void setContext(TestMigrationContext context) {
        this.context = context;
    }

    /**
     * Get the launcher to use for testing
     * 
     * @return DistributedJdbcMigrationLauncher to use for testing
     */
    public DistributedJdbcMigrationLauncher getLauncher() {
        return launcher;
    }

    /**
     * Set the launcher to test
     * 
     * @param launcher the DistributedJdbcMigrationLauncher to test
     */
    public void setLauncher(DistributedJdbcMigrationLauncher launcher) {
        this.launcher = launcher;
    }
}
