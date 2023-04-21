package org.openscience.jmol;

import java.awt.Graphics;
import java.util.Enumeration;

/**
 *  Drawing methods for ChemFrame.
 *
 *  @author Bradley A. Smith (bradley@baysmith.com)
 */
public class ChemFrameRenderer {

    /**
   * Paint this model to a graphics context.  It uses the matrix
   * associated with this model to map from model space to screen
   * space.
   *
   * @param g the Graphics context to paint to
   */
    public synchronized void paint(Graphics g, ChemFrame frame, DisplaySettings settings) {
        if ((frame.getAtoms() == null) || (frame.getNumberOfAtoms() <= 0)) {
            return;
        }
        boolean drawHydrogen = settings.getShowHydrogens();
        frame.transform();
        if (!settings.getFastRendering() || atomReferences == null) {
            if (atomReferences == null || atomReferences.length != frame.getNumberOfAtoms()) {
                atomReferences = new AtomReference[frame.getNumberOfAtoms()];
                for (int i = 0; i < atomReferences.length; ++i) {
                    atomReferences[i] = new AtomReference();
                }
            }
            for (int i = 0; i < frame.getNumberOfAtoms(); ++i) {
                atomReferences[i].index = i;
                atomReferences[i].z = frame.getAtoms()[i].getScreenPosition().z;
            }
            if (frame.getNumberOfAtoms() > 1) {
                sorter.sort(atomReferences);
            }
        }
        for (int i = 0; i < frame.getNumberOfAtoms(); ++i) {
            int j = atomReferences[i].index;
            Atom atom = frame.getAtoms()[j];
            if (drawHydrogen || (atom.getType().getAtomicNumber() != 1)) {
                if (settings.getShowBonds()) {
                    Enumeration bondIter = atom.getBondedAtoms();
                    while (bondIter.hasMoreElements()) {
                        Atom otherAtom = (Atom) bondIter.nextElement();
                        if (drawHydrogen || (otherAtom.getType().getAtomicNumber() != 1)) {
                            if (otherAtom.getScreenPosition().z < atom.getScreenPosition().z) {
                                bondRenderer.paint(g, atom, otherAtom, settings);
                            }
                        }
                    }
                }
                if (settings.getShowAtoms()) {
                    atomRenderer.paint(g, atom, frame.isAtomPicked(j), settings);
                }
                if (settings.getShowBonds()) {
                    Enumeration bondIter = atom.getBondedAtoms();
                    while (bondIter.hasMoreElements()) {
                        Atom otherAtom = (Atom) bondIter.nextElement();
                        if (drawHydrogen || (otherAtom.getType().getAtomicNumber() != 1)) {
                            if (otherAtom.getScreenPosition().z >= atom.getScreenPosition().z) {
                                bondRenderer.paint(g, atom, otherAtom, settings);
                            }
                        }
                    }
                }
                if (settings.getShowVectors()) {
                    if (atom.getVector() != null) {
                        ArrowLine al = new ArrowLine(g, atom.getScreenPosition().x, atom.getScreenPosition().y, atom.getScreenVector().x, frame.getAtoms()[j].getScreenVector().y, false, true);
                    }
                }
            }
        }
    }

    /**
   * Renderer for atoms.
   */
    private AtomRenderer atomRenderer = new AtomRenderer();

    /**
   * Renderer for bonds.
   */
    private BondRenderer bondRenderer = new BondRenderer();

    class AtomReference {

        int index = 0;

        float z = 0.0f;
    }

    AtomReference[] atomReferences;

    HeapSorter sorter = new HeapSorter(new HeapSorter.Comparator() {

        public int compare(Object atom1, Object atom2) {
            AtomReference a1 = (AtomReference) atom1;
            AtomReference a2 = (AtomReference) atom2;
            if (a1.z < a2.z) {
                return -1;
            } else if (a1.z > a2.z) {
                return 1;
            }
            return 0;
        }
    });
}
