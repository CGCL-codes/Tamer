package spectralcluster;

import Jama.Matrix;

public class matrixplus {

    public static void main(String[] args) {
        double[][] d = { { 1, 2, 3 }, { 4, 5, 6 }, { 9, 1, 3 } };
        double[][] c = { { 2, 3, 4 }, { 5, 6, 7 }, { 7, 8, 9 } };
        Matrix D = new Matrix(d);
        Matrix C = new Matrix(c);
        C.print(5, 2);
        D.print(5, 2);
        C.plus(D).print(5, 2);
    }

    public void doSomething() {
    }
}
