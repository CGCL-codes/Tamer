package jumbo.euclid;

import jumbo.xml.util.Util;

/**
<P>
 IntMatrix - rectangular integer matrix class
<P>
 IntMatrix represents a rectangular m-x-n  matrix.
 The basic matrix algebra for non-square matrices is represented here
 and this class is also a base for square matrices.
<P>
 Read the signature of each member function carefully as some MODIFY 
 the object and some CREATE A NEW ONE.  Among the reasons for this 
 is that subclassing (e.g to IntSquareMatrix) is easier with
 one of these forms in certain cases.  Note that if you modify
 an object, then all references to it will refer to the changed object

@author (C) P. Murray-Rust, 1996
*/
public class IntMatrix extends Status {

    /** number of rows
*/
    protected int rows = 0;

    /** number of columns
*/
    protected int cols = 0;

    /** the matrix 
*/
    protected int[][] flmat = new int[0][0];

    /** Default matrix, with cols = rows = 0
*/
    public IntMatrix() {
    }

    /** A rows*cols matrix set to 0 (rows or cols < 0 defaults to 0)
*/
    public IntMatrix(int r, int c) {
        if (rows < 0) rows = 0;
        if (cols < 0) cols = 0;
        rows = r;
        cols = c;
        flmat = new int[r][c];
    }

