package org.nakedobjects.reflector.java.reflect;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectAction;
import org.nakedobjects.object.NakedObjectField;
import org.nakedobjects.object.NakedObjectSpecificationException;
import org.nakedobjects.object.Persistable;
import org.nakedobjects.object.control.Allow;
import org.nakedobjects.object.reflect.OneToOnePeer;
import org.nakedobjects.object.reflect.ValuePeer;
import org.nakedobjects.testing.DummyNakedObject;
import org.nakedobjects.testing.DummyOneToOneAssociation;
import org.nakedobjects.testing.DummyValueAssociation;
import org.nakedobjects.testing.TestSpecification;
import org.nakedobjects.testing.TestSystem;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

public class JavaIntrospectorTest extends TestCase {

    private JavaIntrospector reflector;

    private TestSystem system;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(new TestSuite(JavaIntrospectorTest.class));
    }

    protected void setUp() throws ClassNotFoundException {
        LogManager.getLoggerRepository().setThreshold(Level.OFF);
        system = new TestSystem();
        system.init();
        system.addSpecification(new TestSpecification());
        system.addSpecification(new TestSpecification(String.class.getName()));
        reflector = new JavaIntrospector(JavaObjectForReflector.class, new DummyBuilder(), new JavaSpecificationLoader());
        reflector.introspect();
    }

    protected void tearDown() throws Exception {
        system.shutdown();
    }

    public void testActionSortOrder() throws NakedObjectSpecificationException {
        NakedObjectAction[] actions = reflector.getObjectActions();
        assertEquals("start", actions[0].getId());
        assertEquals("stop", actions[1].getId());
    }

    public void testFieldSortOrder() throws NakedObjectSpecificationException {
        NakedObjectField[] fields = reflector.getFields();
        assertEquals(3, fields.length);
        assertEquals("One", fields[0].getId());
        assertEquals("Two", fields[1].getId());
        assertEquals("Three", fields[2].getId());
    }

    public void testClassActionList() throws NakedObjectSpecificationException {
        NakedObjectAction[] actions = reflector.getClassActions();
        assertEquals(2, actions.length);
    }

    public void testObjectActionList() throws NakedObjectSpecificationException {
        NakedObjectAction[] actions = reflector.getObjectActions();
        assertEquals(2, actions.length);
    }

    public void testClassActionSortOrder() throws NakedObjectSpecificationException {
        NakedObjectAction[] actions = reflector.getClassActions();
        assertEquals("top", actions[0].getId());
        assertEquals("bottom", actions[1].getId());
    }

    public void testObjectActionName() {
        NakedObjectAction[] actions = reflector.getObjectActions();
        assertEquals(null, actions[0].getName());
        assertEquals("object action name", actions[1].getName());
    }

    public void testObjectActionDescription() {
        NakedObjectAction[] actions = reflector.getObjectActions();
        assertEquals(null, actions[0].getDescription());
        assertEquals("object action description", actions[1].getDescription());
    }

    public void testClassActionName() {
        NakedObjectAction[] actions = reflector.getClassActions();
        assertEquals("class action name", actions[0].getName());
        assertEquals(null, actions[1].getName());
    }

    public void testClassActionDescription() {
        NakedObjectAction[] actions = reflector.getClassActions();
        assertEquals("class action description", actions[0].getDescription());
        assertEquals(null, actions[1].getDescription());
    }

    public void testClassActionParameters() {
        NakedObjectAction[] actions = reflector.getClassActions();
        assertEquals(0, actions[0].getParameterCount());
        assertEquals(1, actions[1].getParameterCount());
        assertEquals(1, actions[1].getParameterLabels().length);
        assertEquals("parameter name 1", actions[1].getParameterLabels()[0]);
    }

    public void testObjectActionParameters() {
        NakedObjectAction[] actions = reflector.getObjectActions();
        assertEquals(0, actions[1].getParameterCount());
        assertEquals(1, actions[0].getParameterCount());
        assertEquals(1, actions[0].getParameterLabels().length);
        assertEquals("parameter name 2", actions[0].getParameterLabels()[0]);
    }

    public void testClassActionValidate() {
        NakedObjectAction[] actions = reflector.getClassActions();
        assertEquals(Allow.DEFAULT, actions[0].isParameterSetValid(null, new Naked[0]));
        assertEquals(Allow.DEFAULT, actions[1].isParameterSetValid(null, new Naked[0]));
        JavaObjectForReflector.classActionValid = "not now";
        assertEquals(true, actions[0].isParameterSetValid(null, new Naked[0]).isVetoed());
        assertEquals("not now", actions[0].isParameterSetValid(null, new Naked[0]).getReason());
        assertEquals(Allow.DEFAULT, actions[1].isParameterSetValid(null, new Naked[0]));
    }

    public void testObjectActionValidate() {
        NakedObjectAction[] actions = reflector.getObjectActions();
        JavaObjectForReflector object = new JavaObjectForReflector();
        NakedObject adapter = new DummyNakedObject(object);
        assertEquals(Allow.DEFAULT, actions[0].isParameterSetValid(adapter, new Naked[1]));
        assertEquals(Allow.DEFAULT, actions[1].isParameterSetValid(adapter, new Naked[1]));
        object.objectActionValid = "not valid";
        assertEquals(true, actions[0].isParameterSetValid(adapter, new Naked[1]).isVetoed());
        assertEquals("not valid", actions[0].isParameterSetValid(adapter, new Naked[1]).getReason());
        assertEquals(Allow.DEFAULT, actions[1].isParameterSetValid(adapter, new Naked[1]));
    }

    public void testShortName() {
        assertEquals("JavaObjectForReflector", reflector.shortName());
    }

    public void testPluralName() {
        assertEquals("Plural", reflector.pluralName());
    }

    public void testPersistable() {
        assertEquals(Persistable.USER_PERSISTABLE, reflector.persistable());
        reflector = new JavaIntrospector(JavaObjectForReflectorTransient.class, new DummyBuilder(), new JavaSpecificationLoader());
        reflector.introspect();
        assertEquals(Persistable.TRANSIENT, reflector.persistable());
    }

    public void testSingularName() {
        assertEquals("Singular", reflector.singularName());
    }

    public void testFields() throws Exception {
        NakedObjectField[] fields = reflector.getFields();
        assertEquals(3, fields.length);
        ValuePeer member = ((DummyValueAssociation) fields[0]).getPeer();
        assertEquals("One", member.getIdentifier().getName());
        OneToOnePeer member2 = ((DummyOneToOneAssociation) fields[2]).getPeer();
        assertEquals("Three", member2.getIdentifier().getName());
    }

    public void testNameManipulations() {
        assertEquals("CarRegistration", JavaIntrospector.javaBaseName("getCarRegistration"));
        assertEquals("Driver", JavaIntrospector.javaBaseName("Driver"));
        assertEquals("Register", JavaIntrospector.javaBaseName("actionRegister"));
        assertEquals("", JavaIntrospector.javaBaseName("action"));
    }

    public void testSuperclass() {
        assertEquals(Object.class.getName(), reflector.getSuperclass());
    }

    public void testInterfaces() {
        String[] interfaces = reflector.getInterfaces();
        assertEquals(2, interfaces.length);
        assertEquals(Interface1.class.getName(), interfaces[0]);
        assertEquals(Interface2.class.getName(), interfaces[1]);
    }

    public void testLookup() {
        assertFalse(reflector.isLookup());
    }

    public void testIntrospectsList() {
        JavaSpecificationLoader specLoader = new JavaSpecificationLoader();
        specLoader.setOneToManyStrategies(new OneToManyStrategy[] { new ListStrategy() });
        reflector = new JavaIntrospector(JavaObjectWithList.class, new DummyBuilder(), specLoader);
        reflector.introspect();
        NakedObjectField[] fields = reflector.getFields();
        assertEquals("number of fields", 1, fields.length);
        assertEquals("id", "Method", fields[0].getId());
    }
}
