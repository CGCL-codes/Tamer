package net.sf.easymol.io.pdb;

public class PDBTitle extends PDBSection {

    private String content;

    private int continuation;

    public PDBTitle(int continuation, String content) {
        this.continuation = continuation;
        this.content = content;
    }

    public String read() {
        return "TITLE" + PDBSpaceMaker.FOUR + continuation + PDBSpaceMaker.ONE + content;
    }
}
