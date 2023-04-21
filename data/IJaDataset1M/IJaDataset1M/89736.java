package jopt.mp.spi;

public class Presolver {

    private MatrixA a;

    private double feasibilityPrecision;

    /**
	 * Creates a new presolver
	 * 
	 * @param a						Matrix presolver operates upon
	 * @param feasibilityPrecision	amount by which a variable may violate its bounds and still be considered feasible
	 */
    public Presolver(MatrixA a, double feasibilityPrecision) {
        this.a = a;
        this.feasibilityPrecision = feasibilityPrecision;
    }

    /**
	 * Returns amount by which a variable may violate its bounds and still be considered feasible
	 */
    public double getFeasibilityPrecision() {
        return feasibilityPrecision;
    }

    /**
	 * Sets amount by which a variable may violate its bounds and still be considered feasible
	 */
    public void setFeasibilityPrecision(double feasibilityPrecision) {
        this.feasibilityPrecision = feasibilityPrecision;
    }

    public void checkBoundsAndRowRedundancy(double reducedLower[], double reducedUpper[], boolean rowRedundant[]) throws MatrixInvalidException, InfeasibleException, UnboundedException {
        if (reducedLower != null && reducedLower.length != a.getColCount()) throw new IllegalArgumentException("reduced lower bound array must be same length as column count in matrix A");
        if (reducedUpper != null && reducedUpper.length != a.getColCount()) throw new IllegalArgumentException("reduced upper bound array must be same length as column count in matrix A");
        if (rowRedundant != null && rowRedundant.length != a.getRowCount()) throw new IllegalArgumentException("row status array must be same length as row count in matrix A");
        if (a.getRowCount() == 0) throw new MatrixInvalidException("Matrix A has no rows");
        if (a.getColCount() == 0) throw new MatrixInvalidException("Matrix A has no columns");
        for (int i = 0; i < a.getColCount(); i++) {
            int colNzeros = a.getColNzeroCount(i);
            if (colNzeros == 0) {
                double obj = a.getObjective(i);
                if (DoubleUtil.isEqual(obj, 0, feasibilityPrecision)) {
                    double val = a.getLowerBound(i);
                    reducedLower[i] = val;
                    reducedUpper[i] = val;
                } else if (obj > 0) {
                    double val = a.getLowerBound(i);
                    reducedLower[i] = val;
                    reducedUpper[i] = val;
                    if (val == -DoubleUtil.INFINITY) throw new UnboundedException("column " + i + " is unbounded");
                } else {
                    double val = a.getUpperBound(i);
                    reducedLower[i] = val;
                    reducedUpper[i] = val;
                    if (val == DoubleUtil.INFINITY) throw new UnboundedException("column " + i + " is unbounded");
                }
            } else {
                reducedLower[i] = a.getLowerBound(i);
                reducedUpper[i] = a.getUpperBound(i);
            }
        }
        int nzeroStart[] = new int[] { 0 };
        int nzeroCols[] = new int[a.getColCount()];
        double nzeroVals[] = new double[a.getColCount()];
        int totalSize[] = new int[a.getColCount()];
        for (int i = 0; i < a.getRowCount(); i++) {
            int rowNzeros = a.getRowNzeroCount(i);
            if (rowNzeros == 0) throw new MatrixInvalidException("row " + i + " has no non-zero values");
            if (rowNzeros == 1) {
                int col = nzeroCols[0];
                double lower = reducedLower[col];
                double upper = reducedUpper[col];
                a.getRowNzeros(i, i, nzeroStart, nzeroCols, nzeroVals, totalSize);
                double rhs = a.getRHS(i);
                double a_ij = nzeroVals[0];
                double newBound = rhs / nzeroVals[0];
                double newLower = lower;
                double newUpper = upper;
                int cmp = DoubleUtil.compare(a_ij, rhs, feasibilityPrecision);
                if (cmp <= 0) {
                    if (a_ij > 0) newUpper = newBound; else newLower = newBound;
                }
                if (cmp >= 0) {
                    if (a_ij > 0) newLower = newBound; else newUpper = newBound;
                }
                if (DoubleUtil.compare(newLower, lower, feasibilityPrecision) < 0 || DoubleUtil.compare(newUpper, upper, feasibilityPrecision) > 0) throw new InfeasibleException("column " + col + " has infeasible bounds");
                reducedLower[col] = newLower;
                reducedUpper[col] = newUpper;
                rowRedundant[i] = true;
            }
            double rhs = a.getRHS(i);
            double smallestB = 0;
            double largestB = 0;
            for (int j = 0; j < rowNzeros; j++) {
                int col = nzeroCols[j];
                double a_ij = nzeroVals[j];
                if (a_ij > 0) {
                    smallestB += a_ij * reducedLower[col];
                    largestB += a_ij * reducedUpper[col];
                } else {
                    smallestB += a_ij * reducedUpper[col];
                    largestB += a_ij * reducedLower[col];
                }
            }
            if (smallestB > DoubleUtil.INFINITY) smallestB = DoubleUtil.INFINITY; else if (smallestB < -DoubleUtil.INFINITY) smallestB = -DoubleUtil.INFINITY;
            if (largestB > DoubleUtil.INFINITY) largestB = DoubleUtil.INFINITY; else if (largestB < -DoubleUtil.INFINITY) largestB = -DoubleUtil.INFINITY;
            char rowType = a.getRowType(i);
            if (rowType == MatrixA.ROW_TYPE_GEQ) {
                rhs = -rhs;
                double tmp = smallestB;
                smallestB = -largestB;
                largestB = -tmp;
            }
            int cmp = DoubleUtil.compare(rhs, smallestB, feasibilityPrecision);
            if (cmp < 0) throw new InfeasibleException("row " + i + " is infeasible");
            if (cmp == 0) {
                for (int j = 0; j < rowNzeros; j++) {
                    int col = nzeroCols[j];
                    double a_ij = nzeroVals[j];
                    if (a_ij > 0) reducedUpper[col] = reducedLower[col]; else reducedLower[col] = reducedUpper[col];
                }
                rowRedundant[i] = true;
            } else if (largestB > smallestB && DoubleUtil.compare(rhs, largestB, feasibilityPrecision) >= 0) {
                rowRedundant[i] = true;
            }
            int infColCount = 0;
            int infCol = -1;
            double infColVal = 0d;
            for (int j = 0; j < rowNzeros; j++) {
                int col = nzeroCols[j];
                boolean infLower = reducedLower[col] == -DoubleUtil.INFINITY;
                boolean infUpper = reducedUpper[col] == DoubleUtil.INFINITY;
                if (infLower || infUpper) {
                    infColCount++;
                    if (infColCount > 1) break;
                    double a_ij = nzeroVals[j];
                    if ((infLower && a_ij > 0) || (infUpper && a_ij < 0)) {
                        infCol = col;
                        infColVal = a_ij;
                    }
                }
            }
            if (infColCount == 0 && smallestB > -DoubleUtil.INFINITY && smallestB < DoubleUtil.INFINITY) {
                double rhsDelta = rhs - smallestB;
                for (int j = 0; j < rowNzeros; j++) {
                    int col = nzeroCols[j];
                    double a_ij = nzeroVals[j];
                    if (a_ij > 0) {
                        double lower = reducedLower[col];
                        double upper = reducedUpper[col];
                        double newBound = lower + (rhsDelta / a_ij);
                        if (newBound < upper) {
                            if (newBound < lower) throw new InfeasibleException("column " + infCol + " has infeasible bounds");
                            reducedUpper[infCol] = newBound;
                        }
                    } else {
                        double lower = reducedLower[col];
                        double upper = reducedUpper[col];
                        double newBound = upper + (rhsDelta / a_ij);
                        if (newBound > lower) {
                            if (newBound > upper) throw new InfeasibleException("column " + infCol + " has infeasible bounds");
                            reducedLower[infCol] = newBound;
                        }
                    }
                }
            } else if (infColCount == 1 && infCol >= 0) {
                double newBound = rhs;
                for (int j = 0; j < rowNzeros; j++) {
                    int col = nzeroCols[j];
                    if (j != infCol) {
                        double a_ij = nzeroVals[j];
                        if (a_ij > 0) newBound -= a_ij * reducedLower[col]; else newBound -= a_ij * reducedUpper[col];
                    }
                }
                newBound /= infColVal;
                if (newBound > -DoubleUtil.INFINITY && newBound < DoubleUtil.INFINITY) {
                    if (infColVal > 0) {
                        double lower = reducedLower[infCol];
                        if (newBound < lower) throw new InfeasibleException("column " + infCol + " has infeasible bounds");
                        reducedUpper[infCol] = newBound;
                    } else {
                        double upper = reducedUpper[infCol];
                        if (newBound > upper) throw new InfeasibleException("column " + infCol + " has infeasible bounds");
                        reducedLower[infCol] = newBound;
                    }
                }
            }
        }
    }
}
