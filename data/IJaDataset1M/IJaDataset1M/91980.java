package com.raelity.org.openide.util;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.EventListener;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A listener wrapper that delegates to another listener but hold
 * only weak reference to it, so it does not prevent it to be finalized.
 *
 * @author Jaroslav Tulach
 */
abstract class WeakListenerImpl implements java.util.EventListener {

    private static final Logger LOG = Logger.getLogger(WeakListenerImpl.class.getName());

    /** weak reference to listener */
    private ListenerReference ref;

    /** class of the listener */
    Class listenerClass;

    /** weak reference to source */
    private Reference<Object> source;

    /**
     * @param listenerClass class/interface of the listener
     * @param l listener to delegate to, <code>l</code> must be an instance of
     * listenerClass
     */
    protected WeakListenerImpl(Class listenerClass, java.util.EventListener l) {
        this.listenerClass = listenerClass;
        ref = new ListenerReference(l, this);
    }

    /** Setter for the source field. If a WeakReference to an underlying listener is
     * cleared and enqueued, that is, the original listener is garbage collected,
     * then the source field is used for deregistration of this WeakListenerImpl, thus making
     * it eligible for garbage collection if no more references exist.
     *
     * This method is particularly useful in cases where the underlying listener was
     * garbage collected and the event source, on which this listener is listening on,
     * is quiet, i.e. does not fire any events for long periods. In this case, this listener
     * is not removed from the event source until an event is fired. If the source field is
     * set however, WeakListenerImpls that lost their underlying listeners are removed
     * as soon as the ReferenceQueue notifies the WeakListenerImpl.
     *
     * @param source is any Object or <code>null</code>, though only setting an object
     * that has an appropriate remove*listenerClass*Listener method and on which this listener is listening on,
     * is useful.
     */
    protected final void setSource(Object source) {
        if (source == null) {
            this.source = null;
        } else {
            this.source = new WeakReference<Object>(source);
        }
    }

    /** Method name to use for removing the listener.
    * @return name of method of the source object that should be used
    *   to remove the listener from listening on source of events
    */
    protected abstract String removeMethodName();

    /** Getter for the target listener.
    * @param ev the event the we want to distribute
    * @return null if there is no listener because it has been finalized
    */
    protected final java.util.EventListener get(java.util.EventObject ev) {
        Object l = ref.get();
        if (l == null) {
            ref.requestCleanUp((ev == null) ? null : ev.getSource());
        }
        return (EventListener) l;
    }

    Object getImplementator() {
        return this;
    }

    public String toString() {
        Object listener = ref.get();
        return getClass().getName() + "[" + ((listener == null) ? "null" : (listener.getClass().getName() + "]"));
    }

    public static <T extends EventListener> T create(Class<T> lType, Class<? super T> apiType, T l, Object source) {
        ProxyListener pl = new ProxyListener(lType, apiType, l);
        pl.setSource(source);
        return lType.cast(pl.proxy);
    }

    /** Weak property change listener
    */
    static class PropertyChange extends WeakListenerImpl implements PropertyChangeListener {

        /** Constructor.
        * @param l listener to delegate to
        */
        public PropertyChange(PropertyChangeListener l) {
            super(PropertyChangeListener.class, l);
        }

        /** Constructor.
        * @param clazz required class
        * @param l listener to delegate to
        */
        PropertyChange(Class clazz, PropertyChangeListener l) {
            super(clazz, l);
        }

        /** Tests if the object we reference to still exists and
        * if so, delegate to it. Otherwise remove from the source
        * if it has removePropertyChangeListener method.
        */
        public void propertyChange(PropertyChangeEvent ev) {
            PropertyChangeListener l = (PropertyChangeListener) super.get(ev);
            if (l != null) {
                l.propertyChange(ev);
            }
        }

        /** Method name to use for removing the listener.
        * @return name of method of the source object that should be used
        *   to remove the listener from listening on source of events
        */
        protected String removeMethodName() {
            return "removePropertyChangeListener";
        }
    }

    /** Weak vetoable change listener
    */
    static class VetoableChange extends WeakListenerImpl implements VetoableChangeListener {

        /** Constructor.
        * @param l listener to delegate to
        */
        public VetoableChange(VetoableChangeListener l) {
            super(VetoableChangeListener.class, l);
        }

        /** Tests if the object we reference to still exists and
        * if so, delegate to it. Otherwise remove from the source
        * if it has removePropertyChangeListener method.
        */
        public void vetoableChange(PropertyChangeEvent ev) throws PropertyVetoException {
            VetoableChangeListener l = (VetoableChangeListener) super.get(ev);
            if (l != null) {
                l.vetoableChange(ev);
            }
        }

