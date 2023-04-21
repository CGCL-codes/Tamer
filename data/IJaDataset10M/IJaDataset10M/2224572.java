package net.sourceforge.combean.samples.mathprog.lp.matrixrounding;

import java.util.Locale;
import java.util.Scanner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Load a matrix from a Readable object.
 * 
 * @author schickin
 *
 */
public class MatrixToRoundLoader {

    private static Log log = LogFactory.getLog(MatrixToRoundLoader.class);

    private double[][] M = null;

    /**
     * Constructor
     * 
     * @param dataSource the Readable from where the matrix shall be read
     */
    public MatrixToRoundLoader(Readable dataSource) {
        loadMatrix(dataSource);
    }

    /**
     * Fill the internal data structures with data from the Readable.
     * 
     * @param dataSource the Readable from where the matrix shall be read
     */
    private void loadMatrix(Readable dataSource) {
        Scanner scan = new Scanner(dataSource);
        scan.useLocale(Locale.US);
        int numRows = scan.nextInt();
        int numCols = scan.nextInt();
        log.debug(("#rows: " + numRows + ", #cols: " + numCols));
        this.M = new double[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                this.M[row][col] = scan.nextDouble();
            }
        }
    }

    /**
     * Return the matrix that has been read.
     * 
     * @return Returns the matrix.
     */
    public final double[][] getM() {
        return this.M;
    }
}