    /** Formed by feeding in an existing 1-D array to a rowsXcols matrix. 
    THE COLUMN IS THE FASTEST MOVING INDEX, i.e. the matrix is filled
    as flmat(0,0), flmat(0,1) ... C-LIKE.
    COPIES the array 
@exception BadArgumentException size of array is not rows*cols
*/
    public IntMatrix(int rows, int cols, int[] array) throws BadArgumentException {
        this(rows, cols);
        if (array.length != rows * cols) {
            throw new BadArgumentException();
        }
        this.rows = rows;
        this.cols = cols;
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flmat[i][j] = array[count++];
            }
        }
    }

    /** initalises all elements in the array with a given int[] 
*/
    public IntMatrix(int r, int c, int f) {
        this(r, c);
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flmat[i][j] = f;
            }
        }
    }

    /** submatrix of another matrix; fails if lowrow > hirow, lowrow < 0, etc. 
COPIES the parts of <TT>m</TT>
@exception BadArgumentException impossible value of hirow, hicol, lowrow, lowcol
*/
    public IntMatrix(IntMatrix m, int lowrow, int hirow, int lowcol, int hicol) throws BadArgumentException {
        this(hirow - lowrow + 1, hicol - lowcol + 1);
        if (hirow >= m.getRows() || hicol >= m.getRows() || lowrow < 0 || lowcol < 0) {
            throw new BadArgumentException();
        }
        for (int i = 0, mrow = lowrow; i < rows; i++, mrow++) {
            for (int j = 0, mcol = lowcol; j < cols; j++, mcol++) {
                flmat[i][j] = m.flmat[mrow][mcol];
            }
        }
    }

    /** copy constructor - COPIES the other matrix 
*/
    public IntMatrix(IntMatrix m) {
        this(m.rows, m.cols);
        for (int i = 0; i < rows; i++) {
            System.arraycopy(m.flmat[i], 0, flmat[i], 0, cols);
        }
    }

    /** shallowCopy 
*/
    public void shallowCopy(IntMatrix m) {
        this.rows = m.rows;
        this.cols = m.cols;
        this.flmat = m.flmat;
    }

    /** COPY from an existing matrix  - check that it is rectangular 
@exception NonRectMatrixException m has rows of different lengths
*/
    public IntMatrix(int[][] m) throws NonRectMatrixException {
        this(m.length, m[0].length);
        for (int i = 0; i < rows; i++) {
            if (m[i].length != cols) {
                throw new NonRectMatrixException("cols " + cols + " row: " + i + " length: " + m[i].length);
            }
            for (int j = 0; j < cols; j++) {
                flmat[i][j] = m[i][j];
            }
        }
    }

    public IntMatrix clone(IntMatrix m) {
        return new IntMatrix(m);
    }

    /** get number of rows
*/
    public int getRows() {
        return rows;
    }

    /** get number of columns 
*/
    public int getCols() {
        return cols;
    }

    public int[][] getMatrix() {
        return flmat;
    }

    /** get matrix as int[] (in C order: m(0,0), m(0,1) ...)
*/
    public int[] getMatrixAsArray() {
        int[] temp = new int[rows * cols];
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp[count++] = flmat[i][j];
            }
        }
        return temp;
    }

    /** are two matrices equal in all elements?
@exception UnequalMatricesException m and <TT>this</TT> are different sizes
*/
    public boolean equals(IntMatrix m) throws UnequalMatricesException {
        checkConformable(m);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (flmat[i][j] != m.flmat[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkConformable(IntMatrix m) throws UnequalMatricesException {
        if (rows != m.rows || cols != m.cols) {
            throw new UnequalMatricesException();
        }
    }

    private void checkConformable2(IntMatrix m) throws UnequalMatricesException {
        if (m.rows != cols) {
            throw new UnequalMatricesException();
        }
    }

    /** matrix addition - adds conformable matrices giving NEW matrix
@exception UnequalMatricesException m and <TT>this</TT> are different sizes
*/
    public IntMatrix plus(IntMatrix m2) throws UnequalMatricesException {
        IntMatrix m = new IntMatrix(m2.rows, m2.cols);
        checkConformable(m2);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                m.flmat[i][j] = flmat[i][j] + m2.flmat[i][j];
            }
        }
        return m;
    }

    /** matrix subtraction - subtracts conformable matrices giving NEW matrix
@exception UnequalMatricesException m and <TT>this</TT> are different sizes
*/
    public IntMatrix subtract(IntMatrix m2) throws UnequalMatricesException {
        IntMatrix m = new IntMatrix(m2.rows, m2.cols);
        checkConformable(m2);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                m.flmat[i][j] = flmat[i][j] - m2.flmat[i][j];
            }
        }
        return m;
    }

    /** unary minus - negate all elements of matrix; MODIFIES matrix  
*/
    public void negative() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flmat[i][j] = -flmat[i][j];
            }
        }
    }

    /** matrix multiplication - multiplies conformable matrices to give NEW matrix
result = 'this' * m;  (order matters)
@exception UnequalMatricesException m and <TT>this</TT> are different sizes
*/
    public IntMatrix multiply(IntMatrix m) throws UnequalMatricesException {
        checkConformable2(m);
        IntMatrix m1 = new IntMatrix(rows, m.cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                m1.flmat[i][j] = 0;
                for (int k = 0; k < cols; k++) {
                    m1.flmat[i][j] += flmat[i][k] * m.flmat[k][j];
                }
            }
        }
        return m1;
    }

    /** matrix multiplication by a scalar - MODIFIES matrix 
*/
    public void multiplyBy(int f) {
        IntMatrix m = new IntMatrix(this);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flmat[i][j] *= f;
            }
        }
    }

    /** matrix multiplication of a COLUMN vector 
@exception UnequalMatricesException f.size() differs from cols
*/
    public IntArray multiply(IntArray f) throws UnequalMatricesException {
        if (f.size() != this.cols) {
            throw new UnequalMatricesException();
        }
        int[] temp = new int[rows];
        int[] farray = f.getArray();
        for (int i = 0; i < rows; i++) {
            temp[i] = 0;
            for (int j = 0; j < cols; j++) {
                temp[i] += this.flmat[i][j] * farray[j];
            }
        }
        IntArray ff = new IntArray(temp);
        return ff;
    }

    /** extracts a given element from the matrix
*/
    public int elementAt(int row, int col) {
        return flmat[row][col];
    }

    /** extracts a given element from the matrix
*/
    public int elementAt(Int2 rowcol) {
        return flmat[rowcol.elementAt(0)][rowcol.elementAt(1)];
    }

    /** inserts a given element to the matrix - MODIFIES matrix 
*/
    public void setElementAt(int row, int col, int f) {
        flmat[row][col] = f;
    }

    /** get largest element
*/
    public int largestElement() {
        Int2 temp = indexOfLargestElement();
        return this.elementAt(temp);
    }

    /** get index of largest element
*/
    public Int2 indexOfLargestElement() {
        int f = Integer.MIN_VALUE;
        int im = 0;
        int jm = 0;
        for (int irow = 0; irow < rows; irow++) {
            for (int jcol = 0; jcol < cols; jcol++) {
                if (f < flmat[irow][jcol]) {
                    f = flmat[irow][jcol];
                    im = irow;
                    jm = jcol;
                }
            }
        }
        return new Int2(im, jm);
    }

    /** get largest element in a column 
*/
    public int largestElementInColumn(int jcol) {
        return this.elementAt(indexOfLargestElementInColumn(jcol), jcol);
    }

    /** get index of largest element in column
*/
    public int indexOfLargestElementInColumn(int jcol) {
        int f = Integer.MIN_VALUE;
        int imax = 0;
        for (int irow = 0; irow < rows; irow++) {
            if (f < flmat[irow][jcol]) {
                f = flmat[irow][jcol];
                imax = irow;
            }
        }
        return imax;
    }

    /** get largest element in a row
*/
    public int largestElementInRow(int irow) {
        return this.elementAt(irow, indexOfLargestElementInRow(irow));
    }

    /** get index of largest element in row (or -1 if default matrix)
*/
    public int indexOfLargestElementInRow(int irow) {
        if (irow < 1 || irow > rows) return -1;
        int f = Integer.MIN_VALUE;
        int imax = 0;
        for (int jcol = 0; jcol < cols; jcol++) {
            if (f < flmat[irow][jcol]) {
                f = flmat[irow][jcol];
                imax = jcol;
            }
        }
        return imax;
    }

    /** get smallest element
*/
    public int smallestElement() {
        Int2 temp = indexOfSmallestElement();
        return this.elementAt(temp);
    }

    /** get index of smallest element
*/
    public Int2 indexOfSmallestElement() {
        int f = Integer.MAX_VALUE;
        int im = 0;
        int jm = 0;
        for (int irow = 0; irow < rows; irow++) {
            for (int jcol = 0; jcol < cols; jcol++) {
                if (f > flmat[irow][jcol]) {
                    f = flmat[irow][jcol];
                    im = irow;
                    jm = jcol;
                }
            }
        }
        return new Int2(im, jm);
    }

    /** get smallest element in a column
*/
    public int smallestElementInColumn(int jcol) {
        return this.elementAt(indexOfSmallestElementInColumn(jcol), jcol);
    }

    /** get index of smallest elem in column 
*/
    public int indexOfSmallestElementInColumn(int jcol) {
        if (jcol < 1 || jcol > cols) return 0;
        int f = Integer.MAX_VALUE;
        int imax = 0;
        for (int irow = 0; irow < rows; irow++) {
            if (f > flmat[irow][jcol]) {
                f = flmat[irow][jcol];
                imax = irow;
            }
        }
        return imax;
    }

    /** get smallest element in a row
*/
    public int smallestElementInRow(int irow) {
        return this.elementAt(irow, indexOfSmallestElementInRow(irow));
    }

    /** get index of smallest element in row
*/
    public int indexOfSmallestElementInRow(int irow) {
        int f = Integer.MAX_VALUE;
        int imax = 0;
        for (int jcol = 0; jcol < cols; jcol++) {
            if (f > flmat[irow][jcol]) {
                f = flmat[irow][jcol];
                imax = jcol;
            }
        }
        return imax;
    }

    /** get column data from matrix
*/
    public IntArray extractColumnData(int col) {
        IntArray fa = new IntArray(rows);
        for (int i = 0; i < rows; i++) {
            fa.setElementAt(i, this.flmat[i][col]);
        }
        return fa;
    }

    /** get row data from matrix
*/
    public IntArray extractRowData(int row) {
        return new IntArray(flmat[row].length, flmat[row]);
    }

    /** clear matrix
*/
    public void clearMatrix() {
        for (int irow = 0; irow < rows; irow++) {
            for (int jcol = 0; jcol < cols; jcol++) {
                flmat[irow][jcol] = 0;
            }
        }
    }

    /** initialise matrix to given int 
*/
    public void setAllElements(int f) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flmat[i][j] = f;
            }
        }
    }

    /** transpose matrix - creates new Matrix 
*/
    public IntMatrix getTranspose() {
        int[][] m = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                m[j][i] = this.flmat[i][j];
            }
        }
        IntMatrix mm = null;
        try {
            mm = new IntMatrix(m);
        } catch (NonRectMatrixException e) {
            Util.bug(e);
        }
        return mm;
    }

    /** is the matrix square?
*/
    public boolean isSquare() {
        return (cols == rows && cols > 0);
    }

    /** delete column from matrix and close up
*/
    public void deleteColumn(int col) {
        if (col > 0 && col <= cols) {
            int[][] temp = new int[rows][cols - 1];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < col; j++) {
                    temp[i][j] = flmat[i][j];
                }
                for (int j = col + 1; j < cols; j++) {
                    temp[i][j - 1] = flmat[i][j];
                }
            }
            cols--;
            flmat = temp;
        }
    }

    /** delete 2 or more adjacent columns (inclusive) from matrix and close up
*/
    public void deleteColumns(int low, int high) {
        if (high > cols - 1) high = cols - 1;
        for (int i = 0; i < rows; i++) {
            this.flmat[i] = IntArray.deleteElements(this.flmat[i], low, high);
        }
        cols -= (high - low + 1);
    }

    /** delete row from matrix and close up
*/
    public void deleteRow(int row) {
        deleteRows(row, row);
    }

    /** delete 2 or more adjacent rows (inclusive) from matrix and close up;
  if (high > rows-1 high -> rows-1; or low < 0, low -> 0*/
    public void deleteRows(int low, int high) {
        if (high >= rows) high = rows - 1;
        if (low < 0) low = 0;
        if (low > high) return;
        int newrows = rows + high - low + 1;
        int temp[][] = new int[newrows][cols];
        int oldrow = 0;
        int newrow = 0;
        while (oldrow < rows) {
            if (oldrow < low || oldrow > high) {
                temp[newrow++] = flmat[oldrow];
            }
            oldrow++;
        }
        this.rows = newrows;
        flmat = temp;
    }

    /** replace data in a single column - return false if impossible 
*/
    public boolean replaceColumnData(int column, IntArray f) {
        if (column >= 0 && column < cols && f.size() == rows) {
            int[] temp = f.getArray();
            for (int i = 0; i < rows; i++) {
                flmat[i][column] = temp[i];
            }
            return true;
        } else {
            return false;
        }
    }

    /** and from int[]
*/
    public void replaceColumnData(int starting_col, int[] f) {
        IntArray temp = new IntArray(rows, f);
        replaceColumnData(starting_col, temp);
    }

    /** replace data in a block of columns
*/
    public void replaceColumnData(int start_column, IntMatrix m) {
        if (this == m) return;
        int mcols = m.getCols();
        int mrows = m.getRows();
        if (start_column > 0 && (start_column + mcols) <= (cols + 1) && mrows == rows) {
            for (int i = 0; i < rows; i++) {
                for (int j = start_column; j < mcols; j++) {
                    flmat[i][j] = m.flmat[i][j];
                }
            }
        }
    }

    /** insert a hole into the matric and expand 
*/
    public void insertColumns(int after_col, int delta_cols) {
        if (after_col >= 0 && after_col <= cols && delta_cols > 0) {
            int newcols = delta_cols + cols;
            IntMatrix temp = new IntMatrix(rows, newcols);
            for (int irow = 0; irow < rows; irow++) {
                for (int jcol = 0; jcol < after_col; jcol++) {
                    temp.flmat[irow][jcol] = this.flmat[irow][jcol];
                }
                for (int jcol = after_col; jcol < cols; jcol++) {
                    temp.flmat[irow][jcol + delta_cols] = this.flmat[irow][jcol];
                }
            }
            shallowCopy(temp);
        }
    }

    /** add data as column or column block into matrix and expand
*/
    public void insertColumnData(int after_col, IntArray f) {
        if (f.size() == rows) {
            insertColumns(after_col, 1);
            replaceColumnData(after_col + 1, f);
        }
    }

    /** add data as column or column block into matrix and expand
*/
    public void insertColumnData(int after_col, IntMatrix m) {
        if (this == m) return;
        int mcols = m.getCols();
        if (m.getRows() == rows && mcols > 0 && after_col >= 0 && after_col <= cols) {
            insertColumns(after_col, mcols);
            replaceColumnData(after_col + 1, m);
        }
    }

    /** make space for new rows in matrix and expand
*/
    public void insertRows(int after_row, int delta_rows) {
        if (after_row >= 0 && after_row <= cols && delta_rows > 0) {
            int newrows = delta_rows + rows;
            IntMatrix temp = new IntMatrix(newrows, cols);
            for (int jcol = 0; jcol < cols; jcol++) {
                for (int irow = 0; irow < after_row; irow++) {
                    temp.flmat[irow][jcol] = this.flmat[irow][jcol];
                }
                for (int irow = after_row; irow < rows; irow++) {
                    temp.flmat[irow + delta_rows][jcol] = this.flmat[irow][jcol];
                }
            }
            shallowCopy(temp);
        }
    }

    /** overwrite existing row of data
@exception UnequalMatricesException f.size() and cols differ
*/
    public void replaceRowData(int row, IntArray f) throws UnequalMatricesException {
        int mcols = f.size();
        if (mcols != cols) {
            throw new UnequalMatricesException();
        }
        System.arraycopy(f.getArray(), 0, flmat[row], 0, mcols);
    }

    /** and using a int[]
@exception UnequalMatricesException f.length and cols differ
*/
    public void replaceRowData(int row, int[] f) throws UnequalMatricesException {
        IntArray temp = new IntArray(cols, f);
        replaceRowData(row, temp);
    }

    /** overwrite existing block of rows; if too big, copying is truncated
@exception UnequalMatricesException m.rows and <TT>this.rows</TT> differ
*/
    public void replaceRowData(int after_row, IntMatrix m) throws UnequalMatricesException {
        if (this == m) return;
        if (cols != m.cols) {
            throw new UnequalMatricesException();
        }
        int irow = after_row + 1;
        int mcount = 0;
        while (irow < rows && mcount < m.rows) {
            System.arraycopy(flmat[irow], 0, m.flmat[mcount++], 0, cols);
        }
    }

    /** insert 2 or more adjacent rows of data into matrix and expand
@exception UnequalMatricesException m.cols and <TT>this.cols</TT>differ
*/
    public void insertRowData(int after_row, IntMatrix m) throws UnequalMatricesException {
        if (this == m) return;
        int mcols = m.getCols();
        int mrows = m.getRows();
        if (after_row >= 0 && after_row <= rows && mcols == cols) {
            insertRows(after_row, mrows);
            replaceRowData(after_row + 1, m);
        }
    }

    /** insert row of data into matrix and expand
@exception UnequalMatricesException f.size() and <TT>this.cols</TT> differ
*/
    public void insertRowData(int after_row, IntArray f) throws UnequalMatricesException {
        int mcols = f.size();
        if (after_row >= 0 && after_row <= rows && mcols == cols) {
            insertRows(after_row, 1);
            replaceRowData(after_row + 1, f);
        }
    }

    /** append data to matrix columnwise
@exception UnequalMatricesException f.size() and <TT>this.rows</TT> differ
*/
    public void appendColumnData(IntArray f) throws UnequalMatricesException {
        insertColumnData(cols, f);
    }

    /** append data to matrix columnwise
@exception UnequalMatricesException m.rows and <TT>this.rows</TT> differ
*/
    public void appendColumnData(IntMatrix m) throws UnequalMatricesException {
        insertColumnData(cols, m);
    }

    /** append data to matrix rowwise
@exception UnequalMatricesException m.cols and <TT>this.cols</TT> differ
*/
    public void appendRowData(IntArray f) throws UnequalMatricesException {
        insertRowData(rows, f);
    }

    /** append data to matrix rowwise
@exception UnequalMatricesException m.cols and <TT>this.cols</TT> differ
*/
    public void appendRowData(IntMatrix m) throws UnequalMatricesException {
        insertRowData(rows, m);
    }

    /** replaces the data starting at (low_row, low_col) and extending by the
 dimensions for the matrix m 
*/
    public void replaceSubMatrixData(int low_row, int low_col, IntMatrix m) {
        if (this == m) return;
        if (low_row > 0 && low_col > 0) {
            int mrows = m.getRows();
            int mcols = m.getCols();
            if (low_row + mrows - 1 < rows && low_col + mcols - 1 < cols) {
                for (int i = 0; i < mrows; i++) {
                    for (int j = 0; j < mcols; j++) {
                        flmat[i + low_row - 1][j] = m.flmat[i][j];
                    }
                }
            }
        }
    }

    /** reorder the columns of a matrix. 
@exception UnequalMatricesException is.size() and <TT>this.cols</TT> differ
@exception ArrayIndexOutOfBoundsException an element of <TT>is</TT> >= <TT>this.cols</TT>
*/
    public IntMatrix reorderColumnsBy(IntSet is) throws UnequalMatricesException, ArrayIndexOutOfBoundsException {
        if (is.size() != cols) {
            throw new UnequalMatricesException();
        }
        IntMatrix temp = new IntMatrix(rows, is.size());
        for (int i = 0; i < is.size(); i++) {
            int icol = is.elementAt(i);
            if (icol >= cols || icol < 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            IntArray coldat = this.extractColumnData(icol);
            temp.replaceColumnData(i, coldat);
        }
        return temp;
    }

    /** reorder the rows of a matrix. Deleting rows is allowed
@exception UnequalMatricesException is.size() and <TT>this.rows</TT> differ
@exception ArrayIndexOutOfBoundsException an element of <TT>is</TT> >= <TT>this.rows</TT>
*/
    public IntMatrix reorderRowsBy(IntSet is) throws UnequalMatricesException, ArrayIndexOutOfBoundsException {
        if (is.size() != rows) {
            throw new UnequalMatricesException();
        }
        IntMatrix temp = new IntMatrix(is.size(), cols);
        for (int i = 0; i < is.size(); i++) {
            int irow = is.elementAt(i);
            if (irow >= rows || irow < 0) {
                throw new ArrayIndexOutOfBoundsException("irow: " + irow);
            }
            IntArray rowdat = this.extractRowData(irow);
            temp.replaceRowData(i, rowdat);
        }
        return temp;
    }

    /** extract a IntMatrix submatrix from a IntMatrix
@exception BadArgumentException low/high_row/col are outside range of <TT>this</TT>
*/
    public IntMatrix extractSubMatrixData(int low_row, int high_row, int low_col, int high_col) throws BadArgumentException {
        return new IntMatrix(this, low_row, high_row, low_col, high_col);
    }

    /** produce a mask of those elements which fall in a range (1) else (0)
*/
    public IntMatrix elementsInRange(IntRange r) {
        IntMatrix m = new IntMatrix(rows, cols);
        for (int irow = 0; irow < rows; irow++) {
            for (int jcol = 0; jcol < cols; jcol++) {
                int elem = 0;
                if (r.includes(elementAt(irow, jcol))) {
                    elem = 1;
                }
                m.setElementAt(irow, jcol, elem);
            }
        }
        return m;
    }

    /** output matrix - very crude... 
*/
    public String toString() {
        StringBuffer sb = new StringBuffer("\n");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(" ");
                sb.append(flmat[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /** tests IntMatrix routines = new IntMatrix
*/
    public static void main(String args[]) {
        System.out.println("-----------Testing IntMatrix------------\n");
        int i, j;
        System.out.println("................................................\n");
        IntMatrix m0 = new IntMatrix();
        System.out.println("m0: " + m0 + "\n");
        System.out.println("................................................\n");
        IntMatrix m1 = new IntMatrix(3, 2);
        System.out.println("m1: " + m1 + "\n");
        System.out.println("................................................\n");
        int[][] temp = new int[4][5];
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 5; j++) {
                temp[i][j] = 10 * i + j;
            }
        }
        IntMatrix m3 = null;
        try {
            m3 = new IntMatrix(temp);
        } catch (NonRectMatrixException e) {
            Util.bug(e);
        }
        m1 = new IntMatrix(m3);
        System.out.println("m3: " + m3 + "\n");
        System.out.println("................................................\n");
        IntMatrix m4 = new IntMatrix(m3);
        System.out.println("m4: " + m4 + "\n");
        System.out.println("................................................\n");
        int f = m4.elementAt(3, 4);
        System.out.println(" f: " + f + "\n");
        System.out.println("................................................\n");
        int[][] tm = m4.getMatrix();
        System.out.println(": \n");
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 5; j++) {
                System.out.print(tm[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("................................................\n");
        System.out.println("................................................\n");
        IntMatrix m14 = new IntMatrix(m3);
        System.out.println("................................................\n");
        System.out.println("................................................\n");
        IntArray ff = new IntArray(4);
        ff.setElementAt(0, 81);
        ff.setElementAt(1, 82);
        ff.setElementAt(2, 83);
        ff.setElementAt(3, 84);
        m14.insertColumnData(2, ff);
        System.out.println("m14: " + m14 + "\n");
        System.out.println("................................................\n");
        IntArray ff1 = new IntArray(6);
        ff1.setElementAt(0, 71);
        ff1.setElementAt(1, 72);
        ff1.setElementAt(2, 73);
        ff1.setElementAt(3, 74);
        ff1.setElementAt(4, 75);
        ff1.setElementAt(5, 76);
        try {
            m14.insertRowData(2, ff1);
        } catch (Exception e) {
            System.out.println("m14.insertRowData(2, ff1) " + e);
        }
        System.out.println("................................................\n");
        System.out.println("m14: " + m14 + "\n");
        System.out.println("................................................\n");
        int tempx[] = { 17, 18, 19, 27, 28, 29, 37, 38, 39, 47, 48, 49, 57, 58, 59 };
        IntMatrix m15 = null;
        try {
            m15 = new IntMatrix(5, 3, tempx);
        } catch (BadArgumentException e) {
            Util.bug(e);
        }
        System.out.println("m15 " + m15);
        System.out.println("................................................\n");
        m14.insertColumnData(2, m15);
        System.out.println("m14: " + m14 + "\n");
        System.out.println("................................................\n");
        try {
            m14.appendColumnData(m15);
            System.out.println("m14: " + m14 + "\n");
            System.out.println("................................................\n");
            m14.appendRowData(m14);
        } catch (Exception e) {
            Util.bug(e);
        }
        System.out.println("m14: " + m14 + "\n");
        System.out.println("................................................\n");
        int tempx1[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 11, 12 };
        IntArray f3 = new IntArray(12, tempx1);
        System.out.println("................................................\n");
        try {
            m14.insertRowData(0, f3);
        } catch (Exception e) {
            Util.bug(e);
        }
        System.out.println("m14: " + m14 + "\n");
        System.out.println("................................................\n");
        IntMatrix m16 = new IntMatrix(3, 4);
        m14.replaceSubMatrixData(2, 3, m16);
        System.out.println("m14: " + m14 + "\n");
        System.out.println("................................................\n");
        IntMatrix m9 = null;
        try {
            m9 = new IntMatrix(m4, 2, 4, 2, 3);
        } catch (BadArgumentException e) {
            Util.bug(e);
        }
        System.out.println("m9: " + m9 + "\n");
        System.out.println("................................................\n");
        m4.setElementAt(3, 4, 67);
        m4.setElementAt(2, 3, 65);
        int rows = m4.getRows();
        int cols = m4.getCols();
        System.out.println("................................................\n");
        System.out.println(m4);
        System.out.println("................................................\n");
        f = m4.largestElement();
        System.out.println("f :" + f + "\n");
        System.out.println("................................................\n");
        Int2 ij = m4.indexOfLargestElement();
        System.out.println("ij:" + ij + "\n");
        System.out.println("................................................\n");
        f = m4.smallestElement();
        System.out.println("f :" + f + "\n");
        System.out.println("................................................\n");
        ij = m4.indexOfSmallestElement();
        System.out.println("ij:" + ij + "\n");
        System.out.println("................................................\n");
        f = m4.smallestElementInRow(2);
        System.out.println("f :" + f + "\n");
        System.out.println("................................................\n");
        i = m4.indexOfSmallestElementInRow(2);
        System.out.println("i :" + i + "\n");
        System.out.println("................................................\n");
        f = m4.largestElementInRow(2);
        System.out.println("f :" + f + "\n");
        System.out.println("................................................\n");
        i = m4.indexOfLargestElementInRow(2);
        System.out.println("i :" + i + "\n");
        System.out.println("................................................\n");
        f = m4.smallestElementInColumn(2);
        System.out.println("f :" + f + "\n");
        System.out.println("................................................\n");
        i = m4.indexOfSmallestElementInColumn(2);
        System.out.println("i :" + i + "\n");
        System.out.println("................................................\n");
        f = m4.largestElementInColumn(2);
        System.out.println("f :" + f + "\n");
        System.out.println("................................................\n");
        i = m4.indexOfLargestElementInColumn(2);
        System.out.println("i :" + i + "\n");
        System.out.println("................................................\n");
        IntArray r2 = new IntArray(m4.extractRowData(2));
        System.out.println("r2:\n" + r2 + "\n");
        System.out.println("................................................\n");
        IntArray c3 = new IntArray(m4.extractColumnData(2));
        System.out.println("c3:\n" + c3 + "\n");
        System.out.println("................................................\n");
        int ia[] = { 3, 5, 1, 2 };
        IntSet is = null;
        try {
            is = new IntSet(ia);
        } catch (Exception e) {
            Util.bug(e);
        }
        System.out.println("is:\n" + is + "\n");
        System.out.println("................................................\n");
        IntMatrix f8 = null;
        try {
            f8 = new IntMatrix(m4.reorderColumnsBy(is));
        } catch (Exception e) {
            Util.bug(e);
        }
        System.out.println("f8:\n" + f8 + "\n");
        System.out.println("................................................\n");
        int ib[] = { 3, 0, 1, 2 };
        IntSet it = null;
        try {
            it = new IntSet(ib);
        } catch (Exception e) {
            Util.bug(e);
        }
        System.out.println("it:\n" + it + "\n");
        System.out.println("................................................\n");
        try {
            f8 = m4.reorderRowsBy(it);
        } catch (Exception e) {
            Util.bug(e);
        }
        System.out.println("f8:\n" + f8 + "\n");
        System.out.println("................................................\n");
        f = m4.elementAt(3, 4);
        System.out.println(" f: " + f + "\n");
        System.out.println("................................................\n");
        boolean b = false;
        try {
            b = (m3.equals(m4));
        } catch (Exception e) {
            Util.bug(e);
        }
        System.out.println(" b: " + b + "\n");
        System.out.println("................................................\n");
        m3.negative();
        System.out.println("m3: " + m3 + "\n");
        System.out.println("................................................\n");
        m3.multiplyBy(-13);
        System.out.println("m3: " + m3 + "\n");
        System.out.println("................................................\n");
        IntMatrix m8 = null;
        try {
            m8 = m3.plus(m4);
        } catch (Exception e) {
            Util.bug(e);
        }
        System.out.println("m8: " + m8 + "\n");
        System.out.println("................................................\n");
        try {
            m3 = m3.plus(m4);
        } catch (Exception e) {
            Util.bug(e);
        }
        System.out.println("m3: " + m3 + "\n");
        System.out.println("................................................\n");
        try {
            m3 = m3.subtract(m4);
        } catch (Exception e) {
            Util.bug(e);
        }
        System.out.println("m3: " + m3 + "\n");
        System.out.println("................................................\n");
        m3.deleteColumn(2);
        System.out.println("m3: " + m3 + "\n");
        System.out.println("................................................\n");
        m3 = m4;
        System.out.println("m3: " + m3 + "\n");
        System.out.println("................................................\n");
        m3.deleteColumns(2, 3);
        System.out.println("m3: " + m3 + "\n");
        System.out.println("................................................\n");
        m3 = m4;
        System.out.println("m3: " + m3 + "\n");
        System.out.println("................................................\n");
        m3.deleteRows(2, 3);
        System.out.println("m3: " + m3 + "\n");
        System.out.println("................................................\n");
        m4 = m4.getTranspose();
        IntMatrix m2 = new IntMatrix(m4);
        System.out.println("m4: " + m4 + "\n");
        System.out.println("................................................\n");
        b = m2.isSquare();
        System.out.println(" b: " + b + "\n");
        System.out.println("................................................\n");
        m4.clearMatrix();
        System.out.println("m4: " + m4 + "\n");
        System.out.println("................................................\n");
        m4.setAllElements(23);
        System.out.println("m4: " + m4 + "\n");
        System.out.println("................................................\n");
        int vct[] = { 3, 2, 1 };
        IntArray vc = new IntArray(3, vct);
        System.out.println("vc:\n" + vc + "\n");
        System.out.println("................................................\n");
        IntArray vx = null;
        try {
            vx = m1.multiply(vc);
        } catch (UnequalMatricesException e) {
            Util.bug(e);
        }
        System.out.println("vx:\n" + vx + "\n");
        System.out.println("................................................\n");
        IntMatrix m7 = null;
        try {
            m7 = m1.multiply(m4);
        } catch (UnequalMatricesException e) {
            Util.bug(e);
        }
        System.out.println("m7: " + m7 + "\n");
        System.out.println("................................................\n");
    }
}
