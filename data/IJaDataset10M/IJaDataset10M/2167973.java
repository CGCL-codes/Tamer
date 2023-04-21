package uk.ac.shef.wit.aleph.algorithm.graphlab.util;

import no.uib.cipr.matrix.*;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;
import uk.ac.shef.wit.commons.ObjectIndex;
import java.util.Collection;

/**
 * Several matrix-related utility methods.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class UtilMatrix {

    public static Matrix addDiagonal(final Matrix matrix, final double shift) {
        for (int i = 0, max = Math.min(matrix.numRows(), matrix.numColumns()); i < max; ++i) matrix.add(i, i, shift);
        return matrix;
    }

    public static Vector set(final Vector x, final double alpha, final Vector y, final double beta, final Vector z) {
        for (int i = 0, size = y.size(); i < size; ++i) x.set(i, alpha * y.get(i) + beta * z.get(i));
        return x;
    }

    public static boolean isStationary(final Vector stationary, final Matrix p) {
        System.out.println("Checking if vector is stationary");
        boolean result = true;
        for (int i = 0; i < stationary.size(); ++i) {
            final double v = stationary.get(i);
            double sum = 0.0;
            for (int j = 0; j < stationary.size(); ++j) sum += stationary.get(j) * p.get(j, i);
            if (0.01 < Math.abs(sum - v)) {
                result = false;
                break;
            }
        }
        return result;
    }

    public static boolean isStochastic(final Matrix p) {
        System.out.println("Checking if matrix is stochastic");
        final double[] rowSum = new double[p.numRows()];
        for (final MatrixEntry e : p) rowSum[e.row()] += e.get();
        boolean result = true;
        for (int i = 0; i < p.numRows(); ++i) if (rowSum[i] <= 0.99 || rowSum[i] >= 1.01) {
            System.out.println("row " + i + " does not sum to one: " + rowSum[i]);
            result = false;
            break;
        }
        return result;
    }

    public static Vector vectorResize(final Vector vector, final int size) {
        Vector resized = vector;
        if (vector.size() < size) {
            resized = new DenseVector(size);
            for (final VectorEntry label : vector) resized.set(label.index(), label.get());
        }
        return resized;
    }

    public static Matrix squareMatrixView(final Matrix matrix, final Collection<Integer> indices) {
        return squareMatrixView(matrix, indices, new ObjectIndex<Integer>());
    }

    public static Matrix squareMatrixView(final Matrix matrix, final Collection<Integer> indices, final ObjectIndex<Integer> idsMap) {
        final Matrix view = new FlexCompRowMatrix(indices.size(), indices.size());
        for (final MatrixEntry e : matrix) if (indices.contains(e.row()) && indices.contains(e.column())) view.set(idsMap.add(e.row()), idsMap.add(e.column()), e.get());
        return view;
    }

    public static Vector vectorView(final Vector vector, final Collection<Integer> indices) {
        return vectorView(vector, indices, new ObjectIndex<Integer>());
    }

    public static Vector vectorView(final Vector vector, final Collection<Integer> indices, final ObjectIndex<Integer> idsMap) {
        final Vector view = new SparseVector(indices.size());
        for (final VectorEntry e : vector) if (indices.contains(e.index())) view.set(idsMap.add(e.index()), e.get());
        return view;
    }
}
