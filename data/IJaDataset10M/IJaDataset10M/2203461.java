package org.retro.neural;

class NotEqualToComparator extends FEComparator {

    protected boolean compare(FieldElement a, FieldElement b) {
        return !a.equals(b);
    }
}
