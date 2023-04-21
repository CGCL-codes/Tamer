package org.jthompson.monsoon;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.Proxy;
import org.apache.log4j.Logger;
import org.jthompson.monsoon.annotations.Event;
import org.jthompson.monsoon.annotations.Event.EventType;

public class EventExecutor implements MethodInterceptor {

    private Object obj;

    public EventExecutor() {
    }

    public EventExecutor(Object obj) {
        this.obj = obj;
    }

    public Object getObject() {
        return this.obj;
    }

    private List<Thread> executingThreads = new ArrayList<Thread>();

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        log.info("Invoking method on CGLIB proxy");
        Object retValFromSuper = null;
        try {
            if (!Modifier.isAbstract(method.getModifiers())) {
                retValFromSuper = proxy.invokeSuper(obj, args);
            }
        } finally {
            if (method.isAnnotationPresent(Event.class)) {
                Event event = method.getAnnotation(Event.class);
                if (event.type().equals(EventType.DISPATCHER)) {
                    List<Object> targets = EventMapping.instance().getEventMapping(event.name());
                    for (Object target : targets) {
                        dispatchEvent(target, retValFromSuper, event.async());
                    }
                    if (event.waitForCompletion()) {
                        for (Thread t : executingThreads) {
                            try {
                                t.join();
                            } catch (InterruptedException e) {
                                log.error("Join interrupted", e);
                            }
                        }
                    }
                }
            }
        }
        return retValFromSuper;
    }

    private void dispatchEvent(Object target, Object value, boolean async) throws Throwable {
        Method[] methods = getMethods(target);
        for (Method m : methods) {
            if (m.isAnnotationPresent(Event.class)) {
                Event methodEvent = m.getAnnotation(Event.class);
                if (methodEvent.type().equals(EventType.LISTENER)) {
                    if (async) {
                        executingThreads.add(invokeAsynchronous(m, target, value));
                    } else {
                        invokeSynchronous(m, target, value);
                    }
                }
            }
        }
    }

    private void invokeSynchronous(Method method, Object target, Object value) throws Exception {
        method.invoke(target, value);
    }

    private Thread invokeAsynchronous(final Method method, final Object target, final Object value) throws Exception {
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    method.invoke(target, value);
                } catch (Exception e) {
                    log.error("Error invoking event", e);
                }
            }
        });
        t.start();
        return t;
    }

    private Method[] getMethods(Object target) {
        Method[] methods = null;
        if (Proxy.isProxyClass(target.getClass())) {
            target = target.getClass().getSuperclass();
        } else if (Enhancer.isEnhanced(target.getClass())) {
            List<Method> methodList = new ArrayList<Method>();
            Enhancer.getMethods(target.getClass().getSuperclass(), target.getClass().getInterfaces(), methodList);
            methods = (Method[]) methodList.toArray(new Method[0]);
        } else {
            methods = target.getClass().getMethods();
        }
        return methods;
    }

    private static final Logger log = Logger.getLogger(EventExecutor.class);
}
