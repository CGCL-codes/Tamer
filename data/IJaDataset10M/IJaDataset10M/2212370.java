package org.uk2005.data.test;

import junit.framework.*;
import java.util.*;
import org.uk2005.data.*;

/**
 * @author	<a href="mailto:niklas@saers.com">Niklas Saers</a>
 * @version	$Id: TestSimpleThing.java,v 1.4 2002/09/13 07:17:59 niklasjs Exp $
 */
public class TestSimpleThing extends TestCase {

    /**
	 * Our test-subject.
	 * This test will only use one primary subject for all the tests.
	 * This means that all tests should leave it in a valid state.
	 * (not necessarily clean it)
	 */
    SimpleThing subject = new SimpleThing();

    /**
	 * Test values
	 */
    Hashtable testValues;

    /**
	 * Default constructor.
	 */
    public TestSimpleThing() {
        super("testSimpleThing");
    }

    /**
	 * Constructs a test case with the specified name.
	 *
	 * @param	name the name of the test
	 */
    public TestSimpleThing(String name) {
        super(name);
    }

    /**
	 * Set up our hashtable of atoms and names
	 */
    public void setUp() {
        testValues = new Hashtable();
        testValues.put("BoolTrue", new BooleanAtom("true"));
        testValues.put("IntThree", new IntegerAtom("3"));
        testValues.put("-3.2-4.55", new DoubleRangeAtom(-3.2, 4.55, "3.21"));
        testValues.put("3Sheep", new TextAtom("Three merry sheep"));
        testValues.put("Grass", new FloatAtom("-2.31"));
        testValues.put("Birthday", new LongRangeAtom(101, 20020906, "19790124"));
        testValues.put("DubbleUp", new DoubleAtom("-4.62"));
        testValues.put("AnotherBD", new LongAtom("19771218"));
        testValues.put("IntRan", new IntegerRangeAtom(0, 100, "42"));
        testValues.put("It", new FloatRangeAtom((float) -4.62, (float) 100.0, "42.01"));
    }

    /**
	 * This function tests Thing.getClassName()
	 */
    public void testGetClassName() {
        assertTrue(subject.getClassName().equals("org.uk2005.data.SimpleThing"));
    }

