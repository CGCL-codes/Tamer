package org.jmol.viewer;

class Turn extends ProteinStructure {

    Turn(AlphaPolymer apolymer, int monomerIndex, int monomerCount) {
        super(apolymer, JmolConstants.PROTEIN_STRUCTURE_TURN, monomerIndex, monomerCount);
    }
}
