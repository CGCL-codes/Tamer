package swarm;

/**
 * A heterogenious pair (2-tuple). 
 * 
 * @see #pair(Object, Object)
 * 
 * @param <A> The type of the first member
 * @param <B> The type of the second member.
 */
public final class Pair<A, B> {

    public final A first;

    public final B second;

    public Pair(A first, B second) {
        super();
        this.first = first;
        this.second = second;
    }

    /**
	 * A static method that simply calls the contructor. The reason for this is that it
	 * allows the compiler to infer the type arguments.
	 * 
	 * @param <A>
	 * @param <B>
	 * @param a
	 * @param b
	 * @return
	 */
    public static <A, B> Pair<A, B> pair(A a, B b) {
        return new Pair<A, B>(a, b);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((first == null) ? 0 : first.hashCode());
        result = prime * result + ((second == null) ? 0 : second.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Pair<?, ?> other = (Pair<?, ?>) obj;
        if (first == null) {
            if (other.first != null) return false;
        } else if (!first.equals(other.first)) return false;
        if (second == null) {
            if (other.second != null) return false;
        } else if (!second.equals(other.second)) return false;
        return true;
    }
}
