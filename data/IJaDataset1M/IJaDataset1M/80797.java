package org.jmathematics.calc.type;

import java.util.Comparator;

public interface Type<T> extends Comparator<T> {

    boolean isZero(T value);

    boolean isOne(T value);

    T getZero();

    T getOne();

    T divide(T dividend, T divisor);

    T subtract(T minuend, T subtrahend);

    T multiply(T multiplier, T multiplicand);

    T add(T augend, T addend);
}
