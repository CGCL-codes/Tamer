package org.datascooter.bundle;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.datascooter.bundle.attribute.PersistAttribute;
import org.datascooter.bundle.attribute.PersistReferenceAttribute;
import org.datascooter.exception.EntityNotMappedException;
import org.datascooter.inface.IBundleChangeListener;
import org.datascooter.inface.IBundleProvider;
import org.datascooter.utils.DSSettings;
import org.datascooter.utils.LangUtils;

/**
 * Purposed for mapping management . EntityMapper contains and manage of all system mappings
 * 
 * Each class can be mapped many times only entity is unique.
 * 
 * @author nemo
 * 
 */
public final class DSMapper {

    private static Map<String, EntityBundle> bundleMap = Collections.synchronizedMap(new HashMap<String, EntityBundle>());

    private static List<EntityBundle> bundleList = Collections.synchronizedList(new ArrayList<EntityBundle>());

    private static List<IBundleProvider> providerList = Collections.synchronizedList(new ArrayList<IBundleProvider>());

    private static List<IBundleChangeListener> changeListeners = Collections.synchronizedList(new ArrayList<IBundleChangeListener>());

    private static IBundleProvider defaultProvider = new FieldMappingProvider();

    private static DSMapper instance = new DSMapper();

    private static List<CrossTable> crossList = Collections.synchronizedList(new ArrayList<CrossTable>());

    private static Map<String, List<PersistReferenceAttribute>> unresolvedRefs = Collections.synchronizedMap(new HashMap<String, List<PersistReferenceAttribute>>());

    private DSMapper() {
        super();
    }

