package exceptions;

@SuppressWarnings("serial")
public class MatrixZeroDeterminant extends Exception {

    public String toString() {
        return "Matrix has zero determinant";
    }
}
