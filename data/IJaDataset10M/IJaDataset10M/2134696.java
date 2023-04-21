package org.tinymarbles.impl.proxy;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.tinymarbles.impl.Crud;
import org.tinymarbles.model.Persistent;

/**
 * @author duke
 * 
 */
public class PersistentProxyWrapper implements ProxyWrapper {

    private static final Logger LOGGER = Logger.getLogger(PersistentProxyWrapper.class);

    private final Crud crud;

    private final PersistentEntityFactory eFactory;

    /**
	 * Gets the Crud object
	 * 
	 * @return the Crud object
	 */
    public Crud getCrud() {
        return this.crud;
    }

    public PersistentProxyWrapper(final Crud crud) {
        this(new PersistentEntityFactory(), crud);
    }

    protected PersistentProxyWrapper(final PersistentEntityFactory eFactory, Crud crud) {
        this.crud = crud;
        this.eFactory = eFactory;
    }

    public boolean isProxy(Object obj) {
        return (obj instanceof GenericProxy);
    }

    @SuppressWarnings("unchecked")
    public Object unwrapIfNeeded(Object object) {
        Object result = object;
        if (object != null) {
            if (isProxy(object)) {
                result = unwrapProxy(object);
            } else if (object instanceof Collection) {
                result = unwrapCollection((Collection) object);
            } else if (object instanceof Map) {
                result = unwrapMap((Map) object);
            } else if (object.getClass().isArray()) {
                result = unwrapInPlace((Object[]) object);
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("object not unwrapped: " + result);
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    protected Collection unwrapCollection(Collection source) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("unwrapping collection of " + source.size() + " elements in place");
        }
        if (source.isEmpty()) {
            return (Collection) (isProxy(source) ? ((GenericProxy) source).getTarget() : source);
        } else {
            Class<? extends Collection> sourceClass = (isProxy(source) ? ((GenericProxy) source).getTargetClass() : source.getClass());
            Collection copy = newInstance(sourceClass);
            for (Object obj : source) {
                copy.add(unwrapIfNeeded(obj));
            }
            return copy;
        }
    }

    @SuppressWarnings("unchecked")
    protected <K, V> Map unwrapMap(Map<K, V> source) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("unwrapping map of " + source.size() + " elements in place");
        }
        if (source.isEmpty()) {
            return (Map) (isProxy(source) ? ((GenericProxy) source).getTarget() : source);
        } else {
            Class<? extends Map> sourceClass = (isProxy(source) ? ((GenericProxy) source).getTargetClass() : source.getClass());
            Map copy = newInstance(sourceClass);
            for (Map.Entry<K, V> entry : source.entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                copy.put(unwrapIfNeeded(key), unwrapIfNeeded(value));
            }
            return copy;
        }
    }

    private Map newInstance(Class<? extends Map> class1) {
        try {
            return class1.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Maps of type " + class1 + " are not supported; any maps passed to persistent objects must provide an empty constructor");
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Maps of type " + class1 + " are not supported; any maps passed to persistent objects must provide an empty constructor");
        }
    }

    protected Collection newInstance(Class<? extends Collection> class1) {
        try {
            return class1.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("collections of type " + class1 + " are not supported; any collections passed to persistent objects must provide an empty constructor");
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("collections of type " + class1 + " are not supported; any collections passed to persistent objects must provide an empty constructor");
        }
    }

    protected Object[] unwrapInPlace(Object[] target) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("unwrapping array of " + target.length + " elements in place");
        }
        for (int i = 0; i < target.length; i++) {
            target[i] = unwrapIfNeeded(target[i]);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("unwrapped array: " + Arrays.asList(target));
        }
        return target;
    }

    @SuppressWarnings("unchecked")
    protected Object unwrapProxy(Object object) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("unwrapping persistent proxy: " + object);
        }
        Object result = ((GenericProxy) object).getTarget();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("unwrapped persistent object: " + result);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Object wrapIfNeeded(Object source) {
        Object result = source;
        if (source != null) {
            if (source instanceof Persistent) {
                result = wrapPersistentIfNeeded((Persistent) source);
            } else if (source instanceof Collection) {
                result = wrapCollectionIfNeeded((Collection) source);
            } else if (source instanceof Iterator) {
                result = wrapIteratorIfNeeded((Iterator) source);
            } else if (source instanceof Map) {
                result = wrapMapIfNeeded((Map) source);
            } else if (source.getClass().isArray()) {
                result = wrapArrayIfNeeded((Object[]) source);
            }
        }
        return result;
    }

    protected void initialize(Object source) {
        this.getCrud().initialize(source);
    }

    protected Object[] wrapArrayIfNeeded(Object[] source) {
        for (int i = source.length - 1; i >= 0; i--) {
            source[i] = wrapIfNeeded(source[i]);
        }
        return source;
    }

    public <T extends Persistent<? extends Serializable>> Collection<T> wrapCollectionIfNeeded(Collection<T> source) {
        if (isProxy(source)) {
            return source;
        } else {
            initialize(source);
            return this.eFactory.create(source, this);
        }
    }

    @SuppressWarnings("unchecked")
    public <K, T extends Persistent<? extends Serializable>> Map<? extends K, T> wrapMapIfNeeded(Map<K, T> source) {
        if (isProxy(source)) {
            return source;
        } else {
            initialize(source);
            return this.eFactory.create(source, this);
        }
    }

    public <T extends Persistent<? extends Serializable>> Iterator<T> wrapIteratorIfNeeded(Iterator<T> source) {
        return (isProxy(source) ? source : this.eFactory.create(source, this));
    }

    @SuppressWarnings("unchecked")
    protected Persistent wrapPersistentIfNeeded(Persistent pObj) {
        return (isProxy(pObj) ? pObj : this.eFactory.create(pObj, this.getCrud(), this));
    }

    public Object[] unwrap(Object[] args) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("unwrapping array of " + args.length + " elements...");
        }
        Object[] result = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = unwrapIfNeeded(args[i]);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("unwrapped array: " + Arrays.asList(result));
        }
        return result;
    }
}
