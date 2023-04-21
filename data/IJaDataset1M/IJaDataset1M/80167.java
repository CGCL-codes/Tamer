package org.openi.web.controller.admin;

import org.openi.application.Application;
import org.openi.web.controller.BaseControllerTestCase;
import org.springframework.mock.web.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Uddhab Pant <br>
 * @version $Revision: 1.7 $ $Date: 2006/04/12 00:39:12 $ <br>
 *
 * Test case for application form controller.
 *
 *
 */
public class ApplicationFormControllerTest extends BaseControllerTestCase {

    private ApplicationFormController ac1;

    private ApplicationFormController ac2;

    private MockHttpServletRequest request;

    private HttpServletResponse response;

    private ModelAndView mv;

    /**
     * Initialize test objects
     *
     * @throws IOException
     */
    public void setUp() throws Exception {
        logger.info("Setting up");
        super.setUp();
        ac1 = (ApplicationFormController) appContext.getBean("applicationFormController");
        ac2 = new ApplicationFormController();
        ac2.setApplicationContext(webAppContext);
        request = newPost("/editapplication.htm");
        response = new MockHttpServletResponse();
    }

    /**
     * Test form
     *
     * @throws Exception
     */
    public void testEdit() throws Exception {
        logger.info("Testing form");
        assertFalse("Session form is not false", ac1.isSessionForm());
        assertEquals("Command name is not equal", "application", ac1.getCommandName());
        assertEquals("Command class name is not equal", "org.openi.application.Application", ac1.getCommandClass().getName());
        assertEquals("Form view name is not equal", "editApplicationForm", ac1.getFormView());
        assertEquals("Success view is not equal", "editApplicationRedirect", ac1.getSuccessView());
    }

    /**
     * Test submit
     */
    public void testOnSubmit() throws Exception {
        logger.info("Testing submit");
        ac2.setSessionForm(ac1.isSessionForm());
        ac2.setFormView(ac1.getFormView());
        ac2.setSuccessView(ac1.getSuccessView());
        ac2.setCommandClass(ac1.getCommandClass());
        ac2.setCommandName(ac1.getCommandName());
        request.addParameter("save", "save");
        request.addParameter("applicationTitle", "OpenI� Open Intelligence Portal");
        request.addParameter("applicationAdmins", "upant");
        request.addParameter("poweredByLogoName", "../openi-projects/openi/images/openi-footer.png");
        request.addParameter("logonImageName", "../openi-projects/openi/images/openi-logo.png");
        request.addParameter("customerSupport", "http://sourceforge.net/forum/?group_id=142873");
        request.addParameter("copyrightMessage", "copyright (c) 2005, Loyalty Matrix, Inc.");
        request.addParameter("basicAuthentication", "true");
        request.addParameter("applicationAdminPermissions", "Application_Administration,Project_Administration,Save_Public,Save_Private,Delete_Public,Delete_Private,Create_New,Upload_File,Diagnostics,Configure_Datasource,Manage_Files");
        request.addParameter("projectAdminPermissions", "Project_Administration,Save_Public,Save_Private,Delete_Public,Delete_Private,Create_New,Upload_File,Configure_Datasource,Manage_Files");
        request.addParameter("projectUserPermissions", "Save_Private,Delete_Private,Create_New,Configure_Datasource");
        mv = ac2.handleRequest(request, response);
        Application app = (Application) mv.getModel().get(ac2.getCommandName());
        assertNotNull("application is null", app);
        assertEquals("title is not equal", "OpenI� Open Intelligence Portal", app.getApplicationTitle());
        assertEquals("app admins is not equal", "upant", app.getApplicationAdmins());
        assertEquals("powered by logo in not equal", "../openi-projects/openi/images/openi-footer.png", app.getPoweredByLogoName());
        assertEquals("logon image is not equal", "../openi-projects/openi/images/openi-logo.png", app.getLogonImageName());
        assertEquals("contact support is not equal", "http://sourceforge.net/forum/?group_id=142873", app.getCustomerSupport());
        assertEquals("copy right is not equal", "copyright (c) 2005, Loyalty Matrix, Inc.", app.getCopyrightMessage());
        assertEquals("basic auth is not equal", "true", String.valueOf(app.isBasicAuthentication()));
        assertEquals("app admin permissions is not equal", "Application_Administration,Project_Administration,Save_Public,Save_Private,Delete_Public,Delete_Private,Create_New,Upload_File,Diagnostics,Configure_Datasource,Manage_Files", app.getApplicationAdminPermissions());
        assertEquals("project admin permissions is not equal", "Project_Administration,Save_Public,Save_Private,Delete_Public,Delete_Private,Create_New,Upload_File,Configure_Datasource,Manage_Files", app.getProjectAdminPermissions());
        assertEquals("project user permission is not equal", "Save_Private,Delete_Private,Create_New,Configure_Datasource", app.getProjectUserPermissions());
    }

    protected void tearDown() {
        ac1 = null;
        ac2 = null;
    }
}
