package net.sourceforge.javautil.common.proxy;

import java.awt.Image;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sourceforge.javautil.common.exception.ThrowableManagerRegistry;
import net.sourceforge.javautil.common.reflection.cache.ClassCache;
import net.sourceforge.javautil.common.reflection.cache.ClassMethod;

/**
 * A proxy that will take an interface of whose type a collection will contain
 * to invoke all the types in the collection for each proxy method invocation.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: CollectionTargetProxy.java 2461 2010-10-24 11:36:56Z ponderator $
 * @param <T>
 */
public class CollectionTargetProxy<T> implements InvocationHandler {

    /**
	 * Assumes the current thread class loader.
	 * 
	 * @see #create(ClassLoader, Class, Collection)
	 */
    public static <I> I create(Class<I> iface, Collection<I> listenerList) {
        return create(Thread.currentThread().getContextClassLoader(), iface, listenerList);
    }

    /**
	 * Assumes that exception protection is turned off
	 * 
	 * @see #create(ClassLoader, Class, Collection, boolean)
	 */
    public static <I> I create(ClassLoader cl, Class<I> iface, Collection<I> listenerList) {
        return create(cl, iface, listenerList, false);
    }

    /**
	 * @param <I> The type of interface/listener
	 * @param cl The class loader
	 * @param iface The listener interface
	 * @param listenerList The list used to get the current set of listeners for each event propogation
	 * @param exceptionProtect True if each call to the different listeners/targets should be wrapped and not cause other to fail when 
	 * 	an exception is thrown, otherwise false
	 * 
	 * @return A proxy for invocation of events that will be propogated to the listeners currently on the list for
	 * each invocation.
	 */
    public static <I> I create(ClassLoader cl, Class<I> iface, Collection<I> listenerList, boolean exceptionProtect) {
        return (I) Proxy.newProxyInstance(cl, new Class[] { iface }, new CollectionTargetProxy<I>(iface, listenerList, exceptionProtect));
    }

    private final Collection<T> targets;

    private final Class<T> interfaceClass;

    private final ClassMethod condition;

    private final boolean exceptionProtect;

    private CollectionTargetProxy(Class<T> interfaceClass, Collection<T> list, boolean exceptionProtect) {
        this.interfaceClass = interfaceClass;
        this.targets = list;
        this.exceptionProtect = exceptionProtect;
        this.condition = ClassCache.getFor(interfaceClass).getMethod(CollectionProxyCondition.class);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) return method.invoke(targets, args);
        ResultToArgument rta = method.getAnnotation(ResultToArgument.class);
        if (targets.size() == 0) {
            return rta != null ? args[rta.value()] : null;
        }
        List<T> target = new ArrayList<T>(this.targets);
        Object result = null;
        for (T listener : target) {
            if (this.condition != null && !(Boolean) this.condition.invoke(listener, args)) continue;
            if (this.exceptionProtect) {
                try {
                    result = method.invoke(listener, args);
                } catch (Exception e) {
                    ThrowableManagerRegistry.caught(e);
                }
            } else result = method.invoke(listener, args);
            if (rta != null) args[rta.value()] = result;
        }
        return target.size() == 0 && rta != null ? args[rta.value()] : result;
    }

    /**
	 * @param target The target to be added to collective invocations
	 */
    public void addTarget(T target) {
        synchronized (targets) {
            this.targets.add(target);
        }
    }

    /**
	 * @param target The target to be removed from collective invocations
	 */
    public void removeTarget(T target) {
        synchronized (targets) {
            this.targets.remove(target);
        }
    }

    public String toString() {
        return "CollectionTargetProxy[" + this.interfaceClass + "]";
    }
}
