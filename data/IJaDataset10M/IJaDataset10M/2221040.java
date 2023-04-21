package com.sourceforge.oraclewicket.app.usermgr.standard;

import com.sourceforge.oraclewicket.AppTester;
import com.sourceforge.oraclewicket.WicketApplication;
import com.sourceforge.oraclewicket.app.usermgr.mgr.standard.StandardUserMgrBean;
import com.sourceforge.oraclewicket.app.usermgr.mgr.standard.StandardUserPasswordMgrPage;
import junit.framework.TestCase;

public class TestStandardUserPasswordMgrPage extends TestCase {

    private AppTester tester;

    @Override
    public void setUp() {
        tester = new AppTester(new WicketApplication());
    }

    public void testRenderStandardUserPasswordMgrPage() {
        tester.doUnitTestLogin();
        tester.startPage(new StandardUserPasswordMgrPage(new StandardUserMgrBean(4, "UNIT_TEST", null, null, null, null, null, null, false)));
        tester.assertRenderedPage(StandardUserPasswordMgrPage.class);
    }
}