    /**
	 * This function tests Thing.put() and Thing.get()
	 */
    public void testPutGetAtom() {
        subject = fillThing(subject);
        Enumeration e = testValues.keys();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            Atom atom = (Atom) testValues.get(name);
            Atom subjectAtom = subject.get(name);
            assertNotNull(subjectAtom);
            assertTrue(atom == subjectAtom);
        }
    }

    private SimpleThing fillThing(SimpleThing st) {
        Enumeration e = testValues.keys();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            Atom atom = (Atom) testValues.get(name);
            assertNotNull(name);
            assertNotNull(atom);
            st.put(name, atom);
        }
        return st;
    }

    /**
	 * Tests that testPutGetAtom() is idempotent
	 */
    public void testIdempotentPutGetAtom() {
        testPutGetAtom();
        Enumeration e = subject.elements();
        int i = 0;
        while (e.hasMoreElements()) {
            e.nextElement();
            i++;
        }
        testPutGetAtom();
        e = subject.elements();
        int j = 0;
        while (e.hasMoreElements()) {
            e.nextElement();
            j++;
        }
        assertTrue(i == j);
    }

    /**
	 * This function tests Thing.compareTo(Thing)
	 * and Thing.compareTo(String)
	 */
    public void testCompareTo() {
        SimpleThing one = fillThing(new SimpleThing());
        SimpleThing another = fillThing(new SimpleThing());
        assertTrue(one.compareTo((Object) another) == 0);
        assertTrue(one.compareTo((Thing) another) == 0);
        Enumeration valueKeys = testValues.keys();
        another.setLabel((String) valueKeys.nextElement());
        assertTrue(one.compareTo(another) != 0);
    }

    /**
	 * This function tests Thing.getName() and Thing.setName()
	 */
    public void testGetSetName() {
        String name = "Bugs Bunny";
        subject.setName(name);
        assertTrue(subject.getName().equals(name));
    }

    /**
	 * This function tests Thing.getLabel() and Thing.setLabel()
	 */
    public void testGetSetLabel() {
        String label = "Cartoon ville";
        subject.setLabel(label);
        assertTrue(subject.getLabel().equals(label));
    }

    /**
	 * This function tests Thing.keys(), Thing.has() and Thing.elements()
	 */
    public void testKeysAndHasAndElements() {
        testPutGetAtom();
        Enumeration e = subject.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            assertTrue(testValues.containsKey(key));
            assertNotNull(subject.get(key));
            assertEquals(testValues.get(key), subject.get(key));
        }
        e = testValues.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            assertTrue(subject.has(key));
        }
        assertTrue(!subject.has("InvalidKey"));
        e = subject.elements();
        while (e.hasMoreElements()) {
            Atom atom = (Atom) e.nextElement();
            assertNotNull(atom);
            assertTrue(testValues.containsValue(atom));
        }
    }

    /**
	 * This function tests Thing.getThing(),
	 * Thing.putThing(Thing) and Thing.putThing(String, Thing)
	 */
    public void testGetPutThing() {
        testPutGetAtom();
        SimpleThing one = fillThing(new SimpleThing("one"));
        SimpleThing another = fillThing(new SimpleThing("another"));
        subject.putThing(one);
        subject.putThing(another);
        subject.putThing("third", another);
        assertNotNull(subject.getThing("one"));
        assertNotNull(subject.getThing("another"));
        assertNotNull(subject.getThing("third"));
        assertTrue(subject.getThing("one").equals(one));
        assertTrue(subject.getThing("another").equals(another));
        assertTrue(subject.getThing("third").equals(another));
    }

    /**
	 * This function tests Thing.things() and Thing.thingNames()
	 * Also tests that testGetPutThing is idempodent
	 */
    public void testThingsAndThingNames() {
        SimpleThing one = fillThing(new SimpleThing("one"));
        SimpleThing another = fillThing(new SimpleThing("another"));
        testGetPutThing();
        Enumeration e = subject.things();
        int i = 0;
        while (e.hasMoreElements()) {
            SimpleThing thing = (SimpleThing) e.nextElement();
            assertNotNull(thing);
            assertTrue((thing.equals(one)) || (thing.equals(another)));
            i++;
        }
        testGetPutThing();
        e = subject.thingNames();
        int j = 0;
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            if (key.equals("one")) assertTrue(one.equals(subject.getThing(key))); else assertTrue(another.equals(subject.getThing(key)));
            j++;
        }
        assertTrue(i == j);
    }

    /**
	 * This function tests Thing.getSchemaName() and
	 * Thing.setSchemaName()
	 */
    public void testGetSetSchemaName() {
        String name = "The Oh Grand Scheme";
        subject.setSchemaName(name);
        assertTrue(subject.getSchemaName().equals(name));
    }

    /**
	 * This function tests Thing.getValue() and Thing.setValue()
	 */
    public void testGetSetValue() {
        String val = "0";
        subject.setValue(val);
        assertTrue(subject.getValue().equals(val));
    }

    /**
	 * This function tests Thing.copy() and Thing.equals()
	 */
    public void testCopyAndEquals() {
        Thing copy = subject.copy();
        assertNotNull(copy);
        assertTrue(subject.equals(copy));
        assertTrue(copy.equals(subject));
    }

    /**
	 * This function tests Thing.isParent(boolean) and Thing.isParent()
	 */
    public void testIsParent() {
        subject.isParent(true);
        assertTrue(subject.isParent());
        subject.isParent(false);
        assertTrue(!subject.isParent());
        subject.isParent(true);
        assertTrue(subject.isParent());
    }
}
