package javalens.rwrapper;

import javalens.maths.Maths;
import javalens.util.LinearModelResult;
import javalens.astronomy.Event;
import javalens.util.ComplexNumber;
import javalens.*;
import javalens.globalconfig.config;
import javalens.maths.Maths;
import javalens.util.CalculationException;
import javalens.util.LinearModelResult;
import javalens.util.Util;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Pierre
 */
public class REngineWrapper implements Maths {

    private Rengine engine = null;

    private static REngineWrapper singleton;

    private REngineWrapper() {
        if (engine == null) {
            initR();
        }
    }

    public static REngineWrapper getREngineWrapper() {
        if (singleton == null) {
            singleton = new REngineWrapper();
            setupFunctions(singleton);
        }
        return singleton;
    }

    public static String arrayToRString(double[] coefficients) {
        StringBuffer ret = new StringBuffer();
        ret.append("c(");
        for (int i = 0; i < coefficients.length; ++i) {
            ret.append(coefficients[i]);
            if (i != coefficients.length - 1) {
                ret.append(",");
            }
            if (i % 8 == 0) {
                ret.append("\n");
            }
        }
        ret.append(")");
        return ret.toString();
    }

    public static String arrayToRStringAndReverse(double[] coefficients) {
        StringBuffer ret = new StringBuffer();
        ret.append("c(");
        for (int i = coefficients.length - 1; i >= 0; --i) {
            ret.append(coefficients[i]);
            if (i != 0) {
                ret.append(",");
            }
            if (i % 8 == 0) {
                ret.append("\n");
            }
        }
        ret.append(")");
        return ret.toString();
    }

    private void initR() {
        if (!Rengine.versionCheck()) {
            System.err.println("** Version mismatch - Java files don't match library version.");
            System.exit(1);
        }
        System.out.println("Creating Rengine (with arguments)");
        engine = new Rengine(new String[] { "--no-save" }, false, new TextConsole());
        System.out.println("Rengine created, waiting for R");
        if (!engine.waitForR()) {
            System.out.println("Cannot load R");
            return;
        }
    }

    /** Evaluate a single line expression to a double array
     * 
     * @param expression Any general R expression
     * @return the resulting double array
     */
    public double[] eval(String expression) {
        REXP x = engine.eval(expression);
        if (x == null) {
            return null;
        }
        return x.asDoubleArray();
    }

    /** Root of polynomial, complex or other
     * 
     * @return The real parts of the roots as an array
     */
    public ComplexNumber[] polyroot(double[] coefficients) {
        String vec = arrayToRStringAndReverse(coefficients);
        engine.eval("r1 <- polyroot(" + vec + ")");
        double[] realPart = eval("Re(r1)");
        double[] imagPart = eval("Im(r1)");
        return ComplexNumber.complexArray(realPart, imagPart);
    }

    public LinearModelResult RLinearModel(double[] x, double[] y, String formula) {
        engine.eval("x <-" + arrayToRString(x));
        engine.eval("y <-" + arrayToRString(y));
        engine.eval("lm1 <- lm(" + formula + ")");
        engine.eval("v1 <- var(lm1$residuals)");
        LinearModelResult result = new LinearModelResult();
        result.setChiSquared(eval("v1")[0]);
        result.setCoefficients(eval("lm1$coefficients"));
        result.setFormula(formula);
        return result;
    }

    public LinearModelResult fitLine(double[] x, double[] y) {
        return RLinearModel(x, y, "y~x");
    }

    public LinearModelResult fitParabola(double[] x, double[] y) {
        return RLinearModel(x, y, "y~x+I(x^2)");
    }

    public LinearModelResult fitCubic(double[] x, double[] y) {
        return RLinearModel(x, y, "y~x+I(x^2)+I(x^3)");
    }

    public LinearModelResult fitFourthOrderPoly(double[] x, double[] y) {
        return RLinearModel(x, y, "y~x+I(x^2)+I(x^3)+I(x^4)");
    }

    public LinearModelResult fitFithOrderPoly(double[] x, double[] y) {
        return RLinearModel(x, y, "y~x+I(x^2)+I(x^3)+I(x^4)+I(x^5)");
    }

    private static void setupFunctions(REngineWrapper w) {
        w.eval("source(\"" + config.PATH_TO_R_FILE + "microlensing.R\")");
    }

    /** Fits a single lens light curve to a data set given by x,y and err data.
     * If err is null, an error of 0.1 mag per point is assumed. We will fit all
     * parameters except f, which we will assume to be 1.0 for the purposes of 
     * feature selection. Well, the allowed range for f is 0.75 to 1.0
     * 
     * @param x     Time in days
     * @param y     Magnitude curve
     * @param err   Error in magnitude or NULL
     * @param chisq A double vector of length one to return the chi-squared of the fit
     * @return a single lens Event that fits.
     */
    public Event fitSingleLensCurve(double[] x, double[] y, double[] err, double[] chisq) throws CalculationException {
        REngineWrapper w = REngineWrapper.getREngineWrapper();
        if (x.length != y.length) {
            throw new CalculationException("Data vectors do not have matching lengths");
        }
        if (err != null && err.length != y.length) {
            throw new CalculationException("Error vector does not have matching length");
        }
        if (chisq.length != 1) {
            throw new CalculationException("chisq should be a double[] of length 1");
        }
        double te = (Util.max(x) - Util.min(x)) / 4.0;
        double tm = (Util.max(x) + Util.min(x)) / 2.0;
        double f = 1.0;
        double m0 = Util.max(y);
        double b = 1.0;
        if (te < 0 || te > 200) {
            throw new CalculationException("te is very wrong. The estimate is " + te);
        }
        if (m0 > 22 || m0 < 14) {
            throw new CalculationException("Baseline seems wrong. Max mag is :" + m0);
        }
        String xdataVal = "xdata<-" + REngineWrapper.arrayToRString(x);
        w.eval(xdataVal);
        String ydataVal = "ydata<-" + REngineWrapper.arrayToRString(y);
        w.eval(ydataVal);
        String errdataVal;
        if (err != null) {
            errdataVal = "errdata<-" + REngineWrapper.arrayToRString(err);
        } else {
            errdataVal = "errdata<-rep(0.01,length(xdata))";
        }
        w.eval(errdataVal);
        String fitFunction = new String("res <- optim(c(" + b + "," + f + "," + m0 + "," + te + "," + tm + "),lensFit," + "lower=c(1e-5,.75,14,1," + (Util.min(x) - tm) + ")," + "upper=c(5,1,22," + (te * 4) + "," + (Util.max(x) + tm) + ")," + "method=\"L-BFGS-B\"" + ")");
        w.eval(fitFunction);
        double[] par = eval("res$par");
        double[] resultChisq = eval("res$value");
        chisq[0] = resultChisq[0];
        if (par.length != 5) {
            throw new CalculationException("Incorrect length result vector returned from R?!");
        }
        Event ret = new Event(par[0], par[3], par[4], par[2]);
        ret.setF(par[1]);
        return ret;
    }
}
