package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.flow.Sink;

/**
 * A primitive which consumes a number representing an angle in radians and
 * produces its cosine.
 */
public class Cos extends PrimitiveStackMapping {

    private static final String[] IDENTIFIERS = { MathLibrary.NS_2008_08 + "cos", MathLibrary.NS_2007_08 + "cos" };

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Cos() throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[] { new Parameter("x", null, true) };
    }

    public String getComment() {
        return "x  =>  cos(x)";
    }

    public void apply(final StackContext arg, final Sink<StackContext, RippleException> solutions) throws RippleException {
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();
        NumericValue a, result;
        a = mc.toNumericValue(stack.getFirst());
        stack = stack.getRest();
        result = mc.value(Math.cos(a.doubleValue()));
        solutions.put(arg.with(stack.push(result)));
    }

    @Override
    public StackMapping getInverse() throws RippleException {
        return MathLibrary.getAcosValue();
    }
}
