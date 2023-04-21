package uk.ac.ebi.intact.application.editor.struts.action.experiment;

import org.apache.struts.action.ActionServlet;
import servletunit.struts.MockStrutsTestCase;
import uk.ac.ebi.intact.application.editor.LoginPropertiesGetter;
import uk.ac.ebi.intact.application.editor.business.EditorService;
import uk.ac.ebi.intact.application.editor.event.EventListener;
import uk.ac.ebi.intact.application.editor.struts.framework.util.EditorConstants;
import uk.ac.ebi.intact.application.editor.util.LockManager;
import uk.ac.ebi.intact.context.IntactContext;

/**
 * TODO comment it.
 *
 * @author Catherine Leroy (cleroy@ebi.ac.uk)
 * @version $Id$
 */
public class ExperimentDispatchActionTest extends MockStrutsTestCase {

    public void setUp() throws Exception {
        super.setUp();
        IntactContext.getCurrentInstance().getDataContext().beginTransaction();
    }

    public void tearDown() throws Exception {
        super.tearDown();
        IntactContext.getCurrentInstance().getDataContext().commitTransaction();
    }

    public ExperimentDispatchActionTest(String testName) {
        super(testName);
    }

    public void testSubmitAction() {
        ActionServlet actionServlet = getActionServletFromCreate("Experiment");
        setActionServlet(actionServlet);
        addRequestParameter("shortLabel", "unit-test");
        addRequestParameter("fullName", "unit test shortlabel");
        addRequestParameter("organism", "arath");
        addRequestParameter("inter", "bret");
        addRequestParameter("ident", "elisa");
        addRequestParameter("dispatch", "Submit");
        setRequestPathInfo("/expDispatch");
        actionPerform();
        verifyForward("submit");
        verifyForwardPath("/do/exp/submit");
        verifyNoActionErrors();
        verifyNoActionMessages();
        setRequestPathInfo("/exp/submit");
        addRequestParameter("dispatch", "Submit");
        actionPerform();
        verifyForward("result");
    }

    public ActionServlet getActionServletFromCreate(String topic) {
        setActionServlet(getActionServletFromLogin());
        ActionServlet actionServlet = getActionServlet();
        EditorService service = EditorService.getInstance();
        actionServlet.getServletContext().setAttribute(EditorConstants.EDITOR_SERVICE, service);
        actionServlet.getServletContext().setAttribute(EditorConstants.LOCK_MGR, LockManager.getInstance());
        setActionServlet(actionServlet);
        setRequestPathInfo("/sidebar");
        addRequestParameter("topic", topic);
        addRequestParameter("dispatch", "Create");
        actionPerform();
        return getActionServlet();
    }

    private ActionServlet getActionServletFromLogin() {
        LoginPropertiesGetter loginProperties = new LoginPropertiesGetter();
        setRequestPathInfo("/login");
        addRequestParameter("username", loginProperties.getName());
        addRequestParameter("password", loginProperties.getPassword());
        ActionServlet actionServlet = getActionServlet();
        actionServlet.getServletContext().setAttribute(EditorConstants.EVENT_LISTENER, EventListener.getInstance());
        setActionServlet(actionServlet);
        actionPerform();
        return getActionServlet();
    }
}
