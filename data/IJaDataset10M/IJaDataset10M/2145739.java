package com.matthewtavares.jipt.math;

public class ComplexOperator {

    public Complex[][] matrixDotProduct(Complex[][] cMatrix, double[][] dMatrix) {
        int numRows = cMatrix.length;
        int numCols = cMatrix[0].length;
        Complex[][] resultMatrix = new Complex[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                resultMatrix[row][col] = cMatrix[row][col].multiplyByReal(dMatrix[row][col]);
            }
        }
        return resultMatrix;
    }

    public Complex[][] multComplexMatrixByDoubleMatrix(Complex[][] cMatrix, double[][] dMatrix) {
        int numRows = cMatrix.length;
        int numCols = cMatrix[0].length;
        Complex[][] resultMatrix = new Complex[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                double[] realCol = new double[numCols];
                for (int realRow = 0; realRow < numRows; realRow++) {
                    realCol[realRow] = dMatrix[realRow][col];
                }
                Complex tmpSum = multComplexVecByDoubleVec(cMatrix[row], realCol);
                resultMatrix[row][col] = tmpSum;
            }
        }
        return resultMatrix;
    }

    private Complex multComplexVecByDoubleVec(Complex[] cVec, double[] dVec) {
        int length = cVec.length;
        Complex sum = new Complex(0.0, 0.0);
        for (int i = 0; i < length; i++) {
            {
                Complex tmpProduct = cVec[i].multiplyByReal(dVec[i]);
                sum = sum.addComplex(tmpProduct);
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        ComplexOperator co = new ComplexOperator();
        Complex[] cVec = { new Complex(2.0, 3.0), new Complex(4.0, 5.0) };
        double[] dVec = { 5.0, 6.0 };
        Complex cr = co.multComplexVecByDoubleVec(cVec, dVec);
        System.out.println(cr.getReal() + "/" + cr.getImaginary());
    }
}
