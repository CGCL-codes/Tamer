package kg.apc.jmeter.gui;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author undera
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ComponentBorderTest.class, JAbsrtactDialogPanelTest.class, GuiBuilderHelperTest.class, DeleteRowActionTest.class, CopyRowActionTest.class, AddRowActionTest.class, DialogFactoryTest.class, ButtonPanelAddCopyRemoveTest.class, BrowseActionTest.class })
public class GuiSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}
