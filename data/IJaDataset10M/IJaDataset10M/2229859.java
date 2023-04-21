package no.ugland.utransprod.gui.windows;

import static org.mockito.Mockito.when;
import javax.swing.JFrame;
import no.ugland.utransprod.gui.FrontProductionWindow;
import no.ugland.utransprod.gui.SystemReadyListener;
import no.ugland.utransprod.gui.handlers.TableEnum;
import no.ugland.utransprod.service.FrontProductionVManager;
import no.ugland.utransprod.util.ModelUtil;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author atle.brekka
 * 
 */
public class FrontProductionWindowTest extends WindowTest {

    /**
     *
     */
    private FrameFixture frameFixture;

    @BeforeMethod
    protected void setUp() throws Exception {
        super.setUp();
        FrontProductionVManager frontProductionVManager = (FrontProductionVManager) ModelUtil.getBean(FrontProductionVManager.MANAGER_NAME);
        when(managerRepository.getFrontProductionVManager()).thenReturn(frontProductionVManager);
        final FrontProductionWindow frontProductionWindow = new FrontProductionWindow(login, managerRepository, deviationViewHandlerFactory, null);
        frontProductionWindow.setLogin(login);
        JFrame frame = GuiActionRunner.execute(new GuiQuery<JFrame>() {

            protected JFrame executeInEDT() {
                return (JFrame) frontProductionWindow.buildMainWindow(new SystemReadyListener() {

                    public void systemReady() {
                    }
                }, managerRepository);
            }
        });
        frameFixture = new FrameFixture(frame);
        frameFixture.show();
    }

    @AfterMethod
    protected void tearDown() throws Exception {
        frameFixture.cleanUp();
        super.tearDown();
    }

    @Test
    public void testShow() throws Exception {
        frameFixture.requireVisible();
        frameFixture.table(TableEnum.TABLEPRODUCTIONFRONT.getTableName());
        assertEquals("Produksjon av front", frameFixture.target.getTitle());
    }
}