    /**
	 * Returns a bundle by entity name
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
    @SuppressWarnings("nls")
    public static synchronized EntityBundle getBundle(Object obj) throws EntityNotMappedException {
        String entity = LangUtils.entityName(obj);
        EntityBundle bundle = bundleMap.get(entity);
        if (bundle == null) {
            try {
                for (IBundleProvider provider : providerList) {
                    bundle = provider.getBundle(obj);
                    if (bundle != null) {
                        if (!bundle.isVolatile) {
                            addBundle(entity, bundle);
                        }
                        return bundle;
                    }
                }
                if (DSSettings.isUseDefaultProvider()) {
                    Logger.getLogger(DSMapper.class.getName()).info("***DefaultProvider Reflection Parse :   " + entity);
                    bundle = defaultProvider.getBundle(obj);
                    if (!bundle.isVolatile) {
                        addBundle(entity, bundle);
                    }
                }
            } catch (Exception e) {
                throw new EntityNotMappedException(entity, e);
            }
            if (bundle == null) {
                throw new EntityNotMappedException(entity, bundleMap);
            }
        }
        return bundle;
    }

    public static void addBundle(String bundleKey, EntityBundle bundle) throws EntityNotMappedException {
        if (!bundleMap.containsKey(bundleKey)) {
            addBundleSp(bundleKey, bundle);
            fireBundleChanged(bundle);
        }
    }

    public static void addBundleInt(EntityBundle bundle) throws EntityNotMappedException {
        if (!bundleMap.containsKey(bundle.entity)) {
            addBundleSp(bundle.entity, bundle);
        }
    }

    private static void addBundleSp(String bundleKey, EntityBundle bundle) throws EntityNotMappedException {
        bundleMap.put(bundleKey, bundle);
        bundleList.add(bundle);
        bundle.complete();
        for (EntityBundle child : bundle.embeddedEntityList) {
            if (!bundleMap.containsKey(child.entity)) {
                bundleMap.put(child.entity, child);
                bundleList.add(child);
                child.complete();
            }
        }
        provideRefs(bundle);
    }

    public static void provideRefs(EntityBundle bundle) throws EntityNotMappedException {
        provideRefsInt(bundle, bundle.refEntityMap.values());
        List<PersistReferenceAttribute> list = new ArrayList<PersistReferenceAttribute>();
        for (PersistAttribute attribute : bundle.attrArray) {
            if (PersistReferenceAttribute.class.isAssignableFrom(attribute.getClass())) {
                list.add((PersistReferenceAttribute) attribute);
            }
        }
        provideRefsInt(bundle, list);
    }

    private static void provideRefsInt(EntityBundle bundle, Collection<PersistReferenceAttribute> values) throws EntityNotMappedException {
        for (PersistReferenceAttribute attribute : values) {
            if (attribute.getType() == null) {
                EntityBundle bundle2 = bundleMap.get(attribute.getEntity());
                if (bundle2 != null) {
                    attribute.setType(bundle2.id.getType());
                    attribute.setScale(bundle2.id.getScale());
                    attribute.setPrecision(bundle2.id.getPrecision());
                } else {
                    List<PersistReferenceAttribute> list2 = unresolvedRefs.get(attribute.getEntity());
                    if (list2 == null) {
                        list2 = new ArrayList<PersistReferenceAttribute>();
                        unresolvedRefs.put(attribute.getEntity(), list2);
                    }
                    list2.add(attribute);
                }
            }
        }
        List<PersistReferenceAttribute> list = unresolvedRefs.get(bundle.entity);
        if (list != null) {
            for (PersistReferenceAttribute attribute : list) {
                attribute.setType(bundle.id.getType());
                attribute.setScale(bundle.id.getScale());
                attribute.setPrecision(bundle.id.getPrecision());
            }
            list.clear();
        }
    }

    public static void addBundleChangeListener(IBundleChangeListener listener) {
        changeListeners.add(listener);
    }

    public static void fireBundleChanged(EntityBundle bundle) {
        for (IBundleChangeListener listener : changeListeners) {
            listener.bundleChanged(bundle);
        }
    }

    /**
	 * Returns Id attributre for entity
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
    public static String getId(String entity) throws EntityNotMappedException, SecurityException, NoSuchMethodException {
        PersistAttribute id = getBundle(entity).id;
        if (id == null) {
            return null;
        }
        return getBundle(entity).id.name;
    }

    /**
	 * Retrieves an Id value for given object through invoke mapped getter
	 * 
	 * @param obj
	 * @return Object
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws EntityNotMappedException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws WrongMethodException
	 */
    public static Object getIdValue(Object obj) throws EntityNotMappedException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return getBundle(obj).getId(obj);
    }

    public static List<EntityBundle> getBundleList() {
        return bundleList;
    }

    public static void clear() {
        providerList.clear();
        changeListeners.clear();
        bundleList.clear();
        bundleMap.clear();
    }

    /**
	 * Adds a bundle provider - it may be xml parser or other storage of mapping information - by example database
	 * 
	 * @throws Exception
	 */
    public static void addBundleProvider(IBundleProvider provider) throws Exception {
        providerList.add(provider);
        addBundles(provider);
    }

    public static void justAddBundleProvider(IBundleProvider provider) {
        providerList.add(provider);
    }

    /**
	 * Requests all bundle providers about available non volatile bundles. Some bundles may be volatile - can be changed during runtime
	 */
    public static void explore() throws Exception {
        for (IBundleProvider provider : providerList) {
            addBundles(provider);
        }
    }

    private static void addBundles(IBundleProvider provider) throws Exception {
        provider.explore();
        List<EntityBundle> list = provider.provideNonVolatile();
        for (EntityBundle bundle : list) {
            addBundle(bundle.entity, bundle);
        }
        crossList.addAll(provider.getCrossList());
    }

    public static void setBundleProviders(IBundleProvider[] bundleProviders) {
        for (IBundleProvider provider : bundleProviders) {
            justAddBundleProvider(provider);
        }
    }

    public static Map<String, EntityBundle> getBundleMap() {
        return bundleMap;
    }

    public static DSMapper getInstance() {
        return instance;
    }

    public static List<CrossTable> getCrossList() {
        return crossList;
    }
}
