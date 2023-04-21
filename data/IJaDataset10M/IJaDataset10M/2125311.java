package simpleorm.simplets.test;

import simpleorm.simplets.servlet.HMasterServlet;

/** Registers the Requestlets.  All work done by HMaster Servlet. */
public class TestServlet extends HMasterServlet {

    public static final TestPlainRequestlet.TestPlainFactory testFactory = new TestPlainRequestlet.TestPlainFactory().register();

    public static final TestComponentRequestlet.TestComponentsFactory componentTestFactory = new TestComponentRequestlet.TestComponentsFactory().register();
}
