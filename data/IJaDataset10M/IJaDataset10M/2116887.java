package org.objectwiz.spi;

import org.objectwiz.metadata.AssociationPropertyType;
import org.objectwiz.metadata.PropertyType;
import org.objectwiz.metadata.MappedClass;
import org.objectwiz.metadata.MappedProperty;
import org.objectwiz.metadata.PersistenceUnitMetadata;
import org.objectwiz.metadata.SimplePropertyType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotNull;
import org.milyn.scribe.Dao;
import org.objectwiz.PersistenceUnit;
import org.objectwiz.metadata.BusinessBean;
import org.objectwiz.metadata.BusinessMethod;
import org.objectwiz.util.TypeUtils;

/**
 * Metadata builder.
 *
 * Given a set of java classes with JPA annotations, this class creates the
 * corresponding {@link PersistenceUnitMetadata}.
 *
 * This object is not thread-safe.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class JPAClassAnalyzer {

    private static final Log logger = LogFactory.getLog(JPAClassAnalyzer.class);

    private static final Map<TemporalType, SimplePropertyType.Type> temporalTypeMapping;

    static {
        temporalTypeMapping = new HashMap();
        temporalTypeMapping.put(null, SimplePropertyType.Type.DATETIME);
        temporalTypeMapping.put(TemporalType.DATE, SimplePropertyType.Type.DATE);
        temporalTypeMapping.put(TemporalType.TIME, SimplePropertyType.Type.TIME);
        temporalTypeMapping.put(TemporalType.TIMESTAMP, SimplePropertyType.Type.DATETIME);
    }

    private Map<String, Class> classMap;

    private class TypeInformation {

        public boolean isCollection;

        public boolean isIndexedCollection;

        public Class collectionInnerType;

        public MappedClass mappedClass;

        public TypeInformation(boolean isCollection, boolean isIndexedCollection, Class collectionInnerType, MappedClass associatedMappedClass) {
            this.isCollection = isCollection;
            this.isIndexedCollection = isIndexedCollection;
            this.collectionInnerType = collectionInnerType;
            this.mappedClass = associatedMappedClass;
        }
    }

    JPAClassAnalyzer() {
    }

    public static JPAClassAnalyzer instance() {
        return new JPAClassAnalyzer();
    }

    /**
     * This method instanciates {@link MappedClass} objects from a collection
     * of real JPA-annotated classes and registers them to the metadata.
     *
     * NOTE: this method detects parent, embedded and enum classes that are contained
     * within the given collection so providing the entity classes is enough.
     *
     * @param metadata  The metadata that has to be populated
     * @param classes   A collection of classes annotated with {@link Entity}
     */
    public void populateMetadata(PersistenceUnitMetadata metadata, Collection<Class> classes) {
        classMap = new HashMap();
        for (Class clazz : classes) {
            analyzeClass(metadata, clazz, true);
        }
        for (MappedClass mc : metadata.getMappedClasses()) {
            Class clazz = classMap.get(mc.getClassName());
            if (mc.getParentMappedClass() != null) {
                mc.getParentMappedClass().registerChildMappedClass(mc);
            }
            analyzeProperties(mc, clazz);
        }
    }

    /**
     * Helper that registers the {@link MappedClass} into
     * the {@link PersistenceUnitMetadata}.
     */
    private void registerMappedClass(PersistenceUnitMetadata metadata, MappedClass mc, Class clazz) {
        metadata.registerMappedClass(mc);
        classMap.put(clazz.getName(), clazz);
        logger.info("Class registered: " + mc.getClassName() + " [abstract: " + mc.isAbstract() + ", type: " + mc.getType() + "]");
    }

    /**
     * Helper that analyses a JPA-annotated {@link Class} and registers
     * it into the metadata.
     * @param metadata       The metadata to populate
     * @param clazz          The JPA-annotated class
     * @param isChildClass   A flag indicating whether this class is a child
     *                       class (i.e. must in particular have a public no-args
     *                       default constructor).
     */
    private void analyzeClass(PersistenceUnitMetadata metadata, Class clazz, boolean isChildClass) {
        logger.debug("Analyzing class: " + clazz.getName());
        boolean isAbstract = Modifier.isAbstract(clazz.getModifiers());
        if (isChildClass && !(isAbstract || clazz.isEnum())) try {
            clazz.getConstructor(new Class[] {});
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Invalid class, does not have a public no-args constructor: " + clazz.getName());
        }
        boolean embeddable = clazz.getAnnotation(Embeddable.class) != null;
        boolean isSuperclass = clazz.getAnnotation(MappedSuperclass.class) != null;
        Class superclass = clazz.getSuperclass();
        if (superclass.getAnnotation(MappedSuperclass.class) != null) {
            logger.debug("Superclass is a @MappedSuperclass: " + superclass.getName());
            try {
                MappedClass superMappedClass = metadata.getMappedClass(superclass.getName(), false);
                if (superMappedClass == null) {
                    analyzeClass(metadata, superclass, false);
                }
            } catch (ClassNotFoundException e) {
            }
        }
        Iterator<Method> getterIt = getPropertiesMap(clazz).values().iterator();
        while (getterIt.hasNext()) {
            Method getter = getterIt.next();
            Class returnType = getter.getReturnType();
            TypeInformation typeInfo = analyzeGetter(metadata, getter);
            boolean isEnum = returnType.isEnum();
            boolean isCollectionOfEnums = typeInfo.isCollection && typeInfo.collectionInnerType != null && typeInfo.collectionInnerType.isEnum();
            boolean isEmbedded = getter.isAnnotationPresent(Embedded.class);
            if (!(isEnum || isCollectionOfEnums || isEmbedded)) continue;
            MappedClass innerMc = null;
            Class innerClass = typeInfo.isCollection ? typeInfo.collectionInnerType : getter.getReturnType();
            try {
                innerMc = metadata.getMappedClass(innerClass.getName(), false);
            } catch (Exception e) {
                assert false;
            }
            if (innerMc != null) continue;
            if (isEnum) {
                logger.info("Found enumeration: " + innerClass.getName());
                innerMc = new MappedClass(metadata, returnType.getName(), readEnumValuesName(innerClass));
                registerMappedClass(metadata, innerMc, innerClass);
            } else {
                assert isEmbedded;
                logger.info("Found embeddable class: " + innerClass.getName());
                analyzeClass(metadata, innerClass, true);
            }
        }
        MappedClass mc = new MappedClass(metadata, clazz.getName(), superclass == null ? null : superclass.getName(), embeddable, isSuperclass, isAbstract);
        registerMappedClass(metadata, mc, clazz);
    }

    /**
     * Helper method that returns the map of properties detected in a
     * {@link Class}.
     * @param clazz       The class to analyse.
     * @return The map of getters. The keys are the names of the properties
     * and the values the getter {@link Method}s.
     */
    private Map<String, Method> getPropertiesMap(Class clazz) {
        final Map<String, Method> getters = new LinkedHashMap();
        for (Method method : clazz.getDeclaredMethods()) {
            if ((!Modifier.isPublic(method.getModifiers())) || Modifier.isStatic(method.getModifiers())) continue;
            boolean startsWithIs = method.getName().startsWith("is");
            if (method.getName().startsWith("get") || startsWithIs) {
                if (startsWithIs && (!TypeUtils.returnsBoolean(method))) {
                    continue;
                }
                String propertyName = StringUtils.uncapitalize(method.getName().substring(startsWithIs ? 2 : 3));
                getters.put(propertyName, method);
            }
        }
        return getters;
    }

    /**
     * Helper method that registers the properties of a JPA-annotated class
     * into the corresponding {@link MappedClass}.
     * @param mc        The {@link MappedClass} to populate.
     * @param clazz     The {@link Class} object that corresponds to the mapped class.
     */
    private void analyzeProperties(MappedClass mc, Class clazz) {
        Iterator<Entry<String, Method>> it = getPropertiesMap(clazz).entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Method> entry = it.next();
            String propertyName = entry.getKey();
            Method getter = entry.getValue();
            boolean isIdProperty = getter.isAnnotationPresent(Id.class);
            boolean isGenerated = getter.isAnnotationPresent(GeneratedValue.class);
            boolean isVersionProperty = getter.isAnnotationPresent(Version.class);
            boolean isTransient = getter.getAnnotation(Transient.class) != null;
            boolean unique = false;
            Column column = getter.getAnnotation(Column.class);
            if (column != null) {
                unique = column.unique();
            }
            PropertyType type = null;
            try {
                type = analyzeType(mc.getMetadata(), propertyName, mc, getter);
            } catch (Exception e) {
                if (isTransient) {
                    logger.warn("Skipping transient property '" + propertyName + "': type not supported", e);
                } else {
                    throw new RuntimeException("Error while analyzing type for: " + mc.getClassName() + "." + propertyName, e);
                }
            }
            MappedProperty property = new MappedProperty(mc, propertyName, type, false, unique, isTransient, isGenerated);
            mc.registerProperty(property, isIdProperty, isVersionProperty);
            logger.debug(mc.getClassName() + " -> Property: " + propertyName + " registered");
        }
    }

    /**
     * Helper that analyses a getter {@link Method} that corresponds to a
     * property of a {@link MappedClass} in order to determine its type
     * ({@link PropertyType}).
     * @param metadata               The metadata with all registered classes
     * @param propertyName           The name of the property that is analyzed
     * @param propertyMappedClass    The {@link MappedClass} that contains this property
     * @param getter                 The getter {@link Method} associated to this property.
     * @return the {@link PropertyType} corresponding to this property.
     */
    private PropertyType analyzeType(PersistenceUnitMetadata metadata, String propertyName, MappedClass propertyMappedClass, Method getter) {
        boolean isDebugEnabled = logger.isDebugEnabled();
        Class javaType = getter.getReturnType();
        TypeInformation typeInfo = analyzeGetter(metadata, getter);
        boolean isNullable = true;
        if (getter.isAnnotationPresent(Column.class)) {
            Column column = getter.getAnnotation(Column.class);
            isNullable = column.nullable();
        } else if (getter.isAnnotationPresent(JoinColumn.class)) {
            JoinColumn column = getter.getAnnotation(JoinColumn.class);
            isNullable = column.nullable();
        }
        if (getter.isAnnotationPresent(NotNull.class)) {
            isNullable = false;
        }
        if (typeInfo.mappedClass != null) {
            String associatedPropertyName = detectCreateAssociatedProperty(metadata, propertyName, propertyMappedClass, getter, typeInfo);
            if (isDebugEnabled) {
                logger.debug("Analyzing association property '" + propertyName + "':" + "\n  Type: " + javaType + "\n  Nullable: " + isNullable + "\n  Collection: " + (typeInfo.isCollection) + (typeInfo.isCollection ? " (indexed: " + typeInfo.isIndexedCollection + ")" : "") + "\n  Opposite property: " + (associatedPropertyName == null ? "<none>" : typeInfo.mappedClass.getClassName() + "." + associatedPropertyName));
            }
            boolean isEmbedded = getter.getAnnotation(Embedded.class) != null;
            String[] embeddedProperties = null;
            if (isEmbedded) {
                AttributeOverrides ao = getter.getAnnotation(AttributeOverrides.class);
                if (ao != null) {
                    embeddedProperties = new String[ao.value().length];
                    for (int i = 0; i < ao.value().length; i++) {
                        embeddedProperties[i] = ao.value()[i].name();
                    }
                }
            }
            return new AssociationPropertyType(metadata, javaType.getName(), isNullable, typeInfo.isCollection, typeInfo.isIndexedCollection, typeInfo.mappedClass, associatedPropertyName, isEmbedded, embeddedProperties);
        } else {
            if (javaType.isPrimitive()) isNullable = false;
            Class clazz = typeInfo.isCollection ? typeInfo.collectionInnerType : getter.getReturnType();
            SimplePropertyType.Type type = detectSimpleType(getter, clazz);
            logger.debug("Simple property '" + propertyName + "' is: " + type);
            return new SimplePropertyType(metadata, javaType.getName(), type, isNullable, typeInfo.isCollection, typeInfo.isIndexedCollection);
        }
    }

    private SimplePropertyType.Type detectSimpleType(Method getter, Class clazz) {
        if (Date.class.equals(clazz)) {
            Temporal temporal = getter.getAnnotation(Temporal.class);
            return temporalTypeMapping.get(temporal == null ? null : temporal.value());
        } else {
            return SimplePropertyType.parseJavaType(clazz.getName());
        }
    }

    /**
     * The job of this method is to detect what is the opposite property of the
     * relation for the given property, and to create it (as a virtual property)
     * if it does not exist.
     * @param metadata             The metadata of the target unit
     * @param propertyName         Name of the property to analyze
     * @param propertyMappedClass  Class that contains the property to analyze
     * @param getter               Getter method corresponding to that property.
     * @param typeInfo             Information about the getter that was already parsed.
     *                             The mapped class in 'typeInfo' cannot be null.
     * @return String the name of the associated property.
     */
    private String detectCreateAssociatedProperty(PersistenceUnitMetadata metadata, String propertyName, MappedClass propertyMappedClass, Method getter, TypeInformation typeInfo) {
        boolean isTraceEnabled = logger.isTraceEnabled();
        if (typeInfo.mappedClass == null) throw new IllegalArgumentException("typeInfo.mappedClass cannot be null");
        String associatedPropertyName = getMappedBy(getter);
        if (associatedPropertyName != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("Associated mapped class found during first pass: " + associatedPropertyName);
            }
            return associatedPropertyName;
        }
        MappedClass oppositeMappedClass = typeInfo.mappedClass;
        Class oppositeClass = classMap.get(oppositeMappedClass.getClassName());
        List<String> possibleManyToManyMatches = new ArrayList();
        Iterator<Entry<String, Method>> oppositePropertiesIt = getPropertiesMap(oppositeClass).entrySet().iterator();
        while (oppositePropertiesIt.hasNext()) {
            Entry<String, Method> oppositePropertyEntry = oppositePropertiesIt.next();
            Method oppositeGetter = oppositePropertyEntry.getValue();
            String mappedBy = getMappedBy(oppositeGetter);
            if (propertyName.equals(mappedBy)) {
                associatedPropertyName = oppositePropertyEntry.getKey();
                if (isTraceEnabled) {
                    logger.trace("Opposite property detected during second pass: " + associatedPropertyName);
                }
                return associatedPropertyName;
            } else {
                ManyToMany mtm = oppositeGetter.getAnnotation(ManyToMany.class);
                if (mtm != null && StringUtils.isEmpty(mtm.mappedBy())) {
                    logger.debug("Checking potential ManyToMany annotation: " + oppositeGetter.getName());
                    TypeInformation ti = analyzeGetter(metadata, oppositePropertyEntry.getValue());
                    if (ti.mappedClass != null) {
                        logger.trace("Matching: " + ti.mappedClass + " / " + propertyMappedClass + " : " + ti.mappedClass.equals(propertyMappedClass));
                    }
                    if (ti.mappedClass != null && ti.mappedClass.equals(propertyMappedClass)) {
                        possibleManyToManyMatches.add(oppositePropertyEntry.getKey());
                    }
                }
            }
        }
        if (possibleManyToManyMatches.size() > 1) {
            throw new RuntimeException("Unable to detect associated property, several possible matches: " + possibleManyToManyMatches);
        } else if (possibleManyToManyMatches.size() == 1) {
            associatedPropertyName = possibleManyToManyMatches.get(0);
            if (isTraceEnabled) {
                logger.trace("Opposite property selected during third pass: " + associatedPropertyName);
            }
        } else {
            boolean isTransient = getter.isAnnotationPresent(Transient.class);
            if (isTransient) return null;
            boolean isCollection = getter.isAnnotationPresent(ManyToOne.class) || getter.isAnnotationPresent(ManyToMany.class);
            associatedPropertyName = (isCollection ? "ListOf" : "") + propertyMappedClass.getClassSimpleName() + (isCollection ? "s" : "") + "MappedBy" + StringUtils.capitalize(propertyName);
            String typeName = isCollection ? "List<" + oppositeClass.getName() + ">" : oppositeClass.getName();
            AssociationPropertyType type = new AssociationPropertyType(metadata, typeName, true, isCollection, false, propertyMappedClass, propertyName, false, null);
            MappedProperty virtualProperty = new MappedProperty(oppositeMappedClass, associatedPropertyName, type, true, false, false, false);
            oppositeMappedClass.registerProperty(virtualProperty, false, false);
            logger.info("Virtual property registered: " + associatedPropertyName + ", " + "mapping " + propertyMappedClass.getClassName() + "#" + propertyName);
        }
        return associatedPropertyName;
    }

    private TypeInformation analyzeGetter(PersistenceUnitMetadata metadata, Method getter) {
        Class javaType = getter.getReturnType();
        Class collectionInnerType = null;
        boolean isCollection = false;
        boolean isIndexedCollection = false;
        if (javaType.isArray()) {
            isCollection = true;
            collectionInnerType = javaType.getComponentType();
        } else if (Map.class.isAssignableFrom(javaType)) {
            isCollection = true;
            isIndexedCollection = true;
            collectionInnerType = readGenericType(getter, 1);
        } else if (Collection.class.isAssignableFrom(javaType)) {
            isCollection = true;
            collectionInnerType = readGenericType(getter, 0);
        }
        MappedClass mappedClass = null;
        try {
            mappedClass = metadata.getMappedClass((isCollection ? collectionInnerType : javaType).getName(), false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Internal error, method shall return NULL when class is not found", e);
        }
        return new TypeInformation(isCollection, isIndexedCollection, collectionInnerType, mappedClass);
    }

    private String getMappedBy(Method getter) {
        OneToOne oto = getter.getAnnotation(OneToOne.class);
        ManyToMany mtm = getter.getAnnotation(ManyToMany.class);
        OneToMany otm = getter.getAnnotation(OneToMany.class);
        if (oto != null) {
            return StringUtils.defaultIfEmpty(oto.mappedBy(), null);
        } else if (mtm != null) {
            return StringUtils.defaultIfEmpty(mtm.mappedBy(), null);
        } else if (otm != null) {
            return StringUtils.defaultIfEmpty(otm.mappedBy(), null);
        }
        return null;
    }

    private Class readGenericType(Method getter, int offset) {
        if (!(getter.getGenericReturnType() instanceof ParameterizedType)) {
            throw new RuntimeException("could not read parameterized type");
        }
        Type[] genericTypes = ((ParameterizedType) getter.getGenericReturnType()).getActualTypeArguments();
        return (Class) genericTypes[offset];
    }

    private LinkedHashMap<String, String> readEnumValuesName(Class clazz) {
        if (!clazz.isEnum()) throw new IllegalArgumentException("Not an enumeration: " + clazz.getName());
        LinkedHashMap<String, String> values = new LinkedHashMap();
        for (Object constant : clazz.getEnumConstants()) {
            Enum enumValue = (Enum) constant;
            values.put(enumValue.name(), enumValue.toString());
        }
        return values;
    }

    public BusinessBean createBusinessBean(PersistenceUnit unit, String name, String beanFullReference, Class beanInterface) {
        boolean smooksDao = beanInterface.isAnnotationPresent(Dao.class);
        BusinessBean bean = new BusinessBean(unit.getMetadata(), name, beanFullReference, smooksDao);
        registerBusinessMethods(bean, beanInterface);
        return bean;
    }

    private void registerBusinessMethods(BusinessBean bean, Class beanInterface) {
        for (Method method : beanInterface.getMethods()) {
            String methodName = formatMethod(method);
            bean.registerMethod(new BusinessMethod(bean, methodName, method.getName(), toStringArray(method.getParameterTypes()), method.getReturnType().getName()));
            logger.info("Method registered: " + bean.getBeanName() + "#" + methodName);
        }
    }

    private String[] toStringArray(Class[] array) {
        String[] r = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            r[i] = array[i].getName();
        }
        return r;
    }

    private String formatMethod(Method method) {
        StringBuilder parameters = new StringBuilder();
        for (Type type : method.getParameterTypes()) {
            if (parameters.length() > 0) parameters.append(", ");
            if (type instanceof Class) {
                parameters.append(((Class) type).getSimpleName());
            } else {
                parameters.append(type.toString());
            }
        }
        return method.getName() + "(" + parameters + "): " + formatJavaType(method.getReturnType());
    }

    private String formatJavaType(Class type) {
        return type.getName();
    }
}
