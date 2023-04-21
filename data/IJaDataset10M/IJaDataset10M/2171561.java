package edu.ucla.stat.SOCR.applications.demo;

import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.ucla.stat.SOCR.applications.Application;
import edu.ucla.stat.SOCR.util.Matrix;
import edu.ucla.stat.SOCR.core.exceptions.*;
import java.text.DecimalFormat;

/**
 * The triangle experiment is to break a stick at random and see if the pieces
 * form a triangle. If so, is the triangle acute or obtuse?
 */
public class PortfolioApplication2 extends Application {

    protected double[][] ExpectedReturn;

    protected double[][] COVR;

    protected double[][] CORR;

    Matrix covrMatrix, expRetMatrix, corrMatrix;

    private boolean covarianceFlag = true;

    private DecimalFormat formatter = new DecimalFormat("#0.0####");

    protected double t1_x, t2_x;

    protected double t1_y, t2_y;

    SimulatedDataPoints simulatedPoints;

    ChartDataPoints chartDataPoints;

    int numStocks = 2;

    int numSimulate = 5000;

    public PortfolioApplication2() {
        setupUniformSimulate();
    }

    public PortfolioApplication2(int numStocks, int numSimulate) {
        this.numStocks = numStocks;
        this.numSimulate = numSimulate;
        setupUniformSimulate();
    }

    public void setCovarianceMatrix(Matrix covr) throws Exception {
        covrMatrix = covr;
        try {
            covrToCorr();
        } catch (Exception e) {
            covrMatrix = null;
            throw e;
        }
    }

    public void setCorrelationMatrix(Matrix corr) throws InvalidOperationException, Exception {
        if (covrMatrix == null) {
            throw new InvalidOperationException("The covariance matrix must be set before using this method.");
        } else if (covrMatrix.columns != corr.columns) {
            throw new Exception("The size of the covariance and correlation matrix must match.");
        }
        corrMatrix = corr;
        corrToCovr();
    }

    public void setCorrelationMatrix(Matrix corr, double[] variances) throws Exception {
        if (corr.columns != variances.length) {
            throw new Exception("The length of the variances array must equal the size of the corelation matrix.");
        }
        int dim = variances.length;
        covrMatrix = new Matrix(dim, dim);
        for (int i = 0; i < variances.length; i++) {
            covrMatrix.element[i][i] = variances[i];
        }
        setCorrelationMatrix(corr);
    }

    private void setupUniformSimulate() {
        SimulatedDataPoints simulatedPoints = new SimulatedDataPoints(numStocks, numSimulate);
        ContinuousUniformDistribution uniform = new ContinuousUniformDistribution(-2, 2);
        for (int j = 0; j < numSimulate; j++) {
            double stocks[] = new double[numStocks];
            for (int i = 0; i < numStocks; i++) stocks[i] = uniform.simulate();
            simulatedPoints.addPoint(stocks);
        }
    }

    private boolean isAllPositive(int sdpPointer) {
        double[][] sdp = simulatedPoints.getRow(sdpPointer);
        boolean positive = true;
        for (int i = 0; i < numStocks; i++) {
            if (sdp[0][i] < 0) {
                positive = false;
                break;
            }
        }
        return positive;
    }

    private void covrToCorr() throws Exception {
        int dim = covrMatrix.columns;
        double[][] tmp = new double[dim][dim];
        double[] sqrtR = new double[dim];
        for (int i = 0; i < dim; i++) {
            sqrtR[i] = Math.sqrt(covrMatrix.element[i][i]);
        }
        for (int i = 0; i < dim; i++) for (int j = 0; j < dim; j++) if (i == j) {
            tmp[i][j] = 1;
        } else {
            tmp[i][j] = Double.parseDouble(formatter.format(covrMatrix.element[i][j] / (sqrtR[i] * sqrtR[j])));
            if (tmp[i][j] > 1 || tmp[i][j] < -1) throw new Exception();
        }
        corrMatrix = new Matrix(dim, dim, tmp);
    }

    private void corrToCovr() {
        int dim = corrMatrix.columns;
        double[][] tmp = new double[dim][dim];
        double[] sqrtR = new double[dim];
        for (int i = 0; i < dim; i++) {
            sqrtR[i] = Math.sqrt(covrMatrix.element[i][i]);
        }
        for (int i = 0; i < dim; i++) for (int j = 0; j < dim; j++) if (i == j) {
            tmp[i][j] = covrMatrix.element[i][j];
        } else {
            tmp[i][j] = Double.parseDouble(formatter.format(corrMatrix.element[i][j] * sqrtR[i] * sqrtR[j]));
        }
        covrMatrix = new Matrix(dim, dim, tmp);
    }

