package ch.ethz.globis.javaom.collection;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author aldespin
 * @version 1.0
 */
public class Collection {

    public interface Function<T> {

        Collection compute(final Object member);
    }

    protected static class Pair {

        private Collection first;

        private Collection second;

        public Pair(final Collection first, final Collection second) {
            this.first = first;
            this.second = second;
        }

        public Collection first() {
            return this.first;
        }

        public Collection second() {
            return this.second;
        }
    }

    private java.util.Collection<Object> collection;

    private String name;

    private Class memberType;

    private Collection(final String name, final Class memberType) {
        this.name = name;
        this.memberType = memberType;
        this.collection = new ArrayList<Object>();
    }

    private static String generateName() {
        String base = UUID.randomUUID().toString();
        base = base.replaceAll("-", "");
        final int length = base.length();
        final int start = (int) Math.round(Math.random() * (length - 4));
        final int maxLength = length - (start + 1);
        final int end = (int) Math.round(Math.random() * maxLength) + start + 1;
        return base.substring(start, end).toUpperCase();
    }

    private java.util.Collection<Object> get() {
        return this.collection;
    }

    protected Object aMember() {
        if (this.isEmpty()) {
            return null;
        }
        return this.get().iterator().next();
    }

    public String getName() {
        return this.name;
    }

    public Class getMemberType() {
        return this.memberType;
    }

    public boolean add(final Object member) {
        return this.get().add(member);
    }

    public static Collection empty() {
        return new Collection(Collection.generateName(), Object.class);
    }

    public boolean isEmpty() {
        return this.get().isEmpty();
    }

    public static Collection single(final Object member) {
        final Collection result = Collection.empty();
        result.get().add(member);
        return result;
    }

    public boolean isSingle() {
        return this.get().size() == 1;
    }

    public static Collection combine(final Collection lhs, final Collection rhs) {
        final Collection result = Collection.empty();
        for (Object current : lhs.get()) {
            result.get().add(current);
        }
        for (Object current : rhs.get()) {
            result.get().add(current);
        }
        return result;
    }

    public Pair split() {
        if (this.isEmpty()) {
            return null;
        }
        final Object aMember = this.aMember();
        Collection first = Collection.single(aMember);
        Collection second = Collection.empty();
        second.get().addAll(this.get());
        second.get().remove(aMember);
        return new Pair(first, second);
    }

    public static Collection iter(final Function f, final Collection collection) {
        final Collection result = Collection.empty();
        for (Object current : collection.get()) {
            result.get().addAll(f.compute(current).get());
        }
        return result;
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public String toString() {
        final StringBuffer result = new StringBuffer();
        result.append(this.getName() + "<" + this.getMemberType().getSimpleName() + ">\n [ ");
        for (Object current : this.get()) {
            result.append(current.toString() + " ");
        }
        result.append("]");
        return result.toString();
    }
}
