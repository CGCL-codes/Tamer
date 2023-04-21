package net.sourceforge.javautil.common.reflection.cache;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.javautil.common.context.Context;
import net.sourceforge.javautil.common.shutdown.Shutdown;
import net.sourceforge.javautil.common.shutdown.IShutdownHook;

/**
 * A cache of information and composite wrappers that will allow for 
 * a more higher level reflection API.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: ClassCache.java 2705 2010-12-30 19:40:40Z ponderator $
 */
public class ClassCache extends Context implements IShutdownHook {

    /**
	 * @return The current cache for the JVM or current thread
	 */
    public static final ClassCache getCache() {
        ClassCache cache = Context.get(ClassCache.class);
        if (cache == null) {
            synchronized (ClassCache.class) {
                cache = Context.get(ClassCache.class);
                if (cache == null) {
                    cache = new ClassCache();
                    cache.setGlobal();
                }
            }
        }
        return cache;
    }

    /**
	 * Facility method for getting a descriptor from the {@link #getCache()}.
	 *
	 * @see #getDescriptor(Class)
	 */
    public static <T> ClassDescriptor<T> getFor(Class<T> clazz) {
        return getCache().getDescriptor(clazz);
    }

    /**
	 * The cache of class-loader to descriptor references
	 */
    protected final Map<ClassLoader, Map<Class, ClassDescriptor>> cache = new HashMap<ClassLoader, Map<Class, ClassDescriptor>>();

    public ClassCache() {
    }

    /**
	 * This will first see if this class already has a descriptor loaded, if not 
	 * it will create one.
	 * 
	 * @param <T> The type of class
	 * @param clazz The clazz to get a descriptor for
	 * @return A descriptor for interacting with the class
	 */
    public <T> ClassDescriptor<T> getDescriptor(Class<T> clazz) {
        Map<Class, ClassDescriptor> descriptors = cache.get(clazz.getClassLoader());
        if (descriptors == null) {
            synchronized (cache) {
                if (!cache.containsKey(clazz.getClassLoader())) cache.put(clazz.getClassLoader(), descriptors = new LinkedHashMap<Class, ClassDescriptor>());
            }
        }
        ClassDescriptor descriptor = descriptors.get(clazz);
        if (descriptor == null) {
            synchronized (descriptors) {
                if (descriptor == null) descriptors.put(clazz, descriptor = new ClassDescriptor<T>(this, clazz));
            }
        }
        return descriptor;
    }

    /**
	 * @param loader The class loader whose classes should be removed from the cache, if any
	 */
    public void remove(ClassLoader loader) {
        synchronized (cache) {
            this.cache.remove(loader);
        }
    }

    public void shutdown() {
        this.cache.clear();
    }
}
