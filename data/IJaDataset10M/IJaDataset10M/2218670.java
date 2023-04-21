package de.sicari.kernel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import de.sicari.util.CanonicalPath;
import de.sicari.util.NoSuchObjectException;
import de.sicari.util.ObjectExistsException;

/**
 * Manages the global registry of objects. The <code>Environment
 * </code> keeps track on objects that are published by it in the
 * global shared registry. When an <code>Environment</code> is cleared
 * or garbage collected then all objects published by means of it are
 * removed from the global map (if they still are in the global map)
 * and invalidated.<p>
 *
 * By default objects are wrapped into proxy objects before they are
 * published. A lookup returns the proxy rather than the original
 * object, which remains unavailable unless a special permission is
 * granted to the calling thread. Proxys support cutting off the
 * reference to the original object (also subject to permission
 * checks). This means that even though some entities may hold
 * hard references to the proxy, the wrapped object can still be
 * removed and garbage collected if its owning entity becomes
 * unavailable.<p>
 *
 * Subject to permission, objects can be published in a
 * &quot;detached&quot; mode. This prevents objects from being
 * removed from the global map even if the <code>Environment
 * </code> is cleared or garbage collected.
 *
 * @author Volker Roth
 * @author Jan Peters
 * @version "$Id: Environment.java 309 2007-08-29 23:41:15Z jpeters $"
 */
public class Environment extends Object {

    /**
     * Signals that an object shall be &quot;detached&quot;
     * from this <code>Environment</code>. Detached objects
     * are not automatically retracted and invalidated when
     * this <code>Environment</code> is cleared or garbage
     * collected.
     */
    public static final int DETACH = 0x40;

    /**
     * Signals that an object shall <em>not</em> be
     * wrapped into a {@link SecurityProxyHandler}.
     * Hence, the object will <em>neither</em> be
     * bound to the publishing user <em>nor</em> will
     * method calls on that object depend on whether
     * the corresponding {@link ServiceInvocationPermission}
     * may be granted or not.
     */
    public static final int DECONTROL = 0x80;

    /**
     * Signals that an object shall be published without
     * a wrapping proxy. This ability should be reserved
     * to trusted objects that do not attack other code.
     * Additionally, code that calls such objects should
     * either be trusted or not allowed to invoke <code>
     * Thread.stop()</code> on calling threads.
     */
    public static final int NO_PROXY = 0;

    /**
     * Signals that an object shall be wrapped into a
     * proxy whose handler allows to cut the reference
     * to the wrapped object.
     */
    public static final int PLAIN_PROXY = 1;

    /**
     * Signals that an object shall be wrapped into a proxy
     * that decouples calling threads and execution threads
     * when publishing the object.
     */
    public static final int ASYNC_PROXY = 2;

    /**
     * Signals that an object shall be wrapped into a proxy
     * that marshalls and unmarshalls arguments and results
     * of calls in addition to decoupling caller and
     * execution threads. This is the most paranoid level
     * of object publishing. It is also the least efficient
     * one.
     */
    public static final int FULL_PROXY = 3;

    /**
     * The root path.
     */
    public static final CanonicalPath ROOT = new CanonicalPath('/');

    /**
     * The global static lock used for synchronization.
     */
    private static final Object lock_ = new Object();

    /**
     * The empty <code>EnvironmentCallback</code> that is being used
     * instead of <code>null</code>, so calls on {@link #callback_}
     * can be atomic and do not need to be synchronized
     */
    private static final EnvironmentCallback null_callback_ = new EnvironmentCallback() {

        public Object onLookupEmpty(String key) {
            return null;
        }

        public void onPublication(String key, Object object) {
        }

        public void onRetraction(String key, Object object) {
        }
    };

    /**
     * The <code>InheritableThreadLocal</code> variable that
     * is used to bind <code>Environment</code> instances to
     * threads.
     */
    private static final InheritableThreadLocal envRef_ = new InheritableThreadLocal();

    /**
     * The global <code>SortedMap</code> that is used to keep
     * references to the published objects.
     */
    private static SortedMap global_ = new TreeMap();

    /**
     * The global <code>Map</code> that keeps the watch
     * keys.
     */
    private static Map watch_ = new HashMap();

    /**
     * The callback registrant.
     */
    private transient EnvironmentCallback callback_;

    /**
     * The local <code>Map</code> that is used to keep references
     * to the objects published by means of this instance.
     */
    private transient Map local_;

    /**
     * The <code>ClassLoader</code> that is used to load proxy
     * classes.
     */
    private transient ClassLoader loader_;

