package org.jostraca.process.test;

import org.jostraca.Constants;
import org.jostraca.Template;
import org.jostraca.BasicTemplate;
import org.jostraca.BasicTemplatePath;
import org.jostraca.util.FileUtil;
import org.jostraca.util.PropertySet;
import java.io.File;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** Test cases for org.jostraca.process package. */
public class PackageTest extends TestCase {

    public PackageTest(String pName) {
        super(pName);
    }

    public static void main(String[] pArgs) {
        TestRunner.run(suite());
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite(PackageTest.class.getName());
        suite.addTest(TemplateHandlerManagerTest.suite());
        suite.addTest(BasicProcessManagerTest.suite());
        suite.addTest(CompilerProcessStageTest.suite());
        suite.addTest(ControllerProcessStageTest.suite());
        suite.addTest(MergerProcessStageTest.suite());
        suite.addTest(ParserProcessStageTest.suite());
        suite.addTest(PreparerProcessStageTest.suite());
        suite.addTest(ProcessStageTest.suite());
        suite.addTest(SaverProcessStageTest.suite());
        return suite;
    }

    public static PropertySet getSystemPropertySet() throws Exception {
        String base = FileUtil.findFile("org/jostraca/process/test").getAbsolutePath();
        PropertySet systemps = new PropertySet();
        systemps.load(new File(base, "../../../../../conf/system.conf"));
        return systemps;
    }

    public static Template makeTemplate(String pName) throws Exception {
        String base = FileUtil.findFile("org/jostraca/process/test").getAbsolutePath();
        PropertySet systemps = getSystemPropertySet();
        BasicTemplate tm = new BasicTemplate();
        tm.setPropertySet(Constants.CONF_system, systemps);
        String tmfp = base + "/tm/" + pName + ".jtm";
        BasicTemplatePath tmp01 = new BasicTemplatePath(tmfp);
        tmp01.resolve(new String[] {});
        tm.load(tmp01);
        return tm;
    }
}
