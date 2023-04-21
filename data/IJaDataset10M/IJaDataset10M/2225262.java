package org.objectwiz.representation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectwiz.Application;
import org.objectwiz.FetchStrategy;
import org.objectwiz.PersistenceUnit;
import org.objectwiz.metadata.MappedClass;

/**
 * {@link ObjectProxy} based on a unique {@link EntityRepresentation} for both
 * input and output conversion.
 *
 * It is abstract since it cannot implement the {@link #findPojo(MappedClass,Object)}
 * method.
 *
 * Each instance is associated to an {@link Application} and possibly with one of
 * the units from this application.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class RepresentationBasedObjectProxy implements ObjectProxy {

    private static final Log logger = LogFactory.getLog(RepresentationBasedObjectProxy.class);

    private static Map<String, RepresentationBasedObjectProxy> proxies = new HashMap();

    private EntityRepresentation representation;

    private Application application;

    private PersistenceUnit unit;

    private ReferenceResolver resolver;

    /**
     * Constructs a proxy for converting objects of the given unit from/to
     * the given representation.
     * @param application        The target application (cannot be NULL).
     * @param unit               The target unit. If this parameter is NULL, this proxy
     *                           will iterate over all the units of the application
     *                           when trying to resolve a parameter, and the <i>prepare</i>
     *                           method will not be available.
     * @param representation     Representation from/to which convert objects.
     *                           May be NULL: in that case this class shall just
     *                           act as a transparent proxy i.e. perform no
     *                           convertion.
     */
    private RepresentationBasedObjectProxy(Application application, PersistenceUnit unit, EntityRepresentation representation) {
        if (application == null) throw new NullPointerException("application is NULL");
        if (EntityRepresentation.POJO.equals(representation)) representation = null;
        this.application = application;
        this.unit = unit;
        this.representation = representation;
        if (unit == null) {
            this.resolver = new ReferenceResolverImpl(application, representation);
        } else {
            this.resolver = new ReferenceResolverImpl(unit, representation);
        }
    }

    /**
     * Returns a proxy for the given {@link PersistenceUnit}/{@link EntityRepresentation}.
     * Proxies returned by this method are initialized lazily, cached internally and thread-safe.
     */
    private static RepresentationBasedObjectProxy instance(final Application application, final PersistenceUnit unit, EntityRepresentation representation) {
        String key;
        if (unit == null) {
            key = application.getUniqueURI();
        } else {
            key = unit.getUniqueURI();
        }
        key += "#" + (representation == null ? "null" : representation.getClass());
        RepresentationBasedObjectProxy proxy = proxies.get(key);
        if (proxy != null) return proxy;
        synchronized (RepresentationBasedObjectProxy.class) {
            proxy = proxies.get(key);
            if (proxy == null) {
                proxy = new RepresentationBasedObjectProxy(application, unit, representation);
                proxies.put(key, proxy);
            }
            return proxy;
        }
    }

    public static RepresentationBasedObjectProxy instance(Application application) {
        return instance(application, (PersistenceUnit) null, application.getCurrentRepresentation());
    }

    public static RepresentationBasedObjectProxy instance(Application application, EntityRepresentation representation) {
        return instance(application, (PersistenceUnit) null, representation);
    }

    public static RepresentationBasedObjectProxy instance(PersistenceUnit unit) {
        return instance(unit.getApplication(), unit, unit.getApplication().getCurrentRepresentation());
    }

    public static RepresentationBasedObjectProxy instance(PersistenceUnit unit, EntityRepresentation representation) {
        return instance(unit.getApplication(), unit, representation);
    }

    public static RepresentationBasedObjectProxy instance(Application application, String unitName) {
        return instance(application, unitName, application.getCurrentRepresentation());
    }

    public static RepresentationBasedObjectProxy instance(Application application, String unitName, EntityRepresentation representation) {
        PersistenceUnit unit = application.getPersistenceUnits().get(unitName);
        if (unit == null) throw new IllegalArgumentException("Unit not found: " + unitName);
        return instance(unit, representation);
    }

    private PersistenceUnit resolveActualUnit(Object pojo) {
        if (unit == null) {
            return EntityRepresentation.POJO.resolveUnit(application, pojo);
        } else {
            return unit;
        }
    }

    @Override
    public ReferenceResolver getReferenceResolver() {
        return this.resolver;
    }

    @Override
    public Object prepareObject(Object object, FetchStrategy strategy) {
        if (representation == null) return object;
        if (logger.isDebugEnabled()) {
            logger.debug("Preparing: " + object + " / Fetch strategy: " + strategy);
        }
        if (object == null) {
            return null;
        }
        logger.debug("object class: " + object.getClass().getName());
        if (object instanceof Collection) {
            object = ((Collection) object).toArray();
        }
        if (object.getClass().isArray()) {
            if (object.getClass().getComponentType().isPrimitive()) {
                return object;
            } else {
                return prepareArray((Object[]) object, strategy);
            }
        }
        return prepareSingleObject(object, strategy);
    }

    @Override
    public Object[] prepareArray(Object[] array, FetchStrategy strategy) {
        if (representation == null) return array;
        Object[] preparedArray = new Object[array.length];
        for (int i = 0; i < array.length; i++) {
            preparedArray[i] = prepareObject(array[i], strategy);
        }
        return preparedArray;
    }

    @Override
    public Object prepareSingleObject(Object object, FetchStrategy strategy) {
        if (representation == null) return object;
        if (object == null) return null;
        if (strategy == null) {
            strategy = FetchStrategy.SINGLE_ENTITY;
        }
        MappedClass mc = null;
        PersistenceUnit targetUnit = resolveActualUnit(object);
        if (targetUnit == null) {
            return object;
        }
        try {
            mc = EntityRepresentation.POJO.getMappedClass(targetUnit, object, true);
        } catch (ClassNotFoundException e) {
        }
        if (mc != null) {
            boolean deep = FetchStrategy.WHOLE_GRAPH.equals(strategy);
            return representation.fromPojo(targetUnit, object, deep);
        } else {
            return object;
        }
    }

    @Override
    public List prepareList(List list, final FetchStrategy strategy) {
        if (representation == null) return list;
        ArrayList replicatedList = new ArrayList();
        for (Object result : list) {
            replicatedList.add(prepareObject(result, strategy));
        }
        return replicatedList;
    }

    @Override
    public Object prepareObjectWithSimilarScope(Object pojo, Object originalObject) {
        logger.debug("Preparing pojo with similar scope: " + pojo + " (representation is: " + representation + ")");
        if (pojo == null) return null;
        if (representation == null) return pojo;
        return representation.createObjectWithSimilarScope(resolveActualUnit(pojo), pojo, originalObject);
    }

    @Override
    public Object resolvePojo(Object object) {
        if (representation == null || object == null) return object;
        if (representation.isRepresented(object)) {
            if (unit != null) {
                return representation.toPojo(unit, object, resolver);
            } else {
                PersistenceUnit u = representation.resolveUnit(application, object);
                if (u == null) throw new RuntimeException("No unit can handle this object: " + object);
                return representation.toPojo(u, object, resolver);
            }
        }
        if (object instanceof Map) {
            return resolvePojos((Map) object);
        } else if (object instanceof List) {
            return resolvePojos((List) object);
        } else if (object.getClass().isArray() && (!object.getClass().getComponentType().isPrimitive())) {
            return resolvePojos((Object[]) object);
        }
        return object;
    }

    @Override
    public Object[] resolvePojos(Object[] objects) {
        return resolvePojos(objects, Object.class);
    }

    @Override
    public <E> E[] resolvePojos(E[] objects, Class<E> targetClass) {
        if (representation == null) return objects;
        if (objects == null) return null;
        Object array = Array.newInstance(targetClass, objects.length);
        for (int i = 0; i < objects.length; i++) {
            Array.set(array, i, resolvePojo(objects[i]));
        }
        return (E[]) array;
    }

    @Override
    public List resolvePojos(List list) {
        if (representation == null) return list;
        if (list == null) return null;
        return Arrays.asList(resolvePojos(list.toArray()));
    }

    @Override
    public Map resolvePojos(Map map) {
        if (representation == null) return map;
        if (map == null) return null;
        Map newMap = new LinkedHashMap();
        for (Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            newMap.put(resolvePojo(entry.getKey()), resolvePojo(entry.getValue()));
        }
        return newMap;
    }
}
