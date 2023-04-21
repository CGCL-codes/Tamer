package net.sourceforge.freejava.reflect.query;

public class EqualsName implements INamePredicate {

    private final String name;

    private final INamePredicate next;

    public EqualsName(String name) {
        this(name, null);
    }

    public EqualsName(String name, INamePredicate next) {
        if (name == null) throw new NullPointerException("name");
        this.name = name;
        this.next = next;
    }

    @Override
    public INamePredicate next() {
        return next;
    }

    @Override
    public boolean test(String name) {
        if (!this.name.equals(name)) return false;
        if (next != null && !next.test(name)) return false;
        return true;
    }
}
