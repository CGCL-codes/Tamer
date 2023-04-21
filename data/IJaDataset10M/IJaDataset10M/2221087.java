package toxTree.cramer;

import org.openscience.cdk.config.Elements;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import toxTree.core.IDecisionRule;
import toxTree.query.FunctionalGroups;
import toxTree.query.MolAnalyser;
import toxTree.query.MolFlags;
import toxTree.tree.cramer.RuleHasOnlySaltSulphonateSulphate;
import ambit2.core.data.MoleculeTools;

/**
 * TODO add description
 * @author ThinClient
 * <b>Modified</b> 2005-9-25
 */
public class RuleHasOnlySaltSulphonateSulphateTest extends AbstractRuleTest {

    @Override
    protected IDecisionRule createRule() {
        return new RuleHasOnlySaltSulphonateSulphate();
    }

    public void test() throws Exception {
        Object[][] answer = { { "CCCCC(=O)[O-].[Na+]", new Integer(1) }, { "[Ca+2].O=C[O-].O=C[O-]", new Integer(2) } };
        for (int i = 0; i < 2; i++) {
            IAtomContainer a = FunctionalGroups.createAtomContainer(answer[i][0].toString());
            MolAnalyser.analyse(a);
            assertTrue(rule2test.verifyRule(a));
            Object mf = a.getProperty(MolFlags.MOLFLAGS);
            assertNotNull(mf);
            IAtomContainerSet residues = ((MolFlags) mf).getResidues();
            assertNotNull(residues);
            assertEquals(((Integer) answer[i][1]).intValue(), residues.getAtomContainerCount());
        }
    }

    public void testSulphateOfAmine() throws Exception {
        IAtomContainer a = phenazineMethosulphate();
        MolAnalyser.analyse(a);
        assertTrue(rule2test.verifyRule(a));
        Object mf = a.getProperty(MolFlags.MOLFLAGS);
        assertNotNull(mf);
        IAtomContainerSet residues = ((MolFlags) mf).getResidues();
        assertNotNull(residues);
        assertEquals(1, residues.getAtomContainerCount());
    }

    public void testSulphonate() throws Exception {
        IAtomContainer a = FunctionalGroups.createAtomContainer("[Na+].[O-]S(=O)(=O)CCCCS(=O)(=O)[O-].[Na+]");
        MolAnalyser.analyse(a);
        assertTrue(rule2test.verifyRule(a));
        Object mf = a.getProperty(MolFlags.MOLFLAGS);
        assertNotNull(mf);
        IAtomContainerSet residues = ((MolFlags) mf).getResidues();
        assertNotNull(residues);
        assertEquals(1, residues.getAtomContainerCount());
    }

    public void testSulphate() throws Exception {
        IAtomContainer a = FunctionalGroups.createAtomContainer("[Na+].CCCCCCCCCCCCOCCOCCOCCOS([O-])(=O)=O");
        MolAnalyser.analyse(a);
        assertTrue(rule2test.verifyRule(a));
        Object mf = a.getProperty(MolFlags.MOLFLAGS);
        assertNotNull(mf);
        IAtomContainerSet residues = ((MolFlags) mf).getResidues();
        assertNotNull(residues);
        assertEquals(1, residues.getAtomContainerCount());
    }

    public void testHydroChloridOfAmine() throws Exception {
        IAtomContainer a = FunctionalGroups.createAtomContainer("[Cl-].[NH3+]C1CCCCC1");
        MolAnalyser.analyse(a);
        assertTrue(rule2test.verifyRule(a));
        Object mf = a.getProperty(MolFlags.MOLFLAGS);
        assertNotNull(mf);
        IAtomContainerSet residues = ((MolFlags) mf).getResidues();
        assertNotNull(residues);
        assertEquals(1, residues.getAtomContainerCount());
        assertTrue(FunctionalGroups.hasGroup(residues.getAtomContainer(0), FunctionalGroups.primaryAmine(false)));
    }

