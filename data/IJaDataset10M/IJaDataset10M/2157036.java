package nu.esox.util;

public class AndPredicate extends BinaryPredicate {

    public AndPredicate(PredicateIF left, PredicateIF right) {
        super(left, right);
    }

    protected boolean isTrue(PredicateIF left, PredicateIF right) {
        return left.isTrue() && right.isTrue();
    }

    static final long serialVersionUID = 5806067845682464164L;
}
