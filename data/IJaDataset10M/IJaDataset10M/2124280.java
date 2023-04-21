package org.alcibiade.eternity.editor.solver.swap;

public class SwapCoords {

    private int coordA;

    private int coordB;

    public SwapCoords(int coordA, int coordB) {
        super();
        assert coordA >= 0;
        assert coordB >= 0;
        assert coordA != coordB;
        this.coordA = coordA;
        this.coordB = coordB;
    }

    public int getCoordA() {
        return coordA;
    }

    public int getCoordB() {
        return coordB;
    }
}
