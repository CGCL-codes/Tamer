package gloodb;

import gloodb.GlooException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for retrieving persistence attributes of classes / objects
 * stored in the repository. The accessible persistence attributes are:
 * <ul>
 * <li>Identity fields.
 * <li>Version field.
 * <li>Version manager.
 * <li>Pre/Post callbacks.
 * </ul>
 */
public class PersistencyAttributes {

    /**
	 * The utility cashes persistence information internally.
	 */
    private static final Map<Class<? extends Serializable>, PersistencyAttributes> classInfoMap = Collections.synchronizedMap(new HashMap<Class<? extends Serializable>, PersistencyAttributes>());

    /**
	 * Returns the {@link PreRemove}, {@link PreUpdate}, {@link PostRemove},
	 * {@link PostRestore} or {@link PostUpdate} callback method.
	 * 
	 * @param clazz
	 *            The callback type. Can be one of {@link PreRemove}.class,
	 *            {@link PreUpdate}.class, {@link PostRemove}.class,
	 *            {@link PostRestore}.class or {@link PostUpdate}.class.
	 * @param victimObject
	 *            The persistent object to retrieve attributes from.
	 * @return The requested pre/post callback method of the victim object.
	 */
    public static Method getCallback(Class<? extends Annotation> clazz, Serializable victimObject) {
        PersistencyAttributes pci = getPersistentInfo(victimObject);
        if (pci == null) return null;
        if (clazz.equals(PostRestore.class)) {
            return pci.postRestore;
        }
        if (clazz.equals(PreRemove.class)) {
            return pci.preRemove;
        }
        if (clazz.equals(PostRemove.class)) {
            return pci.postRemove;
        }
        if (clazz.equals(PreUpdate.class)) {
            return pci.preStore;
        }
        if (clazz.equals(PostUpdate.class)) {
            return pci.postStore;
        }
        if (clazz.equals(PreCreate.class)) {
            return pci.preCreate;
        }
        if (clazz.equals(PostCreate.class)) {
            return pci.postCreate;
        }
        return null;
    }

    /**
	 * Creates an identity from field values. This identity can be used to
	 * restore an object from the repository. The field values must be provided
	 * in the order they are specified in the persistent class.
	 * 
	 * @param idFields
	 *            The fields used to specify the identity.
	 * @return The object identity.
	 */
    public static Serializable getId(Serializable... idFields) {
        if (idFields.length == 0) {
            throw new GlooException("Need at least one field to specify an object identity");
        }
        return (idFields.length == 1 ? idFields[0] : new AggregateIdentity(idFields));
    }

    /**
	 * Returns the identity of a persistent object.
	 * 
	 * @param persistentObject
	 *            The persistent object.
	 * @return The identity of the persistent object.
	 */
    public static Serializable getIdForObject(Serializable persistentObject) {
        PersistencyAttributes info = getPersistentInfo(persistentObject);
        if (info != null) {
            return getPersistentInfo(persistentObject).getId(persistentObject);
        }
        return null;
    }

    /**
	 * Gets the id from this variant. The variant can be:
	 * <ul><li>an object assignable from T. The returned value is PersitencyAttributes.getIdForObject(variant)
	 * <li>? extends Reference<T>. The returned value is ((Reference<T>)variant).getId()
	 * <li>anything else. The return value is variant.
	 * @param <T> The type.
	 * @param clazz The base class for this variant.
	 * @param variant The variant value.
	 * @return The id of the T object, Reference<T> reference or the variant itself.
	 */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> Serializable getIdFromVariant(Class<T> clazz, Serializable variant) {
        if (variant == null) return null;
        if (variant instanceof Reference) {
            return ((Reference<T>) variant).getId();
        }
        if (clazz.isAssignableFrom(variant.getClass())) {
            return PersistencyAttributes.getIdForObject(variant);
        }
        return variant;
    }

    /**
	 * Returns the ids of interceptors for this object.
	 * @param persistentObject The object
	 * @return The interceptor ids.
	 */
    public static List<Class<? extends Interceptor<? extends Serializable>>> getInterceptorIds(Serializable persistentObject) {
        PersistencyAttributes info = getPersistentInfo(persistentObject);
        if (info != null) {
            return getPersistentInfo(persistentObject).interceptorIds;
        }
        return null;
    }

