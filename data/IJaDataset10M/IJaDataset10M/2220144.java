package org.jmove.java.loader.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmove.core.JmoveException;
import org.jmove.core.Thing;
import org.jmove.core.ThingFilter;
import org.jmove.java.loader.LoadTypesFromModule;
import org.jmove.java.loader.aspects.Location;
import org.jmove.java.model.JModel;
import org.jmove.java.model.JPackage;
import org.jmove.java.model.Type;
import org.jmove.java.util.JavaUtil;
import org.jmove.oo.Module;
import java.util.Set;

/**
 * Testing the loading of types for a source module.
 *
 * @author Michael Juergens
 */
public class LoadTypesFromSourceModuleTest extends TestCase {

    private JModel testModel;

    public static Test suite() {
        return new TestSuite(LoadTypesFromSourceModuleTest.class);
    }

    public LoadTypesFromSourceModuleTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        testModel = new JModel();
    }

    /**
     * If no module is specified for the load action,
     * then it will fail with an exception.
     */
    public void testNullModule() {
        LoadTypesFromModule loadTypes = new LoadTypesFromModule(null);
        try {
            loadTypes.perform();
            fail();
        } catch (JmoveException jme) {
        }
    }

    /**
     * The load action cannot be performed on a module without location.
     * in this case the action will fail with an exception.
     */
    public void testModuleWithoutLocation() {
        Module module = testModel.defineModule("foo");
        LoadTypesFromModule loadTypes = new LoadTypesFromModule(module);
        try {
            loadTypes.perform();
            fail();
        } catch (JmoveException jme) {
        }
    }

    /**
     * The location of a module has to be a valid location. This means the location
     * must exist physically. If this condition is violated then the action will fail
     * with an exception.
     */
    public void testModuleWitNonExistentLocation() {
        Module module = testModel.defineModule("foo");
        Location.attach(module, "./foo/foo.jar");
        LoadTypesFromModule loadTypes = new LoadTypesFromModule(module);
        try {
            loadTypes.perform();
            fail();
        } catch (JmoveException jme) {
        }
    }

    /**
     * We can add a module explicitly to the model as an alternative to taking the
     * modules from the classpath. The advantage is, that we can use other id's for
     * the modules.
     */
    public void testDefineModule() {
        loadModel();
        assertTrue(testModel.getTypeCount() > 119);
        System.out.println("Total count of classes for module 'org.jmove.java' is " + testModel.getTypeCount() + ".");
    }

    /**
     * When the types are loaded from the module then the containment dependency
     * between inner and declaring classes must be defined.
     */
    public void testInnerClassContainment() {
        loadModel();
        Type innerType = testModel.lookupType("org.jmove.java.test.model.InnerTypesTestClassA.Inner1");
        assertNotNull(innerType);
        assertTrue(innerType.isInnerType());
        Type declaringType = innerType.getDeclaringType();
        assertNotNull(declaringType);
        assertEquals(JavaUtil.containerIdOf(innerType.id()), declaringType.id());
    }

    /**
     * No class with inner classes should never be defined as a package. this had been a temporary bug."
     */
    public void testDisjointClassAndPackageIds() {
        loadModel();
        Set packagesWithAmbiguosIds = testModel.things(JPackage.class, new ThingFilter() {

            public boolean accept(Thing thing) {
                return (testModel.lookupType(thing.id()) != null);
            }
        });
        assertEquals(0, packagesWithAmbiguosIds.size());
    }

    protected void loadModel() {
        Module module = testModel.defineModule("jmove.test");
        assertSame(module, testModel.lookupModule("jmove.test"));
        Location.attach(module, "../source/java/org/jmove/java/");
        assertEquals("../source/java/org/jmove/java/", Location.from(module).getLocation());
        LoadTypesFromModule loadTypes = new LoadTypesFromModule(module);
        loadTypes.perform();
    }
}
