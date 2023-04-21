package org.xmlcml.cml.tools.test;

import java.io.StringReader;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.cml.base.CMLElement.CoordinateType;
import org.xmlcml.cml.element.CMLAtomSet;
import org.xmlcml.cml.element.CMLBuilder;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.tools.AtomSetTool;
import org.xmlcml.cml.tools.ConnectionTableTool;

/**
 * test AtomSetTool.
 *
 * @author pm286
 *
 */
public class AtomSetToolTest extends AbstractToolTest {

    String sproutS = "" + "<molecule " + CML_XMLNS + " title='sprout'>" + "  <atomArray>" + "    <atom id='a1' elementType='C'/>" + "    <atom id='a2' elementType='C'/>" + "    <atom id='a3' elementType='C'/>" + "    <atom id='a4' elementType='C'/>" + "    <atom id='a5' elementType='C'/>" + "    <atom id='a6' elementType='C'/>" + "    <atom id='a7' elementType='F'/>" + "    <atom id='a8' elementType='Cl'/>" + "    <atom id='a9' elementType='Br'/>" + "    <atom id='a10' elementType='I'/>" + "    <atom id='a11' elementType='H'/>" + "    <atom id='a12' elementType='C'/>" + "    <atom id='a13' elementType='O'/>" + "   </atomArray>" + "   <bondArray>" + "     <bond id='a1 a2' atomRefs2='a1 a2'/>" + "     <bond id='a2 a3' atomRefs2='a2 a3'/>" + "     <bond id='a3 a4' atomRefs2='a3 a4'/>" + "     <bond id='a4 a5' atomRefs2='a4 a5'/>" + "     <bond id='a5 a6' atomRefs2='a5 a6'/>" + "     <bond id='a1 a6' atomRefs2='a1 a6'/>" + "     <bond id='a1 a7' atomRefs2='a1 a7'/>" + "     <bond id='a2 a8' atomRefs2='a2 a8'/>" + "     <bond id='a3 a9' atomRefs2='a3 a9'/>" + "     <bond id='a4 a10' atomRefs2='a4 a10'/>" + "     <bond id='a5 a11' atomRefs2='a5 a11'/>" + "     <bond id='a6 a12' atomRefs2='a6 a12'/>" + "     <bond id='a12 a13' atomRefs2='a12 a13'/>" + "  </bondArray>" + "</molecule>" + "";

    CMLMolecule sprout = null;

    /**
     * setup.
     *
     * @exception Exception
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        sprout = (CMLMolecule) parseValidString(sproutS);
    }

    /**
     * Test method for
     * 'org.xmlcml.cml.element.CMLAtomSet.getOverlapping3DAtoms(CMLAtomSet
     * otherSet)'
     */
    @Test
    @Ignore("needs checking")
    public void testGetOverlapping3DAtomsCMLAtomSet() {
        CMLMolecule mol1 = null;
        try {
            mol1 = (CMLMolecule) new CMLBuilder().build(new StringReader("<molecule " + CML_XMLNS + ">" + "  <atomArray>" + "    <atom id='a1' x3='1.0' y3='2.0' z3='0.0'/>" + "    <atom id='a2' x3='3.0' y3='4.0' z3='0.0'/>" + "    <atom id='a3' x3='2.0' y3='3.0' z3='1.0'/>" + "  </atomArray>" + "</molecule>")).getRootElement();
        } catch (Exception e) {
            Assert.fail("bug " + e);
        }
        CMLMolecule mol2 = null;
        try {
            mol2 = (CMLMolecule) new CMLBuilder().build(new StringReader("<molecule " + CML_XMLNS + ">" + "  <atomArray>" + "    <atom id='a11' x3='1.0' y3='2.0' z3='0.0'/>" + "    <atom id='a12' x3='3.0' y3='4.0' z3='0.0'/>" + "    <atom id='a13' x3='2.0' y3='3.0' z3='-1.0'/>" + "  </atomArray>" + "</molecule>")).getRootElement();
        } catch (Exception e) {
            Assert.fail("bug " + e);
        }
        AtomSetTool atomSetTool1 = new AtomSetTool(mol1.getAtomSet());
        CMLAtomSet atomSet = atomSetTool1.getOverlapping3DAtoms(mol2.getAtomSet(), CoordinateType.CARTESIAN);
        Assert.assertEquals("overlap", new String[] { "a1", "a2" }, atomSet.getXMLContent());
        atomSet = atomSetTool1.getOverlapping3DAtoms(mol2.getAtomSet(), CoordinateType.FRACTIONAL);
        Assert.assertEquals("overlap", 0, atomSet.size());
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.AtomSetTool(CMLAtomSet)'
     */
    @Test
    @Ignore
    public void testAtomSetTool() {
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.getBondSet(CMLAtomSet)'
     */
    @Test
    @Ignore
    public void testGetBondSet() {
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.createValenceAngles(CMLAtomSet, boolean, boolean)'
     */
    @Test
    @Ignore
    public void testCreateValenceAngles() {
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.createValenceTorsions(CMLAtomSet, boolean, boolean)'
     */
    @Test
    @Ignore
    public void testCreateValenceTorsions() {
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.sprout()'
     */
    @Test
    public void testSprout() {
        List<CMLAtomSet> ringNucleiAtomSets = new ConnectionTableTool(sprout).getRingNucleiAtomSets();
        Assert.assertEquals("sprout size", 1, ringNucleiAtomSets.size());
        CMLAtomSet sproutAtomSet = ringNucleiAtomSets.get(0);
        Assert.assertEquals("pre sprout size", 6, sproutAtomSet.size());
        AtomSetTool sproutAtomSetTool = new AtomSetTool(sproutAtomSet);
        CMLAtomSet sproutAtomSprout = sproutAtomSetTool.sprout();
        Assert.assertEquals("sprout size", 12, sproutAtomSprout.size());
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.areOrderedAtomSetsDifferent(CMLAtomSet[])'
     */
    @Test
    @Ignore
    public void testAreOrderedAtomSetsDifferent() {
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.createAtomSet(List<CMLMolecule>)'
     */
    @Test
    @Ignore
    public void testCreateAtomSet() {
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.resetParents()'
     */
    @Test
    @Ignore
    public void testResetParents() {
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.setParent(CMLAtom, CMLAtom)'
     */
    @Test
    @Ignore
    public void testSetParent() {
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.getParent(CMLAtom)'
     */
    @Test
    @Ignore
    public void testGetParent() {
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.getNearestAtom2OfSameElementType(CMLAtom)'
     */
    @Test
    @Ignore
    public void testGetNearestAtom2OfSameElementType() {
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.removeUnmatchedAtoms(CMLMap, CMLAtomSet, String)'
     */
    @Test
    @Ignore
    public void testRemoveUnmatchedAtoms() {
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.getOverlapping3DAtoms(CMLAtomSet, CoordinateType)'
     */
    @Test
    @Ignore
    public void testGetOverlapping3DAtoms() {
    }

    /** Test method for 'org.xmlcml.cml.tools.AtomSetTool.matchNearestAtoms(CMLAtomSet, double)'
     */
    @Test
    @Ignore
    public void testMatchNearestAtoms() {
    }
}
