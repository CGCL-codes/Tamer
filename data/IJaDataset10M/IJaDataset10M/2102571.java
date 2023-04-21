package net.fortytwo.flow;

public interface Mapping<D, R, E extends Exception> {

    /**
	 * @return whether this function is referentially transparent w.r.t. all of its
	 * parameters.
	 */
    boolean isTransparent();

    void apply(D arg, Sink<R, E> solutions) throws E;
}
