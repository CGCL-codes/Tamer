package org.jostraca.process.test;

import org.jostraca.Service;
import org.jostraca.Template;
import org.jostraca.process.PreparerProcessStage;
import org.jostraca.process.TemplateHandlerManager;
import org.jostraca.util.ListUtil;
import org.jostraca.util.CommandLineUserMessageHandler;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import java.util.ArrayList;

/** Test cases for TemplateHandlerManager.
 */
public class PreparerProcessStageTest extends TestCase {

    public PreparerProcessStageTest(String pName) {
        super(pName);
    }

    public static TestSuite suite() {
        return new TestSuite(PreparerProcessStageTest.class);
    }

    public static void main(String[] pArgs) {
        TestRunner.run(suite());
    }

    public void testPreparer() throws Exception {
        CommandLineUserMessageHandler cm = new CommandLineUserMessageHandler();
        Template tm01 = PackageTest.makeTemplate("tm01");
        tm01.getPropertySet(Service.CONF_template).set(TemplateHandlerManager.PROPERTY_PREFIX + "Preparer", "org.jostraca.process.GenericPreparer");
        ArrayList tmlist = ListUtil.make(tm01);
        PreparerProcessStage pps = new PreparerProcessStage();
        pps.setUserMessageHandler(cm);
        pps.process(tmlist);
    }
}
