package util;

/**
 * @author barkholt
 * 
 */
public class UnorderedPair<T> {

    private T one;

    private T other;

    public UnorderedPair(T one, T other) {
        if (one.hashCode() > other.hashCode()) {
            this.one = one;
            this.other = other;
        } else {
            this.one = other;
            this.other = one;
        }
    }

    public T getOne() {
        return one;
    }

    public T getOther() {
        return other;
    }

    public int hashCode() {
        return (one.toString() + other.toString()).hashCode();
    }

    public boolean equals(Object other) {
        return hashCode() == other.hashCode();
    }
}
