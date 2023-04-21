package com.sourceforge.oraclewicket.app.usermgr.standard;

import org.apache.wicket.util.tester.FormTester;
import com.sourceforge.oraclewicket.AppTester;
import com.sourceforge.oraclewicket.WicketApplication;
import com.sourceforge.oraclewicket.app.usermgr.mgr.standard.StandardUserMgrBean;
import com.sourceforge.oraclewicket.app.usermgr.mgr.standard.StandardUserRoleMgrPage;
import junit.framework.TestCase;

public class TestStandardUserRoleMgrPage extends TestCase {

    private AppTester tester;

    @Override
    public void setUp() {
        tester = new AppTester(new WicketApplication());
    }

    public void testRenderStandardUserPasswordMgrPage() {
        tester.doUnitTestLogin();
        tester.startPage(new StandardUserRoleMgrPage(new StandardUserMgrBean(4, "UNIT_TEST", null, null, null, null, null, null, false)));
        tester.assertRenderedPage(StandardUserRoleMgrPage.class);
    }

    public void testSubmitForm() {
        testRenderStandardUserPasswordMgrPage();
        FormTester formTester = tester.newFormTester("standardUserRoleMgrForm");
        formTester.submit();
        tester.assertRenderedPage(StandardUserRoleMgrPage.class);
    }
}
