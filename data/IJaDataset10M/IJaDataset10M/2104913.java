package org.ejml.data;

import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains functions useful for testing the results of matrices
 *
 * @author Peter Abeles
 */
public class UtilTestMatrix {

    public static void checkMat(DenseMatrix64F mat, double... d) {
        double data[] = mat.getData();
        for (int i = 0; i < mat.getNumElements(); i++) {
            assertEquals(d[i], data[i], 1e-6);
        }
    }

    public static void checkSameElements(double tol, int length, double a[], double b[]) {
        double aa[] = new double[length];
        double bb[] = new double[length];
        System.arraycopy(a, 0, aa, 0, length);
        System.arraycopy(b, 0, bb, 0, length);
        Arrays.sort(aa);
        Arrays.sort(bb);
        for (int i = 0; i < length; i++) {
            if (Math.abs(aa[i] - bb[i]) > tol) fail("Mismatched elements");
        }
    }

    public static void checkNumFound(int expected, double tol, double value, double data[]) {
        int numFound = 0;
        for (int i = 0; i < data.length; i++) {
            if (Math.abs(data[i] - value) <= tol) numFound++;
        }
        assertEquals(expected, numFound);
    }
}
