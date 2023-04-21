package org.expasy.jpl.core.mol.chem;

import java.text.ParseException;
import junit.framework.Assert;
import org.expasy.jpl.core.mol.chem.api.Molecule;
import org.expasy.jpl.core.mol.monomer.aa.AminoAcid;
import org.expasy.jpl.core.mol.polymer.pept.Peptide;
import org.junit.Before;
import org.junit.Test;

public class JPLMassCalculatorTest {

    MassCalculator MONO_MASS_CALC = MassCalculator.getMonoAccuracyInstance();

    MassCalculator AVG_MASS_CALC = MassCalculator.getAvgAccuracyInstance();

    Molecule methane;

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        methane = ChemicalFacade.getMolecule("CH4");
    }

    @Test
    public void testDefaultMassCalc() {
        System.out.println(MONO_MASS_CALC.getMass(methane));
        System.out.println(AVG_MASS_CALC.getMass(methane));
    }

    @Test
    public void testMoleculeMassCalc() {
        Assert.assertTrue(MONO_MASS_CALC.getMass(methane) != AVG_MASS_CALC.getMass(methane));
    }

    @Test
    public void testAAAndImmoMasses() {
        Assert.assertEquals(163.063, MONO_MASS_CALC.getMass(AminoAcid.RAD_Y), 0.001);
        Assert.assertEquals(163.176, AVG_MASS_CALC.getMass(AminoAcid.RAD_Y), 0.001);
        Assert.assertEquals(135.068, MONO_MASS_CALC.getMass(AminoAcid.IMMO_Y), 0.001);
        Assert.assertEquals(135.166, AVG_MASS_CALC.getMass(AminoAcid.IMMO_Y), 0.001);
    }

    @Test
    public void testProtonMass() throws ParseException {
        System.out.println(MONO_MASS_CALC.getMass(ChemicalFacade.getMolecule("H(+)")));
        Assert.assertEquals(MONO_MASS_CALC.getMass(ChemicalFacade.getMolecule("H(+)")), MONO_MASS_CALC.getProtonMass());
    }

    @Test
    public void testElectronMass() throws ParseException {
        System.out.println(MONO_MASS_CALC.getMass(ChemicalFacade.getMolecule("H(+)")));
        System.out.println(MONO_MASS_CALC.getMass(ChemicalFacade.getMolecule("H")));
        System.out.println(MONO_MASS_CALC.getMass(ChemicalFacade.getMolecule("H(-)")));
    }

    @Test
    public void testComputeMz() throws ParseException {
        double mz = MONO_MASS_CALC.getMz(100, 2);
        Assert.assertEquals(51.0072761077, mz, 0.0001);
    }

    @Test
    public void testComputeMz2() throws ParseException {
        double mz = MONO_MASS_CALC.getMz(100, 2, 1);
        Assert.assertEquals(102.0151, mz, 0.0001);
    }

    @Test
    public void testComputeMz3() throws ParseException {
        double mz = MONO_MASS_CALC.getMz(100, 1, 2);
        Assert.assertEquals(-101.0083, mz, 0.0001);
    }

    @Test
    public void testComputeMz4() throws ParseException {
        double mz = MONO_MASS_CALC.getMz(100, 2, 2);
        Assert.assertEquals(102.0156, mz, 0.0001);
    }

    @Test
    public void testComputeMz5() throws ParseException {
        Peptide pep = new Peptide.Builder("PEPTIDE").protons(2).build();
        double mz = MONO_MASS_CALC.getMz(pep);
        Assert.assertEquals(400.68, mz, 0.01);
    }

    @Test
    public void testComputeMass() throws ParseException {
        double mass = MONO_MASS_CALC.getMass(51.0072761077, 2);
        Assert.assertEquals(100, mass, 0.0001);
    }

    @Test
    public void testComputeMass1() throws ParseException {
        Peptide pep = new Peptide.Builder("AAAAAAAAAAAAAADPVAGDHENIVSDSTQASR").protons(2).build();
        double mz = MONO_MASS_CALC.getMass(pep);
        Assert.assertEquals(2993.44, mz, 0.01);
    }

    @Test
    public void testComputeMass2() throws ParseException {
        Peptide pep = new Peptide.Builder("H({42.01})_AAAAEKNVPLYKHLADLSK_HO").protons(2).build();
        double mz = MONO_MASS_CALC.getMass(pep);
        Assert.assertEquals(2082.146, mz, 0.001);
        pep = new Peptide.Builder("H({42.01})_ALALKAALENHAVPYDSKK_HO").protons(2).build();
        mz = MONO_MASS_CALC.getMass(pep);
        Assert.assertEquals(2082.146, mz, 0.001);
    }

    public void testComputeMassRostyk() throws ParseException {
        Peptide pep = new Peptide.Builder("C({-17.026549})AALITGHNR").protons(2).build();
        double mw = MONO_MASS_CALC.getMass(pep);
        double mz = MONO_MASS_CALC.getMz(pep);
        System.out.println(pep + ": mz=" + mz + ", mw=" + mw);
        pep = new Peptide.Builder("C({42.0106})AALITGHNR").protons(2).build();
        mw = MONO_MASS_CALC.getMass(pep);
        mz = MONO_MASS_CALC.getMz(pep);
        System.out.println(pep + ": mz=" + mz + ", mw=" + mw);
        pep = new Peptide.Builder("AC({450.20})YVC({450.20})GK").protons(3).build();
        mw = MONO_MASS_CALC.getMass(pep);
        mz = MONO_MASS_CALC.getMz(pep);
        System.out.println(pep + ": mz=" + mz + ", mw=" + mw);
    }
}
