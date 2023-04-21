package org.openscience.cdk.config;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.CDKTestCase;
import org.openscience.cdk.PeriodicTableElement;
import org.openscience.cdk.interfaces.IElement;

/**
 * Checks the functionality of the ElementPTFactory
 *
 * @cdk.module test-core
 */
public class ElementPTFactoryTest extends CDKTestCase {

    @Test
    public void testGetInstance() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        Assert.assertNotNull(elefac);
    }

    @Test
    public void testGetSize() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        Assert.assertTrue(elefac.getSize() > 0);
    }

    @Test
    public void testGetElements() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        Assert.assertNotNull(elefac.getElements());
    }

    @Test
    public void testGetElement_String() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        PeriodicTableElement pte = elefac.getElement("H");
        Assert.assertNotNull(pte);
    }

    @Test
    public void testConfigure_PeriodicTableElement() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        PeriodicTableElement pte = elefac.configure(new PeriodicTableElement("H"));
        Assert.assertNotNull(pte);
    }

    @Test
    public void testConfigureE_PeriodicTableElement() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        IElement element = elefac.configureE(new PeriodicTableElement("H"));
        Assert.assertNotNull(element);
    }

    @Test
    public void testGetAtomicNumber_PeriodicTableElement() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        Assert.assertEquals(1.0079760, elefac.getAtomicNumber(new PeriodicTableElement("H")), 0.01);
    }

    @Test
    public void testGetName_PeriodicTableElement() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        Assert.assertEquals("Hydrogen", elefac.getName(new PeriodicTableElement("H")));
    }

    @Test
    public void testGetChemicalSerie_PeriodicTableElement() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        Assert.assertEquals("Nonmetals", elefac.getChemicalSerie(new PeriodicTableElement("H")));
    }

    @Test
    public void testGetPeriod_PeriodicTableElement() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        Assert.assertEquals(1, elefac.getPeriod(new PeriodicTableElement("H")), 0.1);
    }

    @Test
    public void testGetGroup_PeriodicTableElement() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        Assert.assertEquals(1, elefac.getGroup(new PeriodicTableElement("H")), 0.1);
    }

    @Test
    public void testGetPhase_PeriodicTableElement() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        Assert.assertEquals("Gas", elefac.getPhase(new PeriodicTableElement("H")));
    }

    @Test
    public void testGetVdwRadius_PeriodicTableElement() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        Assert.assertEquals(1.2, elefac.getVdwRadius(new PeriodicTableElement("H")), 0.1);
    }

    @Test
    public void testGetCASid_PeriodicTableElement() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        Assert.assertEquals("1333-74-0", elefac.getCASid(new PeriodicTableElement("H")));
    }

    @Test
    public void testGetCovalentRadius_PeriodicTableElement() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        Assert.assertEquals(0.37, elefac.getCovalentRadius(new PeriodicTableElement("H")), 0.1);
    }

    @Test
    public void testGetPaulingEneg_PeriodicTableElement() throws Exception {
        ElementPTFactory elefac = ElementPTFactory.getInstance();
        Assert.assertEquals(2.2, elefac.getPaulingEneg(new PeriodicTableElement("H")), 0.1);
    }
}
