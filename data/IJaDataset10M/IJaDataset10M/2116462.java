package net.lunglet.array4j.matrix.packed;

import java.nio.Buffer;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.Matrix;

/**
 * Matrix packed by columns.
 */
public interface PackedMatrix extends Matrix {

    /** Get data buffer. */
    Buffer data();

    /** Returns <CODE>true</CODE> if the matrix is lower triangular. */
    boolean isLowerTriangular();

    /** Returns <CODE>true</CODE> if the matrix is symmetric. */
    boolean isSymmetric();

    /** Returns <CODE>true</CODE> if the matrix is upper triangular. */
    boolean isUpperTriangular();

    /** Get the storage type (heap or direct). */
    Storage storage();
}
