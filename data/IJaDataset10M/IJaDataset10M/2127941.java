package org.openscience.cdk.modeling.builder3d;

import java.io.InputStream;
import java.util.List;
import javax.vecmath.Point3d;
import junit.framework.Assert;
import org.junit.Test;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.CDKTestCase;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.nonotify.NNMolecule;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

/**
 * Tests for AtomPlacer3D
 *
 * @cdk.module test-builder3d
 * @cdk.svnrev  $Revision: 12144 $
 */
public class AtomPlacer3DTest extends CDKTestCase {

    boolean standAlone = false;

    /**
	 *  Sets the standAlone attribute 
	 *
	 *@param  standAlone  The new standAlone value
	 */
    public void setStandAlone(boolean standAlone) {
        this.standAlone = standAlone;
    }

    @Test
    public void testAllHeavyAtomsPlaced_IAtomContainer() {
        IAtomContainer ac = MoleculeFactory.makeAlphaPinene();
        Assert.assertFalse(new AtomPlacer3D().allHeavyAtomsPlaced(ac));
        for (IAtom atom : ac.atoms()) {
            atom.setFlag(CDKConstants.ISPLACED, true);
        }
        Assert.assertTrue(new AtomPlacer3D().allHeavyAtomsPlaced(ac));
    }

    @Test
    public void testFindHeavyAtomsInChain_IAtomContainer_IAtomContainer() throws Exception {
        String filename = "data/mdl/allmol232.mol";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        MDLV2000Reader reader = new MDLV2000Reader(ins);
        ChemFile chemFile = (ChemFile) reader.read((ChemObject) new ChemFile());
        List containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
        IMolecule ac = new NNMolecule((IAtomContainer) containersList.get(0));
        addExplicitHydrogens(ac);
        IAtomContainer chain = ac.getBuilder().newAtomContainer();
        for (int i = 16; i < 25; i++) {
            chain.addAtom(ac.getAtom(i));
        }
        chain.addAtom(ac.getAtom(29));
        chain.addAtom(ac.getAtom(30));
        int[] result = new AtomPlacer3D().findHeavyAtomsInChain(ac, chain);
        Assert.assertEquals(16, result[0]);
        Assert.assertEquals(11, result[1]);
    }

    @Test
    public void testNumberOfUnplacedHeavyAtoms_IAtomContainer() {
        IMolecule ac = MoleculeFactory.makeAlphaPinene();
        int count = new AtomPlacer3D().numberOfUnplacedHeavyAtoms(ac);
        Assert.assertEquals(10, count);
    }

    @Test
    public void testGetPlacedHeavyAtoms_IAtomContainer_IAtom() {
        IMolecule ac = MoleculeFactory.makeAlphaPinene();
        IAtomContainer acplaced = new AtomPlacer3D().getPlacedHeavyAtoms(ac, ac.getAtom(0));
        Assert.assertEquals(0, acplaced.getAtomCount());
        ac.getAtom(1).setFlag(CDKConstants.ISPLACED, true);
        acplaced = new AtomPlacer3D().getPlacedHeavyAtoms(ac, ac.getAtom(0));
        Assert.assertEquals(1, acplaced.getAtomCount());
    }

    @Test
    public void testGetPlacedHeavyAtom_IAtomContainer_IAtom_IAtom() {
        IMolecule ac = MoleculeFactory.makeAlphaPinene();
        IAtom acplaced = new AtomPlacer3D().getPlacedHeavyAtom(ac, ac.getAtom(0), ac.getAtom(1));
        Assert.assertNull(acplaced);
        ac.getAtom(1).setFlag(CDKConstants.ISPLACED, true);
        acplaced = new AtomPlacer3D().getPlacedHeavyAtom(ac, ac.getAtom(0), ac.getAtom(2));
        Assert.assertEquals(ac.getAtom(1), acplaced);
        acplaced = new AtomPlacer3D().getPlacedHeavyAtom(ac, ac.getAtom(0), ac.getAtom(1));
        Assert.assertNull(acplaced);
    }

    @Test
    public void testGetPlacedHeavyAtom_IAtomContainer_IAtom() {
        IMolecule ac = MoleculeFactory.makeAlphaPinene();
        IAtom acplaced = new AtomPlacer3D().getPlacedHeavyAtom(ac, ac.getAtom(0));
        Assert.assertNull(acplaced);
        ac.getAtom(1).setFlag(CDKConstants.ISPLACED, true);
        acplaced = new AtomPlacer3D().getPlacedHeavyAtom(ac, ac.getAtom(0));
        Assert.assertEquals(ac.getAtom(1), acplaced);
    }

    @Test
    public void testGeometricCenterAllPlacedAtoms_IAtomContainer() throws CDKException, Exception {
        IMolecule ac = MoleculeFactory.makeAlphaPinene();
        for (int i = 0; i < ac.getAtomCount(); i++) {
            ac.getAtom(i).setFlag(CDKConstants.ISPLACED, true);
        }
        ac.getAtom(0).setPoint3d(new Point3d(1.39, 2.04, 0));
        ac.getAtom(0).setPoint3d(new Point3d(2.02, 2.28, -1.12));
        ac.getAtom(0).setPoint3d(new Point3d(3.44, 2.80, -1.09));
        ac.getAtom(0).setPoint3d(new Point3d(3.91, 2.97, 0.35));
        ac.getAtom(0).setPoint3d(new Point3d(3.56, 1.71, 1.16));
        ac.getAtom(0).setPoint3d(new Point3d(2.14, 2.31, 1.29));
        ac.getAtom(0).setPoint3d(new Point3d(0, 1.53, 0));
        ac.getAtom(0).setPoint3d(new Point3d(2.83, 3.69, 1.17));
        ac.getAtom(0).setPoint3d(new Point3d(3.32, 4.27, 2.49));
        ac.getAtom(0).setPoint3d(new Point3d(2.02, 4.68, 0.35));
        Point3d center = new AtomPlacer3D().geometricCenterAllPlacedAtoms(ac);
        Assert.assertEquals(2.02, center.x, 0.01);
        Assert.assertEquals(4.68, center.y, 0.01);
        Assert.assertEquals(0.35, center.z, 0.01);
    }
}
