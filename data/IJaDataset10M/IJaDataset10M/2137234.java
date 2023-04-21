package net.sf.derquinsej.tuple;

/**
 * Implementation of a 3-element tuple.
 * @author Andres Rodriguez
 * @param <T1> First element type.
 * @param <T2> Second element type.
 * @param <T3> Third element type.
 */
final class Tuple3Impl<T1, T2, T3> extends AbstractTuple3<T1, T2, T3> {

    /**
	 * Constructor.
	 * @param e1 First element.
	 * @param e2 Second element.
	 * @param e3 Third element.
	 */
    Tuple3Impl(T1 e1, T2 e2, T3 e3) {
        super(e1, e2, e3);
    }
}
