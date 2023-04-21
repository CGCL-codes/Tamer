package clusterscomparator;

import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @param <T>
 * @author misiek
 */
public class Cluster<T> implements Comparable<Cluster<T>> {

    private T name;

    private Collection<T> elements = new HashSet<T>();

    public Cluster(final T name) {
        this.name = name;
    }

    public T getName() {
        return name;
    }

    public int size() {
        return elements.size();
    }

    public void add(final T element) {
        elements.add(element);
    }

    public Collection<T> getElements() {
        return elements;
    }

    public int compareTo(final Cluster<T> cluster) {
        if (cluster == null) {
            return -1;
        }
        if (this.size() > cluster.size()) {
            return -1;
        } else if (this.size() < cluster.size()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(final Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        } else {
            Cluster<T> cluster = (Cluster<T>) obj;
            return (this.compareTo(cluster) == 0);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
