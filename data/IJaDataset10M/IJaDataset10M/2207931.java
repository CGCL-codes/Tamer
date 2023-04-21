package agonism.ce;

import com.objectspace.jgl.Pair;
import com.objectspace.jgl.UnaryPredicate;

/**
 * Unary Predicate that returns the object if it is an instance of the class or
 * classes that are specified when the IsClassPredicate is constructed. Otherwise
 * it returns null.
 */
public class IsClassPredicate implements UnaryPredicate {

    private final Class[] m_classes;

    public IsClassPredicate(Class clazz) {
        this(new Class[] { clazz });
    }

    public IsClassPredicate(Class[] classes) {
        m_classes = classes;
    }

    public boolean execute(Object obj) {
        Object item;
        if (obj instanceof Pair) {
            item = ((Pair) obj).second;
        } else {
            item = obj;
        }
        for (int i = 0; i < m_classes.length; i++) {
            if (m_classes[i].isInstance(item)) {
                return true;
            }
        }
        return false;
    }
}
