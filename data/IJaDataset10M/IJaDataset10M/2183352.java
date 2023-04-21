package org.dom4j.util;

import java.lang.ref.WeakReference;

/**
 * <p>
 * <code>PerThreadSingleton</code> is an implementation of the
 * SingletonStrategy used to provide common factory access to a single object
 * instance based on an implementation strategy for one object instance per
 * thread. This is useful in replace of the ThreadLocal usage.
 * </p>
 * 
 * @author <a href="mailto:ddlucas@users.sourceforge.net">David Lucas </a>
 * @version $Revision: 1.3 $
 */
public class PerThreadSingleton implements SingletonStrategy {

    private String singletonClassName = null;

    private ThreadLocal perThreadCache = new ThreadLocal();

    public PerThreadSingleton() {
    }

    public void reset() {
        perThreadCache = new ThreadLocal();
    }

    public Object instance() {
        Object singletonInstancePerThread = null;
        WeakReference ref = (WeakReference) perThreadCache.get();
        if (ref == null || ref.get() == null) {
            Class clazz = null;
            try {
                clazz = Thread.currentThread().getContextClassLoader().loadClass(singletonClassName);
                singletonInstancePerThread = clazz.newInstance();
            } catch (Exception ignore) {
                try {
                    clazz = Class.forName(singletonClassName);
                    singletonInstancePerThread = clazz.newInstance();
                } catch (Exception ignore2) {
                }
            }
            perThreadCache.set(new WeakReference(singletonInstancePerThread));
        } else {
            singletonInstancePerThread = ref.get();
        }
        return singletonInstancePerThread;
    }

    public void setSingletonClassName(String singletonClassName) {
        this.singletonClassName = singletonClassName;
    }
}
