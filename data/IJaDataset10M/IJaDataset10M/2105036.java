package net.sf.josas.ui;

import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import net.sf.josas.model.ConfigurationModel;
import net.sf.josas.om.AbstractDBTestCase;
import net.sf.josas.ui.swing.control.ConfigurationController;
import net.sf.josas.ui.swing.view.ConfigurationDialog;

/**
 * Test the use of the zip window.
 *
 * @author frederic
 *
 */
public class TestConfigurationDialog extends AbstractDBTestCase {

    /** Frame fixture. */
    private static FrameFixture window;

    /** Dialog fixture. */
    private static DialogFixture dialog;

    /**
    * Setup the database and the frame fixture for testing.
    */
    @BeforeClass
    public static void setupBeforeClass() {
        AbstractDBTestCase.setUpBeforeClass();
        ConfigurationModel model = new ConfigurationModel();
        JFrame frame = new JFrame();
        ConfigurationController controller = new ConfigurationController(model, frame);
        window = new FrameFixture(frame);
        ConfigurationDialog cfgDialog = controller.getView();
        dialog = new DialogFixture(window.robot, cfgDialog);
        window.show();
    }

    /**
    * Clean up.
    */
    @AfterClass
    public static void teardownAfterClass() {
        window.robot.cleanUp();
    }

    /**
    * Data initialization.
    *
    * @return Data set
    * @throws DataSetException
    *             forwards exception raised by dbUnit
    * @throws IOException
    *             forwards exception raised by dbUnit
    */
    @Override
    protected final IDataSet getDataSet() throws DataSetException, IOException {
        return new FlatXmlDataSet(new File(EMPTY_DATASET));
    }

    /**
    * Test default state of components.
    */
    @Test
    public final void testComponents() {
        dialog.show();
        dialog.button("general").requireVisible().requireEnabled();
        dialog.button("standingdata").requireVisible().requireEnabled();
        dialog.button("HELP").requireVisible().requireEnabled();
        dialog.button("OK").requireVisible().requireEnabled();
        dialog.button("CANCEL").requireVisible().requireEnabled();
        dialog.button("APPLY").requireVisible().requireDisabled();
        dialog.button("general").click();
        dialog.panel("general").requireVisible();
        dialog.button("standingdata").click();
        dialog.panel("standingdata").requireVisible();
    }
}
