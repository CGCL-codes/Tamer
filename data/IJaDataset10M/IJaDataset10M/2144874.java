package com.apelonTests.struts.dtsbrowser;

import java.io.IOException;
import com.apelon.struts.dtsbrowser.*;
import com.apelon.struts.common.*;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.extensions.TestSetup;
import com.apelonTests.ConfiguredStrutsTest;

/**
 * Runs test cases for the GetRolePropAction in the struts framework.
 *
 * @author Apelon, Inc.
 * @version 1.0
 * @since DTS 2.3.1
 */
public class TestGetRolePropAction extends DTSBrowserTest {

    public TestGetRolePropAction(String name) {
        super(name);
    }

    public TestGetRolePropAction() {
        this(TestGetRolePropAction.class.getName());
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestGetRolePropAction("testGetRoleProp"));
        TestSetup wrapper = new DTSBrowserTestSetup(suite);
        return wrapper;
    }

    /**
     * Check to ensure that the file that was selected is the
     * document that ends up in the session for use by the view.
     */
    public void testGetRoleProp() throws Exception {
        getSession().setAttribute("serverIsDown", "false");
        getSession().setAttribute("ConnectionType", "socketServer");
        setRequestPathInfo("/GetRoleAndProperty");
        addRequestParameter("conceptname", pattern());
        actionPerform();
        verifyForward("result");
        verifyNoActionErrors();
    }
}
