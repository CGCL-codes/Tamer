package com.sourceforge.oraclewicket.app.refdata.mgr.list;

import junit.framework.TestCase;
import com.sourceforge.oraclewicket.app.refdata.mgr.RefDataBean;
import com.sourceforge.oraclewicket.AppTester;
import com.sourceforge.oraclewicket.WicketApplication;

public class TestListMgrPage extends TestCase {

    private AppTester tester;

    @Override
    public void setUp() {
        tester = new AppTester(new WicketApplication());
    }

    public void testRenderCodedListMgrPage() {
        tester.doUnitTestLogin();
        tester.startPage(new ListMgrPage(new RefDataBean(1, "", "", true, "X", true)));
        tester.assertRenderedPage(ListMgrPage.class);
    }
}