    private void printMatrix(double[][] m, String name) {
        System.out.println("---Print " + name + "-------");
        for (int i = 0; i < numStocks; i++) {
            for (int j = 0; j < numStocks; j++) System.out.print(name + i + "," + j + "=" + m[i][j] + "   ");
            System.out.println("\n");
        }
        System.out.println("----------");
    }

    private void computeTangent() {
        Matrix z1 = Matrix.inverse(covrMatrix);
        double[][] r = new double[numStocks][1];
        double[][] expRet = expRetMatrix.element;
        for (int i = 0; i < numStocks; i++) {
            r[i][0] = (expRet[i][0] - t1_y);
        }
        Matrix zr = new Matrix(numStocks, 1, r);
        Matrix z = Matrix.multiply(z1, zr);
        double zSum = 0;
        double[] z0 = z.getColumn(0);
        for (int i = 0; i < numStocks; i++) zSum += z0[i];
        double[][] x = new double[numStocks][1];
        for (int i = 0; i < numStocks; i++) x[i][0] = z0[i] / zSum;
        Matrix mx = new Matrix(numStocks, 1, x);
        Matrix mx1 = mx.transpose();
        Matrix my = Matrix.multiply(mx1, expRetMatrix);
        t2_y = Double.parseDouble(my.toString());
        Matrix mx2 = Matrix.multiply(mx1, covrMatrix);
        Matrix mx3 = Matrix.multiply(mx2, mx);
        t2_x = Math.sqrt(Double.parseDouble(mx3.toString()));
        System.out.println("t1_x=" + t1_x + " t1_y=" + t1_y);
        System.out.println("t2_x=" + t2_x + " t2_y=" + t2_y);
        double delta = (t2_y - t1_y) / (t2_x - t1_x);
        t2_x = t2_x * 2;
        t2_y = t1_y + t2_x * delta;
    }

    private void computeChartDataPoints() {
        double point_x, point_y;
        Matrix x1, x2;
        chartDataPoints = new ChartDataPoints(numSimulate);
        for (int i = 0; i < numSimulate; i++) {
            x1 = new Matrix(1, numStocks, simulatedPoints.getRow(i));
            x2 = x1.transpose();
            Matrix x3 = Matrix.multiply(x1, covrMatrix);
            Matrix x4 = Matrix.multiply(x3, x2);
            Matrix x5 = Matrix.multiply(x1, expRetMatrix);
            point_x = Math.sqrt(Double.parseDouble(x4.toString()));
            point_y = Double.parseDouble(x5.toString());
            if (point_x != 0 && point_x <= t2_x && point_y <= t2_y) chartDataPoints.addPoint(point_x, point_y);
        }
    }

    private class ChartDataPoints {

        double x[];

        double y[];

        int pointCount;

        ChartDataPoints(int numSimulate) {
            x = new double[numSimulate];
            y = new double[numSimulate];
            pointCount = 0;
        }

        public void addPoint(double x, double y) {
            this.x[pointCount] = x;
            this.y[pointCount] = y;
            pointCount++;
        }

        public double getX(int pointer) {
            return x[pointer];
        }

        public double getY(int pointer) {
            return y[pointer];
        }

        public int getPointCount() {
            return pointCount;
        }
    }

    private class SimulatedDataPoints {

        double x[][];

        int numStocks;

        int numSimulate;

        int pointCount;

        SimulatedDataPoints(int numStocks, int numSimulate) {
            x = new double[numSimulate][numStocks];
            pointCount = 0;
            this.numStocks = numStocks;
            this.numSimulate = numSimulate;
        }

        public void addPoint(double[] xValues) {
            double sum = 0;
            for (int i = 0; i < numStocks; i++) sum += xValues[i];
            if (sum > 0.1 || sum < -0.1) {
                for (int i = 0; i < numStocks; i++) x[pointCount][i] = xValues[i] / sum;
            }
            pointCount++;
        }

        public double[][] getRow(int pointer) {
            double[][] x1 = new double[1][numStocks];
            for (int i = 0; i < numStocks; i++) x1[0][i] = x[pointer][i];
            return x1;
        }

        public double[][] getColumn(int stockNum) {
            double[][] y1 = new double[pointCount][1];
            for (int i = 0; i < pointCount; i++) y1[i][0] = x[i][stockNum];
            return y1;
        }

        public int getPointCount() {
            return pointCount;
        }
    }
}
