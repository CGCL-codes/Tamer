package org.kwantu.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.kwantu.app.context.M2ApplicationContext;
import org.kwantu.m2.KwantuFaultException;
import org.kwantu.m2.model.AbstractApplicationController;
import org.kwantu.m2.model.SimpleLoggerToLog;
import org.kwantu.m2.test.M2TestDatabase;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author chris
 */
public class ModelBrowserTest {

    private static final Log LOG = LogFactory.getLog(ModelBrowserTest.class);

    private WicketApplicationController controller;

    private ModelBrowser modelBrowser;

    private M2TestDatabase m2TestDatabase;

    private Session currentSession;

    @BeforeClass
    public void init() {
        controller = new WicketApplicationController();
        controller.setFeedbacker(new SimpleLoggerToLog(LOG));
        m2TestDatabase = M2TestDatabase.acquire();
        new M2ApplicationContext(controller, null, new CurrentSessionProvider() {

            @Override
            public Session getCurrentSession(final String key) {
                if (currentSession == null) {
                    LOG.info("open Session");
                    currentSession = m2TestDatabase.openSession();
                }
                return currentSession;
            }

            @Override
            public AbstractApplicationController getController() {
                return controller;
            }
        });
        modelBrowser = controller.getM2Context().getModelBrowser();
    }

    @AfterClass
    public void cleanup() {
        detach();
        m2TestDatabase.release();
    }

    @Test
    public void deleteClassTest() {
        modelBrowser.kwantuModelAdd("M1");
        detach();
        modelBrowser.kwantuClassAdd(modelBrowser.getKwantuModel("M1"), "C");
        detach();
        assertEquals(modelBrowser.getKwantuModel("M1").getKwantuBusinessObjectModel().getKwantuClasses().size(), 1);
        detach();
        modelBrowser.kwantuModelDelete(modelBrowser.getKwantuModel("M1"));
        detach();
        assertEquals(modelBrowser.getKwantuModels().size(), 0);
    }

    private void detach() {
        LOG.info("detach");
        controller.getM2Context().getModelBrowser().detach();
        if (currentSession != null && currentSession.isOpen()) {
            try {
                if (currentSession.getTransaction().isActive()) {
                    currentSession.getTransaction().rollback();
                }
            } catch (HibernateException ex) {
                throw new KwantuFaultException(ex);
            } finally {
                currentSession.close();
                currentSession = null;
            }
        }
    }
}
