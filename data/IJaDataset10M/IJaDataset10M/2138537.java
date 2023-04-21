package org.nakedobjects.nof.reflect.java.reflect;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;
import junit.framework.TestCase;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.nakedobjects.noa.adapter.NakedCollection;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.nof.core.reflect.Allow;
import org.nakedobjects.nof.core.util.DebugString;
import org.nakedobjects.nof.reflect.java.facets.propcoll.read.JavaBeanPropertyAccessorMethod;
import org.nakedobjects.nof.reflect.java.reflect.collections.JavaCollectionAssociation;
import org.nakedobjects.nof.reflect.peer.MemberIdentifier;
import org.nakedobjects.nof.reflect.peer.MemberIdentifierImpl;
import org.nakedobjects.nof.testsystem.TestPojo;
import org.nakedobjects.nof.testsystem.TestProxyNakedCollection;
import org.nakedobjects.nof.testsystem.TestProxyNakedObject;
import org.nakedobjects.nof.testsystem.TestProxySystem;

public class JavaCollectionAssociationTest extends TestCase {

    private TestProxySystem system;

    private JavaCollectionAssociation collectionField;

    private TestObjectWithCollection testPojo;

    private TestProxyNakedObject testAdapter;

    private Vector collection;

    private TestPojo elementToAdd;

    protected void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        system = new TestProxySystem();
        system.init();
        MemberIdentifier identifer = new MemberIdentifierImpl(TestObjectWithCollection.class.getName());
        Class elementType = TestPojo.class;
        collectionField = new JavaCollectionAssociation(identifer, elementType);
        Method getMethod = TestObjectWithCollection.class.getMethod("getList", new Class[0]);
        collectionField.addFacet(new JavaBeanPropertyAccessorMethod(getMethod, collectionField));
        collection = new Vector();
        testPojo = new TestObjectWithCollection(collection, false);
        testAdapter = system.createPersistentTestObject(testPojo);
        elementToAdd = new TestPojo();
    }

    public void testType() throws Exception {
        assertEquals(system.getSpecification(TestPojo.class), collectionField.getSpecification());
    }

    public void testCollectionSizeZero() {
        NakedCollection c = collectionField.getAssociations(testAdapter);
        assertEquals(0, c.size());
    }

    public void testAddAssociation() {
        NakedCollection c = collectionField.getAssociations(testAdapter);
        TestPojo elementToAdd = new TestPojo();
        collectionField.addAssociation(testAdapter, system.createPersistentTestObject(elementToAdd));
        assertEquals(1, c.size());
        assertEquals(elementToAdd, c.firstElement().getObject());
    }

    public void testRemoveAssociation() {
        NakedCollection c = collectionField.getAssociations(testAdapter);
        TestProxyNakedObject elementAdapter = system.createPersistentTestObject();
        c.add(elementAdapter);
        assertEquals(1, c.size());
        collectionField.removeAssociation(testAdapter, elementAdapter);
        assertEquals(0, c.size());
    }

    public void testRemoveAllAssociations() {
        NakedCollection c = collectionField.getAssociations(testAdapter);
        c.add(system.createPersistentTestObject());
        c.add(system.createPersistentTestObject());
        assertEquals(2, c.size());
        collectionField.removeAllAssociations(testAdapter);
        assertEquals(0, c.size());
    }

    public void testInitAssociations() {
        NakedObject elementToAdd1 = system.createPersistentTestObject();
        NakedObject elementToAdd2 = system.createPersistentTestObject();
        collectionField.initOneToManyAssociation(testAdapter, new NakedObject[] { elementToAdd1, elementToAdd2 });
        NakedCollection c = collectionField.getAssociations(testAdapter);
        assertEquals(2, c.size());
        Enumeration elements = c.elements();
        assertEquals(elementToAdd1, elements.nextElement());
        assertEquals(elementToAdd2, elements.nextElement());
        assertFalse(elements.hasMoreElements());
    }

    public void testIsAddValid() throws Exception {
        Consent addValid = collectionField.isAddValid(testAdapter, system.createPersistentTestObject(elementToAdd));
        assertEquals(Allow.DEFAULT, addValid);
    }

    public void testIsRemoveValid() throws Exception {
        Consent addValid = collectionField.isRemoveValid(testAdapter, system.createPersistentTestObject(elementToAdd));
        assertEquals(Allow.DEFAULT, addValid);
    }

    public void testDebug() throws Exception {
        DebugString debug = new DebugString();
        collectionField.debugData(debug);
        assertTrue(debug.toString().length() > 0);
    }

    public void testToString() throws Exception {
        assertTrue(collectionField.toString().length() > 0);
    }

    public void testIsAddValidDefaultsToAllow() throws Exception {
        Consent addValid = collectionField.isAddValid(testAdapter, system.createPersistentTestObject(elementToAdd));
        assertEquals(Allow.DEFAULT, addValid);
    }

    public void testIsAddValidReflectsCollection() throws Exception {
        TestProxyNakedCollection c = (TestProxyNakedCollection) collectionField.getAssociations(testAdapter);
        c.setupAddValidMessage("veto");
        Consent addValid = collectionField.isAddValid(testAdapter, system.createPersistentTestObject(elementToAdd));
        assertTrue(addValid.isVetoed());
    }

    public void testIsRemoveValidDefaultsToAllow() throws Exception {
        Consent addValid = collectionField.isRemoveValid(testAdapter, system.createPersistentTestObject(elementToAdd));
        assertEquals(Allow.DEFAULT, addValid);
    }

    public void testIsRemoveValidReflectsCollection() throws Exception {
        TestProxyNakedCollection c = (TestProxyNakedCollection) collectionField.getAssociations(testAdapter);
        c.setupRemoveValidMessage("veto");
        Consent addValid = collectionField.isRemoveValid(testAdapter, system.createPersistentTestObject(elementToAdd));
        assertTrue(addValid.isVetoed());
    }

    public void testIsAddValidVetoesNull() {
        Consent consent = collectionField.isAddValid(testAdapter, null);
        assertFalse(consent.isAllowed());
    }

    public void testIsRemoveValidVetoesNull() {
        Consent consent = collectionField.isRemoveValid(testAdapter, null);
        assertFalse(consent.isAllowed());
    }
}
