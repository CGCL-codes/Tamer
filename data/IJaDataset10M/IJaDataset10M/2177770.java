package org.nakedobjects.reflector.original.reflect;

import org.nakedobjects.applib.NonPersistable;
import org.nakedobjects.object.Persistable;
import org.nakedobjects.testing.TestSpecification;
import org.nakedobjects.testing.TestSystem;
import junit.framework.TestCase;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

public class JavaSpecificationTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(JavaSpecificationTest.class);
    }

    private TestSystem system;

    protected void setUp() throws ClassNotFoundException {
        LogManager.getLoggerRepository().setThreshold(Level.OFF);
        system = new TestSystem();
        system.init();
        system.addSpecificationToLoader(new TestSpecification());
        system.addSpecificationToLoader(new TestSpecification(String.class.getName()));
    }

    protected void tearDown() throws Exception {
        system.shutdown();
    }

    public void testPersistable() throws Exception {
        system.addSpecificationToLoader(new TestSpecification(Object.class.getName()));
        system.addSpecificationToLoader(new TestSpecification(Interface1.class.getName()));
        system.addSpecificationToLoader(new TestSpecification(Interface2.class.getName()));
        JavaSpecification spec = new JavaSpecification(JavaObjectForReflector.class, new DummyBuilder(), new JavaReflector());
        spec.introspect();
        assertEquals(Persistable.USER_PERSISTABLE, spec.persistable());
    }

    public void testNotPersistable() throws Exception {
        system.addSpecificationToLoader(new TestSpecification(Object.class.getName()));
        system.addSpecificationToLoader(new TestSpecification(Interface1.class.getName()));
        system.addSpecificationToLoader(new TestSpecification(Interface2.class.getName()));
        system.addSpecificationToLoader(new TestSpecification(NonPersistable.class.getName()));
        JavaSpecification spec = new JavaSpecification(JavaObjectForReflectorTransient.class, new DummyBuilder(), new JavaReflector());
        spec.introspect();
        assertEquals(Persistable.TRANSIENT, spec.persistable());
    }
}
