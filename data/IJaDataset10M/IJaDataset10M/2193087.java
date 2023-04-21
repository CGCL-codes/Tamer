package org.openscience.cdk.interfaces;

import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Checks the functionality of {@link IMoleculeSet} implementations.
 *
 * @cdk.module test-interfaces
 */
public abstract class AbstractMoleculeSetTest extends AbstractAtomContainerSetTest {

    @Test
    public void testGetMoleculeCount() {
        IMoleculeSet som = (IMoleculeSet) newChemObject();
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        Assert.assertEquals(3, som.getMoleculeCount());
    }

    @Test
    public void testGetMolecule_int() {
        IMoleculeSet som = (IMoleculeSet) newChemObject();
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        Assert.assertNotNull(som.getMolecule(2));
        Assert.assertNull(som.getMolecule(3));
    }

    @Test
    public void testAddMolecule_IMolecule() {
        IMoleculeSet som = (IMoleculeSet) newChemObject();
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        Assert.assertEquals(5, som.getMoleculeCount());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        Assert.assertEquals(7, som.getMoleculeCount());
    }

    @Test
    public void testAdd_IMoleculeSet() {
        IMoleculeSet som = (IMoleculeSet) newChemObject();
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        IMoleculeSet som2 = (IMoleculeSet) newChemObject();
        som2.add(som);
        Assert.assertEquals(5, som2.getMoleculeCount());
    }

    @Test
    public void testSetMolecules_arrayIMolecule() {
        IMoleculeSet som = (IMoleculeSet) newChemObject();
        IMolecule[] set = new IMolecule[5];
        set[0] = som.getBuilder().newMolecule();
        set[1] = som.getBuilder().newMolecule();
        set[2] = som.getBuilder().newMolecule();
        set[3] = som.getBuilder().newMolecule();
        set[4] = som.getBuilder().newMolecule();
        Assert.assertEquals(0, som.getMoleculeCount());
        som.setMolecules(set);
        Assert.assertEquals(5, som.getMoleculeCount());
    }

    @Test
    public void testMolecules() {
        IMoleculeSet som = (IMoleculeSet) newChemObject();
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        Assert.assertEquals(3, som.getMoleculeCount());
        Iterator<IAtomContainer> mols = som.molecules().iterator();
        int count = 0;
        while (mols.hasNext()) {
            count++;
            mols.next();
        }
        Assert.assertEquals(3, count);
        mols = som.molecules().iterator();
        while (mols.hasNext()) {
            mols.next();
            mols.remove();
        }
        Assert.assertEquals(0, som.getMoleculeCount());
    }

    @Test
    public void testGrowMoleculeArray() {
        IMoleculeSet som = (IMoleculeSet) newChemObject();
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        Assert.assertEquals(7, som.getAtomContainerCount());
    }

    @Test
    public void testMoleculeSet() {
        IMoleculeSet som = (IMoleculeSet) newChemObject();
        Assert.assertNotNull(som);
        Assert.assertEquals(0, som.getMoleculeCount());
    }

    @Test
    public void testGetMolecules() {
        IMoleculeSet som = (IMoleculeSet) newChemObject();
        Assert.assertEquals(0, som.getAtomContainerCount());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        som.addMolecule(som.getBuilder().newMolecule());
        Assert.assertEquals(3, som.getAtomContainerCount());
        Assert.assertNotNull(som.getMolecule(0));
        Assert.assertNotNull(som.getMolecule(1));
        Assert.assertNotNull(som.getMolecule(2));
    }

    @Test
    public void testToString() {
        IMoleculeSet som = (IMoleculeSet) newChemObject();
        String description = som.toString();
        for (int i = 0; i < description.length(); i++) {
            Assert.assertTrue(description.charAt(i) != '\n');
            Assert.assertTrue(description.charAt(i) != '\r');
        }
    }

    @Test
    public void testClone() throws Exception {
        IMoleculeSet som = (IMoleculeSet) newChemObject();
        Object clone = som.clone();
        Assert.assertTrue(clone instanceof IMoleculeSet);
        Assert.assertNotSame(som, clone);
    }

    @Test
    public void testCloneDuplication() throws Exception {
        IMoleculeSet moleculeSet = (IMoleculeSet) newChemObject();
        moleculeSet.addMolecule(moleculeSet.getBuilder().newMolecule());
        Object clone = moleculeSet.clone();
        Assert.assertTrue(clone instanceof IMoleculeSet);
        IMoleculeSet clonedSet = (IMoleculeSet) clone;
        Assert.assertNotSame(moleculeSet, clonedSet);
        Assert.assertEquals(moleculeSet.getMoleculeCount(), clonedSet.getMoleculeCount());
    }

    @Test
    public void testCloneMultiplier() throws Exception {
        IMoleculeSet moleculeSet = (IMoleculeSet) newChemObject();
        moleculeSet.addMolecule(moleculeSet.getBuilder().newMolecule());
        moleculeSet.setMultiplier(moleculeSet.getMolecule(0), 2.0);
        Object clone = moleculeSet.clone();
        Assert.assertTrue(clone instanceof IMoleculeSet);
        IMoleculeSet clonedSet = (IMoleculeSet) clone;
        Assert.assertNotSame(moleculeSet, clonedSet);
        Assert.assertEquals(2, moleculeSet.getMultiplier(0).intValue());
        Assert.assertEquals(2, clonedSet.getMultiplier(0).intValue());
    }

    @Test
    public void testStateChanged_IChemObjectChangeEvent() {
        ChemObjectListenerImpl listener = new ChemObjectListenerImpl();
        IMoleculeSet chemObject = (IMoleculeSet) newChemObject();
        chemObject.addListener(listener);
        chemObject.addMolecule(chemObject.getBuilder().newMolecule());
        Assert.assertTrue(listener.changed);
    }

    private class ChemObjectListenerImpl implements IChemObjectListener {

        private boolean changed;

        private ChemObjectListenerImpl() {
            changed = false;
        }

        @Test
        public void stateChanged(IChemObjectChangeEvent e) {
            changed = true;
        }

        @Test
        public void reset() {
            changed = false;
        }
    }
}
