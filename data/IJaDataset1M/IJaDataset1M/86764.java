package com.trazere.util.closure;

import com.trazere.util.function.Function0;
import com.trazere.util.text.Description;
import com.trazere.util.type.Either;
import com.trazere.util.type.Maybe;

/**
 * The {@link LinearClosure} class represents closures which are evaluated only once.
 * <p>
 * Once computed, values of the linear closures are memoized and the computation context is released.
 * 
 * @param <T> Type of the value.
 * @param <X> Type of the exceptions.
 */
public class LinearClosure<T, X extends Exception> implements Closure<T, X> {

    /**
	 * Build a closure evaluating to the result of the given function.
	 * 
	 * @param <T> Type of the value.
	 * @param <X> Type of the exceptions.
	 * @param function The function computing the value.
	 * @return The closure.
	 */
    public static <T, X extends Exception> LinearClosure<T, X> build(final Function0<? extends T, ? extends X> function) {
        return new LinearClosure<T, X>(function);
    }

    /** The value. */
    protected Either<Function0<? extends T, ? extends X>, T> _value;

    /**
	 * Instantiate a closure using the given function.
	 * 
	 * @param function The function computing the value.
	 */
    public LinearClosure(final Function0<? extends T, ? extends X> function) {
        assert null != function;
        _value = Either.<Function0<? extends T, ? extends X>, T>left(function);
    }

    public T evaluate() throws X {
        if (_value.isRight()) {
            return _value.asRight().getRight();
        } else {
            final T value = _value.asLeft().getLeft().evaluate();
            _value = Either.right(value);
            return value;
        }
    }

    public boolean isEvaluated() {
        return _value.isRight();
    }

    public Maybe<T> asMaybe() {
        return _value.isRight() ? Maybe.some(_value.asRight().getRight()) : Maybe.<T>none();
    }

    @Override
    public String toString() {
        if (_value.isRight()) {
            return String.valueOf(_value.asRight().getRight());
        } else {
            final Description description = Description.buildObjectDescription(this);
            description.append("Function", _value.asLeft().getLeft());
            return description.toString();
        }
    }
}
