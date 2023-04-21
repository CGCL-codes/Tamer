package net.sf.jguard.jee.jsf;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.mycila.testing.junit.MycilaJunitRunner;
import net.sf.jguard.core.PolicyEnforcementPointOptions;
import net.sf.jguard.core.authentication.schemes.LoginPasswordFormSchemeHandler;
import net.sf.jguard.core.enforcement.PolicyEnforcementPoint;
import net.sf.jguard.core.technology.Scopes;
import net.sf.jguard.core.test.JGuardTestFiles;
import net.sf.jguard.ext.authentication.manager.AbstractAuthenticationManager;
import net.sf.jguard.jee.HttpConstants;
import net.sf.jguard.jee.SecurityTestCase;
import net.sf.jguard.jee.listeners.ContextListener;
import net.sf.jguard.jsf.AccessListener;
import net.sf.jguard.jsf.authentication.JSFScopes;
import org.apache.myfaces.lifecycle.LifecycleImpl;
import org.apache.shale.test.base.AbstractViewControllerTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.servlet.ServletContextEvent;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * test jGuard overall integration in a JSF webapp.
 *
 * @author <a href="mailto:diabolo512@users.sourceforge.net">Charles Gay</a>
 */
@RunWith(MycilaJunitRunner.class)
public class AccessListenerTest extends AbstractViewControllerTestCase implements SecurityTestCase {

    private static final String JGUARD_AUTHENTICATION_XML = JGuardTestFiles.J_GUARD_AUTHENTICATION_XML.getLabel();

    private static final String JGUARD_FILTER_XML = JGuardTestFiles.J_GUARD_FILTER_XML.getLabel();

    private URL filterConfigurationLocation = Thread.currentThread().getContextClassLoader().getResource(JGUARD_FILTER_XML);

    private UIViewRoot root = null;

    private AccessListener listener = null;

    @Mock
    private NavigationHandler nh = null;

    private static final String WELCOME_JSP = "/welcome.jsp";

    private static final String LOGON_PROCESS_JSP = "/logonProcess.jsp";

    private Class<? extends Scopes> authenticationBindingsClass = JSFScopes.class;

    private URL applicationPath;

    public PolicyEnforcementPoint policyEnforcementPoint;

    private static final String GUEST = "guest";

    private static final String UNKNOWN_JSP = "/unknown.jsp";

    private static final String VIP_AREA_JSP = "/jsp/vipArea.jsp";

    private static final String ACCESS_DENIED = "accessDenied";

    private static final String AUTHENTICATION_FAILED = "authenticationFailed";

    private static final String ADMIN = "admin";

    private static final String WEIRD_STRING = "weirdString";

    private static final String JSP_LOGON_JSP = "/jsp/logon.jsp";

    protected static String authorizationXmlFileLocation = JGuardTestFiles.J_GUARD_AUTHORIZATION_XML.getLabel();

    protected static String authenticationXmlFileLocation = JGuardTestFiles.J_GUARD_USERS_PRINCIPALS_XML.getLabel();

    protected static String filterLocation = JGuardTestFiles.J_GUARD_FILTER_XML.getLabel();

    protected static String authenticationConfigurationLocation = JGuardTestFiles.J_GUARD_AUTHENTICATION_XML.getLabel();

    protected static final String JGUARD_JSF_EXAMPLE = "jguard-jsf-example";

    private static final String APPLICATION_NAME = JGUARD_JSF_EXAMPLE;

    protected static final String J_GUARD_AUTHENTICATION_XML = JGuardTestFiles.J_GUARD_AUTHENTICATION_XML.getLabel();

    protected static final String J_GUARD_AUTHORIZATION_XML = JGuardTestFiles.J_GUARD_AUTHORIZATION_XML.getLabel();

    protected MockFilterChain filterChain;

    protected GuiceFilter guiceFilter = new GuiceFilter();

    public Injector injector;

    public DummyJSFContextListener dummyContextListener;

