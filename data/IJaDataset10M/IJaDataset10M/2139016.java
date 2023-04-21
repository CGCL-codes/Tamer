package galronnlp.math;

/**
 *
 * @author Daniel A. Galron
 */
public class MatrixIndexException extends Exception {

    /**
     * Creates a new instance of MatrixIndexException
     */
    public MatrixIndexException(int i, int j) {
        super();
        System.err.println("Illegal index [" + i + "][" + j + "]");
    }
}
