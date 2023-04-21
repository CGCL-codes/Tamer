package Application.Main;

import java.io.Serializable;
import java.util.Scanner;

/**
 *
 * @author Me
 */
public class MatrixInRowMajor extends MatrixBase implements Serializable {

    private static final long serialVersionUID = 199277541169325905L;

    public float[] rows[];

    /** Creates a new instance of MatrixInRowMajor */
    public MatrixInRowMajor(int _numRows, int _numCols) {
        numRows = _numRows;
        numCols = _numCols;
        rows = new float[numRows][];
        for (int i = 0; i < numRows; i++) {
            rows[i] = new float[numCols];
        }
    }

    public String ToString() {
        String retStr = "MatrixInRowMajor \n";
        retStr += "numRows: " + numRows;
        retStr += "numCols: " + numCols;
        return retStr;
    }

    /** The file is formatted as comma separated values, one matrix row to each
    *   line in file.
    *  The number of rows and cols in the file has to match the size
    *   of the matrix declared in the constructor (that built the object).
    * 
    * @param fileName
    */
    public void fillSelfFromFile(String fileName) {
        float floatValue = 0;
        String floatString;
        Scanner paramScanner;
        paramScanner = super.setUpScanner(fileName);
        for (int r = 0; r < numRows; r += 1) {
            for (int c = 0; c < numCols; c += 1) {
                floatString = paramScanner.next();
                floatValue = Float.parseFloat(floatString);
                rows[r][c] = floatValue;
            }
        }
    }

    /**clones all the rows, so get exact copy, including data in cells
    *
    * @return
    */
    public MatrixInRowMajor cloneSelf() {
        MatrixInRowMajor newClone;
        newClone = new MatrixInRowMajor(numRows, numCols);
        for (int r = 0; r < numRows; r += 1) {
            newClone.rows[r] = rows[r].clone();
        }
        return newClone;
    }
}
