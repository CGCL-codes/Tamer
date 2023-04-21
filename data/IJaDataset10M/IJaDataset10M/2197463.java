package de.denkselbst.niffler.prologservice.swi;

import jpl.Term;

public class ExampleAtIndex {

    public final Term example;

    public final int index;

    public ExampleAtIndex(Term t, int i) {
        example = t;
        index = i;
    }

    public boolean isPoisonPill() {
        return example == null && index == -1;
    }
}
