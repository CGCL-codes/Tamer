package edu.udo.cs.ai.nemoz.model.entities;

/**
 * TODO Enter a type comment here!
 *
 * @author oflasch
 */
public class Aspect implements Entity<Aspect> {

    protected final String name;

    public Aspect(final String name) {
        if (name == null) throw new IllegalArgumentException("aspect name must not be null");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "A:" + name;
    }

    @Override
    public boolean equals(final Object rhs) {
        if (rhs == this) return true;
        if (!(rhs instanceof Aspect)) return false;
        return name.equals(((Aspect) rhs).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public int compareTo(final Aspect rhs) {
        return name.compareTo(rhs.name);
    }
}
