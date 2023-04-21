package fi.tuska.jalkametri.util;

/**
 * A dumb wrapper for two objects stored in one object.
 */
public class Pair {

    private Object first;

    private Object second;

    public Pair(Object first, Object second) {
        this.first = first;
        this.second = second;
    }

    public Object getFirst() {
        return first;
    }

    public void setFirst(Object first) {
        this.first = first;
    }

    public Object getSecond() {
        return second;
    }

    public void setSecond(Object second) {
        this.second = second;
    }

    public String toString() {
        return "[" + first.toString() + ";" + second.toString() + "]";
    }

    public int hashCode() {
        return first != null ? first.hashCode() : 0;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Pair)) return false;
        Pair p = (Pair) o;
        if (first == null) return p.first == null;
        if (second == null) return p.second == null;
        return first.equals(p.first) && second.equals(p.second);
    }
}
