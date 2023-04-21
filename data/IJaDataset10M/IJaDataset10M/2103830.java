package org.matsim.core.utils.misc;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * @author mrieser
 */
public abstract class ClassUtils {

    /**
	 * Returns all classes and implemented interfaces of the given class.
	 * For each of the returned classes, an according call to
	 * <code>instanceof</code> would also return <code>true</code>.
	 *
	 * @param klass
	 * @return all classes and implemented interfaces of the given class
	 */
    public static Set<Class<?>> getAllTypes(final Class<?> klass) {
        Set<Class<?>> set = new HashSet<Class<?>>();
        Stack<Class<?>> stack = new Stack<Class<?>>();
        stack.add(klass);
        while (!stack.isEmpty()) {
            Class<?> c = stack.pop();
            set.add(c);
            for (Class<?> k : c.getInterfaces()) {
                stack.push(k);
            }
            if (c.getSuperclass() != null) {
                stack.push(c.getSuperclass());
            }
        }
        return set;
    }
}
