package org.jmathematics.impl;

import org.jmathematics.number.IntegerNumber;
import org.jmathematics.number.Numbers;

public abstract class AbstractIntegerNumber extends AbstractRationalNumber implements IntegerNumber {

    public String toString() {
        return String.valueOf(toBigInteger());
    }

    public final IntegerNumber getDenumerator() {
        return Numbers.ONE;
    }

    public final IntegerNumber getNumerator() {
        return this;
    }

    @Override
    public boolean isClosedRepresentation() {
        return true;
    }
}
