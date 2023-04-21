package org.encog.engine.network.activation;

import org.encog.mathutil.BoundMath;

/**
 * An activation function based on the sin function.
 * 
 * @author jheaton
 */
public class ActivationSIN implements ActivationFunction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5301501177778271284L;

    /**
	 * Construct the sin activation function.
	 */
    public ActivationSIN() {
        this.params = new double[0];
    }

    /**
	 * The parameters.
	 */
    private double[] params;

    /**
	 * @return The object cloned;
	 */
    @Override
    public final ActivationFunction clone() {
        return new ActivationSIN();
    }

    /**
	 * @return Return true, sin has a derivative.
	 */
    @Override
    public final boolean hasDerivative() {
        return true;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final void activationFunction(final double[] x, final int start, final int size) {
        for (int i = start; i < start + size; i++) {
            x[i] = BoundMath.sin(x[i]);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final double derivativeFunction(final double b, final double a) {
        return BoundMath.cos(b);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final String[] getParamNames() {
        final String[] result = {};
        return result;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final double[] getParams() {
        return this.params;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final void setParam(final int index, final double value) {
        this.params[index] = value;
    }
}
