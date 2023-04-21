package net.sf.webwarp.util.beans.comparator;

import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.webwarp.util.exception.ExceptionType;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * This class compares all properties of two beans (source and destination). Comparison between primitives and it's wrapper types are handled transparently; in
 * the {@link #classesToCompare} you may specify either primitiv types or it's wrapper types.
 * <p>
 * If a (source!) bean properties name is not contained in the {@link #propertiesToIgnore} and its type is assignable to a {@linkplain #classesToCompare} it
 * will be included in the comparison (notice that the destination beans property must not be of the same type or be a subtype...).
 * <p>
 * For any boolean property (boolean or Boolean) the read method may start with <i>'get'</i> or <i>'is'</i>.
 * <p>
 * Property comparison is done
 * <ul>
 * <li>if values are instanceof {@link Comparable} via it's {@link Comparable#compareTo(Object)} method
 * <li>esle via the {@link Object#equals(Object)} method
 * <p>
 * Per Default there are the fallowing {@link PropertyValueComparator}s registered:
 * <ul>
 * <li>{@link StringValueComparator}
 * <li>{@link DateMillisValueComparator}
 */
@SuppressWarnings("unchecked")
public class BeanPropertiesComparator {

    private static final Class[] DEFAULT_CLASSES_TO_COMPARE = new Class[] { Boolean.class, Character.class, CharSequence.class, Number.class, Date.class };

    private static final boolean DEFAULT_RECURSIVE = false;

    private static final String[] DEFAULT_PROPERTIES_TO_IGNORE = null;

    private static final Map<Class, Class> PRIMITIV_WRAPPERS = new HashMap<Class, Class>();

    static {
        PRIMITIV_WRAPPERS.put(boolean.class, Boolean.class);
        PRIMITIV_WRAPPERS.put(byte.class, Byte.class);
        PRIMITIV_WRAPPERS.put(short.class, Short.class);
        PRIMITIV_WRAPPERS.put(int.class, Integer.class);
        PRIMITIV_WRAPPERS.put(long.class, Long.class);
        PRIMITIV_WRAPPERS.put(float.class, Float.class);
        PRIMITIV_WRAPPERS.put(double.class, Double.class);
        PRIMITIV_WRAPPERS.put(char.class, Character.class);
    }

    private Map<Class, PropertyValueComparator> propertyValueComparators = new HashMap<Class, PropertyValueComparator>();

    private static final Logger log = Logger.getLogger(BeanPropertiesComparator.class);

    private Class[] classesToCompare = DEFAULT_CLASSES_TO_COMPARE;

    private String[] propertiesToIgnore = DEFAULT_PROPERTIES_TO_IGNORE;

    private boolean recursiv = DEFAULT_RECURSIVE;

    private boolean compareClassProperty = false;

    /**
     * Constructs a new {@link BeanPropertiesComparator} object with some usefull default values.
     * <p>
     * <b>{@link #classesToCompare}</b>
     * <ul>
     * <li>{@link Boolean}
     * <li>{@link Character}
     * <li>{@link CharSequence}: {@link String}, {@link StringBuffer}, {@link StringBuilder}
     * <li>{@link Number}: {@link Byte}, {@link Short}, {@link Integer}, {@link Long}, {@link Float}, {@link Double}, {@link BigDecimal},
     * {@link BigInteger}
     * <li>{@link Date}: {@link java.sql.Date}. {@link Timestamp}
     * </ul>
     * <p>
     * <b>{@link #propertiesToIgnore}</b> null
     * <p>
     * <b>{@link #recursiv}</b> false
     */
    public BeanPropertiesComparator() {
        this(DEFAULT_CLASSES_TO_COMPARE, DEFAULT_PROPERTIES_TO_IGNORE, DEFAULT_RECURSIVE);
    }

    /**
     * Constructs a new {@link BeanPropertiesComparator} object with some usefull default values.
     * <p>
     * <b>{@link #classesToCompare}</b>
     * <ul>
     * <li>{@link Boolean}
     * <li>{@link Character}
     * <li>{@link CharSequence}: {@link String}, {@link StringBuffer}, {@link StringBuilder}
     * <li>{@link Number}: {@link Byte}, {@link Short}, {@link Integer}, {@link Long}, {@link Float}, {@link Double}, {@link BigDecimal},
     * {@link BigInteger}
     * <li>{@link Date}: {@link java.sql.Date}. {@link Timestamp}
     * </ul>
     * <p>
     * <b>{@link #recursiv}</b> false
     */
    public BeanPropertiesComparator(String[] propertiesToIgnore) {
        this(DEFAULT_CLASSES_TO_COMPARE, propertiesToIgnore, DEFAULT_RECURSIVE);
    }

    /**
     * Constructs a new {@link BeanPropertiesComparator} object with some usefull default values.
     * <p>
     * <b>{@link #classesToCompare}</b>
     * <ul>
     * <li>{@link Boolean}
     * <li>{@link Character}
     * <li>{@link CharSequence}: {@link String}, {@link StringBuffer}, {@link StringBuilder}
     * <li>{@link Number}: {@link Byte}, {@link Short}, {@link Integer}, {@link Long}, {@link Float}, {@link Double}, {@link BigDecimal},
     * {@link BigInteger}
     * <li>{@link Date}: {@link java.sql.Date}. {@link Timestamp}
     * </ul>
     */
    public BeanPropertiesComparator(String[] propertiesToIgnore, boolean recursiv) {
        this(DEFAULT_CLASSES_TO_COMPARE, propertiesToIgnore, recursiv);
    }

    /**
     * Constructs a new {@link BeanPropertiesComparator} object with some usefull default values.
     * <p>
     * <b>{@link #classesToCompare}</b>
     * <ul>
     * <li>{@link Boolean}
     * <li>{@link Character}
     * <li>{@link CharSequence}: {@link String}, {@link StringBuffer}, {@link StringBuilder}
     * <li>{@link Number}: {@link Byte}, {@link Short}, {@link Integer}, {@link Long}, {@link Float}, {@link Double}, {@link BigDecimal},
     * {@link BigInteger}
     * <li>{@link Date}: {@link java.sql.Date}. {@link Timestamp}
     * </ul>
     * <p>
     * <b>{@link #propertiesToIgnore}</b> null
     */
    public BeanPropertiesComparator(boolean recursiv) {
        this(DEFAULT_CLASSES_TO_COMPARE, DEFAULT_PROPERTIES_TO_IGNORE, recursiv);
    }

    /**
     * Constructs a new {@link BeanPropertiesComparator} object with some usefull default values.
     * <p>
     * <b>{@link #propertiesToIgnore}</b> null
     * <p>
     * <b>{@link #recursiv}</b> false
     */
    public BeanPropertiesComparator(Class[] classesToCompare) {
        this(classesToCompare, DEFAULT_PROPERTIES_TO_IGNORE, DEFAULT_RECURSIVE);
    }

    /**
     * Constructs a new {@link BeanPropertiesComparator} object with some usefull default values.
     * <p>
     * <b>{@link #propertiesToIgnore}</b> null
     */
    public BeanPropertiesComparator(Class[] classesToCompare, boolean recursiv) {
        this(classesToCompare, DEFAULT_PROPERTIES_TO_IGNORE, recursiv);
    }

    /**
     * This is the most specific constructor.
     * 
     * @param classesToCompare
     * @param propertiesToIgnore
     * @param recursiv
     */
    public BeanPropertiesComparator(Class[] classesToCompare, String[] propertiesToIgnore, boolean recursiv) {
        validateClassesToCompare(classesToCompare);
        validatePropertiesToIgnore(propertiesToIgnore);
        init();
        this.classesToCompare = classesToCompare;
        this.propertiesToIgnore = propertiesToIgnore;
        this.recursiv = recursiv;
    }

    /**
     * The property values of the source bean are considered as <i>'oldValues'</i> and the property values of the destination bean are considered as
     * <i>'newValues'</i>. Only the property types of the source bean will be compared with the {@link #classesToCompare}, to check if they are assignable.
     * The property types of the destination bean are irrelevant.
     * 
     * @param src
     * @param dest
     * @return empty list if no changes are found.
     */
    public List<PropertyChangeEvent> compareBeans(Object src, Object dest) {
        Validate.notNull(src, "src may not be null");
        Validate.notNull(dest, "dest may not be null");
        final List<PropertyChangeEvent> events = new ArrayList<PropertyChangeEvent>();
        compareBeans(src, dest, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                events.add(evt);
            }
        });
        return events;
    }

    /**
     * The property values of the source bean are considered as <i>'oldValues'</i> and the property values of the destination bean are considered as
     * <i>'newValues'</i>. Only the property types of the source bean will be compared with the {@link #classesToCompare}, to check if they are assignable.
     * The property types of the destination bean are irrelevant. If the property values are not the same the
     * {@link PropertyChangeListener#propertyChange(PropertyChangeEvent)} method is called.
     * 
     * @param src
     * @param dest
     * @param listener
     * @throws BeanPropertiesComparatorException
     */
    public void compareBeans(Object src, Object dest, PropertyChangeListener listener) {
        Validate.notNull(src, "src may not be null");
        Validate.notNull(dest, "dest may not be null");
        Validate.notNull(listener, "listener may not be null");
        try {
            PropertyDescriptor[] srcDescriptors = Introspector.getBeanInfo(src.getClass()).getPropertyDescriptors();
            PropertyDescriptor[] destDescriptors = srcDescriptors;
            if (src.getClass() != dest.getClass()) {
                destDescriptors = Introspector.getBeanInfo(dest.getClass()).getPropertyDescriptors();
            }
            for (PropertyDescriptor srcDescriptor : srcDescriptors) {
                PropertyDescriptor destDescriptor = findPropertyDescriptor(destDescriptors, srcDescriptor.getName());
                if (destDescriptor == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("the object" + dest + " has no property: " + srcDescriptor.getName() + ". So the comparison of this property will be omitted");
                    }
                    continue;
                }
                if (!ignoreProperty(srcDescriptor)) {
                    if (isComparePropertyClass(srcDescriptor.getPropertyType())) {
                        compareProperties(src, srcDescriptor, dest, destDescriptor, listener);
                    } else if (recursiv) {
                        Class<?> propertyType = srcDescriptor.getPropertyType();
                        if (Collection.class.isAssignableFrom(propertyType)) {
                            return;
                        } else if (Map.class.isAssignableFrom(propertyType)) {
                            return;
                        } else if (propertyType.isArray()) {
                            return;
                        }
                        Object srcVal = srcDescriptor.getReadMethod().invoke(src);
                        Object destVal = destDescriptor.getReadMethod().invoke(dest);
                        if (srcVal == null || destVal == null) {
                            compareProperties(src, srcDescriptor, dest, destDescriptor, listener);
                        } else {
                            compareBeans(srcVal, destVal, listener);
                        }
                    }
                }
            }
        } catch (BeanPropertiesComparatorException e) {
            throw e;
        } catch (Exception e) {
            throw new BeanPropertiesComparatorException(ExceptionType.RUNTIME, BeanPropertiesComparatorException.ERROR_COMPARATOR_UNKNOWN, e);
        }
    }

    private void init() {
        propertyValueComparators.put(String.class, new StringValueComparator());
        propertyValueComparators.put(Date.class, new DateMillisValueComparator());
    }

    private Method getReadMethod(Object o, PropertyDescriptor descriptor) {
        Method method = descriptor.getReadMethod();
        if (method != null) {
            return method;
        } else {
            String name = descriptor.getName();
            String baseName = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (descriptor.getPropertyType() == boolean.class) {
                try {
                    method = o.getClass().getMethod("get" + baseName);
                    return method;
                } catch (Exception e) {
                    if (log.isDebugEnabled()) {
                        log.debug("Encountered invalid property: get" + baseName);
                    }
                }
            }
            if (descriptor.getPropertyType() == Boolean.class) {
                try {
                    method = o.getClass().getMethod("is" + baseName);
                    return method;
                } catch (Exception e) {
                    if (log.isDebugEnabled()) {
                        log.debug("Encountered invalid property: is" + baseName);
                    }
                }
            }
            throw new IllegalArgumentException("object: " + o + " has no read method for property: " + descriptor.getName());
        }
    }

    private void compareProperties(Object src, PropertyDescriptor srcDescriptor, Object dest, PropertyDescriptor destDescriptor, PropertyChangeListener listener) {
        Object srcVal = getPropertyValue(src, srcDescriptor);
        Object destVal = getPropertyValue(dest, destDescriptor);
        compareValues(src, srcDescriptor, srcVal, dest, destDescriptor, destVal, listener);
    }

    private Object getPropertyValue(Object src, PropertyDescriptor srcDescriptor) {
        Method method = getReadMethod(src, srcDescriptor);
        try {
            return method.invoke(src);
        } catch (Exception e) {
            throw new BeanPropertiesComparatorException(ExceptionType.RUNTIME, BeanPropertiesComparatorException.ERROR_GETTING_PROPERTY, e);
        }
    }

    private void compareValues(Object src, PropertyDescriptor srcDescriptor, Object srcVal, Object dest, PropertyDescriptor destDescriptor, Object destVal, PropertyChangeListener listener) {
        PropertyValueComparator comparator = propertyValueComparators.get(srcDescriptor.getPropertyType());
        if (comparator != null) {
            if (comparator.valueChanged(srcVal, destVal)) {
                firePropertyValueChanged(src, srcDescriptor.getName(), srcVal, destVal, listener);
            }
        } else {
            defaultCompareValues(src, srcDescriptor, srcVal, dest, destDescriptor, destVal, listener);
        }
    }

    private void defaultCompareValues(Object src, PropertyDescriptor srcDescriptor, Object srcVal, Object dest, PropertyDescriptor destDescriptor, Object destVal, PropertyChangeListener listener) {
        if (srcVal == null && destVal == null) {
            return;
        }
        if (srcVal != null && destVal != null) {
            if (srcVal instanceof Comparable && destVal instanceof Comparable) {
                Comparable srcComp = (Comparable) srcVal;
                Comparable destComp = (Comparable) destVal;
                try {
                    if (srcComp.compareTo(destComp) == 0) {
                        return;
                    }
                } catch (Exception e) {
                }
            }
            if (srcVal.equals(destVal)) {
                return;
            }
        }
        firePropertyValueChanged(src, srcDescriptor.getName(), srcVal, destVal, listener);
    }

    private void firePropertyValueChanged(Object src, String name, Object srcVal, Object destVal, PropertyChangeListener listener) {
        listener.propertyChange(new PropertyChangeEvent(src, name, srcVal, destVal));
    }

    private boolean isComparePropertyClass(Class propertyType) {
        for (Class clazz : classesToCompare) {
            if (clazz.isAssignableFrom(propertyType)) {
                return true;
            }
            if (propertyType.isPrimitive()) {
                if (clazz.isAssignableFrom(PRIMITIV_WRAPPERS.get(propertyType))) {
                    return true;
                }
            }
            if (clazz.isPrimitive()) {
                if (PRIMITIV_WRAPPERS.get(clazz).isAssignableFrom(propertyType)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean ignoreProperty(PropertyDescriptor descriptor) {
        if (!compareClassProperty && "class".equals(descriptor.getName())) {
            return true;
        }
        if (propertiesToIgnore != null) {
            for (String nameToIgnore : propertiesToIgnore) {
                if (descriptor.getName().equals(nameToIgnore)) {
                    if (log.isDebugEnabled()) {
                        log.debug("ignoring property: " + nameToIgnore);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private PropertyDescriptor findPropertyDescriptor(PropertyDescriptor[] descriptors, String name) {
        for (PropertyDescriptor descriptor : descriptors) {
            if (descriptor.getName().equals(name)) {
                return descriptor;
            }
        }
        return null;
    }

    private void validateClassesToCompare(Class[] classesToCompare) {
        Validate.notNull(classesToCompare, "classesToCompare may not be null");
        Validate.isTrue(classesToCompare.length > 0, "classesToCompare may not be empty");
        for (Class clazz : classesToCompare) {
            Validate.notNull(clazz, "classesToCompare may not have null elements");
        }
    }

    private void validatePropertiesToIgnore(String[] propertiesToIgnore) {
        if (propertiesToIgnore != null && propertiesToIgnore.length > 0) {
            for (String propertyName : propertiesToIgnore) {
                Validate.notNull(propertyName, "propertiesToIgnor may not have null elements");
            }
        }
    }

    public void addPropertyValueComparator(Class clazz, PropertyValueComparator propertyValueComparator) {
        propertyValueComparators.put(clazz, propertyValueComparator);
    }

    public PropertyValueComparator removePropertyValueComparator(Class clazz) {
        return propertyValueComparators.remove(clazz);
    }

    public Collection<PropertyValueComparator> getPropertyValueComparators() {
        return propertyValueComparators.values();
    }

    public Class[] getClassesToCompare() {
        return classesToCompare.clone();
    }

    public void setClassesToCompare(Class[] classesToCompare) {
        this.classesToCompare = classesToCompare;
    }

    public String[] getPropertiesToIgnore() {
        return propertiesToIgnore.clone();
    }

    public void setPropertiesToIgnore(String[] propertiesToIgnore) {
        this.propertiesToIgnore = propertiesToIgnore;
    }

    public boolean isRecursiv() {
        return recursiv;
    }

    public void setRecursiv(boolean recursiv) {
        this.recursiv = recursiv;
    }

    /**
     * defaults to false
     * 
     * @return
     */
    public boolean isCompareClassProperty() {
        return compareClassProperty;
    }

    public void setCompareClassProperty(boolean compareClassProperty) {
        this.compareClassProperty = compareClassProperty;
    }
}
