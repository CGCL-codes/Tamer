package org.personalsmartspace.cm.reasoning.proximity.diffusion;

import jama.Matrix;
import java.awt.geom.AffineTransform;

public class AffineGenerator {

    public AffineGenerator() {
    }

    public AffineTransform generateAffine(Position sourcePosA, Position sourcePosB, Position sourcePosC, Position destinationPosA, Position destinationPosB, Position destinationPosC) {
        double[][] array = new double[6][6];
        array[0][3] = array[0][4] = array[0][5] = array[1][3] = array[1][4] = array[1][5] = array[2][3] = array[2][4] = array[2][5] = array[3][0] = array[3][1] = array[3][2] = array[4][0] = array[4][1] = array[4][2] = array[5][0] = array[5][1] = array[5][2] = 0;
        array[0][2] = array[1][2] = array[2][2] = array[3][5] = array[4][5] = array[5][5] = 1;
        array[0][0] = sourcePosA.getX();
        array[0][1] = sourcePosA.getY();
        array[1][0] = sourcePosB.getX();
        array[1][1] = sourcePosB.getY();
        array[2][0] = sourcePosC.getX();
        array[2][1] = sourcePosC.getY();
        array[3][3] = sourcePosA.getX();
        array[3][4] = sourcePosA.getY();
        array[4][3] = sourcePosB.getX();
        array[4][4] = sourcePosB.getY();
        array[5][3] = sourcePosC.getX();
        array[5][4] = sourcePosC.getY();
        Matrix A = new Matrix(array);
        double[][] array1 = { { destinationPosA.getX() }, { destinationPosB.getX() }, { destinationPosC.getX() }, { destinationPosA.getY() }, { destinationPosB.getY() }, { destinationPosC.getY() } };
        Matrix B = new Matrix(array1);
        Matrix C = A.solve(B);
        double[] aff = { C.get(0, 0), C.get(3, 0), C.get(1, 0), C.get(4, 0), C.get(2, 0), C.get(5, 0) };
        return new AffineTransform(aff);
    }
}
