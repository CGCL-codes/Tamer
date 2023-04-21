package gap.jac.reflect.generics.scope;

import java.lang.reflect.TypeVariable;

/**
 * This class is used to provide enclosing scopes for top level classes.
 * We cannot use <tt>null</tt> to represent such a scope, since the
 * enclosing scope is computed lazily, and so the field storing it is
 * null until it has been computed. Therefore, <tt>null</tt> is reserved
 * to represent an as-yet-uncomputed scope, and cannot be used for any
 * other kind of scope.
 */
public class DummyScope implements Scope {

    private static DummyScope singleton = new DummyScope();

    private DummyScope() {
    }

    /**
     * Factory method. Enforces the singleton pattern - only one
     * instance of this class ever exists.
     */
    public static DummyScope make() {
        return singleton;
    }

    /**
     * Lookup a type variable in the scope, using its name. Always returns
     * <tt>null</tt>.
     * @param name - the name of the type variable being looked up
     * @return  null
     */
    public TypeVariable<?> lookup(String name) {
        return null;
    }
}
