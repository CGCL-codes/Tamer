package br.edu.ufcg.msnlab.methods.dekker;

import static br.edu.ufcg.msnlab.util.Util.isEqual;
import static br.edu.ufcg.msnlab.util.Util.liesBetween;
import static br.edu.ufcg.msnlab.util.Util.sameSigns;
import static java.lang.Math.abs;
import java.util.LinkedList;
import java.util.List;
import br.edu.ufcg.msnlab.misc.Function;

/**
 * TODO DOCUMENT ME!
 * @author Edigley Pereira Fraga
 * @author Jaindson Valentim Santana
 */
public class DekkerSolverImpl implements DekkerSolver {

    private static final int DEFAULT_MAX_ITERATIONS = 50;

    public List<DekkerResult> solve(Function f, double a, double b, double tol) throws DekkerException {
        return solve(f, a, b, tol, DEFAULT_MAX_ITERATIONS);
    }

    public List<DekkerResult> solve(Function f, double a, double b, double tol, int maxIterations) throws DekkerException {
        double a_0 = a;
        double b_0 = b;
        double fa_0 = f.calculate(a_0);
        if (sameSigns(fa_0, b_0)) {
        }
        double cA;
        double cB;
        double oldB;
        double s;
        double m;
        List<DekkerResult> guesses = new LinkedList<DekkerResult>();
        if (abs(f.calculate(a_0)) < abs(f.calculate(b_0))) {
            double aux = a_0;
            a_0 = b_0;
            b_0 = aux;
        }
        oldB = a_0;
        cA = a_0;
        cB = b_0;
        int iter = 1;
        guesses.add(new DekkerResultImpl(cB));
        while (!isEqual(f.calculate(cB), 0.0, tol) && (iter <= maxIterations)) {
            s = getS(cB, oldB, f);
            m = getM(cA, cB);
            oldB = cB;
            if (liesBetween(s, cB, m, tol)) {
                cB = s;
            } else {
                cB = m;
            }
            if (sameSigns(cA, cB)) {
                cA = oldB;
            }
            if (abs(f.calculate(cA)) < abs(f.calculate(cB))) {
                double aux = cA;
                cA = cB;
                cB = aux;
            }
            guesses.add(new DekkerResultImpl(cB));
            iter++;
        }
        if (iter > maxIterations) {
            throw new DekkerException("The best guess was not found in less then " + maxIterations + " iterations");
        }
        return guesses;
    }

    /**
     * This calculation is based on bisection method.
     * @param ca
     * @param cb
     * @return
     */
    private double getM(double ca, double cb) {
        return (ca + cb) / 2.0;
    }

    /**
     * This calculation is based on secant method.
     * @param cB
     * @param oldB
     * @param function
     * @return
     */
    private double getS(double cB, double oldB, Function function) {
        double numerator = cB - oldB;
        double denominator = function.calculate(cB) - function.calculate(oldB);
        return cB - (numerator / denominator) * function.calculate(cB);
    }

    public List<DekkerResult> solve(Function funcion, double tolerance) {
        throw new UnsupportedOperationException();
    }
}