    /**
	 * Returns the persistence attributes of an object.
	 * 
	 * @param persistentObject
	 *            The persistent object.
	 * @return The persistence attributes.
	 */
    public static PersistencyAttributes getPersistentInfo(Serializable persistentObject) {
        if (persistentObject == null) {
            return null;
        }
        Class<? extends Serializable> clazz = persistentObject.getClass();
        PersistencyAttributes result = classInfoMap.get(clazz);
        if (result == null) {
            result = new PersistencyAttributes(clazz);
            classInfoMap.put(clazz, result);
        }
        return result;
    }

    /**
	 * Returns the sorting method for the given index.
	 * @param persistentObject The object
	 * @param indexName The index name
	 * @return The sorting method.
	 */
    public static Method getSortingMethod(Serializable persistentObject, String indexName) {
        PersistencyAttributes info = getPersistentInfo(persistentObject);
        if (info != null) {
            return info.sortingMethodMap.get(indexName);
        }
        return null;
    }

    /**
	 * Returns the sorting value for the given index name.
	 * @param persistentObject The persistent object.
	 * @param indexName The index name.
	 * @param args SortingCriteria arguments if any.
	 * @return The key value.
	 * @throws IllegalArgumentException If the sorting criteria method cannot be invoked.
	 * @throws IllegalAccessException If the sorting criteria method cannot be invoked.
	 * @throws InvocationTargetException If the sorting criteria method cannot be invoked.
	 */
    @SuppressWarnings("null")
    public static Serializable getSortingValue(Serializable persistentObject, String indexName, Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method m = getSortingMethod(persistentObject, indexName);
        if (m != null) return null;
        return (Serializable) m.invoke(persistentObject, args);
    }

    /**
	 * Increments the version of the copy persistent object. Throws a
	 * LockingException if the original and copy are not at the same initial
	 * version.
	 * 
	 * @param original
	 *            The version value in the repository.
	 * @param copy
	 *            The version value of the copy.
	 * @throws LockingException
	 *             If the original and copy values are not the same.
	 */
    public static void incrementVersion(Serializable original, Serializable copy) {
        PersistencyAttributes pci = getPersistentInfo(original);
        if (pci.isVersioned()) {
            pci.versionManager.increment(original, copy, pci.versionField);
        }
    }

    private ArrayList<Object> identityFields;

    private Field versionField;

    private VersionManager versionManager;

    private Method getIdMethod;

    private Method postRestore;

    private Method preRemove;

    private Method postRemove;

    private Method preStore;

    private Method postStore;

    private Method preCreate;

    private Method postCreate;

    private ArrayList<Class<? extends Interceptor<? extends Serializable>>> interceptorIds;

    private HashMap<String, Method> sortingMethodMap;

    private PersistencyAttributes() {
    }

    @SuppressWarnings("unchecked")
    private PersistencyAttributes(Class<? extends Serializable> persistentObjectClass) {
        init();
        for (Class<? extends Serializable> clazz = persistentObjectClass; clazz != null; clazz = (Class<? extends Serializable>) clazz.getSuperclass()) {
            parseClass(clazz);
            parseFields(clazz);
            parseMethods(clazz);
        }
        validate(persistentObjectClass);
    }

    private void addSortingMethod(SortingCriteria a, Method m) {
        String[] names = a.value();
        if (names != null && names.length > 0) {
            for (String name : names) {
                Method method = sortingMethodMap.get(name);
                if (method != null) {
                    throw new GlooException(String.format("Index %s defines multiple SortingCriteria: %s", name, m.getName()));
                }
                sortingMethodMap.put(name, m);
            }
        } else {
            throw new GlooException(String.format("SortingCriteria for method %s must declare at least one index name", m.getName()));
        }
    }

    private Serializable getId(Serializable persistentObject) {
        try {
            if (getIdMethod != null) return (Serializable) getIdMethod.invoke(persistentObject);
            int size = this.identityFields.size();
            if (size == 1) {
                Object idObject = this.identityFields.get(0);
                return getIdValue(idObject, persistentObject);
            } else {
                Serializable[] ids = new Serializable[this.identityFields.size()];
                int i = 0;
                for (Object idObject : this.identityFields) {
                    ids[i++] = getIdValue(idObject, persistentObject);
                }
                return new AggregateIdentity(ids);
            }
        } catch (Exception ex) {
            throw new GlooException(ex);
        }
    }

