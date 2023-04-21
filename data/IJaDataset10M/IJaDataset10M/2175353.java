package org.encog.engine.network.activation;

import org.encog.EncogError;

/**
 * An activation function that only allows a specified number, usually one, of
 * the out-bound connection to win. These connections will share in the sum of
 * the output, whereas the other neurons will receive zero.
 * 
 * This activation function can be useful for "winner take all" layers.
 * 
 */
public class ActivationCompetitive implements ActivationFunction {

    /**
	 * The offset to the parameter that holds the max winners.
	 */
    public static final int PARAM_COMPETITIVE_MAX_WINNERS = 0;

    /**
	 * The serial ID.
	 */
    private static final long serialVersionUID = 5396927873082336888L;

    /**
	 * The parameters.
	 */
    private final double[] params;

    /**
	 * Create a competitive activation function with one winner allowed.
	 */
    public ActivationCompetitive() {
        this(1);
    }

    /**
	 * Create a competitive activation function with the specified maximum
	 * number of winners.
	 * 
	 * @param winners
	 *            The maximum number of winners that this function supports.
	 */
    public ActivationCompetitive(final int winners) {
        this.params = new double[1];
        this.params[ActivationCompetitive.PARAM_COMPETITIVE_MAX_WINNERS] = winners;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final void activationFunction(final double[] x, final int start, final int size) {
        final boolean[] winners = new boolean[x.length];
        double sumWinners = 0;
        for (int i = 0; i < this.params[0]; i++) {
            double maxFound = Double.NEGATIVE_INFINITY;
            int winner = -1;
            for (int j = start; j < start + size; j++) {
                if (!winners[j] && (x[j] > maxFound)) {
                    winner = j;
                    maxFound = x[j];
                }
            }
            sumWinners += maxFound;
            winners[winner] = true;
        }
        for (int i = start; i < start + size; i++) {
            if (winners[i]) {
                x[i] = x[i] / sumWinners;
            } else {
                x[i] = 0.0;
            }
        }
    }

    /**
	 * @return A cloned copy of this object.
	 */
    @Override
    public final ActivationFunction clone() {
        return new ActivationCompetitive((int) this.params[ActivationCompetitive.PARAM_COMPETITIVE_MAX_WINNERS]);
    }

    /**
	 * Implements the activation function. The array is modified according to
	 * the activation function being used. See the class description for more
	 * specific information on this type of activation function.
	 * 
	 * @param d
	 *            The input array to the activation function.
	 * @return The derivative.
	 */
    @Override
    public final double derivativeFunction(final double b, final double a) {
        throw new EncogError("Can't use the competitive activation function " + "where a derivative is required.");
    }

    /**
	 * @return The maximum number of winners this function supports.
	 */
    public final int getMaxWinners() {
        return (int) this.params[ActivationCompetitive.PARAM_COMPETITIVE_MAX_WINNERS];
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final String[] getParamNames() {
        final String[] result = { "maxWinners" };
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
	 * @return False, indication that no derivative is available for this
	 *         function.
	 */
    @Override
    public final boolean hasDerivative() {
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final void setParam(final int index, final double value) {
        this.params[index] = value;
    }
}
