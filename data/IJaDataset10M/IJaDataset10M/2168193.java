package galronnlp.math;

/**
 *
 * @author Daniel A. Galron
 */
public class IdentityMatrix extends Matrix {

    /** Creates a new instance of IdentityMatrix */
    public IdentityMatrix(int s) {
        super(s, s);
        for (int i = 0; i < s; i++) {
            try {
                this.put(i, i, 1.0);
            } catch (MatrixIndexException ex) {
                ex.printStackTrace();
            }
        }
    }
}
