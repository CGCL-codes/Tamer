package net.community.chest.math.compare;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <P>Copyright GPLv2</P>
 *
 * @param <V> Type of negated comparator
 * @author Lyor G.
 * @since May 27, 2009 2:32:24 PM
 */
public class ComparatorNegator<V> implements InvocationHandler {

    private final V _c;

    public final V getComparator() {
        return _c;
    }

    public ComparatorNegator(final V c) throws IllegalArgumentException {
        if (null == (_c = c)) throw new IllegalArgumentException("No comparator instance provided");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Object c = getComparator(), v = method.invoke(c, args);
        if (v instanceof Boolean) return Boolean.valueOf(!((Boolean) v).booleanValue());
        return v;
    }

    public static final <V> V negate(Class<V> vc, V c) throws IllegalArgumentException {
        if (null == c) return null;
        if ((null == vc) || (!vc.isInterface())) throw new IllegalArgumentException("negate(" + ((null == vc) ? null : vc.getName()) + ") missing or not an interface");
        final Thread t = Thread.currentThread();
        final ClassLoader cl = (null == t) ? null : t.getContextClassLoader();
        final Class<?>[] ca = { vc };
        return vc.cast(Proxy.newProxyInstance(cl, ca, new ComparatorNegator<V>(c)));
    }
}
