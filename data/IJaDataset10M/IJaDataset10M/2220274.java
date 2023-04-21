package org.xmlcml.jumbo3;

import java.util.StringTokenizer;
import org.xmlcml.cml.CMLAtom;
import org.xmlcml.cml.CMLBondStereo;
import org.xmlcml.cml.CMLException;
import org.xmlcml.cml.CMLStringVal;
import org.xmlcml.cml.CMLSymbol;

public class BondStereoImpl extends AbstractAtomRefImpl implements org.xmlcml.cml.CMLBondStereo {

    protected CMLSymbol symbol = null;

    protected String val = "";

    protected String type = null;

    String[] atomRef = null;

    public BondStereoImpl() {
        type = CMLBondStereo.STRING;
    }

    public BondStereoImpl(String val) {
        type = CMLBondStereo.STRING;
        this.val = val;
    }

    /** create from builtin child of CMLBond */
    public BondStereoImpl(CMLStringVal sv) throws CMLException {
        if (sv == null) throw new CMLException("Null stereo");
        val = sv.getStringValue();
        String atomRefs = sv.getAttribute(ATOM_REFS);
        StringTokenizer st = new StringTokenizer(atomRefs, " ,");
        if (st.countTokens() != 2) throw new CMLException("CMLBond Stereo must have TWO atomRefs");
        atomRef = new String[2];
        atomRef[0] = st.nextToken();
        atomRef[1] = st.nextToken();
    }

    /** create with knowledge of ligands */
    public BondStereoImpl(CMLAtom[] ligands, String val) {
        this.val = val;
        if (ligands != null) {
            for (int i = 0; i < ligands.length; i++) {
                this.addAtomRef(ligands[i]);
            }
        }
    }

    public BondStereoImpl(CMLSymbol symbol) throws CMLException {
        if (symbol == null) throw new CMLException("Null stereo");
        type = SYMBOL;
        this.symbol = symbol;
    }

    /** copy constructor */
    public BondStereoImpl(CMLBondStereo bondStereo) throws CMLException {
        this.type = ((BondStereoImpl) bondStereo).type;
        this.symbol = new SymbolImpl(((BondStereoImpl) bondStereo).symbol);
        this.val = ((BondStereoImpl) bondStereo).val;
        if (((BondStereoImpl) bondStereo).atomRef != null) {
            this.atomRef = new String[((BondStereoImpl) bondStereo).atomRef.length];
            for (int i = 0; i < this.atomRef.length; i++) {
                this.atomRef[i] = ((BondStereoImpl) bondStereo).atomRef[i];
            }
        }
    }

    protected String getClassTagName() {
        return ELEMENT_NAMES[BONDSTEREO];
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CMLBondStereo)) return false;
        BondStereoImpl p = (BondStereoImpl) obj;
        if (p.type.equals(this.type)) {
            if (p.type.equals(CMLBondStereo.STRING)) {
            }
        } else {
            return (p.symbol.equals(this.symbol));
        }
        return false;
    }

    /** get the type of the parity */
    public String getType() {
        return type;
    }

    /** returns a floating point value; exception provides for symbolilc values later */
    public CMLSymbol getSymbol() throws CMLException {
        if (!SYMBOL.equals(type)) {
            throw new CMLException("Not a symbolic parity");
        }
        return symbol;
    }

    /** get the value */
    public String getStringValue() {
        return val;
    }

    /** String representation */
    public String toString() {
        if (CMLBondStereo.STRING.equals(type)) return val;
        return "" + symbol;
    }
}
