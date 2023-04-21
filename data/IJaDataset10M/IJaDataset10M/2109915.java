package org.openscience.cdk.test;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.interfaces.IChemObjectBuilder;

/**
 * Checks the functionality of the AtomTypeFactory
 *
 * @cdk.module test-data
 */
public class PseudoAtomTest extends CDKTestCase {

    protected IChemObjectBuilder builder;

    public PseudoAtomTest(String name) {
        super(name);
    }

    public void setUp() {
        builder = DefaultChemObjectBuilder.getInstance();
    }

    public static Test suite() {
        return new TestSuite(PseudoAtomTest.class);
    }

    public void testPseudoAtom() {
        IPseudoAtom a = builder.newPseudoAtom();
        assertEquals("R", a.getSymbol());
        assertNull(a.getPoint3d());
        assertNull(a.getPoint2d());
        assertNull(a.getFractionalPoint3d());
    }

    public void testPseudoAtom_IElement() {
        IElement element = builder.newElement();
        IPseudoAtom a = builder.newPseudoAtom(element);
        assertEquals("R", a.getSymbol());
        assertNull(a.getPoint3d());
        assertNull(a.getPoint2d());
        assertNull(a.getFractionalPoint3d());
    }

    public void testPseudoAtom_String() {
        String label = "Arg255";
        IPseudoAtom a = builder.newPseudoAtom(label);
        assertEquals("R", a.getSymbol());
        assertEquals(label, a.getLabel());
        assertNull(a.getPoint3d());
        assertNull(a.getPoint2d());
        assertNull(a.getFractionalPoint3d());
    }

    public void testPseudoAtom_String_Point2d() {
        Point2d point = new Point2d(1.0, 2.0);
        String label = "Arg255";
        IPseudoAtom a = builder.newPseudoAtom(label, point);
        assertEquals("R", a.getSymbol());
        assertEquals(label, a.getLabel());
        assertEquals(point, a.getPoint2d());
        assertNull(a.getPoint3d());
        assertNull(a.getFractionalPoint3d());
    }

    public void testPseudoAtom_String_Point3d() {
        Point3d point = new Point3d(1.0, 2.0, 3.0);
        String label = "Arg255";
        IPseudoAtom a = builder.newPseudoAtom(label, point);
        assertEquals("R", a.getSymbol());
        assertEquals(label, a.getLabel());
        assertEquals(point, a.getPoint3d());
        assertNull(a.getPoint2d());
        assertNull(a.getFractionalPoint3d());
    }

    public void testGetLabel() {
        String label = "Arg255";
        IPseudoAtom a = builder.newPseudoAtom(label);
        assertEquals(label, a.getLabel());
    }

    public void testSetLabel_String() {
        String label = "Arg255";
        IPseudoAtom atom = builder.newPseudoAtom(label);
        String label2 = "His66";
        atom.setLabel(label2);
        assertEquals(label2, atom.getLabel());
    }

    public void testGetFormalCharge() {
        IPseudoAtom atom = builder.newPseudoAtom("Whatever");
        assertEquals(0, atom.getFormalCharge());
    }

    public void testSetFormalCharge_int() {
        IPseudoAtom atom = builder.newPseudoAtom("Whatever");
        atom.setFormalCharge(+5);
        assertEquals(0, atom.getFormalCharge());
    }

    public void testSetHydrogenCount_int() {
        IPseudoAtom atom = builder.newPseudoAtom("Whatever");
        atom.setHydrogenCount(+5);
        assertEquals(0, atom.getHydrogenCount());
    }

    public void testSetCharge_double() {
        IPseudoAtom atom = builder.newPseudoAtom("Whatever");
        atom.setCharge(0.78);
        assertEquals(0.0, atom.getCharge(), 0.001);
    }

    public void testSetExactMass_double() {
        IPseudoAtom atom = builder.newPseudoAtom("Whatever");
        atom.setExactMass(12.001);
        assertEquals(0.0, atom.getExactMass(), 0.001);
    }

    public void testSetStereoParity_int() {
        IPseudoAtom atom = builder.newPseudoAtom("Whatever");
        atom.setStereoParity(-1);
        assertEquals(0, atom.getStereoParity());
    }

    public void testPseudoAtom_IAtom() {
        IAtom atom = builder.newAtom("C");
        Point3d fract = new Point3d(0.5, 0.5, 0.5);
        Point3d threeD = new Point3d(0.5, 0.5, 0.5);
        Point2d twoD = new Point2d(0.5, 0.5);
        atom.setFractionalPoint3d(fract);
        atom.setPoint3d(threeD);
        atom.setPoint2d(twoD);
        IPseudoAtom a = builder.newPseudoAtom(atom);
        assertEquals(fract, a.getFractionalPoint3d(), 0.0001);
        assertEquals(threeD, a.getPoint3d(), 0.0001);
        assertEquals(twoD, a.getPoint2d(), 0.0001);
    }

    /**
     * Method to test the clone() method
     */
    public void testClone() throws Exception {
        IAtom atom = builder.newPseudoAtom("C");
        Object clone = atom.clone();
        assertTrue(clone instanceof IPseudoAtom);
    }

    /**
     * Method to test wether the class complies with RFC #9.
     */
    public void testToString() {
        IAtom atom = builder.newPseudoAtom("R");
        String description = atom.toString();
        for (int i = 0; i < description.length(); i++) {
            assertTrue(description.charAt(i) != '\n');
            assertTrue(description.charAt(i) != '\r');
        }
    }

    /**
     * Test for bug #1778479 "MDLWriter writes empty PseudoAtom label string".
     * We decided to let the pseudo atoms have a default label of '*'.
     *
     * Author: Andreas Schueller <a.schueller@chemie.uni-frankfurt.de>
     * 
     * @cdk.bug 1778479
     */
    public void testBug1778479DefaultLabel() {
        IPseudoAtom atom = builder.newPseudoAtom();
        assertNotNull("Test for PseudoAtom's default label", atom.getLabel());
        assertEquals("Test for PseudoAtom's default label", "*", atom.getLabel());
    }
}
