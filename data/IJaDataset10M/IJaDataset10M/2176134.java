package org.expasy.jpl.core.mol.chem;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import junit.framework.Assert;
import org.expasy.jpl.commons.base.io.Serializer;
import org.expasy.jpl.core.mol.chem.MassCalculator.Accuracy;
import org.expasy.jpl.core.mol.chem.api.Atom;
import org.expasy.jpl.core.mol.chem.impl.Element;
import org.expasy.jpl.core.mol.chem.impl.ElementManager;
import org.expasy.jpl.core.mol.chem.impl.Isotope;
import org.junit.Test;

public class JPLAtomManagerTest {

    ElementManager manager = ElementManager.getInstance();

    @Test
    public void displayPeriodicTable() throws ParseException {
        System.out.println(manager);
    }

    @Test
    public void displayAllAtoms() throws ParseException {
        for (String key : manager.getAllAtoms().keySet()) {
            System.out.println(key + ": " + manager.getAllAtoms().get(key));
        }
    }

    @Test
    public void getCarbon() throws ParseException {
        Assert.assertEquals("carbon", manager.getAtom("C").getId());
    }

    @Test
    public void testGetCarbon14() throws ParseException {
        System.out.println(manager.getIsotope("C", 14));
    }

    @Test
    public void testGetCarbon14bis() throws ParseException {
        System.out.println(manager.getIsotope("C14"));
    }

    @Test
    public void testHashCode() throws ParseException {
        System.out.println(manager.getIsotope("C", 14).hashCode());
        System.out.println(manager.getIsotope("C", 12).hashCode());
    }

    @Test
    public void testEquals() throws ParseException {
        boolean isEquals = manager.getIsotope("C", 12).equals(manager.getIsotope("C", 12));
        Assert.assertTrue(isEquals);
    }

    @Test
    public void testDifference() throws ParseException {
        boolean isEquals = manager.getIsotope("C", 14).equals(manager.getIsotope("C", 12));
        Assert.assertFalse(isEquals);
    }

    @Test
    public void testHashSet() throws ParseException {
        Set<Atom> atoms = new HashSet<Atom>();
        atoms.add(manager.getIsotope("C", 12));
        atoms.add(manager.getIsotope("C", 13));
        atoms.add(manager.getIsotope("C", 14));
        atoms.add(manager.getElement("C14"));
        atoms.add(manager.getAtom("C"));
        for (Atom element : atoms) {
            System.out.println(element.getClass() + ":" + element);
        }
        Assert.assertEquals(4, atoms.size());
    }

    @Test
    public void testGetCarbonMasses() throws ParseException {
        Assert.assertNotSame(manager.getAtom("C").getMass(Accuracy.AVERAGE), manager.getIsotope("C", 14).getMass());
    }

    @Test(expected = ParseException.class)
    public void testGetBadFormatAtom() throws ParseException {
        manager.getAtom("C13");
    }

    @Test
    public void testGetBadFormatAtom2() throws ParseException {
        manager.getIsotope("C13");
    }

    @Test
    public void testGetCarbon() throws ParseException {
        System.out.println(manager.getIsotope("C"));
        System.out.println(manager.getIsotope("C", 14));
    }

    @Test
    public void testGetHydrogen() throws ParseException {
        System.out.println(manager.getIsotope("H"));
        System.out.println(manager.getElement("H1"));
    }

    @Test
    public void testAtomSerializing() throws Exception {
        Element hydrogen = manager.getAtom("H");
        Serializer<Element> serializer = Serializer.newInstance();
        serializer.serialize(hydrogen, "/tmp/test.ser");
        final Element serial = serializer.deserialize("/tmp/test.ser");
        System.out.println(serial.getId() + ", " + serial.getSymbol() + ", " + serial.getIsotopes() + ", " + serial.getStableIsotope());
        Assert.assertTrue(hydrogen == serial);
    }

    @Test
    public void testIsotopeSerializing() throws Exception {
        Isotope deuterium = manager.getIsotope("H", 2);
        Serializer<Isotope> serializer = Serializer.newInstance();
        serializer.serialize(deuterium, "/tmp/test.ser");
        final Isotope serial = serializer.deserialize("/tmp/test.ser");
        Assert.assertTrue(deuterium == serial);
    }

    @Test
    public void getSize() {
        Assert.assertEquals(315, manager.size());
    }
}
