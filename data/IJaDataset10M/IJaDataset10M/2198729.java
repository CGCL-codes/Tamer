package org.dfdaemon.il2.core.event.util;

import org.dfdaemon.il2.core.event.AcceptEvents;
import org.dfdaemon.il2.core.event.Event;
import org.dfdaemon.il2.core.event.EventHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author octo
 */
public class AcceptEventInvoker {

    private WeakHashMap<Class<? extends EventHandler>, Map<Class<? extends Event>, Set<Method>>> cache = new WeakHashMap<Class<? extends EventHandler>, Map<Class<? extends Event>, Set<Method>>>();

    public void invokeMethods(EventHandler handler, Event event) throws InvocationTargetException, IllegalAccessException {
        final Map<Class<? extends Event>, Set<Method>> map = findClassMapping(handler.getClass());
        for (Class<? extends Event> eventClass : map.keySet()) {
            if (eventClass.isAssignableFrom(event.getClass())) {
                for (Method method : map.get(eventClass)) {
                    method.invoke(handler, event);
                }
            }
        }
    }

    public Map<Class<? extends Event>, Set<Method>> findClassMapping(Class<? extends EventHandler> aClass) {
        Map<Class<? extends Event>, Set<Method>> map = cache.get(aClass);
        if (map == null) {
            map = new HashMap<Class<? extends Event>, Set<Method>>();
            getMethodsForEvent(aClass, map);
            cache.put(aClass, map);
        }
        return map;
    }

    @SuppressWarnings({ "SuspiciousMethodCalls", "unchecked" })
    private void getMethodsForEvent(final Class<?> aClass, final Map<Class<? extends Event>, Set<Method>> map) {
        final Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            final AcceptEvents annotation = method.getAnnotation(AcceptEvents.class);
            if (annotation == null) continue;
            if (annotation.events().length == 0) {
                final Class<?>[] pClasses = method.getParameterTypes();
                if (pClasses.length < 1) throw new IllegalArgumentException("Need at least 1 parameter for empty AcceptEvent annotation: " + aClass.getName() + ": " + method.getName());
                Class<? extends Event> eventClass = null;
                for (Class<?> pClass : pClasses) {
                    if (Event.class.isAssignableFrom(pClass)) {
                        eventClass = (Class<? extends Event>) pClass;
                        break;
                    }
                }
                if (eventClass == null) {
                    throw new IllegalArgumentException("Need at least 1 parameter for empty AcceptEvent annotation which extends Event.class: " + aClass.getName() + ": " + method.getName());
                }
                Set<Method> rms = map.get(eventClass);
                if (rms == null) {
                    rms = new HashSet<Method>();
                    map.put(eventClass, rms);
                }
                method.setAccessible(true);
                rms.add(method);
            } else {
                for (Class<? extends Event> eventClass : annotation.events()) {
                    Set<Method> rms = map.get(eventClass);
                    if (rms == null) {
                        rms = new HashSet<Method>();
                        map.put(eventClass, rms);
                    }
                    method.setAccessible(true);
                    rms.add(method);
                }
            }
        }
    }
}