    private Serializable getIdValue(Object idObject, Serializable persistentObject) throws IllegalArgumentException, IllegalAccessException {
        if (idObject instanceof Field) {
            Field f = (Field) idObject;
            return (Serializable) f.get(persistentObject);
        } else {
            return (Serializable) idObject;
        }
    }

    private void init() {
        this.versionField = null;
        this.versionManager = null;
        this.identityFields = new ArrayList<Object>();
        this.getIdMethod = null;
        this.interceptorIds = new ArrayList<Class<? extends Interceptor<? extends Serializable>>>();
        this.sortingMethodMap = new HashMap<String, Method>();
    }

    private boolean isVersioned() {
        return this.versionField != null && this.versionManager != null;
    }

    private void parseClass(Class<? extends Serializable> persistentObjectClass) {
        parseClassIdentity(persistentObjectClass);
        parseClassInterceptors(persistentObjectClass);
    }

    private void parseClassIdentity(Class<? extends Serializable> persistentObjectClass) {
        Annotation annotation = persistentObjectClass.getAnnotation(Identity.class);
        if (annotation != null) {
            int idx = ((Identity) annotation).idx();
            if (this.identityFields.size() > idx && this.identityFields.get(idx) != null) {
                throw new GlooException(String.format("Duplicated idx value %d found for class level identity in class %s", idx, persistentObjectClass.getName()));
            }
            while (this.identityFields.size() < idx + 1) {
                this.identityFields.add(null);
            }
            this.identityFields.set(idx, persistentObjectClass);
        }
    }

    private void parseClassInterceptors(Class<? extends Serializable> persistentObjectClass) {
        Intercepted annotation = persistentObjectClass.getAnnotation(Intercepted.class);
        if (annotation != null) {
            interceptorIds.addAll(Arrays.asList(annotation.value()));
        }
    }

    private void parseFields(Class<? extends Serializable> persistentObjectClass) {
        for (Field f : persistentObjectClass.getDeclaredFields()) {
            parseIdentityFields(f);
            parseVersionField(persistentObjectClass, f);
        }
    }

    private void parseGetId(Method m) {
        Identity id = m.getAnnotation(Identity.class);
        if (id != null) {
            if (id.idx() != 0) {
                throw new GlooException("Identity methods should always use idx = 0");
            }
            if (getIdMethod != null) throw new GlooException(String.format("Ambigous identity method %s declaration. " + "Only one method per class can be annotated with @Identity", m.getName()));
            getIdMethod = m;
        }
    }

    private void parseIdentityFields(Field f) {
        if (f.isAnnotationPresent(Identity.class)) {
            f.setAccessible(true);
            int idx = f.getAnnotation(Identity.class).idx();
            if (this.identityFields.size() > idx && this.identityFields.get(idx) != null) {
                throw new GlooException(String.format("Duplicated idx value %d found for identity field %s in class %s", idx, f.toString(), f.getDeclaringClass().getName()));
            }
            while (this.identityFields.size() < idx + 1) {
                this.identityFields.add(null);
            }
            this.identityFields.set(idx, f);
        }
    }

    private void parseMethods(Class<? extends Serializable> persistentObjectClass) {
        for (Method m : persistentObjectClass.getDeclaredMethods()) {
            parseGetId(m);
            parsePostRestoreMethod(m);
            parsePreRemoveMethod(m);
            parsePostRemoveMethod(m);
            parsePreUpdateMethod(m);
            parsePostUpdateMethod(m);
            parsePreCreateMethod(m);
            parsePostCreateMethod(m);
            parseSortingCriteriaMethod(m);
        }
    }

    private void parsePostCreateMethod(Method m) {
        Annotation a = m.getAnnotation(PostCreate.class);
        if (a == null) {
            return;
        }
        if (this.postCreate != null) {
            throw new GlooException(String.format("A single method can be annotated with PostCreate: %s", m.toString()));
        }
        m.setAccessible(true);
        this.postCreate = m;
    }

    private void parsePostRemoveMethod(Method m) {
        Annotation a = m.getAnnotation(PostRemove.class);
        if (a == null) {
            return;
        }
        if (this.postRemove != null) {
            throw new GlooException(String.format("A single method can be annotated with PostRemove: %s", m.toString()));
        }
        m.setAccessible(true);
        this.postRemove = m;
    }

