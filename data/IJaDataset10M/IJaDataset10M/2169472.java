package net.sf.rcpforms.modeladapter.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This registry finds the appropriate {@link ModelAdapter} for a given model or model type. It 
 * asks all registered ModelAdapters and caches the results:
 * <ul>
 *   <li>{@link ModelAdapter#canAdapt(Object)}</li>
 *   <li>{@link ModelAdapter#canAdaptClass(Object)}</li>
 * </ul>
 *
 * <h2>Caching strategy</h2>
 * To boost performance, <code>ModelAdapterRegistry</code> caches the results from previous calls
 *  to {@link #getAdapterForInstance(Object)} or {@link #getAdapterForMetaClass(Object)}. </br>
 *  In both cases, the <b>class of the model</b> ({@link Object#getClass()}) serves as <b>key</b> to store
 *   ModelAdapter instances in a cache map.
 *  <p>
 *  The premiss here is the same ModelAdapter instance can be reused for models of
 *  the same type.
 *  <p>
 *  If ModelAdapter's decide highly dynamically if it {@link ModelAdapter#canAdapt(Object) can adapt}
 *  a model or even reads some states in the model, <u>then caching of the ModelAdapter instance must
 *  be suppressed by the ModelAdapter by overwritting {@link ModelAdapter#allowsCachingOfAdapterInstance()}
 *  and return <code>false</code> in it!</u> 
 * <p>
 *
 * @since 1.4.0
 */
public class ModelAdapterRegistry {

    /**
     * @return the singleton default instance 
     */
    public static ModelAdapterRegistry getDefault() {
        return s_instance;
    }

    /**
     * Convenience method, delegates to {@link #getDefault()}.{@link #getAdapterForInstance(Object)}
     */
    public static ModelAdapter getDefaultAdapterForInstance(Object object) {
        return s_instance.getAdapterForInstance(object);
    }

    /**
     * Convenience method, delegates to {@link #getDefault()}.{@link #getAdapterForMetaClass(Object)}
     */
    public static ModelAdapter getDefaultAdapterForMetaClass(Object metaClassToAdapt) {
        return s_instance.getAdapterForMetaClass(metaClassToAdapt);
    }

    private static ModelAdapterRegistry s_instance = new ModelAdapterRegistry();

    private final List<ModelAdapter> adapters;

    private final Object LOCK;

    private final HashMap<Class<?>, ModelAdapter> modelClass2adapterCache;

    private final HashMap<Object, ModelAdapter> type2adapterCache;

    private ModelAdapterRegistry() {
        super();
        adapters = new ArrayList<ModelAdapter>();
        LOCK = new Object();
        modelClass2adapterCache = new HashMap<Class<?>, ModelAdapter>(100, 0.5f);
        type2adapterCache = new HashMap<Object, ModelAdapter>(100, 0.5f);
    }

    ModelAdapter getAdapterForInstance(Object object) {
        ModelAdapter adapter;
        Class<?> modelClass = null;
        if (object != null) {
            modelClass = object.getClass();
            adapter = modelClass2adapterCache.get(modelClass);
            if (adapter != null) {
                return adapter;
            }
        }
        adapter = findAdapterForInstance(object);
        if (adapter != null && modelClass != null && adapter.allowsCachingOfAdapterInstance()) {
            synchronized (LOCK) {
                modelClass2adapterCache.put(modelClass, adapter);
            }
        }
        return adapter;
    }

    ModelAdapter getAdapterForMetaClass(Object metaClassToAdapt) {
        ModelAdapter adapter;
        if (metaClassToAdapt != null) {
            adapter = type2adapterCache.get(metaClassToAdapt);
            if (adapter != null) {
                return adapter;
            }
        }
        adapter = findAdapterForMetaClass(metaClassToAdapt);
        if (adapter != null && metaClassToAdapt != null && adapter.allowsCachingOfAdapterInstance()) {
            synchronized (LOCK) {
                type2adapterCache.put(metaClassToAdapt, adapter);
            }
        }
        return adapter;
    }

    protected ModelAdapter findAdapterForInstance(Object object) {
        synchronized (LOCK) {
            for (ModelAdapter adapter : adapters) {
                if (adapter.canAdapt(object)) {
                    return adapter;
                }
            }
            return BeanAdapter.getInstance();
        }
    }

    protected ModelAdapter findAdapterForMetaClass(Object metaClassToAdapt) {
        synchronized (LOCK) {
            for (ModelAdapter adapter : adapters) {
                if (adapter.canAdaptClass(metaClassToAdapt)) {
                    return adapter;
                }
            }
            return BeanAdapter.getInstance();
        }
    }

    /**
     * Registers a model adapter to use the framework with different kinds of data models, e.g.
     * Maps, Beans, EMF Objects
     * 
     * @return true if adapter was added, false if adapter was not added because it was already
     *         registered
     */
    public boolean registerAdapter(ModelAdapter adapter) {
        synchronized (LOCK) {
            boolean result = false;
            if (!adapters.contains(adapter)) {
                result = adapters.add(adapter);
            }
            return result;
        }
    }

    /**
     * Unregisters a model adapter to use the framework with different kinds of data models, e.g.
     * Maps, Beans, EMF Objects
     * 
     * @return true if adapter was removed, false if it was not found
     */
    public boolean unregisterAdapter(ModelAdapter adapter) {
        synchronized (LOCK) {
            boolean found = adapters.remove(adapter);
            return found;
        }
    }
}
