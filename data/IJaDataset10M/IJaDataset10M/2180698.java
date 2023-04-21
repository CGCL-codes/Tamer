package org.opensourcephysics.stp.ising.meanfieldequation;

import java.text.DecimalFormat;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.PlotFrame;
import org.opensourcephysics.numerics.*;

/**
 * MeanFieldApp class.
 *
 *  @author Hui Wang
 *  @version 1.0   revised 11/12/06
 */
public class MeanFieldApp extends AbstractCalculation implements Function {

    PlotFrame plotFrame = new PlotFrame("m", "tanh(m)", "");

    PlotFrame energyFrame = new PlotFrame("m", "f(m)", "Free energy");

    double beta = 1.0;

    double J = 1.0;

    double q = 4.0;

    double b = 1.0;

    double mxlimit, pxlimit;

    double xtol = 5.0e-1;

    double[] root;

    boolean negative = false;

    DecimalFormat numberFormat = (DecimalFormat) DecimalFormat.getInstance();

    MeanFieldApp() {
        root = new double[3];
        plotFrame.setConnected(true);
        energyFrame.setConnected(true);
        numberFormat.setMinimumFractionDigits(3);
    }

    public void reset() {
        control.setValue("beta", 1.0);
        control.setValue("J", 1.0);
        control.setValue("q", 4.0);
        control.setValue("B", 0.0);
    }

    public void calculate() {
        beta = control.getDouble("beta");
        J = control.getDouble("J");
        b = control.getDouble("B");
        if (b < 0) {
            negative = true;
            b = -b;
        } else negative = false;
        pxlimit = 5.0;
        mxlimit = -5.0 - xtol / 2;
        double tol = 1.0e-6;
        root[0] = root[1] = root[2] = Double.NaN;
        double x = mxlimit;
        while (x < pxlimit) {
            findRoots(x, x + xtol, tol);
            x += xtol;
        }
        control.clearMessages();
        if (countRoots() == 1) {
            control.println("Root = " + numberFormat.format(root[0]));
        } else if (countRoots() > 1) {
            mxlimit = root[0] - 1;
            pxlimit = root[2] + 1;
            for (int i = 0; i < 3; i++) {
                if (negative) root[i] = -root[i];
                if (!Double.isNaN(root[i])) control.println("Root " + i + " = " + numberFormat.format(root[i]));
            }
        } else {
            control.println("No roots found");
        }
        plot();
    }

    public boolean findRoots(double mx, double px, double tol) {
        double r0 = Root.bisection(this, mx, px, tol);
        if (Double.isNaN(r0)) return false;
        storeRoots(r0);
        return true;
    }

    public void storeRoots(double r) {
        int i = 0;
        while (i < 3 && !Double.isNaN(root[i])) i++;
        if (i == 3) {
            System.out.println("More than three roots found!");
            return;
        }
        root[i] = r;
    }

    public int countRoots() {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            if (!Double.isNaN(root[i])) counter++;
        }
        return counter;
    }

    public void plot() {
        if (!negative) {
            double x = mxlimit;
            while (x < pxlimit) {
                plotFrame.append(1, x, mftanh(x));
                plotFrame.append(2, x, x);
                energyFrame.append(1, x, freeenergy(x));
                x += 0.02;
            }
        } else {
            double x = mxlimit;
            while (x < pxlimit) {
                plotFrame.append(1, -x, mftanh(x));
                plotFrame.append(2, -x, x);
                energyFrame.append(1, -x, freeenergy(x));
                x += 0.02;
            }
        }
        plotFrame.render();
    }

    public double freeenergy(double x) {
        return 0.5 * J * q * x * x - 1.0 / beta * Math.log(2 * cosh(beta * (q * J * x + b)));
    }

    public double evaluate(double x) {
        return mftanh(x) - x;
    }

    public double mftanh(double x) {
        return tanh(beta * (J * q * x + b));
    }

    public double tanh(double x) {
        double ex, mex;
        ex = Math.exp(x);
        mex = 1 / ex;
        return (ex - mex) / (ex + mex);
    }

    public double cosh(double x) {
        double ex, mex;
        ex = Math.exp(x);
        mex = 1 / ex;
        return (ex + mex) / 2;
    }

    public static void main(String[] args) {
        CalculationControl.createApp(new MeanFieldApp(), args);
    }
}
