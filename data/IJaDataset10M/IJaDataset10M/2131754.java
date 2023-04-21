package com.avatal.test.strutstestcase;

import com.avatal.test.*;

public class AdminUserActionTest extends AbstractStrutsTestCase {

    public DeleteTestUserDAO dao = new DeleteTestUserDAO();

    public AdminUserActionTest(String name) throws Exception {
        super(name);
    }

    public void testAdminUserAction() throws Exception {
        setRequestPathInfo("/adminUser");
        addRequestParameter("updateItem", new String[] { "1" });
        addRequestParameter("target", "deactivateUser");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/layouts/rootLayout.jsp");
        verifyNoActionErrors();
        verifyActionMessages(new String[] { "globals.message.action_successful" });
        clearRequestParameters();
        setRequestPathInfo("/adminUser");
        addRequestParameter("updateItem", new String[] { "1" });
        addRequestParameter("target", "activateUser");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/layouts/rootLayout.jsp");
        verifyNoActionErrors();
        verifyActionMessages(new String[] { "globals.message.action_successful" });
        clearRequestParameters();
    }
}