    public AccessListenerTest() {
        super("AccessListenerTest");
    }

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        servletContext.addInitParameter(ContextListener.FILTER_LOCATION, filterLocation);
        servletContext.addInitParameter(AbstractAuthenticationManager.AUTHENTICATION_XML_FILE_LOCATION, authenticationXmlFileLocation);
        servletContext.addInitParameter(HttpConstants.AUTHORIZATION_CONFIGURATION_LOCATION, authorizationXmlFileLocation);
        servletContext.addInitParameter(HttpConstants.AUTHENTICATION_CONFIGURATION_LOCATION, authenticationConfigurationLocation);
        servletContext.addInitParameter(HttpConstants.AUTHENTICATION_CONFIGURATION_LOCATION, J_GUARD_AUTHENTICATION_XML);
        servletContext.addInitParameter(HttpConstants.AUTHORIZATION_CONFIGURATION_LOCATION, J_GUARD_AUTHORIZATION_XML);
        URL here = Thread.currentThread().getContextClassLoader().getResource(".");
        servletContext.setDocumentRoot(new File(here.toURI()));
        dummyContextListener = new DummyJSFContextListener();
        ServletContextEvent servletContextEvent = new ServletContextEvent(servletContext);
        dummyContextListener.contextInitialized(servletContextEvent);
        injector = dummyContextListener.getBuiltInjector();
        filterChain = new MockFilterChain();
        MockFilterConfig filterConfig = new MockFilterConfig(servletContext);
        guiceFilter.init(filterConfig);
        servletContext.addInitParameter(PolicyEnforcementPointOptions.APPLICATION_NAME.getLabel(), APPLICATION_NAME);
        servletContext.addInitParameter(AccessListener.LISTENER_CONFIGURATION_LOCATION, JGUARD_FILTER_XML);
        application.setNavigationHandler(nh);
        listener = new AccessListener();
        lifecycle.addPhaseListener(listener);
        root = new UIViewRoot();
    }

    public void tearDown() throws Exception {
    }

    private void setViewIdAndFireJsfLifeCycle(String viewId) {
        root.setViewId(viewId);
        facesContext.setViewRoot(root);
        PhaseEvent phaseEvent = new PhaseEvent(facesContext, PhaseId.RESTORE_VIEW, new LifecycleImpl());
        listener.beforePhase(phaseEvent);
        listener.afterPhase(phaseEvent);
    }

    /**
     * we test that an authorized resource (a welcomeFile in the web.xml for example) granted to the guest user is granted to
     * an unauthenticated user.
     */
    @Test
    public void testAccessToAuthorizedResourceGrantedToGuestSubject() {
        setViewIdAndFireJsfLifeCycle(WELCOME_JSP);
        assertTrue("access to /welcome.jsp should be granted but a redirect has occured ", root.equals(facesContext.getViewRoot()));
    }

    /**
     * an unauthenticated user which <strong>does not answer</strong> to an authentication challenge
     * must be redirected to the FORM authentication challenge page.
     */
    @Test
    public void testAccessToUnauthorizedResourceWithNoSubject() {
        setViewIdAndFireJsfLifeCycle("/welcome2.jsp");
        verify(nh, atLeastOnce()).handleNavigation(facesContext, null, JSP_LOGON_JSP);
    }

    /**
     * an authenticated user which <strong>does not answer</strong> to an authentication challenge
     * must be redirected to the access denied page.
     */
    @Test
    public void testAccessToUnauthorizedResourceWithSubject() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(LoginPasswordFormSchemeHandler.LOGIN, GUEST);
        parameters.put(LoginPasswordFormSchemeHandler.PASSWORD, GUEST);
        externalContext.setRequestParameterMap(parameters);
        setViewIdAndFireJsfLifeCycle(LOGON_PROCESS_JSP);
        verify(nh, atLeastOnce()).handleNavigation(facesContext, null, WELCOME_JSP);
        setViewIdAndFireJsfLifeCycle(UNKNOWN_JSP);
        verify(nh, atLeastOnce()).handleNavigation(facesContext, null, ACCESS_DENIED);
    }

    /**
     * we test that an authentication without any credentials implies a redirection to the authenticationFailed
     * view.
     */
    @Test
    public void testUnsuccessfulAuthentication() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(LoginPasswordFormSchemeHandler.LOGIN, WEIRD_STRING);
        parameters.put(LoginPasswordFormSchemeHandler.PASSWORD, WEIRD_STRING);
        externalContext.setRequestParameterMap(parameters);
        setViewIdAndFireJsfLifeCycle(LOGON_PROCESS_JSP);
        verify(nh, atLeastOnce()).handleNavigation(facesContext, null, AUTHENTICATION_FAILED);
    }

    /**
     * we test that a successful authentication implies a redirect to the authenticationSucceedURI.
     */
    @Test
    public void testSuccessFulAuthentication() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(LoginPasswordFormSchemeHandler.LOGIN, ADMIN);
        parameters.put(LoginPasswordFormSchemeHandler.PASSWORD, ADMIN);
        externalContext.setRequestParameterMap(parameters);
        setViewIdAndFireJsfLifeCycle(LOGON_PROCESS_JSP);
        verify(nh, atLeastOnce()).handleNavigation(facesContext, null, WELCOME_JSP);
    }

    @Test
    public void testAccessToAuthorizedResourceWithSubject() throws Exception {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(LoginPasswordFormSchemeHandler.LOGIN, ADMIN);
        parameters.put(LoginPasswordFormSchemeHandler.PASSWORD, ADMIN);
        externalContext.setRequestParameterMap(parameters);
        setViewIdAndFireJsfLifeCycle(LOGON_PROCESS_JSP);
        verify(nh, atLeastOnce()).handleNavigation(facesContext, null, WELCOME_JSP);
        setViewIdAndFireJsfLifeCycle(VIP_AREA_JSP);
    }

    @Test
    public void testAccessToAuthorizedResourceWithNoSubject() {
        setViewIdAndFireJsfLifeCycle(WELCOME_JSP);
    }
}
