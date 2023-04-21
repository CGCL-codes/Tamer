package org.umlvm.core.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dingouml.metamodel.foundation.core.Association;
import org.dingouml.metamodel.foundation.core.AssociationEnd;
import org.dingouml.metamodel.foundation.core.UmlClass;
import org.dingouml.metamodel.foundation.datatypes.MultiplicityRange;
import org.umlvm.core.UmlClassManager;

/**
 * ClassesTest models a sample UML class diagram from
 * the paper "The Architecture of a UML Virtual Machine"
 * by Dirk Riehle et al (http://www.riehle.org), figure 9 page 8.
 */
public class UmlClassManagerTest extends TestCase {

    private UmlClassManager manager;

    public UmlClassManagerTest(java.lang.String name) {
        super(name);
    }

    protected void setUp() {
        manager = new UmlClassManager();
        try {
            manager.addClass("Child");
            manager.addClass("Parent");
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }

    public static Test suite() {
        return new TestSuite(UmlClassManagerTest.class);
    }

    public void testAddDuplicatedClass() {
        try {
            manager.addClass("Child");
            fail("Add a duplicated class must throw an exception");
        } catch (Exception ex) {
        }
        try {
            manager.addClass("Parent");
            fail("Add a duplicated class must throw an exception");
        } catch (Exception ex) {
        }
    }

    public void testAddAssociation() {
        UmlClass child = null;
        UmlClass parent = null;
        try {
            child = manager.getClass("Child");
            parent = manager.getClass("Parent");
            manager.addAssociation("has", parent, "parent-of", 1, 1, child, "child-of", 0, MultiplicityRange.INFINITY);
        } catch (Exception ex) {
            fail("Exception in testAddAssociation: " + ex.toString());
        }
        Association assoc = (Association) manager.getEntity("has");
        AssociationEnd parentEnd = (AssociationEnd) assoc.findConnection("parent-of");
        assertNotNull(parentEnd);
        assertTrue(parent.containsLink(parentEnd));
        AssociationEnd targetEnd = (AssociationEnd) assoc.findConnection("child-of");
        assertNotNull(targetEnd);
        assertTrue(child.containsLink(targetEnd));
    }
}
