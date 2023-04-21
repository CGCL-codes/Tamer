package mipt.math.function.impl.spec;

import mipt.math.Number;
import mipt.math.function.DifferentiableFunction;
import mipt.math.function.Function;
import mipt.math.function.impl.CosFunction;
import mipt.math.function.impl.FactorialFunction;
import mipt.math.function.impl.PolynomialFunction;
import mipt.math.function.impl.PowFunction;

public class ChebyshevPolynomial implements Function, DifferentiableFunction {

    private int degree = -1;

    private PolynomialFunction polynomial = null;

    public ChebyshevPolynomial(int n) {
        setDegree(n);
    }

    /**
	 * Allows to set the degree of Chebyshev polynomial.
	 */
    public void setDegree(int n) {
        if (n < 0) throw new IllegalArgumentException("The degree of Chebyshev polynomial must be non-negative."); else if (n == 0) {
            this.degree = n;
            Number[] coefficients = { Number.createScalar(1) };
            polynomial = new PolynomialFunction(coefficients);
        } else {
            this.degree = n;
            initPolynomial();
        }
    }

    /**
	 * @see mipt.math.function.Function#calc(mipt.math.Number)
	 */
    public Number calc(Number x) {
        if (polynomial == null) throw new IllegalStateException("The Chebyshev polynomial is not set."); else return polynomial.calc(x);
    }

    /**
	 * Allows to get the degree of Chebyshev polynomial.
	 * @return degree
	 */
    public int getDegree() {
        return degree;
    }

    /**
	 * @see mipt.math.function.DifferentiableFunction#getDerivative()
	 */
    public PolynomialFunction getDerivative() {
        return polynomial.getDerivative();
    }

    public String toString() {
        return polynomial.toString();
    }

    private void initPolynomial() {
        int n = getDegree();
        Number[] coefficients = new Number[n + 1];
        for (int i = 0; i <= n; i++) {
            if ((n - i) % 2 == 1) coefficients[i] = Number.createScalar(0.); else {
                PowFunction.setPowerCanBeZero(true);
                int k = (n - i) / 2;
                coefficients[i] = Number.createScalar(.5).mult(Number.createScalar(n)).mult((new PowFunction(k)).calc(Number.createScalar(-1.)));
                coefficients[i].mult((new FactorialFunction()).calc(Number.createScalar(n).minus(Number.createScalar(k)).minus(Number.one())));
                coefficients[i].mult((new PowFunction(i)).calc(Number.createScalar(2.))).divide((new FactorialFunction()).calc(Number.createScalar(k)));
                coefficients[i].divide((new FactorialFunction()).calc(Number.createScalar(i)));
            }
            polynomial = new PolynomialFunction(coefficients);
        }
    }

    public static Number[] getRoots(int degree) {
        Number[] roots = new Number[degree];
        for (int i = 1; i <= degree; i++) roots[i - 1] = (new CosFunction()).calc(Number.createScalar(.5).mult(Number.createScalar(Math.PI)).mult((2 * i - 1)).divide(Number.createScalar(degree)));
        return roots;
    }

    public static Number[] getExtrema(int degree) {
        Number[] extrema = new Number[degree - 1];
        for (int i = 1; i < degree; i++) extrema[i - 1] = (new CosFunction()).calc(Number.createScalar(Math.PI).mult(i).divide(Number.createScalar(degree)));
        return extrema;
    }
}
