package org.jostraca.unit.test;

import org.jostraca.unit.BasicUnit;
import org.jostraca.unit.BasicUnitList;
import org.jostraca.unit.BasicUnitOrigin;
import org.jostraca.unit.DebugUnitProcessor;
import org.jostraca.section.SectionSet;
import org.jostraca.transform.InsertSectionTransform;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import java.util.Hashtable;

/** <b>Description:</b><br>
 *  Test cases for DateUtil.
 */
public class DebugUnitProcessorTest extends TestCase {

    public DebugUnitProcessorTest(String pName) {
        super(pName);
    }

    public static TestSuite suite() {
        return new TestSuite(DebugUnitProcessorTest.class);
    }

    public static void main(String[] pArgs) {
        TestRunner.run(suite());
    }

    public void testList() throws Exception {
        BasicUnitList bus = new BasicUnitList();
        bus.add(new BasicUnit("text", "init", "it01"));
        bus.add(new BasicUnit("script", "init", "is01"));
        bus.add(new BasicUnit("text", "init", "it02"));
        bus.add(new BasicUnit("script", "init", "is02"));
        bus.add(new BasicUnit("text", "foo", "foot01"));
        bus.add(new BasicUnit("script", "foo", "foos01"));
        Hashtable isattr = new Hashtable();
        isattr.put(InsertSectionTransform.ATTR_unit_source_section, "foo");
        bus.add(new BasicUnit("text", "body", "12345678901234567890123456789012345678901234567890"));
        bus.add(new BasicUnit("text", "body", "bt01"));
        bus.add(new BasicUnit("script", "body", "bs01"));
        bus.add(new BasicUnit("insert-section", "body", "", new BasicUnitOrigin(), isattr));
        bus.add(new BasicUnit("text", "body", "bt02"));
        bus.add(new BasicUnit("script", "body", "bs02"));
        bus.add(new BasicUnit("text", "foo", "foot02"));
        bus.add(new BasicUnit("script", "foo", "foos02"));
        DebugUnitProcessor dup = new DebugUnitProcessor();
        SectionSet ss = dup.process(bus);
        System.out.println(ss);
    }
}
