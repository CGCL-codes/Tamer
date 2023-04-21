package matrixCore;

import java.io.Serializable;
import java.util.Iterator;

public interface MatrixDataStorage {

    int getRowsCount();

    int getColumnsCount();

    double get(int row, int col);

    void set(int row, int col, double value);

    void init(int rowCount, int colCount);

    Iterator<MatrixDataStorageArray> getRowsIterator();

    Iterator<MatrixDataStorageArray> getColumnsIterator();
}
