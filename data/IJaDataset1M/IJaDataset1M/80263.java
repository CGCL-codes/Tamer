package net.sourceforge.javautil.common.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.EventListener;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import net.sourceforge.javautil.common.CollectionUtil;
import net.sourceforge.javautil.common.proxy.AdaptableProxyHandlerAbstract;
import net.sourceforge.javautil.common.proxy.CollectionProxyCondition;
import net.sourceforge.javautil.common.reflection.cache.ClassCache;
import net.sourceforge.javautil.common.reflection.cache.ClassDescriptor;
import net.sourceforge.javautil.common.reflection.cache.ClassMethod;

/**
 * This allows for a {@link EventListener} typed interface along with {@link IEventPropagator} support
 * in a single proxy instance.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class EventProxy implements IEventPropagator, InvocationHandler {

    /**
	 * This assumes the use of the thread scoped class loader.
	 * 
	 * @see #createProxy(ClassLoader, Class, Class)
	 */
    public static <E extends EventObject, L extends java.util.EventListener> IEventPropagator createProxy(Class<L> listenerType, Class<E> eventType) {
        return createProxy(listenerType.getClassLoader(), listenerType, eventType);
    }

    /**
	 * @param <E> The type of events this proxy supports
	 * @param <L> The type of listeners this proxy supports
	 * @param loader The loader to use for creating the proxy
	 * @param listenerType The class of listener type
	 * @param eventType The class of event type
	 * @return The proxy propagator
	 */
    public static <E extends EventObject, L extends java.util.EventListener> IEventPropagator createProxy(ClassLoader loader, Class<L> listenerType, Class<E> eventType) {
        return (IEventPropagator<E, L>) Proxy.newProxyInstance(loader, new Class[] { IEventPropagator.class, listenerType }, new EventProxy(ClassCache.getFor(listenerType), ClassCache.getFor(eventType)));
    }

    protected final ClassDescriptor<? extends EventObject> eventType;

    protected final ClassDescriptor<? extends java.util.EventListener> listenerType;

    protected final List<java.util.EventListener> listeners = new CopyOnWriteArrayList<java.util.EventListener>();

    protected ClassMethod condition;

    protected final Map<String, ClassMethod> handlers = new LinkedHashMap<String, ClassMethod>();

    protected EventProxy(ClassDescriptor listenerType, ClassDescriptor eventType) {
        this.eventType = eventType;
        this.listenerType = listenerType;
        Map<String, List<ClassMethod>> methods = listenerType.getMethods();
        for (String name : methods.keySet()) {
            for (ClassMethod method : methods.get(name)) {
                if (method.getParameterTypes().length == 1 && eventType.getDescribedClass().isAssignableFrom(method.getParameterTypes()[0])) {
                    handlers.put(name, method);
                    break;
                }
            }
        }
        this.condition = listenerType.getMethod(CollectionProxyCondition.class);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.condition != null && this.condition.getJavaMember().equals(method)) return Boolean.TRUE;
        if (method.getDeclaringClass() == Object.class || method.getDeclaringClass() == IEventPropagator.class) {
            return method.invoke(this, args);
        }
        {
            for (java.util.EventListener listener : listeners) {
                if (this.condition != null) {
                    Boolean result = (Boolean) this.condition.invoke(listener, args);
                    if (result != null && !result.booleanValue()) continue;
                }
                method.invoke(listener, args);
            }
        }
        return null;
    }

    public void fire(Object source, String name, Object... arguments) {
        if (this.handlers.containsKey(name)) {
            ClassMethod handler = this.handlers.get(name);
            Event event = (Event) ClassCache.getFor(handler.getParameterTypes()[0]).newInstance(CollectionUtil.insert(arguments, 0, source, name));
            for (java.util.EventListener listener : listeners) {
                if (this.condition != null && (Boolean) this.condition.invoke(listener, event)) continue;
                handler.invoke(listener, event);
            }
        } else {
            EventObject event = eventType.newInstance(CollectionUtil.insert(arguments, 0, source, name));
            for (java.util.EventListener listener : listeners) {
                if (this.condition != null && !(Boolean) this.condition.invoke(listener, event)) continue;
                if (listener instanceof net.sourceforge.javautil.common.event.IEventListener) {
                    ((net.sourceforge.javautil.common.event.IEventListener) listener).handle(event);
                }
            }
        }
    }

    public void addListener(java.util.EventListener listener) {
        this.listeners.add(listener);
    }

    public void addListener(Object pojoListener) {
        this.listeners.add(EventDelegator.createProxy(listenerType.getDescribedClass().getClassLoader(), listenerType.getDescribedClass(), pojoListener));
    }

    public void removeListener(java.util.EventListener listener) {
        this.listeners.remove(listener);
    }

    public void removeListener(Object pojoListener) {
        EventListener l = null;
        for (EventListener listener : listeners) {
            if (Proxy.isProxyClass(listener.getClass())) {
                InvocationHandler handler = Proxy.getInvocationHandler(listener);
                if (handler instanceof EventDelegator) {
                    if (pojoListener == ((EventDelegator) handler).getTarget()) {
                        l = listener;
                        break;
                    }
                }
            }
        }
        if (l != null) listeners.remove(l);
    }
}
