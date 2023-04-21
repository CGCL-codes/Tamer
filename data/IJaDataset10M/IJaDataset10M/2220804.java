package org.xmlcml.cmlimpl.subset;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import jumbo.euclid.Transform2;
import jumbo.xml.util.Util;
import org.xmlcml.cml.CMLAtom;
import org.xmlcml.cml.CMLBond;
import org.xmlcml.cml.CMLException;
import org.xmlcml.cml.CMLCoordinate2;
import org.xmlcml.cml.CMLMolecule;
import org.xmlcml.cml.subset.AtomSet;
import org.xmlcml.cml.subset.ToolHashSet;
import org.xmlcml.cmlimpl.MoleculeImpl;
import org.xmlcml.cmlimpl.Coord2;

/** a set of atoms. Counts number of times an atom has been added
(e.g. for bonds). An AtomSet may have coordinates associated with it which
may or may not be related to the molecule. This should make calcuation of fragment
geometries easier and therefore assembly less painful
*/
public class AtomSetImpl extends ToolHashSetImpl implements AtomSet {

    HashMap atomCountTable = new HashMap();

    protected HashMap coordinate2Map = null;

    public AtomSetImpl() {
        super();
    }

    /** creates atom set from CMLMolecule */
    public AtomSetImpl(CMLMolecule molecule) {
        this();
        init(molecule);
    }

    private void init(CMLMolecule molecule) {
        MoleculeImpl mol = (MoleculeImpl) molecule;
        Vector atomVector = mol.getAtomVector();
        for (int i = 0; i < atomVector.size(); i++) {
            CMLAtom atom = (CMLAtom) atomVector.elementAt(i);
            try {
                this.addAtom(atom);
            } catch (Exception e) {
                Util.bug(e);
            }
        }
    }

    /** creates atom set from Vector of Atoms; if Vector has no elements or they are not Atoms
	throws CIFException
	*/
    public AtomSetImpl(Vector vector) throws CMLException {
        this();
        if (vector == null || vector.size() == 0) throw new CMLException("no atoms in Vector");
        try {
            CMLAtom atom = (CMLAtom) vector.elementAt(0);
            CMLMolecule molecule = atom.getMolecule();
            for (int i = 0; i < vector.size(); i++) {
                atom = (CMLAtom) vector.elementAt(i);
                if (!(atom.getMolecule().equals(molecule))) {
                    throw new CMLException("Vector contains atoms from different molecules");
                }
                try {
                    this.addAtom(atom);
                } catch (Exception e) {
                    Util.bug(e);
                }
            }
        } catch (ClassCastException cce) {
            throw new CMLException("Vector contains non-Atoms");
        }
    }

    /** add an atom. If atom already present, increments count
*/
    public void addAtom(CMLAtom atom) throws CMLException {
        if (atom == null) {
            throw new CMLException("Null atom");
        }
        super.add(atom);
        incrementCount(atom);
    }

    /** adds both atoms in the bond
*/
    public void addBond(CMLBond bond) throws CMLException {
        if (bond == null) {
            throw new CMLException("Null bond");
        }
        addAtom(bond.getAtom(0));
        addAtom(bond.getAtom(1));
    }

    /** combines this with AtomSet to find common elements, else null.
*/
    public AtomSet and(AtomSet atomSet) throws CMLException {
        ToolHashSet temp = super.and(atomSet);
        AtomSet aSet = new AtomSetImpl();
        for (Iterator it = temp.iterator(); it.hasNext(); ) {
            aSet.addAtom((CMLAtom) it.next());
        }
        return aSet;
    }

    /** combines this with AtomSet to find elements NOT in common,
else null.
*/
    public AtomSet xor(AtomSet atomSet) throws CMLException {
        ToolHashSet temp = super.xor(atomSet);
        AtomSet aSet = new AtomSetImpl();
        for (Iterator it = temp.iterator(); it.hasNext(); ) {
            aSet.addAtom((CMLAtom) it.next());
        }
        return aSet;
    }

    /** combines this with AtomSet to find elements NOT in AtomSet,
else null.
*/
    public AtomSet not(AtomSet atomSet) throws CMLException {
        ToolHashSet temp = super.not(atomSet);
        AtomSet aSet = new AtomSetImpl();
        for (Iterator it = temp.iterator(); it.hasNext(); ) {
            aSet.addAtom((CMLAtom) it.next());
        }
        return aSet;
    }

    /** combines this with AtomSet to find all elements.
*/
    public AtomSet or(AtomSet atomSet) throws CMLException {
        ToolHashSet temp = super.or(atomSet);
        AtomSet aSet = new AtomSetImpl();
        for (Iterator it = temp.iterator(); it.hasNext(); ) {
            aSet.addAtom((CMLAtom) it.next());
        }
        return aSet;
    }

    /** how many times has atom been added (0  means atom is not in
set
*/
    public int getCount(CMLAtom atom) {
        Integer count = (Integer) atomCountTable.get(atom);
        return (count == null) ? 0 : count.intValue();
    }

    void incrementCount(CMLAtom atom) {
        int count = getCount(atom);
        atomCountTable.put(atom, new Integer(++count));
    }

    /** get atom coordinate map */
    public HashMap getCoordinate2Map() {
        return this.coordinate2Map;
    }

    /** convenience set coordinates for atom */
    public void setCoord2(CMLAtom atom, CMLCoordinate2 c) throws CMLException {
        if (!(this.contains(atom))) throw new CMLException("atom not in AtomSet");
        if (this.coordinate2Map == null) this.coordinate2Map = new HashMap();
        this.coordinate2Map.put(atom, c);
    }

    /** convenience get coordinates for atom */
    public CMLCoordinate2 getCoord2(CMLAtom atom) {
        if (!(this.contains(atom))) {
            return null;
        }
        if (this.coordinate2Map == null) return null;
        Coord2 coord = (Coord2) this.coordinate2Map.get(atom);
        return coord;
    }

    /** transform coordinates of all atoms in the set (rather inefficient)
*/
    public void transformBy(Transform2 t2) {
        for (Iterator it = coordinate2Map.keySet().iterator(); it.hasNext(); ) {
            Coord2 coord2 = (Coord2) coordinate2Map.get(it.next());
            coord2.transformBy(t2);
        }
    }

    /** translate coordinates of all atoms in the set
*/
    public void translateBy(CMLCoordinate2 offset) {
        for (Iterator it = coordinate2Map.keySet().iterator(); it.hasNext(); ) {
            CMLAtom atom = (CMLAtom) it.next();
            Coord2 coord2 = (Coord2) coordinate2Map.get(atom);
            coordinate2Map.put(atom, coord2.plus((Coord2) offset));
        }
    }

    /** get any atom (convenience method for iterative processing)  This is
	not reproducible. If none left,return null */
    public CMLAtom getStartingAtom() {
        Iterator it = this.iterator();
        return (it.hasNext()) ? (CMLAtom) it.next() : null;
    }

    public String toString() {
        String s = "";
        for (Iterator it = iterator(); it.hasNext(); ) {
            s += "{" + ((CMLAtom) it.next()).toString() + "} ";
        }
        return s;
    }
}
