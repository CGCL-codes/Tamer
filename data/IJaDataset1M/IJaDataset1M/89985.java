package com.banordhessen.matrixrechner.testdata;

import com.banordhessen.matrixrechner.objects.Matrix;
import com.banordhessen.matrixrechner.objects.MatrixObjectHandler;
import com.banordhessen.matrixrechner.objects.MatrixenContainer;

public class TestKlasse {

    public static void main(String[] args) {
        Matrix test = new Matrix(3, 3);
        Matrix test2 = new Matrix(3, 3);
        Matrix invers = new Matrix(3, 3);
        test.setMatrixValues(0, 0, 2);
        test.setMatrixValues(1, 0, -1);
        test.setMatrixValues(2, 0, 3);
        test.setMatrixValues(0, 1, -3);
        test.setMatrixValues(1, 1, 1);
        test.setMatrixValues(2, 1, 1);
        test.setMatrixValues(0, 2, 1);
        test.setMatrixValues(1, 2, -4);
        test.setMatrixValues(2, 2, -2);
        test2.setMatrixValues(0, 0, 2);
        test2.setMatrixValues(1, 0, 2);
        test2.setMatrixValues(2, 0, 4);
        test2.setMatrixValues(0, 1, 2);
        test2.setMatrixValues(1, 1, 6);
        test2.setMatrixValues(2, 1, 3);
        test2.setMatrixValues(0, 2, 1);
        test2.setMatrixValues(1, 2, 7);
        test2.setMatrixValues(2, 2, 1);
        invers.setMatrixValues(0, 0, 1);
        invers.setMatrixValues(1, 0, 0);
        invers.setMatrixValues(2, 0, 0);
        invers.setMatrixValues(0, 1, 0);
        invers.setMatrixValues(1, 1, 1);
        invers.setMatrixValues(2, 1, 0);
        invers.setMatrixValues(0, 2, 0);
        invers.setMatrixValues(1, 2, 0);
        invers.setMatrixValues(2, 2, 1);
        MatrixenContainer testContainer = new MatrixenContainer(test);
        testContainer.addToMatrixCotainer(invers, test2);
        testContainer.addToMatrixCotainer(invers, test2);
        testContainer.addToMatrixCotainer(invers, test2);
        MatrixObjectHandler maObjHandler = new MatrixObjectHandler(test);
        maObjHandler.calcMatrix();
        maObjHandler.printItOut();
    }
}
