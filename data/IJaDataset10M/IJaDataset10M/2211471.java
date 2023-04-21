package org.middleheaven.quantity.math.impl;

import java.util.ArrayList;
import java.util.List;
import org.middleheaven.quantity.math.Conjugatable;
import org.middleheaven.quantity.math.Matrix;
import org.middleheaven.quantity.math.structure.Field;

class EditableMatrix<F extends Field<F>> extends DenseMatrix<F> {

    static <T extends Field<T>> EditableMatrix<T> augmentWithEntity(Matrix<T> m) {
        EditableMatrix<T> e = new EditableMatrix<T>(m.rowsCount(), m.columnsCount() * 2, m.get(0, 0).zero());
        for (int r = 0; r < m.rowsCount(); r++) {
            for (int c = 0; c < m.columnsCount(); c++) {
                e.set(r, c, m.get(r, c));
            }
            e.set(r, m.columnsCount() + r, m.get(0, 0).one());
        }
        return e;
    }

    EditableMatrix(Matrix<F> other) {
        super(other);
    }

    EditableMatrix(int rows, int columns, F value) {
        super(rows, columns, value);
    }

    EditableMatrix(Matrix<F> other, int ri, int c0, int rowCount, int columnCount) {
        super(rowCount, columnCount, null);
        try {
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    this.set(i, j, other.get(ri + i, c0 + j));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }

    EditableMatrix(Matrix<F> other, int[] r, int j0, int j1) {
        super(r.length, j1 - j0 + 1, null);
        try {
            for (int i = 0; i < r.length; i++) {
                for (int j = j0; j <= j1; j++) {
                    this.set(i, j - j0, other.get(r[i], j));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }

    public EditableMatrix<F> set(int r, int c, F value) {
        List<F> row = rows.get(r).elements;
        if (row == null) {
            row = new ArrayList<F>(this.columnsCount());
            rows.get(r).elements = row;
        }
        row.add(c, value);
        return this;
    }

    protected Matrix<F> duplicate() {
        return new EditableMatrix<F>(this);
    }
}
