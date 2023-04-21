package org.acs.elated.test.ui;

import servletunit.struts.*;
import org.acs.elated.app.AppMgr;
import java.io.File;

public class TestCreateItemAction extends MockStrutsTestCase {

    private AppMgr myAppMgr;

    protected void setUp() throws Exception {
        super.setUp();
        this.getSession().setAttribute("fedoraManager", new AppMgr());
        this.setContextDirectory(new File("elatedweb"));
        this.setRequestPathInfo("/createItemAction");
        myAppMgr = (AppMgr) this.getSession().getAttribute("fedoraManager");
        myAppMgr.getUser().setAuthenticated(true);
    }

    public void testCreateItemNoTitle() {
        this.addRequestParameter("title", "");
        this.actionPerform();
        this.verifyForward("failure");
    }

    public void testCreateItemValidFields() {
        this.addRequestParameter("collectionPID", "demo:DemoColl2");
        this.addRequestParameter("title", "CreateItemActionTest");
        this.addRequestParameter("creator", "UI Tester");
        this.addRequestParameter("description", "");
        this.addRequestParameter("subject", "");
        this.addRequestParameter("publisher", "");
        this.addRequestParameter("contributor", "");
        this.addRequestParameter("date", "");
        this.addRequestParameter("type", "");
        this.addRequestParameter("format", "");
        this.addRequestParameter("identifier", "");
        this.addRequestParameter("source", "");
        this.addRequestParameter("language", "");
        this.addRequestParameter("relation", "");
        this.addRequestParameter("coverage", "");
        this.addRequestParameter("rights", "");
        this.addRequestParameter("test", "true");
        this.actionPerform();
        this.verifyForward("success");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
