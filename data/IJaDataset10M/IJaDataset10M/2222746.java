package org.xorm.tests;

import junit.framework.*;
import org.xorm.XORM;
import org.xorm.tests.model.*;
import javax.jdo.PersistenceManager;
import javax.jdo.JDOHelper;

public class TestStringOps extends XORMTestCase {

    public TestStringOps(String name) {
        super(name);
    }

    public void testStringOps() {
        PersistenceManager mgr = factory.getPersistenceManager();
        StringInterface pc = (StringInterface) XORM.newInstance(mgr, StringInterface.class);
        pc.setStringAttribute("TestString");
        System.out.println("StringInterface.toString(): " + pc);
        StringClass sc = (StringClass) XORM.newInstance(mgr, StringClass.class);
        sc.setStringAttribute("TestString");
        System.out.println("StringClass.toString(): " + sc);
        StringClassBase scb = sc;
        System.out.println("StringClassBase.toString(): " + scb);
        Object o = scb;
        System.out.println("Object.toString(): " + o);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestStringOps.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        return new TestSuite(TestStringOps.class);
    }
}
