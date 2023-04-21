package net.sf.easymol.io.pdb;

public class PDBConect extends PDBSection {

    private int from, to, valency;

    public PDBConect(int from, int to, int valency) {
        this.from = from;
        this.to = to;
        this.valency = valency;
    }

    public String read() {
        return "CONECT " + PDBSpaceMaker.ONE + from + PDBSpaceMaker.ONE + to;
    }
}
