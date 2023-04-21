package joelib2.gui.render2D;

import joelib2.molecule.Atom;
import joelib2.molecule.Bond;
import joelib2.molecule.Molecule;
import joelib2.molecule.MoleculeVector;
import joelib2.molecule.fragmentation.ContiguousFragments;
import joelib2.ring.Ring;
import joelib2.util.HelperMethods;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Category;

/**
 * Rendering atoms.
 *
 * @.author     steinbeck
 * @.author     wegnerj
 * @.license    LGPL
 * @.cvsversion    $Revision: 1.7 $, $Date: 2005/02/17 16:48:32 $
 */
public class RenderingAtoms implements java.io.Serializable, Cloneable {

    private static Category logger = Category.getInstance("joelib2.gui.render2D.RenderingAtoms");

    private Hashtable aMap = new Hashtable();

    private List bonds = new Vector();

    private Hashtable fraMap = new Hashtable();

    private List frAtoms = new Vector();

    private List molecules = new Vector();

    private List molFragments = new Vector();

    private List rings = new Vector();

    public RenderingAtoms() {
    }

    public void add(Molecule mol) {
        molecules.add(mol);
        Vector origAtomIdx = new Vector();
        ContiguousFragments fragments = new ContiguousFragments();
        MoleculeVector frags = fragments.getFragmentation(mol, false, origAtomIdx);
        Molecule tmpMol;
        List tmpRings;
        Bond bond;
        Atom atom;
        RenderAtom ra;
        int[] orgIdx;
        for (int i = 0; i < frags.getSize(); i++) {
            tmpMol = frags.getMol(i);
            molFragments.add(tmpMol);
            orgIdx = (int[]) origAtomIdx.get(i);
            for (int j = 1; j <= tmpMol.getAtomsSize(); j++) {
                atom = tmpMol.getAtom(j);
                if (logger.isDebugEnabled()) {
                    logger.debug("fragmented atom " + j + " " + atom.getCoords3D());
                }
                ra = new RenderAtom();
                ra.mol = mol;
                ra.frMol = tmpMol;
                ra.atom = mol.getAtom(orgIdx[j - 1]);
                ra.frAtom = atom;
                ra.fraLabel = null;
                frAtoms.add(ra);
                fraMap.put(ra.frAtom, ra);
                aMap.put(ra.atom, ra);
            }
            tmpRings = tmpMol.getSSSR();
            for (int j = 0; j < tmpRings.size(); j++) {
                rings.add(tmpRings.get(j));
                if (logger.isDebugEnabled()) {
                    logger.debug("ring " + tmpRings.get(j) + " in fragment " + i);
                }
            }
            for (int j = 0; j < tmpMol.getBondsSize(); j++) {
                bond = tmpMol.getBond(j);
                bonds.add(bond);
            }
        }
    }

    public Atom getAtom(Atom renderAtom) {
        RenderAtom ra = (RenderAtom) fraMap.get(renderAtom);
        return ra.atom;
    }

    /**
     * Gets analogue rendering atom for the original atom of the original molecule.
     */
    public RenderAtom getRenderAtom(Atom atom) {
        return (RenderAtom) aMap.get(atom);
    }

    public RenderAtom getRenderAtom(int i) {
        return (RenderAtom) frAtoms.get(i);
    }

    /**
     * Gets rendering atom.
     *
     * @param i
     * @return
     */
    public Atom getRenderAtomAtom(int i) {
        RenderAtom ra = (RenderAtom) frAtoms.get(i);
        return ra.frAtom;
    }

    /**
     * @return
     */
    public int getRenderAtomCount() {
        return frAtoms.size();
    }

    public String getRenderAtomLabel(int i) {
        RenderAtom ra = (RenderAtom) frAtoms.get(i);
        return ra.fraLabel;
    }

    public int getRenderAtomNumber(Atom renderAtom) {
        RenderAtom ra;
        for (int i = 0; i < frAtoms.size(); i++) {
            ra = (RenderAtom) frAtoms.get(i);
            if (ra.frAtom == renderAtom) {
                return i;
            }
        }
        return -1;
    }

    public Atom[] getRenderAtoms() {
        Atom[] tmp = new Atom[frAtoms.size()];
        RenderAtom ra;
        for (int i = 0; i < frAtoms.size(); i++) {
            ra = (RenderAtom) frAtoms.get(i);
            tmp[i] = ra.frAtom;
        }
        return tmp;
    }

    /**
     * @return
     */
    public List getRenderBonds() {
        return bonds;
    }

    public List getRenderFragments() {
        return molFragments;
    }

    public Ring[] getRenderRings() {
        Ring[] tmp = new Ring[rings.size()];
        for (int i = 0; i < rings.size(); i++) {
            tmp[i] = (Ring) rings.get(i);
        }
        return tmp;
    }

    public boolean hasRenderAtomLabel(int i) {
        RenderAtom ra = (RenderAtom) frAtoms.get(i);
        if ((ra.fraLabel != null) && (ra.fraLabel.length() != 0)) {
            return true;
        }
        return false;
    }

    public void setRenderAtomLabel(int i, String label) {
        RenderAtom ra = (RenderAtom) frAtoms.get(i);
        ra.fraLabel = label;
    }

    public void setRenderAtomLabels(Molecule molecule, String labels, String delimiter, String labelDelim) {
        if (labels != null) {
            Vector lV = new Vector();
            HelperMethods.tokenize(lV, labels, delimiter);
            String s;
            int index;
            String label;
            RenderAtom renderAtom;
            for (int i = 0; i < lV.size(); i++) {
                s = (String) lV.get(i);
                index = s.indexOf(labelDelim);
                if (index != -1) {
                    Integer integer = new Integer(s.substring(0, index));
                    label = s.substring(index + 1);
                    renderAtom = this.getRenderAtom(molecule.getAtom(integer.intValue()));
                    renderAtom.fraLabel = label;
                }
            }
        }
    }
}
