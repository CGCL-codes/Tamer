package de.skeptix.evomusic.ea.functions;

/**
 * @author skchang
 *
 * Interface for derivable functions.
 */
public interface IDerivableFunction extends IFunction {

    public double computeDerive(double value);
}
