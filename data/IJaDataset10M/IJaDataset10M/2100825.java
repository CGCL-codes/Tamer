package utils.methods.convex;

import utils.Methods;
import utils.exceptions.OptimizingException;
import utils.methods.Method;
import utils.portfolio.Portfolio;
import Jama.LUDecomposition;
import Jama.Matrix;

public class KunTakker extends Method {

    public KunTakker(Portfolio challenge, double epsilon) {
        super(challenge, epsilon);
        x = new double[challenge.getSize()];
        xLength = x.length;
        for (int i = 0; i < xLength; i++) {
            x[i] = 0.0;
        }
    }

    public KunTakker(Portfolio challenge, double expectedProfit, double epsilon) {
        super(challenge, expectedProfit, epsilon);
        x = new double[challenge.getSize()];
        xLength = x.length;
        for (int i = 0; i < xLength; i++) {
            x[i] = 0.0;
        }
    }

    @Override
    public void evaluate() throws OptimizingException {
        startTime();
        int number = tryFindSimpleSolution();
        x = new double[xLength];
        x[number] = 1.0;
        double minimumRisk = getRisk();
        if (profit[number] < expectedProfit) throw new OptimizingException("unreacheble expected profit", Methods.METOD_KUNTAKKER);
        int status = -1;
        double temp;
        double[] tempArr = new double[xLength];
        for (int i = 0; i < Math.pow(2, xLength + 1); i++) {
            operations++;
            try {
                temp = step(i);
                if (temp >= 0) if (minimumRisk >= temp) {
                    minimumRisk = temp;
                    status = i;
                    for (int j = 0; j < xLength; j++) tempArr[j] = x[j];
                }
                for (int j = 0; j < x.length; j++) {
                    System.out.println("X" + j + " = " + x[j]);
                }
                System.out.println("Final Risk = " + getRisk());
            } catch (OptimizingException e) {
            }
        }
        if (status >= 0) {
            x = tempArr;
        } else {
            x = new double[xLength];
            x[number] = 1.0;
        }
        endTime();
    }

    private String makePostfix(int count) {
        String out = "";
        for (int i = 0; i < count; i++) out += "0";
        return out;
    }

    private double step(int number) throws OptimizingException {
        String code = Integer.toBinaryString(number);
        if (code.length() < xLength + 1) {
            operations++;
            code = makePostfix(xLength + 1 - code.length()) + code;
        }
        double[][] matrixArray = new double[xLength + 1][xLength + 1];
        double[][] matrixFree = new double[xLength + 1][1];
        for (int i = 0; i < code.length(); i++) {
            operations++;
            if (code.charAt(i) == '0') {
                matrixArray[i][i] = 1.0;
                matrixFree[i][0] = 0.0;
            } else {
                if (i < xLength - 1) {
                    for (int j = 0; j < xLength - 1; j++) {
                        if (i != j) {
                            operations += 3;
                            matrixArray[i][j] = 2 * (covariances[i][j] - covariances[i][xLength - 1] - covariances[j][xLength - 1]);
                        } else {
                            operations += 3;
                            matrixArray[i][i] = 2 * covariances[i][i] - 4 * covariances[i][xLength - 1];
                        }
                    }
                    operations++;
                    matrixArray[i][xLength - 1] = (profit[xLength - 1] - profit[i]);
                    matrixArray[i][xLength] = 1.0;
                    operations++;
                    matrixFree[i][0] = -2 * covariances[i][xLength - 1];
                } else if (i == xLength - 1) {
                    for (int j = 0; j < xLength - 1; j++) {
                        operations++;
                        matrixArray[i][j] = (profit[xLength - 1] - profit[j]);
                    }
                    matrixFree[i][0] = profit[xLength - 1] - expectedProfit;
                } else {
                    for (int j = 0; j < xLength - 1; j++) {
                        operations++;
                        matrixArray[i][j] = 1.0;
                    }
                    matrixFree[i][0] = 1.0;
                }
            }
        }
        Matrix matrix = new Matrix(matrixArray);
        LUDecomposition dec = new LUDecomposition(matrix);
        Matrix matrixFreeKoeff = new Matrix(matrixFree);
        Matrix solution = null;
        try {
            operations += Math.pow(xLength, 2);
            solution = dec.solve(matrixFreeKoeff);
        } catch (Exception e) {
            throw new OptimizingException("no variant", Methods.METOD_KUNTAKKER);
        }
        double extra = 0.0;
        for (int i = 0; i < xLength - 1; i++) {
            operations++;
            x[i] = solution.get(i, 0);
            extra += x[i];
        }
        x[xLength - 1] = 1 - extra;
        double l1 = solution.get(xLength - 1, 0);
        double l2 = solution.get(xLength, 0);
        if (!checkFormulas(l1, l2)) throw new OptimizingException("can't find linear solution", Methods.METOD_KUNTAKKER);
        return getRisk();
    }

    private int tryFindSimpleSolution() {
        double temp = -1.0;
        int out = -1;
        for (int i = 0; i < xLength; i++) {
            operations++;
            if (profit[i] > temp) {
                temp = profit[i];
                out = i;
            }
        }
        return out;
    }

    private boolean checkFormulas(double l1, double l2) {
        for (int i = 0; i < xLength; i++) if (x[i] < 0) return false;
        operations += xLength + 2;
        if ((l1 < 0) || (l2 < 0)) return false;
        for (int i = 0; i < xLength - 1; i++) {
            operations++;
            double temp = difMainOnX(i) + l1 * (profit[xLength - 1] - profit[i]) + l2;
            if (temp < -epsilon) return false;
        }
        if (sumProfit() < this.expectedProfit) return false;
        return true;
    }

    private double difMainOnX(int index) {
        double out = 0;
        for (int j = 0; j < xLength - 1; j++) {
            if (index != j) {
                operations += 3;
                out += 2 * (covariances[index][j] - covariances[index][xLength - 1]) * x[j];
            } else {
                operations += 3;
                out += (2 - 4 * covariances[index][xLength - 1]) * x[index];
            }
        }
        operations++;
        out += 2 * covariances[index][xLength - 1];
        return out;
    }

    protected double sumCovarIndex(int i) {
        double out = 0;
        for (int j = 0; j < xLength - 1; j++) {
            operations += 2;
            if (j != i) out += covariances[i][j] * x[j];
        }
        return out;
    }
}
