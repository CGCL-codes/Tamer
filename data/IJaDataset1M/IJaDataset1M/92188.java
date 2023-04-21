package net.sf.metaprint2d.mol2;

import java.util.ArrayList;
import java.util.List;
import net.sf.metaprint2d.Constants;

public class Atom {

    private String name;

    private int atomType = -1;

    private List<Atom> neighbours = new ArrayList<Atom>(4);

    private String atomTypeString;

    public Atom(String atomTypeString) {
        Integer atype = Constants.ATOM_TYPE_INDEX.get(atomTypeString.toUpperCase());
        if (atype != null) {
            atomType = atype.intValue();
            this.atomTypeString = Constants.ATOM_TYPE_LIST.get(atype);
        } else {
            this.atomTypeString = atomTypeString;
        }
    }

    public Atom(int atomType) {
        this.atomType = atomType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addNeighbour(Atom atom) {
        this.neighbours.add(atom);
    }

    public String getName() {
        return name;
    }

    public List<Atom> getNeighbours() {
        return new ArrayList<Atom>(neighbours);
    }

    public int getAtomType() {
        return this.atomType;
    }

    public String getAtomTypeString() {
        return atomTypeString;
    }
}
