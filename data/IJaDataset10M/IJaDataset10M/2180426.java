package org.retro.neural;

class NotOperator implements MonadicOperator {

    public FieldElement apply(FieldElement x) {
        return x.isZero() ? x.one() : x.zero();
    }
}