    /**
     * The default proxy mode. No special proxy permission is
     * required to publish objects with the default proxy mode.
     */
    private transient int mode_;

    /**
     * The tag that is used for checking permissions when <code>
     * Environment.getEnvironment()</code> is called. For agents
     * this tag must be the implicit name of that agent.
     */
    private transient String tag_;

    /**
     * Creates an instance that is backed by a <code>HashMap</code>.
     */
    public Environment() {
        this(new HashMap(), null, FULL_PROXY, null);
    }

    /**
     * Creates an instance.
     *
     * @param map The <code>Map</code> that backs this
     *   <code>Environment</code>.
     * @param cl The <code>ClassLoader</code> that is used
     *   to load proxy classes, or <code>null</code> if the
     *   system class loader shall be used.
     * @param mode The default proxy mode to use when objects
     *   are published without specifying an explicit proxy
     *   mode. Please note that no particular proxy permission
     *   is required to publish objects with the default proxy
     *   mode.
     * @param tag The tag that is used to do permission checks
     *   on <code>Environment.getEnvironment()</code>.
     */
    protected Environment(Map map, ClassLoader cl, int mode, String tag) {
        if (map == null) {
            throw new NullPointerException("Need a map!");
        }
        local_ = map;
        loader_ = (cl == null) ? ClassLoader.getSystemClassLoader() : cl;
        mode_ = mode;
        tag_ = tag;
        callback_ = null_callback_;
    }

    /**
     * Returns the <code>Environment</code> that belongs to
     * the current thread.<p>
     *
     * Before the <code>Environment</code> is returned, this method
     * checks if the current thread has permission to access its
     * <code>Environment</code>. This is done by means of a call
     * to the <code>SecurityManager</code> with the permission
     * <code>ServicePermission(tag(),&quot;id&quot;)</code>. The
     * value returned by <code>tag()</code> must be implicit name
     * of the agent to which this <code>Environment</code> is
     * assigned, or <code>null</code> for the root envrionment.
     *
     * @return The <code>Environment</code> of the current
     *   thread, or <code>null</code> if the calling thread
     *   does not yet have an environment.
     */
    public static Environment getEnvironment() {
        SecurityManager sm;
        Environment env;
        env = (Environment) envRef_.get();
        if (env != null) {
            if ((sm = System.getSecurityManager()) != null) {
                sm.checkPermission(new ServicePermission(env.tag(), "id"));
            } else {
                AccessController.checkPermission(new ServicePermission(env.tag(), "id"));
            }
        }
        return env;
    }

