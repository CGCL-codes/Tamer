package com.siemens.ct.exi.data;

import org.junit.Test;
import com.siemens.ct.exi.CodingMode;
import com.siemens.ct.exi.FidelityOptions;
import com.siemens.ct.exi.QuickTestConfiguration;

public class BuiltInXSDTestCase extends AbstractTestCase {

    public BuiltInXSDTestCase() {
        super("BuiltInXSD Test Cases");
    }

    public static void setupQuickTest() {
        setConfigurationBuiltInXSDIntVal();
    }

    protected void setUp() {
        testCaseOptions.add(new TestCaseOption());
        testCaseOptions.lastElement().setCodingMode(CodingMode.BIT_PACKED);
        testCaseOptions.lastElement().setFidelityOptions(FidelityOptions.createDefault());
        testCaseOptions.lastElement().setFragments(false);
        testCaseOptions.lastElement().setXmlEqual(false);
        testCaseOptions.add(new TestCaseOption());
        testCaseOptions.lastElement().setCodingMode(CodingMode.BYTE_PACKED);
        testCaseOptions.lastElement().setFidelityOptions(FidelityOptions.createDefault());
        testCaseOptions.lastElement().setFragments(false);
        testCaseOptions.lastElement().setXmlEqual(false);
        testCaseOptions.add(new TestCaseOption());
        testCaseOptions.lastElement().setCodingMode(CodingMode.PRE_COMPRESSION);
        testCaseOptions.lastElement().setFidelityOptions(FidelityOptions.createDefault());
        testCaseOptions.lastElement().setFragments(false);
        testCaseOptions.lastElement().setXmlEqual(false);
        testCaseOptions.add(new TestCaseOption());
        testCaseOptions.lastElement().setCodingMode(CodingMode.COMPRESSION);
        testCaseOptions.lastElement().setFidelityOptions(FidelityOptions.createDefault());
        testCaseOptions.lastElement().setFragments(false);
        testCaseOptions.lastElement().setXmlEqual(false);
        testCaseOptions.add(new TestCaseOption());
        testCaseOptions.lastElement().setCodingMode(CodingMode.BIT_PACKED);
        testCaseOptions.lastElement().setFidelityOptions(FidelityOptions.createAll());
        testCaseOptions.lastElement().setFragments(false);
        testCaseOptions.lastElement().setXmlEqual(true);
        testCaseOptions.add(new TestCaseOption());
        testCaseOptions.lastElement().setCodingMode(CodingMode.COMPRESSION);
        testCaseOptions.lastElement().setFidelityOptions(FidelityOptions.createAll());
        testCaseOptions.lastElement().setFragments(false);
        testCaseOptions.lastElement().setXmlEqual(true);
        testCaseOptions.add(new TestCaseOption());
        testCaseOptions.lastElement().setCodingMode(CodingMode.BIT_PACKED);
        testCaseOptions.lastElement().setFidelityOptions(FidelityOptions.createStrict());
        testCaseOptions.lastElement().setFragments(false);
        testCaseOptions.lastElement().setXmlEqual(false);
        testCaseOptions.lastElement().setSchemaInformedOnly(true);
        testCaseOptions.add(new TestCaseOption());
        testCaseOptions.lastElement().setCodingMode(CodingMode.COMPRESSION);
        testCaseOptions.lastElement().setFidelityOptions(FidelityOptions.createStrict());
        testCaseOptions.lastElement().setFragments(false);
        testCaseOptions.lastElement().setXmlEqual(false);
        testCaseOptions.lastElement().setSchemaInformedOnly(true);
    }

    @Test
    public void testBuiltInXSDIntVal() throws Exception {
        setConfigurationBuiltInXSDIntVal();
        _test();
    }

    public static void setConfigurationBuiltInXSDIntVal() {
        QuickTestConfiguration.setXsdLocation("");
        QuickTestConfiguration.setXmlLocation("./data/builtInXSD/intVal.xml");
        QuickTestConfiguration.setExiLocation("./out/builtInXSD/intVal.xml.exi");
    }

    @Test
    public void testBuiltInXSDIntVal2() throws Exception {
        setConfigurationBuiltInXSDIntVal2();
        _test();
    }

    public static void setConfigurationBuiltInXSDIntVal2() {
        QuickTestConfiguration.setXsdLocation("");
        QuickTestConfiguration.setXmlLocation("./data/builtInXSD/intVal2.xml");
        QuickTestConfiguration.setExiLocation("./out/builtInXSD/intVal2.xml.exi");
    }

    @Test
    public void testBuiltInXSDFloatVal() throws Exception {
        setConfigurationBuiltInXSDFloatVal();
        _test();
    }

    public static void setConfigurationBuiltInXSDFloatVal() {
        QuickTestConfiguration.setXsdLocation("");
        QuickTestConfiguration.setXmlLocation("./data/builtInXSD/floatVal.xml");
        QuickTestConfiguration.setExiLocation("./out/builtInXSD/floatVal.xml.exi");
    }
}
