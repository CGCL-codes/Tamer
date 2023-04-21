package tei.cr.component.matrix;

import java.io.IOException;
import tei.cr.component.matrix.variable.Variable;
import cern.colt.matrix.DoubleMatrix3D;

/**
 * TODO : le shadowing du champ matrix n'est peut �tre pas tr�s heureux. Danger de comportements compliqu�s par h�ritage. 
 * @author Sylvain Loiseau
 */
public abstract class AbstractTypedMatrix extends AbstractMatrix implements TypedMatrix {

    protected Variable[] edges;

    protected DoubleMatrix3D matrix;

    public double get(int slice, int row, int col) {
        return matrix.getQuick(slice, row, col);
    }

    /**
     * <strong>Warning</strong> Give the sum of the values for the different edge types.
     * @return the sum
     * @param row the  
     *
     * @see tei.cr.component.matrix.Matrix#get(int, int)
     */
    public double get(int row, int col) {
        double sumOfSlices = 0;
        for (int slice = matrix.slices(); --slice >= 0; ) {
            sumOfSlices += matrix.getQuick(slice, row, col);
        }
        return sumOfSlices;
    }

    public Variable[] getEdge() {
        return edges;
    }

    public int getVarsNumber() {
        return matrix.columns();
    }

    public int getEdgeTypesNumber() {
        return matrix.slices();
    }

    public int getNonZeroNumber() {
        cern.colt.list.IntArrayList slices = new cern.colt.list.IntArrayList();
        cern.colt.list.IntArrayList rows = new cern.colt.list.IntArrayList();
        cern.colt.list.IntArrayList columns = new cern.colt.list.IntArrayList();
        cern.colt.list.DoubleArrayList values = new cern.colt.list.DoubleArrayList();
        matrix.getNonZeros(slices, rows, columns, values);
        return values.size();
    }

    public void saveAsEdgeList(String uri) throws IOException {
        throw new UnsupportedOperationException("Impossible to write a 3D matrix.");
    }

    public void saveWithHeader(String uri) throws IOException {
        throw new UnsupportedOperationException("Impossible to write a 3D matrix.");
    }

    public void saveAsSparseMatrix(String uri) throws IOException {
        throw new UnsupportedOperationException("Impossible to write a 3D matrix.");
    }

    public void save(String uri) throws IOException {
        throw new UnsupportedOperationException("Impossible to write a 3D matrix.");
    }

    public void saveAsDtm(String uri) throws IOException {
        throw new UnsupportedOperationException("Impossible to write a 3D matrix.");
    }

    /**
     * Attention cette m�thode *doit* �tre surcharg�e
     * sinon le champ n'est plus masqu� et c'est la matrice 2D null
     * qui est retourn�e.
     */
    public cern.colt.matrix.impl.AbstractMatrix getRawMatrix() {
        return (cern.colt.matrix.impl.AbstractMatrix) matrix;
    }

    public void setRawMatrix(cern.colt.matrix.impl.AbstractMatrix matrix) {
        if (!(matrix instanceof DoubleMatrix3D)) {
            throw new IllegalArgumentException("The matrix should be a DoubleMatrix3D.");
        }
        this.matrix = (DoubleMatrix3D) matrix;
    }
}
