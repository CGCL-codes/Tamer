package reconcile.assignment;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * An implementation of the classic hungarian algorithm for the assignment problem.
 * 
 * Copyright 2007 Gary Baker (GPL v3)
 * 
 * @author gbaker
 */
public class HungarianAlgorithm implements AssignmentAlgorithm {

    public int[][] computeAssignments(double[][] matrix) {
        reduceMatrix(matrix);
        int[] starsByRow = new int[matrix.length];
        Arrays.fill(starsByRow, -1);
        int[] starsByCol = new int[matrix[0].length];
        Arrays.fill(starsByCol, -1);
        int[] primesByRow = new int[matrix.length];
        Arrays.fill(primesByRow, -1);
        int[] coveredRows = new int[matrix.length];
        int[] coveredCols = new int[matrix[0].length];
        initStars(matrix, starsByRow, starsByCol);
        coverColumnsOfStarredZeroes(starsByCol, coveredCols);
        while (!allAreCovered(coveredCols) && !allAreCovered(coveredRows)) {
            int[] primedZero = primeSomeUncoveredZero(matrix, primesByRow, coveredRows, coveredCols);
            while (primedZero == null) {
                makeMoreZeroes(matrix, coveredRows, coveredCols);
                primedZero = primeSomeUncoveredZero(matrix, primesByRow, coveredRows, coveredCols);
            }
            int columnIndex = starsByRow[primedZero[0]];
            if (-1 == columnIndex) {
                incrementSetOfStarredZeroes(primedZero, starsByRow, starsByCol, primesByRow);
                Arrays.fill(primesByRow, -1);
                Arrays.fill(coveredRows, 0);
                Arrays.fill(coveredCols, 0);
                coverColumnsOfStarredZeroes(starsByCol, coveredCols);
            } else {
                coveredRows[primedZero[0]] = 1;
                coveredCols[columnIndex] = 0;
            }
        }
        int[][] retval = new int[matrix[0].length][];
        for (int i = 0; i < starsByCol.length; i++) {
            retval[i] = new int[] { starsByCol[i], i };
        }
        return retval;
    }

    private boolean allAreCovered(int[] coveredCols) {
        for (int covered : coveredCols) {
            if (0 == covered) return false;
        }
        return true;
    }

    /**
 * the first step of the hungarian algorithm is to find the smallest element in each row and subtract it's values from
 * all elements in that row
 * 
 * @return the next step to perform
 */
    private void reduceMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            double minValInRow = Double.MAX_VALUE;
            for (int j = 0; j < matrix[i].length; j++) {
                if (minValInRow > matrix[i][j]) {
                    minValInRow = matrix[i][j];
                }
            }
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] -= minValInRow;
            }
        }
        for (int i = 0; i < matrix[0].length; i++) {
            double minValInCol = Double.MAX_VALUE;
            for (double[] element : matrix) {
                if (minValInCol > element[i]) {
                    minValInCol = element[i];
                }
            }
            for (int j = 0; j < matrix.length; j++) {
                matrix[j][i] -= minValInCol;
            }
        }
    }

    /**
 * init starred zeroes
 * 
 * for each column find the first zero if there is no other starred zero in that row then star the zero, cover the
 * column and row and go onto the next column
 * 
 * @param costMatrix
 * @param starredZeroes
 * @param coveredRows
 * @param coveredCols
 * @return the next step to perform
 */
    private void initStars(double costMatrix[][], int[] starsByRow, int[] starsByCol) {
        int[] rowHasStarredZero = new int[costMatrix.length];
        int[] colHasStarredZero = new int[costMatrix[0].length];
        for (int i = 0; i < costMatrix.length; i++) {
            for (int j = 0; j < costMatrix[i].length; j++) {
                if (0 == costMatrix[i][j] && 0 == rowHasStarredZero[i] && 0 == colHasStarredZero[j]) {
                    starsByRow[i] = j;
                    starsByCol[j] = i;
                    rowHasStarredZero[i] = 1;
                    colHasStarredZero[j] = 1;
                    break;
                }
            }
        }
    }

    /**
 * just marke the columns covered for any coluimn containing a starred zero
 * 
 * @param starsByCol
 * @param coveredCols
 */
    private void coverColumnsOfStarredZeroes(int[] starsByCol, int[] coveredCols) {
        for (int i = 0; i < starsByCol.length; i++) {
            coveredCols[i] = -1 == starsByCol[i] ? 0 : 1;
        }
    }

    /**
 * finds some uncovered zero and primes it
 * 
 * @param matrix
 * @param primesByRow
 * @param coveredRows
 * @param coveredCols
 * @return
 */
    private int[] primeSomeUncoveredZero(double matrix[][], int[] primesByRow, int[] coveredRows, int[] coveredCols) {
        for (int i = 0; i < matrix.length; i++) {
            if (1 == coveredRows[i]) {
                continue;
            }
            for (int j = 0; j < matrix[i].length; j++) {
                if (0 == matrix[i][j] && 0 == coveredCols[j]) {
                    primesByRow[i] = j;
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }

    /**
 * 
 * @param unpairedZeroPrime
 * @param starsByRow
 * @param starsByCol
 * @param primesByRow
 */
    private void incrementSetOfStarredZeroes(int[] unpairedZeroPrime, int[] starsByRow, int[] starsByCol, int[] primesByRow) {
        int i, j = unpairedZeroPrime[1];
        Set<int[]> zeroSequence = new LinkedHashSet<int[]>();
        zeroSequence.add(unpairedZeroPrime);
        boolean paired = false;
        do {
            i = starsByCol[j];
            paired = -1 != i && zeroSequence.add(new int[] { i, j });
            if (!paired) {
                break;
            }
            j = primesByRow[i];
            paired = -1 != j && zeroSequence.add(new int[] { i, j });
        } while (paired);
        for (int[] zero : zeroSequence) {
            if (starsByCol[zero[1]] == zero[0]) {
                starsByCol[zero[1]] = -1;
                starsByRow[zero[0]] = -1;
            }
            if (primesByRow[zero[0]] == zero[1]) {
                starsByRow[zero[0]] = zero[1];
                starsByCol[zero[1]] = zero[0];
            }
        }
    }

    private void makeMoreZeroes(double[][] matrix, int[] coveredRows, int[] coveredCols) {
        double minUncoveredValue = Double.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            if (0 == coveredRows[i]) {
                for (int j = 0; j < matrix[i].length; j++) {
                    if (0 == coveredCols[j] && matrix[i][j] < minUncoveredValue) {
                        minUncoveredValue = matrix[i][j];
                    }
                }
            }
        }
        for (int i = 0; i < coveredRows.length; i++) {
            if (1 == coveredRows[i]) {
                for (int j = 0; j < matrix[i].length; j++) {
                    matrix[i][j] += minUncoveredValue;
                }
            }
        }
        for (int i = 0; i < coveredCols.length; i++) {
            if (0 == coveredCols[i]) {
                for (int j = 0; j < matrix.length; j++) {
                    matrix[j][i] -= minUncoveredValue;
                }
            }
        }
    }
}