    /**
     * Sets the <code>Environment</code> of the current thread
     * to the given <code>Environment</code>.<p>
     *
     * The current thread must have permission
     * <code>ServicePermission(null, &quot;id&quot;)</code>.
     *
     * @param env The <code>Environment</code> that shall be set,
     *   or <code>null</code> to clear.
     * @exception SecurityException if the current thread
     *   does not have permission to set its environment.
     */
    public static void setEnvironment(Environment env) {
        SecurityManager sm;
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(new ServicePermission(null, "id"));
        } else {
            AccessController.checkPermission(new ServicePermission(null, "id"));
        }
        envRef_.set(env);
    }

    /**
     * @return The tag of this <code>Environment</code>. This
     *   tag is used for permission checking. For agents, the
     *   tag must be the string representation of its implicit
     *   name.
     */
    public String tag() {
        return tag_;
    }

    /**
     * Publishes an object in the server. This method calls
     * {@link #publish(java.lang.String,java.lang.Object,int)
     * <code>publish(key, value, mode_)</code>} where <code>
     * mode_</code> is the default proxy mode.
     *
     * @param key The name of the object. The name may consist
     *   of a path with slashes as separators.
     * @param value The object to publish.
     * @exception ObjectExistsException if some object is already
     *   published under the name of the given one.
     */
    public void publish(String key, Object value) throws ObjectExistsException {
        publish(key, value, mode_);
    }

    /**
     * Publishes an object in the server using the given name.
     * The given <code>mode</code> specifies, which type of proxy
     * shall be used for wrapping the object before publishing it.
     * Valid modes are {@link #NO_PROXY NO_PROXY}, {@link
     * #ASYNC_PROXY ASYNC_PROXY}, {@link #PLAIN_PROXY PLAIN_PROXY},
     * and {@link #FULL_PROXY FULL_PROXY}.
     *
     * @param key The name of the object. The name may consist
     *   of a path with slashes as separators.
     * @param value The object to publish.
     * @param mode The wrapping mode for the object. This value
     *   is ignored and defaults to <code>NO_PROXY</code>.
     * @exception ObjectExistsException if an object is already
     *   published under the name of the given one.
     * @exception SecurityException if the caller has
     *   insufficient permission to publish the object.
     * @see #DETACH
     * @see #DECONTROL
     */
    public void publish(String key, Object value, int mode) throws ObjectExistsException {
        EnvironmentPermission tGuard;
        EnvironmentPermission cGuard;
        SecurityManager sm;
        CanonicalPath path;
        ProxyHandler handler;
        String action;
        Object proxy;
        if (value == null) {
            throw new NullPointerException("Need a value!");
        }
        path = new CanonicalPath(ROOT, key);
        cGuard = new EnvironmentPermission(path, "retract");
        tGuard = new EnvironmentPermission(path, "master");
        switch(mode & ~(DETACH | DECONTROL)) {
            case NO_PROXY:
                action = "NONE";
                handler = null;
                break;
            case PLAIN_PROXY:
                action = "PLAIN";
                handler = new PlainProxyHandler(value, cGuard, tGuard);
                break;
            case ASYNC_PROXY:
                action = "ASYNC";
                handler = new PlainProxyHandler(value, cGuard, tGuard);
                break;
            case FULL_PROXY:
                action = "FULL";
                handler = new PlainProxyHandler(value, cGuard, tGuard);
                break;
            default:
                throw new IllegalArgumentException("Bad mode: " + mode);
        }
        if (mode != mode_) {
            action = "publish," + action;
        } else {
            action = "publish";
        }
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(new EnvironmentPermission(path, action));
        } else {
            AccessController.checkPermission(new EnvironmentPermission(path, action));
        }
        proxy = createBasicProxy(value, handler);
        if ((mode & DECONTROL) == 0) {
            handler = new SecurityProxyHandler(path.toString(), proxy, cGuard, tGuard);
            proxy = Proxy.newProxyInstance(loader_, getAllInterfaces(proxy.getClass()), handler);
        }
        synchronized (lock_) {
            if (global_.containsKey(path)) {
                throw new ObjectExistsException(path.toString());
            }
            global_.put(path, proxy);
            touch(path);
            if ((mode & DETACH) != DETACH) {
                local_.put(path, proxy);
            }
        }
        callback_.onPublication(path.toString(), value);
    }

    /**
     * Creates a proxy for the given object that uses the given
     * {@link ProxyHandler handler}. A proxy is created only if
     * <code>handler</code> is not <code>null</code>, else the
     * given <code>target</code> is returned.
     *
     */
    private Object createBasicProxy(Object target, ProxyHandler handler) {
        Class[] intf;
        Class claz;
        if (target == null) {
            throw new NullPointerException("Need target and handler");
        }
        if (handler == null) {
            return target;
        }
        claz = target.getClass();
        if (Proxy.isProxyClass(claz)) {
            throw new IllegalArgumentException("Won't proxy a proxy");
        }
        intf = getAllInterfaces(claz);
        if (intf.length == 0) {
            throw new IllegalArgumentException("Target implements no interfaces");
        }
        return Proxy.newProxyInstance(loader_, intf, handler);
    }

    /**
     * Returns an array with the interfaces implemented by
     * the given class and its super class.
     *
     * @param target The class whose interfaces shall be returned.
     * @return The array of interfaces implemented by the given
     *   target class and (recursively) all of its superclasses.
     *   The returned array is &quot;compressed&quot;. This means
     *   it contains the smalles set of interfaces that encompass
     *   all implemented interfaces. For instance if <code>A</code>
     *   extends <code>B</code> then only <code>A</code> will be
     *   contained in the array.
     */
    private Class[] getAllInterfaces(Class target) {
        ArrayList list;
        Class[] claz;
        Class old;
        int i;
        int j;
        if (target == null) {
            throw new NullPointerException("Need a target class!");
        }
        claz = null;
        list = new ArrayList(16);
        while (target != null) {
            claz = target.getInterfaces();
            outer: for (i = 0; i < claz.length; i++) {
                for (j = list.size() - 1; j >= 0; j--) {
                    old = (Class) list.get(j);
                    if (old.isAssignableFrom(claz[i])) {
                        list.set(j, claz[i]);
                        continue outer;
                    } else if (claz[i].isAssignableFrom(old)) {
                        continue outer;
                    }
                }
                list.add(claz[i]);
            }
            target = target.getSuperclass();
        }
        return (Class[]) list.toArray(claz);
    }

    /**
     * Retracts the published object. The caller must have permission
     * to retract the service, or must have published the service in
     * the first place. The given name must be the one under which
     * the service was published.
     *
     * @param key The name under which the service was published.
     * @return The retracted object.
     * @exception NoSuchObjectException if no object with the given
     *   name is known.
     */
    public Object retract(String key) throws NoSuchObjectException {
        SecurityManager sm;
        CanonicalPath path;
        Object global;
        Object local;
        path = new CanonicalPath(ROOT, key);
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(new EnvironmentPermission(path, "retract"));
        } else {
            AccessController.checkPermission(new EnvironmentPermission(path, "retract"));
        }
        synchronized (lock_) {
            global = global_.remove(path);
            local = local_.remove(path);
            if (local != null) {
                invalidate(local);
            }
            if (global == null) {
                throw new NoSuchObjectException(path.toString());
            }
            touch(path);
            if (local != global) {
                invalidate(global);
            }
        }
        callback_.onRetraction(path.toString(), global);
        return global;
    }

    /**
     * Look up a published object based on a name.<p>
     *
     * If the globally published object differs from the local
     * object known under the given path then the local reference
     * is cleared.
     *
     * @param key The name of the object to look up. The name
     *   must be the one under which the object is published.
     * @return The named object or <code>null</code> if no
     *   object with the given name is registered.
     * @exception SecurityException if the caller has
     *   insufficient permissions to look up the object with
     *   the given name.
     */
    public Object lookup(String key) {
        SecurityManager sm;
        CanonicalPath path;
        Object global;
        Object local;
        path = new CanonicalPath(ROOT, key);
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(new EnvironmentPermission(path, "lookup"));
        } else {
            AccessController.checkPermission(new EnvironmentPermission(path, "lookup"));
        }
        synchronized (lock_) {
            global = global_.get(path);
            local = local_.get(path);
            if (local != null && local != global) {
                local_.remove(path);
                invalidate(local);
            }
        }
        if (global == null) {
            return callback_.onLookupEmpty(path.toString());
        } else {
            return global;
        }
    }

    /**
     * Returns a <code>Map</code> of the entries whose keys match
     * <code>wildcard</code>. The returned <code>SortedMap</code>
     * is not a view but a complete copy.
     *
     * @param wildcard The wildcard of the entries to be returned.
     * @return A <code>Map</code> with those entries that match
     *   the given wildcard key.
     */
    public SortedMap lookupAll(String wildcard) {
        SecurityManager sm;
        CanonicalPath path;
        CanonicalPath a;
        CanonicalPath b;
        SortedMap map;
        Iterator i;
        Object o;
        String s;
        int n;
        int j;
        path = new CanonicalPath(ROOT, wildcard);
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(new EnvironmentPermission(path, "lookup"));
        } else {
            AccessController.checkPermission(new EnvironmentPermission(path, "lookup"));
        }
        map = new TreeMap();
        n = path.length();
        for (j = 0; j < n; j++) {
            s = path.elementAt(j);
            if (s.equals("*") || s.equals("-")) {
                break;
            }
        }
        synchronized (lock_) {
            if (j == n) {
                o = lookup(wildcard);
                if (o != null) {
                    map.put(path, o);
                }
                return map;
            }
            if (j > 0) {
                a = new CanonicalPath(path.head(j), "/0");
                j--;
                b = new CanonicalPath(path.head(j), path.elementAt(j) + '\0');
                i = global_.subMap(a, b).entrySet().iterator();
            } else {
                i = global_.entrySet().iterator();
            }
            Map.Entry entry;
            while (i.hasNext()) {
                entry = (Map.Entry) i.next();
                a = (CanonicalPath) entry.getKey();
                if (path.implies(a)) {
                    map.put(a, entry.getValue());
                }
            }
        }
        return map;
    }

    /**
     * Adds a watch. Each time an object in the global
     * <code>Map</code> changes whose key starts with the given
     * watch key, the watch key is annotated with the current
     * time. This time can be retrieved using method <code>
     * lastChange(String key)</code>.
     *
     * @param key The key prefix that shall be watched. This key
     *   must not contain wildcards.
     * @return The last modification time of the given watch key.
     *   If the watch key was not registered before then
     *   the current time millis are returned.
     * @exception IllegalArgumentException if <code>key</code>
     *   contains wildcards.
     * @exception SecurityException if the calling thread
     *   has no permission to add the given watch key.
     */
    public long addWatch(String key) {
        SecurityManager sm;
        CanonicalPath path;
        Long millis;
        if (key == null) {
            throw new NullPointerException("Need a key!");
        }
        path = new CanonicalPath(ROOT, key);
        if (path.isWildcard()) {
            throw new IllegalArgumentException("No wildcards allowed!");
        }
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(new EnvironmentPermission(path, "watch"));
        } else {
            AccessController.checkPermission(new EnvironmentPermission(path, "watch"));
        }
        synchronized (lock_) {
            millis = (Long) watch_.get(path);
            if (millis == null) {
                millis = new Long(System.currentTimeMillis());
                watch_.put(path, millis);
            }
            return millis.longValue();
        }
    }

    /**
     * Returns the last modification time of the given watch
     * key or 0 if the given watch key is not registered.
     *
     * @param key The watch key whose most recent modification
     *   time shall be returned.
     * @return The most recent time some object whose key
     *   starts with the given watch key was modified.
     */
    public long lastChange(String key) {
        CanonicalPath path;
        Long millis;
        if (key == null) {
            throw new NullPointerException("Need a key!");
        }
        path = new CanonicalPath(ROOT, key);
        synchronized (lock_) {
            millis = (Long) watch_.get(path);
        }
        return (millis == null) ? 0 : millis.longValue();
    }

    /**
     * Clears all references to objects published by means of
     * this <code>Environment</code>.
     */
    protected void clear() {
        CanonicalPath path;
        Map.Entry entry;
        Iterator i;
        Object global;
        Object local;
        synchronized (lock_) {
            for (i = local_.entrySet().iterator(); i.hasNext(); ) {
                entry = (Map.Entry) i.next();
                path = (CanonicalPath) entry.getKey();
                local = entry.getValue();
                global = global_.get(path);
                if (local == global) {
                    global_.remove(path);
                }
                invalidate(local);
            }
            local_.clear();
        }
    }

    /**
     * Register a callback interface.
     *
     * @param callback The callback interface to be registered
     * @throws NullPointerException
     *   if <code>callback</code> is <code>null</code>
     * @throws ObjectExistsException
     *   if another <code>EnvironmentCallback</code> interface is
     *   already registered
     */
    public void registerCallback(EnvironmentCallback callback) throws NullPointerException, ObjectExistsException {
        EnvironmentPermission permission;
        SecurityManager sm;
        permission = new EnvironmentPermission(new CanonicalPath('/'), "callback");
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(permission);
        } else {
            AccessController.checkPermission(permission);
        }
        if (callback == null) {
            throw new NullPointerException("Callback is null");
        }
        synchronized (lock_) {
            if (callback_.equals(callback)) {
                return;
            }
            if (!null_callback_.equals(callback_)) {
                throw new ObjectExistsException("Existing callback must be deregistered first");
            }
            callback_ = callback;
        }
    }

    /**
     * Deregister a callback interface.
     *
     * @param callback The callback interface to be deregistered
     * @throws NoSuchObjectException
     *   if the given callback interface is not registered
     */
    public void deregisterCallback(EnvironmentCallback callback) throws NoSuchObjectException {
        EnvironmentPermission permission;
        SecurityManager sm;
        permission = new EnvironmentPermission(new CanonicalPath('/'), "callback");
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(permission);
        } else {
            AccessController.checkPermission(permission);
        }
        synchronized (lock_) {
            if (!callback_.equals(callback)) {
                throw new NoSuchObjectException("The specified callback is not registered");
            }
            callback_ = null_callback_;
        }
    }

    /**
     * Updates the modification time of any registered watch paths.
     *
     * @param path The path for which the watch paths shall be
     *   updated.
     */
    private void touch(CanonicalPath path) {
        Long millis;
        int i;
        millis = new Long(System.currentTimeMillis());
        synchronized (lock_) {
            for (i = path.length(); i >= 0; i--) {
                path = path.head(i);
                if (watch_.containsKey(path)) {
                    watch_.put(path, millis);
                }
            }
        }
    }

    /**
     * Invalidates proxy objects by telling the handler to cut
     * its reference to the wrapped object.
     *
     * @param proxy The proxy whose handler shall be invalidated.
     *   If <code>proxy</code> is not a proxy then nothing
     *   happens.
     * @exception SecurityException if the handler of the
     *   given proxy is a <code>ProxyHandler</code> and it does
     *   not permit clearing.
     */
    private void invalidate(Object proxy) {
        InvocationHandler handler;
        if (proxy == null) {
            throw new NullPointerException("Need a proxy object");
        }
        if (Proxy.isProxyClass(proxy.getClass())) {
            handler = Proxy.getInvocationHandler(proxy);
            if (handler instanceof ProxyHandler) {
                System.out.println("Clearing proxy handler.");
                ((ProxyHandler) handler).clear();
            }
        }
    }

    /**
     * Clears this instance upon finalization.
     */
    protected final void finalize() {
        try {
            clear();
        } catch (Throwable e) {
        }
    }
}
