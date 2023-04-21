package org.nakedobjects.nos.client.xat;

import java.util.Enumeration;
import java.util.Hashtable;
import junit.framework.Assert;
import org.nakedobjects.noa.adapter.NakedObject;

public class ExpectationObject {

    private Class type;

    private Hashtable expectedReferences = new Hashtable();

    private Hashtable expectedValues = new Hashtable();

    private TestObject actual;

    public ExpectationObject(final Class type) {
        this.type = type;
    }

    /**
     * 
     * @param viewer
     *            org.nakedobjects.mockobject.View
     */
    public void addActual(final TestObject viewer) {
        NakedObject obj = (NakedObject) viewer.getForNaked();
        if (!obj.getClass().isAssignableFrom(type)) {
            Assert.fail("Expected an object of type " + type.getName() + " but got a " + obj.getClass().getName());
        }
        actual = viewer;
    }

    public void addExpectedReference(final String name, final NakedObject reference) {
        checkFieldUse(name);
        expectedReferences.put(name, new ExpectationValue(reference));
        setHasExpectations();
    }

    private void setHasExpectations() {
    }

    public void addExpectedReference(final String name, final TestObject view) {
        checkFieldUse(name);
        expectedReferences.put(name, new ExpectationValue((NakedObject) view.getForNaked()));
        setHasExpectations();
    }

    public void addExpectedText(final String name, final String value) {
        checkFieldUse(name);
        expectedValues.put(name, value);
        setHasExpectations();
    }

    private void checkFieldUse(final String name) {
        if (expectedValues.containsKey(name) || expectedReferences.containsKey(name)) {
            throw new RuntimeException("Duplicate field: " + name);
        }
    }

    public void clearActual() {
        actual = null;
    }

    protected void clearExpectation() {
        expectedValues.clear();
        expectedReferences.clear();
    }

    public void setExpectNothing() {
        clearExpectation();
        setHasExpectations();
    }

    public void verify() {
        Enumeration e = expectedValues.keys();
        while (e.hasMoreElements()) {
            String fieldName = (String) e.nextElement();
            String fieldValue = actual.getField(fieldName).getTitle();
            Assert.assertEquals("Wrong value for " + fieldName, expectedValues.get(fieldName).toString(), fieldValue);
        }
        e = expectedReferences.keys();
        while (e.hasMoreElements()) {
            String fieldName = (String) e.nextElement();
            NakedObject fieldValue = (NakedObject) actual.getField(fieldName).getForNaked();
            NakedObject ref = ((ExpectationValue) expectedReferences.get(fieldName)).getReference();
            Assert.assertEquals("Wrong reference for " + fieldName, ref, fieldValue);
        }
    }
}
