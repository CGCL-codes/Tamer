package org.openscience.cdk.ringsearch;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.Ring;
import org.openscience.cdk.RingSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.tools.ILoggingTool;
import org.openscience.cdk.tools.LoggingToolFactory;
import org.openscience.cdk.tools.manipulator.RingSetManipulator;

/**
 * Finds the Smallest Set of Smallest Rings. 
 * This is an implementation of the algorithm published in
 * {@cdk.cite FIG96}.
 * 
 * <p>The {@link SSSRFinder} is encouraged to be used, providing an exact
 * algorithm for finding the SSSR.
 *
 * @cdk.module extra
 * @cdk.githash
 * @cdk.keyword smallest-set-of-rings
 * @cdk.keyword ring search
 * @cdk.dictref blue-obelisk:findSmallestSetOfSmallestRings_Figueras
 *
 * @deprecated Use SSSRFinder instead (exact algorithm).
 */
public class FiguerasSSSRFinder {

    private static ILoggingTool logger = LoggingToolFactory.createLoggingTool(FiguerasSSSRFinder.class);

    int trimCounter = 0;

    private static final String PATH = "org.openscience.cdk.ringsearch.FiguerasSSSRFinderRFinder.PATH";

    /**
	 * Finds the Smallest Set of Smallest Rings.
	 *
	 * @param   mol the molecule to be searched for rings 
	 * @return      a RingSet containing the rings in molecule    
	 */
    public RingSet findSSSR(Molecule mol) {
        IBond brokenBond = null;
        RingSet sssr = new RingSet();
        Molecule molecule = new Molecule();
        molecule.add(mol);
        IAtom smallest;
        int smallestDegree, nodesToBreakCounter, degree;
        Atom[] rememberNodes;
        Ring ring;
        List fullSet = new Vector();
        List trimSet = new Vector();
        List nodesN2 = new Vector();
        initPath(molecule);
        logger.debug("molecule.getAtomCount(): " + molecule.getAtomCount());
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            fullSet.add(molecule.getAtom(f));
        }
        logger.debug("fullSet.size(): " + fullSet.size());
        do {
            smallestDegree = 7;
            smallest = null;
            nodesN2.clear();
            for (int f = 0; f < molecule.getAtomCount(); f++) {
                IAtom atom = molecule.getAtom(f);
                degree = molecule.getConnectedBondsCount(atom);
                if (degree == 0) {
                    if (!trimSet.contains(atom)) {
                        logger.debug("Atom of degree 0");
                        trimSet.add(atom);
                    }
                }
                if (degree == 2) {
                    nodesN2.add(atom);
                }
                if (degree < smallestDegree && degree > 0) {
                    smallest = atom;
                    smallestDegree = degree;
                }
            }
            if (smallest == null) break;
            if (smallestDegree == 1) {
                trimCounter++;
                trim(smallest, molecule);
                trimSet.add(smallest);
            } else if (smallestDegree == 2) {
                rememberNodes = new Atom[nodesN2.size()];
                nodesToBreakCounter = 0;
                for (int f = 0; f < nodesN2.size(); f++) {
                    ring = getRing((Atom) nodesN2.get(f), molecule);
                    if (ring != null) {
                        if (!RingSetManipulator.ringAlreadyInSet(ring, sssr)) {
                            sssr.addAtomContainer(ring);
                            rememberNodes[nodesToBreakCounter] = (Atom) nodesN2.get(f);
                            nodesToBreakCounter++;
                        }
                    }
                }
                if (nodesToBreakCounter == 0) {
                    nodesToBreakCounter = 1;
                    rememberNodes[0] = (Atom) nodesN2.get(0);
                }
                for (int f = 0; f < nodesToBreakCounter; f++) {
                    breakBond(rememberNodes[f], molecule);
                }
                if (brokenBond != null) {
                    molecule.addBond(brokenBond);
                    brokenBond = null;
                }
            } else if (smallestDegree == 3) {
                ring = getRing(smallest, molecule);
                if (ring != null) {
                    if (!RingSetManipulator.ringAlreadyInSet(ring, sssr)) {
                        sssr.addAtomContainer(ring);
                    }
                    brokenBond = checkEdges(ring, molecule);
                    molecule.removeElectronContainer(brokenBond);
                }
            }
        } while (trimSet.size() < fullSet.size());
        logger.debug("fullSet.size(): " + fullSet.size());
        logger.debug("trimSet.size(): " + trimSet.size());
        logger.debug("trimCounter: " + trimCounter);
        return sssr;
    }

    /**
	 * This routine is called 'getRing() in Figueras original article
	 * finds the smallest ring of which rootNode is part of.
	 *
	 * @param   rootNode  The Atom to be searched for the smallest ring it is part of
	 * @param   molecule  The molecule that contains the rootNode
	 * @return     The smallest Ring rootnode is part of
	 */
    private Ring getRing(IAtom rootNode, Molecule molecule) {
        IAtom node, neighbor, mAtom;
        List neighbors, mAtoms;
        int OKatoms = molecule.getAtomCount();
        Queue queue = new Queue();
        List path[] = new Vector[OKatoms];
        List intersection = new Vector();
        List ring = new Vector();
        for (int f = 0; f < OKatoms; f++) {
            path[f] = new Vector();
            ((List) molecule.getAtom(f).getProperty(PATH)).clear();
        }
        neighbors = molecule.getConnectedAtomsList(rootNode);
        for (int f = 0; f < neighbors.size(); f++) {
            neighbor = (IAtom) neighbors.get(f);
            queue.push(neighbor);
            ((List) neighbor.getProperty(PATH)).add(rootNode);
            ((List) neighbor.getProperty(PATH)).add(neighbor);
        }
        while (queue.size() > 0) {
            node = (Atom) queue.pop();
            mAtoms = molecule.getConnectedAtomsList(node);
            for (int f = 0; f < mAtoms.size(); f++) {
                mAtom = (IAtom) mAtoms.get(f);
                if (mAtom != ((List) node.getProperty(PATH)).get(((Vector) node.getProperty(PATH)).size() - 2)) {
                    if (((List) mAtom.getProperty(PATH)).size() > 0) {
                        intersection = getIntersection((List) node.getProperty(PATH), (List) mAtom.getProperty(PATH));
                        if (intersection.size() == 1) {
                            logger.debug("path1  ", ((List) node.getProperty(PATH)));
                            logger.debug("path2  ", ((List) mAtom.getProperty(PATH)));
                            logger.debug("rootNode  ", rootNode);
                            logger.debug("ring   ", ring);
                            ring = getUnion(((Vector) node.getProperty(PATH)), ((Vector) mAtom.getProperty(PATH)));
                            return prepareRing(ring, molecule);
                        }
                    } else {
                        mAtom.setProperty(PATH, (Vector) ((Vector) node.getProperty(PATH)).clone());
                        ((List) mAtom.getProperty(PATH)).add(mAtom);
                        queue.push(mAtom);
                    }
                }
            }
        }
        return null;
    }

    /**
	 * Returns the ring that is formed by the atoms in the given vector. 
	 *
	 * @param   vec  The vector that contains the atoms of the ring
	 * @param   mol  The molecule this ring is a substructure of
	 * @return     The ring formed by the given atoms
	 */
    private Ring prepareRing(List vec, Molecule mol) {
        int atomCount = vec.size();
        Ring ring = new Ring(atomCount);
        Atom[] atoms = new Atom[atomCount];
        vec.toArray(atoms);
        ring.setAtoms(atoms);
        try {
            IBond b;
            for (int i = 0; i < atomCount - 1; i++) {
                b = mol.getBond(atoms[i], atoms[i + 1]);
                if (b != null) {
                    ring.addBond(b);
                } else {
                    logger.error("This should not happen.");
                }
            }
            b = mol.getBond(atoms[0], atoms[atomCount - 1]);
            if (b != null) {
                ring.addBond(b);
            } else {
                logger.error("This should not happen either.");
            }
        } catch (Exception exc) {
            logger.debug(exc);
        }
        logger.debug("found Ring  ", ring);
        return ring;
    }

    /**
	 * removes all bonds connected to the given atom leaving it with degree zero.
	 *
	 * @param   atom  The atom to be disconnecred
	 * @param   molecule  The molecule containing the atom
	 */
    private void trim(IAtom atom, Molecule molecule) {
        List<IBond> bonds = molecule.getConnectedBondsList(atom);
        for (int i = 0; i < bonds.size(); i++) {
            molecule.removeElectronContainer((IBond) bonds.get(i));
        }
    }

    /**
	 * initializes a path vector in every Atom of the given molecule
	 *
	 * @param   molecule  The given molecule
	 */
    private void initPath(Molecule molecule) {
        for (int i = 0; i < molecule.getAtomCount(); i++) {
            IAtom atom = molecule.getAtom(i);
            atom.setProperty(PATH, new Vector());
        }
    }

    /**
	 * Returns a Vector that contains the intersection of Vectors vec1 and vec2
	 *
	 * @param   vec1   The first vector
	 * @param   vec2   The second vector
	 * @return     
	 */
    private List getIntersection(List vec1, List vec2) {
        List is = new Vector();
        for (int f = 0; f < vec1.size(); f++) {
            if (vec2.contains((Atom) vec1.get(f))) is.add((Atom) vec1.get(f));
        }
        return is;
    }

    /**
	 * Returns a Vector that contains the union of Vectors vec1 and vec2
	 *
	 * @param   vec1  The first vector
	 * @param   vec2  The second vector
	 * @return     
	 */
    private Vector getUnion(Vector vec1, Vector vec2) {
        Vector is = (Vector) vec1.clone();
        for (int f = vec2.size() - 1; f > -1; f--) {
            if (!vec1.contains((Atom) vec2.elementAt(f))) is.addElement((Atom) vec2.elementAt(f));
        }
        return is;
    }

    /**
	 * Eliminates one bond of this atom from the molecule
	 *
	 * @param   atom  The atom one bond is eliminated of
	 * @param   molecule  The molecule that contains the atom
	 */
    private void breakBond(Atom atom, Molecule molecule) {
        Iterator<IBond> bonds = molecule.bonds().iterator();
        while (bonds.hasNext()) {
            IBond bond = (IBond) bonds.next();
            if (bond.contains(atom)) {
                molecule.removeElectronContainer(bond);
                break;
            }
        }
    }

    /**
	 * Selects an optimum edge for elimination in structures without N2 nodes.
	 *
     * <p>This might be severely broken! Would have helped if there was an
     * explanation of how this algorithm worked.
     *
	 * @param   ring  
	 * @param   molecule  
	 */
    private IBond checkEdges(Ring ring, Molecule molecule) {
        Ring r1, r2;
        RingSet ringSet = new RingSet();
        IBond bond;
        int minMaxSize = Integer.MAX_VALUE;
        int minMax = 0;
        logger.debug("Molecule: " + molecule);
        Iterator<IBond> bonds = ring.bonds().iterator();
        while (bonds.hasNext()) {
            bond = (IBond) bonds.next();
            molecule.removeElectronContainer(bond);
            r1 = getRing(bond.getAtom(0), molecule);
            r2 = getRing(bond.getAtom(1), molecule);
            logger.debug("checkEdges: " + bond);
            if (r1.getAtomCount() > r2.getAtomCount()) {
                ringSet.addAtomContainer(r1);
            } else {
                ringSet.addAtomContainer(r2);
            }
            molecule.addBond(bond);
        }
        for (int i = 0; i < ringSet.getAtomContainerCount(); i++) {
            if (((Ring) ringSet.getAtomContainer(i)).getBondCount() < minMaxSize) {
                minMaxSize = ((Ring) ringSet.getAtomContainer(i)).getBondCount();
                minMax = i;
            }
        }
        return (IBond) ring.getElectronContainer(minMax);
    }
}
