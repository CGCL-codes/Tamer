package common.utilities.fp;

import java.util.ArrayList;
import java.util.Collection;

public class FList<E> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;

    public FList() {
        super();
    }

    public FList(Collection<? extends E> c) {
        super(c);
    }

    public FList(int initialCapacity) {
        super(initialCapacity);
    }

    public UnaryPredicate<E> containsNot = new UnaryPredicate<E>() {

        public boolean eval(E element) {
            return !contains(element);
        }
    };

    public UnaryPredicate<E> contains = new UnaryPredicate<E>() {

        public boolean eval(E element) {
            return contains(element);
        }
    };
}
