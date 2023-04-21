package org.telluriumsource.ft;

import org.telluriumsource.module.JettyLogonModule;
import org.telluriumsource.test.java.TelluriumMockTestNGTestCase;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author: Jian Fang (John.Jian.Fang@gmail.com)
 * 
 * Date: Sep 17, 2010
 */
public class JettyLogonParallelTestCase extends TelluriumMockTestNGTestCase {

    private static JettyLogonModule jlm;

    @BeforeClass
    public static void initUi() {
        registerHtmlBody("JettyLogon");
        jlm = new JettyLogonModule();
        jlm.defineUi();
        useCssSelector(true);
        useTelluriumEngine(true);
        useTrace(true);
        useEngineLog(true);
    }

    @BeforeMethod
    public void connectToLocal() {
        connect("JettyLogon");
    }

    @Test(threadPoolSize = 7, invocationCount = 20)
    public void testLogon() {
        jlm.validate("Form");
        jlm.diagnose("Form.Username.Input");
        jlm.logon("test", "test");
    }

    @AfterClass
    public static void tearDown() {
        showTrace();
    }
}
