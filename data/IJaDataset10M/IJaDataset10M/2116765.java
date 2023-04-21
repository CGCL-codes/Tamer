package edu.neu.ccs.demeterf.lib;

/** Represents Nothing... i.e., when an optional part is missing */
public class None<X> extends Option<X> {

    public None() {
    }

    public boolean isSome() {
        return false;
    }

    public X inner() {
        throw new RuntimeException("None: has nothing!");
    }

    public boolean equals(Object o) {
        return (o instanceof None);
    }

    public String toString() {
        return "None(" + ")";
    }

    public <Y> Option<Y> apply(List.Map<X, Y> m) {
        return Option.<Y>none();
    }
}
