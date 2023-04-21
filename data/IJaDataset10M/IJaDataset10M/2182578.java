package org.jmove.junit.test;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmove.core.ThingHashSet;
import org.jmove.core.ThingSet;
import org.jmove.java.model.JModel;
import org.jmove.java.model.JPackage;
import org.jmove.java.model.Type;
import org.jmove.junit.Assert;

/**
 * This class implements test cases for the Assert utility class.
 * 
 * @author Axel Terfloth
 */
public class AssertNoDependencyTest extends TestCase {

    private JModel testModel;

    protected JPackage pkgA;

    protected JPackage pkgB;

    private JPackage pkgX;

    private JPackage pkgY;

    private Type typeA;

    private Type typeB;

    private Type typeX;

    private Type typeY;

    public static Test suite() {
        return new TestSuite(AssertNoDependencyTest.class);
    }

    public AssertNoDependencyTest() {
        super();
    }

    public AssertNoDependencyTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        testModel = new JModel();
        pkgA = testModel.definePackage("a", null);
        pkgB = testModel.definePackage("b", null);
        pkgX = testModel.definePackage("x", null);
        pkgY = testModel.definePackage("y", null);
        typeA = testModel.defineType("a.A", pkgA);
        typeB = testModel.defineType("b.B", pkgB);
        typeX = testModel.defineType("x.X", pkgX);
        typeY = testModel.defineType("y.Y", pkgY);
        typeA.addDependency(typeY);
        typeB.addDependency(typeX);
    }

    public void testNullParameter() {
        ThingSet set = new ThingHashSet();
        Assert.noDependency(null, null);
        Assert.noDependency(set, null);
        Assert.noDependency(null, set);
    }

    public void testNoDependency() {
        ThingSet set1 = new ThingHashSet();
        set1.add(pkgA);
        set1.add(pkgB);
        ThingSet set2 = new ThingHashSet();
        set2.add(pkgX);
        set2.add(pkgY);
        Assert.noDependency(set2, set1);
    }

    public void testDependency() {
        ThingSet set1 = new ThingHashSet();
        set1.add(pkgA);
        set1.add(pkgB);
        ThingSet set2 = new ThingHashSet();
        set2.add(pkgX);
        set2.add(pkgY);
        try {
            Assert.noDependency(set1, set2);
        } catch (AssertionFailedError e) {
            String message = e.getMessage();
            System.out.print(message);
            assertTrue(message.indexOf("Illegal dependencies found") != -1);
            assertTrue(message.indexOf("a --> y") != -1);
            assertTrue(message.indexOf("a.A --> y.Y") != -1);
            assertTrue(message.indexOf("b --> x") != -1);
            assertTrue(message.indexOf("b.B --> x.X") != -1);
            return;
        }
        fail();
    }
}
