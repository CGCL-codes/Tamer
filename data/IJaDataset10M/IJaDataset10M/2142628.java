package net.sourceforge.ondex.parser.biocyc.sink;

import java.util.ArrayList;

/**
 * Represents a protein
 * @author peschr
 *
 */
public class Protein extends AbstractNode {

    private Gene encodedBy;

    private boolean isComplex;

    private ArrayList<Enzyme> isMemberOf = new ArrayList<Enzyme>();

    private ArrayList<Protein> componentOf = new ArrayList<Protein>();

    public void addIsMemberOf(Enzyme enzyme) {
        isMemberOf.add(enzyme);
    }

    public ArrayList<Enzyme> getIsMemberOf() {
        return isMemberOf;
    }

    public void addComponent(Protein protein) {
        componentOf.add(protein);
    }

    public ArrayList<Protein> getComponentOf() {
        return componentOf;
    }

    public Gene getEncodedBy() {
        return encodedBy;
    }

    public void setEncodedBy(Gene encodedBy) {
        this.encodedBy = encodedBy;
    }

    public boolean isComplex() {
        return isComplex;
    }

    public void setComplex(boolean isComplex) {
        this.isComplex = isComplex;
    }
}
