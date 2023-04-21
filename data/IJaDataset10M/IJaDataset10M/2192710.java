package org.jtools.condition;

import java.util.Collection;
import org.jpattern.condition.Condition;
import org.jpattern.condition.False;
import org.jpattern.condition.True;

/**
 * Returns <code>true</code> if exactly one childfilter return <code>true</code>
 * and all other childfilters (if available) returns <code>false</code>.
 * Otherwise this filter returns <code>false</code>.
 * This also implies, that this filter returns <code>false</code> if no
 * childfilters are available.
 */
public final class Xor<T_Filterable> extends ConditionContainer<T_Filterable> {

    @SuppressWarnings("unchecked")
    private static final Xor xorEmpty = new Xor();

    @SuppressWarnings("unchecked")
    private static final Xor xorNull = new Xor(Null.getInstance());

    @SuppressWarnings("unchecked")
    private static final Xor xorTrue = new Xor(True.getInstance());

    @SuppressWarnings("unchecked")
    private static final Xor xorFalse = new Xor(False.getInstance());

    @SuppressWarnings("unchecked")
    public static <T> Xor<T> valueOf(Null<? super T> condition) {
        return xorNull;
    }

    @SuppressWarnings("unchecked")
    public static <T> Xor<T> valueOf(True<? super T> condition) {
        return xorTrue;
    }

    @SuppressWarnings("unchecked")
    public static <T> Xor<T> valueOf(False<? super T> condition) {
        return xorFalse;
    }

    @SuppressWarnings("unchecked")
    public static <T> Condition<T> valueOf(Condition<? super T>... conditions) {
        if (conditions == null || conditions.length == 0) return xorEmpty;
        if (conditions.length == 1) {
            if (conditions[0] instanceof Null) return xorNull;
            if (conditions[0] instanceof True) return xorTrue;
            if (conditions[0] instanceof False) return xorFalse;
        }
        return new Xor<T>(conditions);
    }

    @SuppressWarnings("unchecked")
    public static <T> Condition<T> valueOf(Collection<Condition<? super T>> conditions) {
        if (conditions == null) return valueOf();
        return valueOf(conditions.toArray(new Condition[conditions.size()]));
    }

    public Xor(Collection<Condition<? super T_Filterable>> conditions) {
        super(conditions);
    }

    public Xor(Condition<? super T_Filterable>... conditions) {
        super(conditions);
    }

    public boolean match(T_Filterable filterable) {
        int matches = 0;
        for (Condition<? super T_Filterable> c : conditions) if ((c != null) && c.match(filterable)) {
            matches++;
            if (matches > 1) return false;
        }
        return (matches == 1);
    }
}
