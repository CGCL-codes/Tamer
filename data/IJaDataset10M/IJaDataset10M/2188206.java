package org.aeroivr.rsmc.web.controller;

import static org.easymock.EasyMock.and;
import static org.easymock.EasyMock.contains;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import org.aeroivr.appserver.admin.AppServerConstants;
import org.aeroivr.rsmc.common.ServiceLocator;
import org.aeroivr.rsmc.web.view.LogonView;

/**
 * @author Andriy Petlyovanyy
 */
public class LogonPageControllerTest extends AbstractPageControllerTest {

    public LogonPageControllerTest(final String testName) {
        super(testName);
    }

    public void testPageGet() throws Exception {
        final PageGetTestParameters<LogonPageController> testParams = new PageGetTestParameters<LogonPageController>();
        pageGetInitTest(LogonPageController.class, testParams);
        testParams.getPrintWriterMock().print(and(and(and(contains("username"), contains("password")), and(contains("<form"), contains("<input"))), contains("Please provide credentials")));
        expectLastCall().once();
        testParams.getControl().replay();
        testParams.getControllerMock().doGet(testParams.getRequestMock(), testParams.getResponseMock());
        testParams.getControl().verify();
    }

    private void checkPagePost(final PagePostTestParameters<LogonPageController> testParams, final boolean validationResult) throws Exception {
        pagePostInitTest(LogonPageController.class, testParams);
        testParams.getParameters().put(LogonView.USERNAME, AppServerConstants.ADMIN_USERNAME);
        testParams.getParameters().put(LogonView.PASSWORD, AppServerConstants.ADMIN_USERNAME);
        testParams.getParameters().put(LogonView.LOGON_BUTTON, LogonView.LOGON_BUTTON);
        testParams.getAppServerClientAdminMock().areCredentialsValid(eq(AppServerConstants.ADMIN_USERNAME), eq(AppServerConstants.ADMIN_USERNAME));
        expectLastCall().andReturn(validationResult).once();
    }

    public void testPagePostWithSuccessfulLogon() throws Exception {
        final PagePostTestParameters<LogonPageController> testParams = new PagePostTestParameters<LogonPageController>();
        checkPagePost(testParams, true);
        testParams.getResponseMock().sendRedirect(eq("startStopServer.html"));
        expectLastCall().once();
        testParams.getControl().replay();
        ServiceLocator.load(testParams.getServiceLocatorMock());
        testParams.getControllerMock().doPost(testParams.getRequestMock(), testParams.getResponseMock());
        testParams.getControl().verify();
    }

    public void testPagePostWithUnsuccessfulLogon() throws Exception {
        final PagePostTestParameters<LogonPageController> testParams = new PagePostTestParameters<LogonPageController>();
        checkPagePost(testParams, false);
        testParams.getControllerMock().setError(eq("Invalid credentials supplied"));
        expectLastCall().once();
        testParams.getResponseMock().getWriter();
        expectLastCall().andReturn(testParams.getPrintWriterMock()).once();
        testParams.getPrintWriterMock().print(and(and(contains("username"), contains("password")), and(contains("<form"), contains("<input"))));
        expectLastCall().once();
        testParams.getControl().replay();
        ServiceLocator.load(testParams.getServiceLocatorMock());
        testParams.getControllerMock().doPost(testParams.getRequestMock(), testParams.getResponseMock());
        testParams.getControl().verify();
    }
}
