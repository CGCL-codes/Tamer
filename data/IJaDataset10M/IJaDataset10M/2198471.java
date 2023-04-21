package mipt.math.function;

import mipt.math.Number;

/**
 * Function using any other function and multiplying its value by the given factor
 * Is differentiable so many times as wrapping function is
 * It's better to use static getFunction() method instead of direct instantiation
 * @author evdokimov
 */
public class FunctionFactor extends AbstractFunctionWrapper implements DifferentiableFunction, ParametrizedFunction {

    protected double factor = 1.;

    protected Function derivative;

    public FunctionFactor() {
    }

    public FunctionFactor(Function func, double factor) {
        super(func);
        setFactor(factor);
    }

    public final double getParameter() {
        return getFactor();
    }

    public final void setParameter(double p) {
        setFactor(p);
    }

    public void setFactor(double factor) {
        if (factor == 0.) throw new IllegalArgumentException("Use trivial function instead of FunctionFactor with 0 power");
        this.factor = factor;
    }

    public final double getFactor() {
        return factor;
    }

    /**
	 * @see mipt.math.function.Function#calc(mipt.math.Number)
	 */
    public final Number calc(Number value) {
        return getFunction().calc(value).mult(factor);
    }

    /**
	 * @see mipt.math.function.DifferentiableFunction#getDerivative()
	 */
    public final Function getDerivative() {
        if (derivative == null) {
            Function diffFunc = getFunction();
            double newFactor = factor;
            if (diffFunc instanceof FunctionFactor) {
                FunctionFactor ff = (FunctionFactor) getFunction();
                diffFunc = ff.getFunction();
                newFactor *= ff.getFactor();
            }
            if (diffFunc instanceof DifferentiableFunction) {
                derivative = ((DifferentiableFunction) diffFunc).getDerivative();
                if (newFactor != 1.) derivative = new FunctionFactor(derivative, newFactor);
            } else throw new IllegalStateException(getFunction() + " is not differentiable");
        }
        return derivative;
    }

    /**
	 * Do not return new FunctionFactor(new FunctionFactor()...) (unlike when using constructor)
	 */
    public static Function getFunction(Function func, double factor) {
        if (factor == 0.) return new TrivialFunction();
        if (factor == 1.) return func;
        if (func instanceof FunctionFactor) {
            FunctionFactor ff = (FunctionFactor) func;
            return new FunctionFactor(ff.getFunction(), factor * ff.getFactor());
        } else return new FunctionFactor(func, factor);
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        FunctionFactor f = (FunctionFactor) obj;
        return getFactor() == f.getFactor();
    }

    /**
	 * String representation of {@link FunctionFactor} as factor*function(x).
	 * Modified with Korchak Anton.
	 */
    public String toString() {
        String result = getFunction().toString();
        if (getFactor() == 1.) return result;
        if (getFactor() == -1.) return "-" + result;
        if (getFactor() == -1.) return "-(" + result + ")";
        String factorString = Number.toString(factor);
        if (factor < 0.) factorString = "(" + factorString + ")";
        return factorString + "*" + result;
    }
}