        /** Method name to use for removing the listener.
        * @return name of method of the source object that should be used
        *   to remove the listener from listening on source of events
        */
        protected String removeMethodName() {
            return "removeVetoableChangeListener";
        }
    }

    /** Weak document modifications listener.
    * This class if final only for performance reasons,
    * can be happily unfinaled if desired.
    */
    static final class Document extends WeakListenerImpl implements DocumentListener {

        /** Constructor.
        * @param l listener to delegate to
        */
        public Document(final DocumentListener l) {
            super(DocumentListener.class, l);
        }

        /** Gives notification that an attribute or set of attributes changed.
        * @param ev event describing the action
        */
        public void changedUpdate(DocumentEvent ev) {
            final DocumentListener l = docGet(ev);
            if (l != null) {
                l.changedUpdate(ev);
            }
        }

        /** Gives notification that there was an insert into the document.
        * @param ev event describing the action
        */
        public void insertUpdate(DocumentEvent ev) {
            final DocumentListener l = docGet(ev);
            if (l != null) {
                l.insertUpdate(ev);
            }
        }

        /** Gives notification that a portion of the document has been removed.
        * @param ev event describing the action
        */
        public void removeUpdate(DocumentEvent ev) {
            final DocumentListener l = docGet(ev);
            if (l != null) {
                l.removeUpdate(ev);
            }
        }

        /** Method name to use for removing the listener.
        * @return name of method of the source object that should be used
        *   to remove the listener from listening on source of events
        */
        protected String removeMethodName() {
            return "removeDocumentListener";
        }

        /** Getter for the target listener.
        * @param event the event the we want to distribute
        * @return null if there is no listener because it has been finalized
        */
        private DocumentListener docGet(DocumentEvent ev) {
            DocumentListener l = (DocumentListener) super.ref.get();
            if (l == null) {
                super.ref.requestCleanUp(ev.getDocument());
            }
            return l;
        }
    }

    /** Weak swing change listener.
    * This class if final only for performance reasons,
    * can be happily unfinaled if desired.
    */
    static final class Change extends WeakListenerImpl implements ChangeListener {

        /** Constructor.
        * @param l listener to delegate to
        */
        public Change(ChangeListener l) {
            super(ChangeListener.class, l);
        }

        /** Called when new file system is added to the pool.
        * @param ev event describing the action
        */
        public void stateChanged(final ChangeEvent ev) {
            ChangeListener l = (ChangeListener) super.get(ev);
            if (l != null) {
                l.stateChanged(ev);
            }
        }

        /** Method name to use for removing the listener.
        * @return name of method of the source object that should be used
        *   to remove the listener from listening on source of events
        */
        protected String removeMethodName() {
            return "removeChangeListener";
        }
    }

    /** Weak version of focus listener.
    * This class if final only for performance reasons,
    * can be happily unfinaled if desired.
    */
    static final class Focus extends WeakListenerImpl implements FocusListener {

        /** Constructor.
        * @param l listener to delegate to
        */
        public Focus(FocusListener l) {
            super(FocusListener.class, l);
        }

        /** Delegates to the original listener.
        */
        public void focusGained(FocusEvent ev) {
            FocusListener l = (FocusListener) super.get(ev);
            if (l != null) {
                l.focusGained(ev);
            }
        }

        /** Delegates to the original listener.
        */
        public void focusLost(FocusEvent ev) {
            FocusListener l = (FocusListener) super.get(ev);
            if (l != null) {
                l.focusLost(ev);
            }
        }

        /** Method name to use for removing the listener.
        * @return name of method of the source object that should be used
        *   to remove the listener from listening on source of events
        */
        protected String removeMethodName() {
            return "removeFocusListener";
        }
    }

    /** Proxy interface that delegates to listeners.
    */
    private static class ProxyListener extends WeakListenerImpl implements InvocationHandler {

        /** Equals method */
        private static Method equalsMth;

        /** Class -> Reference(Constructor) */
        private static final Map<Class, Reference<Constructor>> constructors = new WeakHashMap<Class, Reference<Constructor>>();

        /** proxy generated for this listener */
        public final Object proxy;

        /** @param listener listener to delegate to
        */
        public ProxyListener(Class c, Class api, java.util.EventListener listener) {
            super(api, listener);
            try {
                Reference ref = (Reference) constructors.get(c);
                Constructor proxyConstructor = (ref == null) ? null : (Constructor) ref.get();
                if (proxyConstructor == null) {
                    Class<?> proxyClass = Proxy.getProxyClass(c.getClassLoader(), new Class[] { c });
                    proxyConstructor = proxyClass.getConstructor(new Class[] { InvocationHandler.class });
                    constructors.put(c, new SoftReference<Constructor>(proxyConstructor));
                }
                Object p;
                try {
                    p = proxyConstructor.newInstance(new Object[] { this });
                } catch (java.lang.NoClassDefFoundError err) {
                    p = Proxy.newProxyInstance(c.getClassLoader(), new Class[] { c }, this);
                }
                proxy = p;
            } catch (Exception ex) {
                throw (IllegalStateException) new IllegalStateException(ex.toString()).initCause(ex);
            }
        }

