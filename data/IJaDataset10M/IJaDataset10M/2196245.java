package jfun.yan.monitoring;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * <p>
 * This class encapsulates a queue of various monitors.
 * {@link #getComponentMonitor()} creates a ComponentMonitor instance
 * that will sequentially call all the monitors in the queue.
 * </p>
 * @author Ben Yu.
 *
 */
public class ComponentMonitorQueue {

    private final ArrayList queue = new ArrayList();

    /**
   * Add a ComponentMonitor object to the queue.
   * @param mon the monitor object.
   * @return this ComponentMonitorQueue object.
   */
    public synchronized ComponentMonitorQueue addComponentMonitor(ComponentMonitor mon) {
        queue.add(mon);
        return this;
    }

    /**
   * Add a CtorMonitor object to the queue.
   * @param mon the monitor object.
   * @return this ComponentMonitorQueue object.
   */
    public synchronized ComponentMonitorQueue addCtorMonitor(CtorMonitor mon) {
        queue.add(mon);
        return this;
    }

    /**
   * Add a MethodMonitor object to the queue.
   * @param mon the monitor object.
   * @return this ComponentMonitorQueue object.
   */
    public synchronized ComponentMonitorQueue addMethodMonitor(MethodMonitor mon) {
        queue.add(mon);
        return this;
    }

    /**
   * Add a GetterMonitor object to the queue.
   * @param mon the monitor object.
   * @return this ComponentMonitorQueue object.
   */
    public synchronized ComponentMonitorQueue addCetterMonitor(GetterMonitor mon) {
        queue.add(mon);
        return this;
    }

    /**
   * Add a SetterMonitor object to the queue.
   * @param mon the monitor object.
   * @return this ComponentMonitorQueue object.
   */
    public synchronized ComponentMonitorQueue addSetterMonitor(SetterMonitor mon) {
        queue.add(mon);
        return this;
    }

    /**
   * Add an IndexedGetterMonitor object to the queue.
   * @param mon the monitor object.
   * @return this ComponentMonitorQueue object.
   */
    public synchronized ComponentMonitorQueue addGetterMonitor(IndexedGetterMonitor mon) {
        queue.add(mon);
        return this;
    }

    /**
   * Add an IndexedSetterMonitor object to the queue.
   * @param mon the monitor object.
   * @return this ComponentMonitorQueue object.
   */
    public synchronized ComponentMonitorQueue addSetterMonitor(IndexedSetterMonitor mon) {
        queue.add(mon);
        return this;
    }

    private synchronized Object[] getMonitors() {
        return queue.toArray();
    }

    /**
   * To create a ComponentMonitor instance that sequentially invoke
   * the corresponding methods of all the monitors in the queue.
   */
    public ComponentMonitor getComponentMonitor() {
        return (ComponentMonitor) java.lang.reflect.Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { ComponentMonitor.class }, new java.lang.reflect.InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return fire(method, args);
            }
        });
    }

    private Object fire(Method mtd, Object[] args) throws Throwable {
        final Object[] monitors = getMonitors();
        for (int i = 0; i < monitors.length; i++) {
            final Object mon = monitors[i];
            if (mtd.getDeclaringClass().isInstance(mon)) {
                try {
                    mtd.invoke(mon, args);
                } catch (InvocationTargetException e) {
                    final Throwable cause = e.getTargetException();
                    if (cause != null) throw cause; else throw e;
                }
            }
        }
        return null;
    }
}