    public static IMolecule phenazineMethosulphate() {
        IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();
        IMolecule mol = MoleculeTools.newMolecule(builder);
        IAtom nq = MoleculeTools.newAtom(builder, Elements.NITROGEN);
        nq.setFormalCharge(+1);
        mol.addAtom(nq);
        IAtom a2 = MoleculeTools.newAtom(builder, Elements.SULFUR);
        mol.addAtom(a2);
        IAtom a3 = MoleculeTools.newAtom(builder, Elements.NITROGEN);
        mol.addAtom(a3);
        IAtom a4 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a4);
        IAtom a5 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a5);
        IAtom a6 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a6);
        IAtom a7 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a7);
        IAtom o1 = MoleculeTools.newAtom(builder, Elements.OXYGEN);
        o1.setFormalCharge(-1);
        mol.addAtom(o1);
        IAtom a9 = MoleculeTools.newAtom(builder, Elements.OXYGEN);
        mol.addAtom(a9);
        IAtom a10 = MoleculeTools.newAtom(builder, Elements.OXYGEN);
        mol.addAtom(a10);
        IAtom a11 = MoleculeTools.newAtom(builder, Elements.OXYGEN);
        mol.addAtom(a11);
        IAtom a12 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a12);
        IAtom a13 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a13);
        IAtom a14 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a14);
        IAtom a15 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a15);
        IAtom a16 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a16);
        IAtom a17 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a17);
        IAtom a18 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a18);
        IAtom a19 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a19);
        IAtom a20 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a20);
        IAtom a21 = MoleculeTools.newAtom(builder, Elements.CARBON);
        mol.addAtom(a21);
        IBond b1 = MoleculeTools.newBond(builder, a3, a7, IBond.Order.SINGLE);
        mol.addBond(b1);
        IBond b2 = MoleculeTools.newBond(builder, a4, nq, IBond.Order.SINGLE);
        mol.addBond(b2);
        IBond b3 = MoleculeTools.newBond(builder, a5, nq, IBond.Order.DOUBLE);
        mol.addBond(b3);
        IBond b4 = MoleculeTools.newBond(builder, a6, a5, IBond.Order.SINGLE);
        mol.addBond(b4);
        IBond b5 = MoleculeTools.newBond(builder, a7, a4, IBond.Order.DOUBLE);
        mol.addBond(b5);
        IBond b6 = MoleculeTools.newBond(builder, o1, a2, IBond.Order.SINGLE);
        mol.addBond(b6);
        IBond b7 = MoleculeTools.newBond(builder, a9, a2, IBond.Order.DOUBLE);
        mol.addBond(b7);
        IBond b8 = MoleculeTools.newBond(builder, a10, a2, IBond.Order.DOUBLE);
        mol.addBond(b8);
        IBond b9 = MoleculeTools.newBond(builder, a11, a2, IBond.Order.SINGLE);
        mol.addBond(b9);
        IBond b10 = MoleculeTools.newBond(builder, a12, nq, IBond.Order.SINGLE);
        mol.addBond(b10);
        IBond b11 = MoleculeTools.newBond(builder, a13, a4, IBond.Order.SINGLE);
        mol.addBond(b11);
        IBond b12 = MoleculeTools.newBond(builder, a14, a5, IBond.Order.SINGLE);
        mol.addBond(b12);
        IBond b13 = MoleculeTools.newBond(builder, a15, a6, IBond.Order.SINGLE);
        mol.addBond(b13);
        IBond b14 = MoleculeTools.newBond(builder, a16, a7, IBond.Order.SINGLE);
        mol.addBond(b14);
        IBond b15 = MoleculeTools.newBond(builder, a17, a11, IBond.Order.SINGLE);
        mol.addBond(b15);
        IBond b16 = MoleculeTools.newBond(builder, a18, a13, IBond.Order.DOUBLE);
        mol.addBond(b16);
        IBond b17 = MoleculeTools.newBond(builder, a19, a14, IBond.Order.DOUBLE);
        mol.addBond(b17);
        IBond b18 = MoleculeTools.newBond(builder, a20, a19, IBond.Order.SINGLE);
        mol.addBond(b18);
        IBond b19 = MoleculeTools.newBond(builder, a21, a16, IBond.Order.DOUBLE);
        mol.addBond(b19);
        IBond b20 = MoleculeTools.newBond(builder, a6, a3, IBond.Order.DOUBLE);
        mol.addBond(b20);
        IBond b21 = MoleculeTools.newBond(builder, a18, a21, IBond.Order.SINGLE);
        mol.addBond(b21);
        IBond b22 = MoleculeTools.newBond(builder, a15, a20, IBond.Order.DOUBLE);
        mol.addBond(b22);
        return mol;
    }
}