        /** */
        private static Method getEquals() {
            if (equalsMth == null) {
                try {
                    equalsMth = Object.class.getMethod("equals", new Class[] { Object.class });
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            return equalsMth;
        }

        public java.lang.Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass() == Object.class) {
                if (method == getEquals()) {
                    boolean ret = equals(args[0]);
                    return (ret ? Boolean.TRUE : Boolean.FALSE);
                }
                return method.invoke(this, args);
            }
            EventObject ev = ((args != null) && (args[0] instanceof EventObject)) ? (EventObject) args[0] : null;
            Object listener = super.get(ev);
            if (listener != null) {
                return method.invoke(listener, args);
            } else {
                return null;
            }
        }

        /** Remove method name is composed from the name of the listener.
        */
        protected String removeMethodName() {
            String name = listenerClass.getName();
            int dot = name.lastIndexOf('.');
            name = name.substring(dot + 1);
            int i = name.lastIndexOf('$');
            if (i >= 0) {
                name = name.substring(i + 1);
            }
            return "remove".concat(name);
        }

        /** To string prints class.
        */
        public String toString() {
            return super.toString() + "[" + listenerClass + "]";
        }

        /** Equal is extended to equal also with proxy object.
        */
        public boolean equals(Object obj) {
            return (proxy == obj) || (this == obj);
        }

        Object getImplementator() {
            return proxy;
        }
    }

    /** Reference that also holds ref to WeakListenerImpl.
    */
    private static final class ListenerReference extends WeakReference<Object> implements Runnable {

        private static Class lastClass;

        private static String lastMethodName;

        private static Method lastRemove;

        private static Object LOCK = new Object();

        WeakListenerImpl weakListener;

        public ListenerReference(Object ref, WeakListenerImpl weakListener) {
            super(ref, Utilities.activeReferenceQueue());
            this.weakListener = weakListener;
        }

        /** Requestes cleanup of the listener with a provided source.
         * @param source source of the cleanup
         */
        public synchronized void requestCleanUp(Object source) {
            if (weakListener == null) {
                return;
            }
            if (weakListener.source != source) {
                weakListener.source = new WeakReference<Object>(source) {

                    ListenerReference doNotGCRef = new ListenerReference(new Object(), weakListener);
                };
            }
        }

        public void run() {
            Object[] params = new Object[1];
            Class[] types = new Class[1];
            Object src = null;
            Method remove = null;
            WeakListenerImpl ref;
            synchronized (this) {
                ref = weakListener;
                if ((ref.source == null) || ((src = ref.source.get()) == null)) {
                    return;
                }
                weakListener = null;
            }
            Class methodClass;
            if (src instanceof Class) {
                methodClass = (Class) src;
            } else {
                methodClass = src.getClass();
            }
            String methodName = ref.removeMethodName();
            synchronized (LOCK) {
                if ((lastClass == methodClass) && (lastMethodName == methodName) && (lastRemove != null)) {
                    remove = lastRemove;
                }
            }
            if (remove == null) {
                types[0] = ref.listenerClass;
                remove = getRemoveMethod(methodClass, methodName, types[0]);
                if (remove == null) {
                    LOG.warning("Can't remove " + ref.listenerClass.getName() + " using method " + methodName + " from " + src);
                    return;
                } else {
                    synchronized (LOCK) {
                        lastClass = methodClass;
                        lastMethodName = methodName;
                        lastRemove = remove;
                    }
                }
            }
            params[0] = ref.getImplementator();
            try {
                remove.invoke(src, params);
            } catch (Exception ex) {
                LOG.warning("Problem encountered while calling " + methodClass + "." + methodName + "(...) on " + src);
                LOG.log(Level.WARNING, null, ex);
            }
        }

        private Method getRemoveMethod(Class<?> methodClass, String methodName, Class listenerClass) {
            final Class<?>[] clarray = new Class<?>[] { listenerClass };
            Method m = null;
            try {
                m = methodClass.getMethod(methodName, clarray);
            } catch (NoSuchMethodException e) {
                do {
                    try {
                        m = methodClass.getDeclaredMethod(methodName, clarray);
                    } catch (NoSuchMethodException ex) {
                    }
                    methodClass = methodClass.getSuperclass();
                } while ((m == null) && (methodClass != Object.class));
            }
            if ((m != null) && (!Modifier.isPublic(m.getModifiers()) || !Modifier.isPublic(m.getDeclaringClass().getModifiers()))) {
                m.setAccessible(true);
            }
            return m;
        }
    }
}
