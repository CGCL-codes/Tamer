package jp.riken.omicspace.graphics;

import java.lang.*;
import java.util.*;

public class SplineContainer {

    private int mNumPoints = 0;

    private double[] mX = null;

    private double[] mY = null;

    private double[] mA = null;

    private double[] mB = null;

    private double[] mC = null;

    private double[] mBoundCond1 = new double[2];

    private double[] mBoundCondN = new double[2];

    public SplineContainer(double[] X, double[] Y, int numPoints) {
        setupConstants(numPoints);
        for (int i = 0; i < numPoints; i++) mX[i] = X[i];
        for (int i = 0; i < numPoints; i++) mY[i] = Y[i];
        setupBoundaryConditions();
        calcCoefficients();
    }

    private void setupConstants(int numPoints) {
        mNumPoints = numPoints;
        mX = new double[numPoints];
        mY = new double[numPoints];
        mA = new double[numPoints - 1];
        mB = new double[numPoints - 1];
        mC = new double[numPoints - 1];
    }

    private void setupBoundaryConditions() {
        mBoundCond1[0] = 0.0;
        mBoundCond1[1] = 0.0;
        mBoundCondN[0] = 0.0;
        mBoundCondN[1] = 0.0;
    }

    private void calcCoefficients() {
        double dx1, dx2;
        double dy1, dy2;
        dx1 = mX[1] - mX[0];
        dy1 = mY[1] - mY[0];
        for (int i = 1; i < mNumPoints - 1; i++) {
            dx2 = mX[i + 1] - mX[i];
            dy2 = mY[i + 1] - mY[i];
            mC[i] = dx2 / (dx1 + dx2);
            mB[i] = 1.0 - mC[i];
            mA[i] = 6.0 * (dy2 / dx2 - dy1 / dx1) / (dx1 + dx2);
            dx1 = dx2;
            dy1 = dy2;
        }
        mC[0] = -mBoundCond1[0] / 2.0;
        mB[0] = mBoundCond1[1] / 2.0;
        mA[0] = 0.0;
        for (int i = 1; i < mNumPoints - 1; i++) {
            double p = mB[i] * mC[i - 1] + 2.0;
            mC[i] = -mC[i] / p;
            mB[i] = (mA[i] - mB[i] * mB[i - 1]) / p;
        }
        dy1 = (mBoundCondN[1] - mBoundCondN[0] * mB[mNumPoints - 2]) / (mBoundCondN[0] * mC[mNumPoints - 2] + 2.0);
        for (int i = mNumPoints - 2; i >= 0; i--) {
            dx1 = mX[i + 1] - mX[i];
            dy2 = mC[i] * dy1 + mB[i];
            mA[i] = (dy1 - dy2) / (6.0 * dx1);
            mB[i] = dy2 / 2.0;
            mC[i] = (mY[i + 1] - mY[i]) / dx1 - dx1 * (mB[i] + dx1 * mA[i]);
            dy1 = dy2;
        }
    }

    public int getNumPoints() {
        return mNumPoints;
    }

    public double getValue(double x) {
        if (mNumPoints < 2) return 0.0;
        int i = getSegmentNumber(x);
        double t = (x - mX[i]);
        return mA[i] * t * t * t + mB[i] * t * t + mC[i] * t + mY[i];
    }

    private int getSegmentNumber(double x) {
        int left = 0;
        int right = mNumPoints - 1;
        while (left + 1 < right) {
            int middle = (left + right) / 2;
            if (mX[middle] <= x) left = middle; else right = middle;
        }
        return left;
    }
}
