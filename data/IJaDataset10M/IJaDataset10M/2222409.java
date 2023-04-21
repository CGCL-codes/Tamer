package coho.common.matrix;

import coho.common.number.*;

public class DoubleMatrix extends BasicMatrix<CohoDouble> {

    protected DoubleMatrix(CohoDouble[][] data, int nrows, int ncols) {
        super(CohoDouble.type, nrows, ncols, data);
    }

    public static DoubleMatrix typeCast(BasicMatrix<CohoDouble> m) {
        return new DoubleMatrix(m.data, m.nrows, m.ncols);
    }

    public DoubleMatrix(int nrows, int ncols) {
        super(CohoDouble.zero, nrows, ncols);
    }

    public DoubleMatrix(int[] size) {
        this(size[0], size[1]);
    }

    public DoubleMatrix(Matrix m) {
        this(m.nrows(), m.ncols());
        super.assign(m);
    }

    public DoubleMatrix(CohoNumber[][] data) {
        this(data.length, data[0].length);
        super.assign(data);
    }

    public DoubleMatrix(CohoNumber[] data) {
        this(data.length, 1);
        super.assign(data);
    }

    public DoubleMatrix(Number[][] data) {
        this(data.length, data[0].length);
        super.assign(data);
    }

    public DoubleMatrix(double[][] data) {
        this(data.length, data[0].length);
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                super.assign(data[i][j], i, j);
            }
        }
    }

    public DoubleMatrix(Number[] data) {
        this(data.length, 1);
        super.assign(data);
    }

    public DoubleMatrix(double[] data) {
        this(data.length, 1);
        for (int i = 0; i < length(); i++) {
            super.assign(data[i], i);
        }
    }

    public static DoubleMatrix create(int nrows, int ncols) {
        return new DoubleMatrix(nrows, ncols);
    }

    public static DoubleMatrix create(int[] size) {
        return new DoubleMatrix(size);
    }

    public static DoubleMatrix create(Matrix m) {
        if (m instanceof DoubleMatrix) return (DoubleMatrix) m;
        return new DoubleMatrix(m);
    }

    public static DoubleMatrix create(CohoNumber[][] data) {
        return new DoubleMatrix(data);
    }

    public static DoubleMatrix create(CohoNumber[] data) {
        return new DoubleMatrix(data);
    }

    public static DoubleMatrix create(Number[][] data) {
        return new DoubleMatrix(data);
    }

    public static DoubleMatrix create(Number[] data) {
        return new DoubleMatrix(data);
    }

    public static DoubleMatrix create(double[][] data) {
        return new DoubleMatrix(data);
    }

    public static DoubleMatrix create(double[] data) {
        return new DoubleMatrix(data);
    }

    public DoubleMatrix convert(double[][] data) {
        return new DoubleMatrix(data);
    }

    public DoubleMatrix convert(double[] data) {
        return new DoubleMatrix(data);
    }

    @Override
    public DoubleMatrix convert() {
        return new DoubleMatrix(nrows, ncols);
    }

    @Override
    public DoubleMatrix convert(int nrows, int ncols) {
        return new DoubleMatrix(nrows, ncols);
    }

    @Override
    public DoubleMatrix convert(int[] size) {
        return new DoubleMatrix(size);
    }

    @Override
    public DoubleMatrix convert(Matrix m) {
        if (m instanceof DoubleMatrix) return (DoubleMatrix) m;
        return new DoubleMatrix(m);
    }

    @Override
    public DoubleMatrix convert(CohoNumber[][] data) {
        return new DoubleMatrix(data);
    }

    @Override
    public DoubleMatrix convert(CohoNumber[] data) {
        return new DoubleMatrix(data);
    }

    @Override
    public DoubleMatrix convert(Number[][] data) {
        return new DoubleMatrix(data);
    }

    @Override
    public DoubleMatrix convert(Number[] data) {
        return new DoubleMatrix(data);
    }

    @Override
    public ScaleType elementType() {
        return CohoDouble.type;
    }

    @Override
    public DoubleMatrix assign(CohoNumber v, int row, int col) {
        super.assign(v, row, col);
        return this;
    }

    @Override
    public DoubleMatrix assign(Number v, int row, int col) {
        super.assign(v, row, col);
        return this;
    }

    @Override
    public DoubleMatrix assign(CohoNumber v, int n) {
        super.assign(v, n);
        return this;
    }

    @Override
    public DoubleMatrix assign(Number v, int n) {
        super.assign(v, n);
        return this;
    }

    @Override
    public DoubleMatrix assign(Matrix m) {
        super.assign(m);
        return this;
    }

    @Override
    public DoubleMatrix assign(CohoNumber[][] v) {
        super.assign(v);
        return this;
    }

    @Override
    public DoubleMatrix assign(Number[][] v) {
        super.assign(v);
        return this;
    }

    @Override
    public DoubleMatrix assign(CohoNumber[] v) {
        super.assign(v);
        return this;
    }

    @Override
    public DoubleMatrix assign(Number[] v) {
        super.assign(v);
        return this;
    }

    @Override
    public DoubleMatrix assign(Matrix m, int v_row, int v_col) {
        super.assign(m, v_row, v_col);
        return this;
    }

    @Override
    public DoubleMatrix assign(Matrix m, int n) {
        super.assign(m, n);
        return this;
    }

    @Override
    public DoubleMatrix assign(Matrix m, BooleanMatrix mask) {
        super.assign(m, mask);
        return this;
    }

    @Override
    public DoubleMatrix assign(Matrix m, IntegerMatrix pos) {
        super.assign(m, pos);
        return this;
    }

    @Override
    public DoubleMatrix V(Range row, Range col) {
        return DoubleMatrix.typeCast(super.V(row, col));
    }

    @Override
    public DoubleMatrix V(Range n) {
        return DoubleMatrix.typeCast(super.V(n));
    }

    @Override
    public DoubleMatrix V(Range row, int col) {
        return DoubleMatrix.typeCast(super.V(row, col));
    }

    @Override
    public DoubleMatrix V(int row, Range col) {
        return DoubleMatrix.typeCast(super.V(row, col));
    }

    @Override
    public DoubleMatrix V(IntegerMatrix pos) {
        return DoubleMatrix.typeCast(super.V(pos));
    }

    @Override
    public DoubleMatrix row(int row) {
        return DoubleMatrix.typeCast(super.row(row));
    }

    @Override
    public DoubleMatrix row(Range row) {
        return DoubleMatrix.typeCast(super.row(row));
    }

    @Override
    public DoubleMatrix row(IntegerMatrix pos) {
        return DoubleMatrix.typeCast(super.row(pos));
    }

    @Override
    public DoubleMatrix row(BooleanMatrix pos) {
        return DoubleMatrix.typeCast(super.row(pos));
    }

    @Override
    public DoubleMatrix col(int col) {
        return DoubleMatrix.typeCast(super.col(col));
    }

    @Override
    public DoubleMatrix col(Range col) {
        return DoubleMatrix.typeCast(super.col(col));
    }

    @Override
    public DoubleMatrix col(IntegerMatrix pos) {
        return DoubleMatrix.typeCast(super.col(pos));
    }

    @Override
    public DoubleMatrix col(BooleanMatrix pos) {
        return DoubleMatrix.typeCast(super.col(pos));
    }

    @Override
    public DoubleMatrix ones() {
        return DoubleMatrix.typeCast(super.ones());
    }

    @Override
    public DoubleMatrix zeros() {
        return DoubleMatrix.typeCast(super.zeros());
    }

    @Override
    public DoubleMatrix randoms() {
        return DoubleMatrix.typeCast(super.randoms());
    }

    @Override
    public DoubleMatrix ident() {
        return DoubleMatrix.typeCast(super.ident());
    }

    @Override
    public DoubleMatrix ident(int n) {
        return DoubleMatrix.typeCast(super.ident(n));
    }

    @Override
    public DoubleMatrix diag() {
        return DoubleMatrix.typeCast(super.diag());
    }

    @Override
    public DoubleMatrix fill(CohoNumber v) {
        return DoubleMatrix.typeCast(super.fill(v));
    }

    @Override
    public DoubleMatrix fill(Number v) {
        return DoubleMatrix.typeCast(super.fill(v));
    }

    @Override
    public DoubleMatrix abs() {
        return DoubleMatrix.typeCast(super.abs());
    }

    @Override
    public DoubleMatrix negate() {
        return DoubleMatrix.typeCast(super.negate());
    }

    @Override
    public DoubleMatrix transpose() {
        return DoubleMatrix.typeCast(super.transpose());
    }

    @Override
    public DoubleMatrix inv() throws SingularMatrixException {
        return DoubleMatrix.typeCast(super.inv());
    }

    public DoubleMatrix add(CohoDouble x) {
        return DoubleMatrix.typeCast(super.add(x));
    }

    public DoubleMatrix add(Number x) {
        return DoubleMatrix.typeCast(super.add(x));
    }

    public DoubleMatrix sub(CohoDouble x) {
        return DoubleMatrix.typeCast(super.sub(x));
    }

    public DoubleMatrix sub(Number x) {
        return DoubleMatrix.typeCast(super.sub(x));
    }

    public DoubleMatrix mult(CohoDouble x) {
        return DoubleMatrix.typeCast(super.mult(x));
    }

    public DoubleMatrix mult(Number x) {
        return DoubleMatrix.typeCast(super.mult(x));
    }

    public DoubleMatrix div(CohoDouble x) {
        return DoubleMatrix.typeCast(super.div(x));
    }

    public DoubleMatrix div(Number x) {
        return DoubleMatrix.typeCast(super.div(x));
    }

    public DoubleMatrix add(DoubleMatrix that) {
        return DoubleMatrix.typeCast(super.add(that));
    }

    public DoubleMatrix sub(DoubleMatrix that) {
        return DoubleMatrix.typeCast(super.sub(that));
    }

    public DoubleMatrix mult(DoubleMatrix that) {
        return DoubleMatrix.typeCast(super.mult(that));
    }

    public DoubleMatrix div(DoubleMatrix that) throws SingularMatrixException {
        return DoubleMatrix.typeCast(super.div(that));
    }

    public DoubleMatrix elMult(DoubleMatrix that) {
        return DoubleMatrix.typeCast(super.elMult(that));
    }

    public DoubleMatrix elDiv(DoubleMatrix that) {
        return DoubleMatrix.typeCast(super.elDiv(that));
    }

    public DoubleMatrix leftDiv(DoubleMatrix that) throws SingularMatrixException {
        return DoubleMatrix.typeCast(super.leftDiv(that));
    }

    public static void main(String[] args) {
        double[][] d = { { 1, 2 }, { 2, 3 } };
        DoubleMatrix a = new DoubleMatrix(d);
        BasicMatrix b = BasicMatrix.create(CohoAPR.one, 2, 2);
        b = a.elMult(b);
        System.out.println(new DoubleMatrix(2, 2));
    }
}