    private void parsePostRestoreMethod(Method m) {
        Annotation a = m.getAnnotation(PostRestore.class);
        if (a == null) {
            return;
        }
        if (this.postRestore != null) {
            throw new GlooException(String.format("A single method can be annotated with PostRestore: %s", m.toString()));
        }
        m.setAccessible(true);
        this.postRestore = m;
    }

    private void parsePostUpdateMethod(Method m) {
        Annotation a = m.getAnnotation(PostUpdate.class);
        if (a == null) {
            return;
        }
        if (this.postStore != null) {
            throw new GlooException(String.format("A single method can be annotated with PostUpdate: %s", m.toString()));
        }
        m.setAccessible(true);
        this.postStore = m;
    }

    private void parsePreCreateMethod(Method m) {
        Annotation a = m.getAnnotation(PreCreate.class);
        if (a == null) {
            return;
        }
        if (this.preCreate != null) {
            throw new GlooException(String.format("A single method can be annotated with PreCreate: %s", m.toString()));
        }
        m.setAccessible(true);
        this.preCreate = m;
    }

    private void parsePreRemoveMethod(Method m) {
        Annotation a = m.getAnnotation(PreRemove.class);
        if (a == null) {
            return;
        }
        if (this.preRemove != null) {
            throw new GlooException(String.format("A single method can be annotated with PreRemove: %s", m.toString()));
        }
        m.setAccessible(true);
        this.preRemove = m;
    }

    private void parsePreUpdateMethod(Method m) {
        Annotation a = m.getAnnotation(PreUpdate.class);
        if (a == null) {
            return;
        }
        if (this.preStore != null) {
            throw new GlooException(String.format("A single method can be annotated with PreUpdate: %s", m.toString()));
        }
        m.setAccessible(true);
        this.preStore = m;
    }

    private void parseSortingCriteriaMethod(Method m) {
        Annotation a = m.getAnnotation(SortingCriteria.class);
        if (a == null) {
            return;
        }
        m.setAccessible(true);
        addSortingMethod((SortingCriteria) a, m);
    }

    private void parseVersionField(Class<? extends Serializable> persistentObjectClass, Field f) {
        if (f.isAnnotationPresent(Version.class)) {
            if (f.isAnnotationPresent(Identity.class)) {
                throw new GlooException("Identity fields cannot be used for versioning.");
            }
            if (this.versionField != null) {
                throw new GlooException(String.format("Class %s has more than one field annotated with @Version", persistentObjectClass.getName()));
            }
            this.versionField = f;
            this.versionField.setAccessible(true);
            try {
                this.versionManager = f.getAnnotation(Version.class).manager().newInstance();
            } catch (Exception ex) {
                throw new GlooException(String.format("Cannot create version manager for class %s", persistentObjectClass.getName()));
            }
        }
    }

    private void validate(Class<? extends Serializable> persistentObjectClass) {
        if (this.identityFields.size() > 0 && this.getIdMethod != null) {
            throw new GlooException(String.format("Class %s cannot define both an identity and a method field.", persistentObjectClass.getName()));
        }
        if (this.identityFields.size() == 0 && this.getIdMethod == null) {
            throw new GlooException(String.format("Class %s does not define an identity or method field.", persistentObjectClass.getName()));
        }
        verifyConsistentIds(persistentObjectClass);
        verifyInterceptorStatus(persistentObjectClass);
    }

    private void verifyConsistentIds(Class<? extends Serializable> persistentObjectClass) {
        for (int idx = 0; idx < this.identityFields.size(); idx++) {
            if (this.identityFields.get(idx) == null) {
                throw new GlooException(String.format("Missing identity field index %d in class %s", idx, persistentObjectClass.getName()));
            }
        }
    }

    private void verifyInterceptorStatus(Class<? extends Serializable> persistentObjectClass) {
        if (Interceptor.class.isAssignableFrom(persistentObjectClass)) {
            if (!(this.identityFields.size() == 1 && identityFields.get(0).equals(persistentObjectClass))) {
                throw new GlooException("Interceptors must use their class as identity. Annotate the class  (and only the class) with @Identity");
            }
        }
    }
}
