package task6;

import tools.functionprimitives.ITwoArgFunction;
import tools.functionprimitives.IOneArgFunction;
import tools.Matrix;
import tools.Vector;
import tools.LinearSystem;

/**
 * Created by IntelliJ IDEA.
 * User: Sasha
 * Date: 19.05.2008
 * Time: 0:50:13
 * To change this template use File | Settings | File Templates.
 */
public class SixPointsMethod extends ThermalConductivityNetMethod {

    public SixPointsMethod(double aCoeff, double startX, double startT, int xNodesAmount, int tNodesAmount, double stepX, double stepT, ITwoArgFunction f, IOneArgFunction g, IOneArgFunction alpha, IOneArgFunction beta) {
        super(aCoeff, startX, startT, xNodesAmount, tNodesAmount, stepX, stepT, f, g, alpha, beta);
    }

    protected double[] fillXAxis(double argT, double prevArgT, double[] prevValues, int k) {
        int length = prevValues.length;
        double[] newValues = new double[length];
        newValues[0] = super.alpha.eval(argT);
        newValues[length - 1] = super.beta.eval(argT);
        double[] middleNodes = evalMiddleNodes(newValues, prevValues, argT, k);
        for (int i = 1; i < length - 2; i++) {
            newValues[i] = middleNodes[i - 1];
        }
        return newValues;
    }

    protected double[] evalMiddleNodes(double[] newValues, double[] prevValues, double argT, int k) {
        int length = super.N - 1;
        double[][] matrixCoeff = new double[length][length];
        double[] vectorCoeff = new double[length];
        createSystemCoeff(matrixCoeff, argT, newValues, prevValues);
        createSystemVector(vectorCoeff, newValues, prevValues, k);
        return driving(matrixCoeff, vectorCoeff).copyElements();
    }

    protected void createSystemCoeff(double[][] matrixCoeff, double argT, double[] newValues, double[] prevValues) {
        int length = matrixCoeff.length;
        double a = evalStandartACoeff();
        double b = evalStandartBCoeff();
        matrixCoeff[0][0] = b;
        matrixCoeff[0][1] = a;
        for (int i = 1; i < length - 1; i++) {
            matrixCoeff[i][i - 1] = a;
            matrixCoeff[i][i] = b;
            matrixCoeff[i][i + 1] = a;
        }
        matrixCoeff[length - 1][length - 2] = b;
        matrixCoeff[length - 1][length - 1] = a;
    }

    protected void createSystemVector(double[] vectorCoeff, double[] newValues, double[] prevValues, int k) {
        int length = vectorCoeff.length;
        double middleArgT = super.startT + (k - 0.5) * super.stepT;
        double a = (-1.0) * evalStandartACoeff();
        double b = 1.0 + evalCommonMember();
        for (int i = 0; i < length; i++) {
            vectorCoeff[i] = evalVectorCoeff(i + 1, newValues, prevValues, middleArgT);
        }
    }

    private Vector driving(double[][] matrix, double[] vector) {
        int n = matrix.length - 1;
        double[] m = new double[n + 1];
        double[] k = new double[n + 1];
        m[1] = -matrix[0][1] / matrix[0][0];
        k[1] = vector[0] / matrix[0][0];
        for (int i = 1; i < n; i++) {
            m[i + 1] = -matrix[i][i + 1] / (matrix[i][i - 1] * m[i] + matrix[i][i]);
            k[i + 1] = (vector[i] - matrix[i][i - 1] * k[i]) / (matrix[i][i - 1] * m[i] + matrix[i][i]);
        }
        double[] y = new double[n + 1];
        y[n] = (vector[n] - matrix[n][n - 1] * k[n]) / (matrix[n][n - 1] * m[n] + matrix[n][n]);
        for (int i = n - 1; i >= 0; i--) {
            y[i] = m[i + 1] * y[i + 1] + k[i + 1];
        }
        return new Vector(y);
    }

    protected double evalStandartACoeff() {
        return evalCommonMember() / 2.0;
    }

    protected double evalStandartBCoeff() {
        return 1.0 + (-1) * evalCommonMember();
    }

    private double evalCommonMember() {
        return -super.A * super.stepT / Math.pow(super.stepX, 2.0);
    }

    private double evalVectorCoeff(int index, double[] newValues, double[] prevValues, double middleArgT) {
        double a = (-1.0) * evalStandartACoeff();
        double b = 1.0 + evalCommonMember();
        double result = 0;
        if (index == 1) {
            result = result + a * newValues[index - 1];
        } else {
            if (index + 1 == prevValues.length - 1) {
                return super.stepT * super.f.eval(super.startX + index * super.stepX, middleArgT) + b * prevValues[index - 1] + a * (prevValues[index] + prevValues[index - 2]) + a * newValues[index + 1];
            }
        }
        result += super.stepT * super.f.eval(super.startX + index * super.stepX, middleArgT) + b * prevValues[index] + a * (prevValues[index + 1] + prevValues[index - 1]);
        return result;
    }
}
