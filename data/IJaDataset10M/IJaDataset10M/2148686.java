package org.renjin.primitives.optimize;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.renjin.eval.Context;
import org.renjin.sexp.DoubleVector;
import org.renjin.sexp.Environment;
import org.renjin.sexp.Function;
import org.renjin.sexp.FunctionCall;
import org.renjin.sexp.Vector;

public class UnivariateRealClosure implements UnivariateRealFunction {

    private final Context context;

    private final Environment rho;

    private final Function fn;

    public UnivariateRealClosure(Context context, Environment rho, Function fn) {
        this.context = context;
        this.rho = rho;
        this.fn = fn;
    }

    @Override
    public double value(double x) throws FunctionEvaluationException {
        FunctionCall call = FunctionCall.newCall(fn, new DoubleVector(x));
        Vector y = (Vector) context.evaluate(call, rho);
        return y.getElementAsDouble(0);
    }
}
