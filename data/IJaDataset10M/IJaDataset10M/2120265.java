package javax.management.openmbean;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.management.Descriptor;
import javax.management.DescriptorRead;
import javax.management.ImmutableDescriptor;
import javax.management.MBeanAttributeInfo;
import com.sun.jmx.remote.util.EnvHelp;

/**
 * Describes an attribute of an open MBean.
 *
 * @version     3.43  06/03/15
 * @author      Sun Microsystems, Inc.
 *
 * @since 1.5
 * @since.unbundled JMX 1.1
 */
public class OpenMBeanAttributeInfoSupport extends MBeanAttributeInfo implements OpenMBeanAttributeInfo {

    static final long serialVersionUID = -4867215622149721849L;

    /**
     * @serial The open mbean attribute's <i>open type</i>
     */
    private OpenType<?> openType;

    /**
     * @serial The open mbean attribute's default value
     */
    private final Object defaultValue;

    /**
     * @serial The open mbean attribute's legal values. This {@link
     * Set} is unmodifiable
     */
    private final Set<?> legalValues;

    /**
     * @serial The open mbean attribute's min value
     */
    private final Comparable minValue;

    /**
     * @serial The open mbean attribute's max value
     */
    private final Comparable maxValue;

    private transient Integer myHashCode = null;

    private transient String myToString = null;

    /**
     * Constructs an {@code OpenMBeanAttributeInfoSupport} instance,
     * which describes the attribute of an open MBean with the
     * specified {@code name}, {@code openType} and {@code
     * description}, and the specified read/write access properties.
     *
     * @param name  cannot be a null or empty string.
     *
     * @param description  cannot be a null or empty string.
     *
     * @param openType  cannot be null.
     *
     * @param isReadable {@code true} if the attribute has a getter
     * exposed for management.
     *
     * @param isWritable {@code true} if the attribute has a setter
     * exposed for management.
     *
     * @param isIs {@code true} if the attribute's getter is of the
     * form <tt>is<i>XXX</i></tt>.
     *
     * @throws IllegalArgumentException if {@code name} or {@code
     * description} are null or empty string, or {@code openType} is
     * null.
     */
    public OpenMBeanAttributeInfoSupport(String name, String description, OpenType<?> openType, boolean isReadable, boolean isWritable, boolean isIs) {
        this(name, description, openType, isReadable, isWritable, isIs, (Descriptor) null);
    }

    /**
     * <p>Constructs an {@code OpenMBeanAttributeInfoSupport} instance,
     * which describes the attribute of an open MBean with the
     * specified {@code name}, {@code openType}, {@code
     * description}, read/write access properties, and {@code Descriptor}.</p>
     *
     * <p>The {@code descriptor} can contain entries that will define
     * the values returned by certain methods of this class, as
     * explained in the {@link <a href="package-summary.html#constraints">
     * package description</a>}.
     *
     * @param name  cannot be a null or empty string.
     *
     * @param description  cannot be a null or empty string.
     *
     * @param openType  cannot be null.
     *
     * @param isReadable {@code true} if the attribute has a getter
     * exposed for management.
     *
     * @param isWritable {@code true} if the attribute has a setter
     * exposed for management.
     *
     * @param isIs {@code true} if the attribute's getter is of the
     * form <tt>is<i>XXX</i></tt>.
     *
     * @param descriptor The descriptor for the attribute.  This may be null
     * which is equivalent to an empty descriptor.
     *
     * @throws IllegalArgumentException if {@code name} or {@code
     * description} are null or empty string, or {@code openType} is
     * null, or the descriptor entries are invalid as described in the
     * {@link <a href="package-summary.html#constraints">package
     * description</a>}.
     *
     * @since 1.6
     */
    public OpenMBeanAttributeInfoSupport(String name, String description, OpenType<?> openType, boolean isReadable, boolean isWritable, boolean isIs, Descriptor descriptor) {
        super(name, (openType == null) ? null : openType.getClassName(), description, isReadable, isWritable, isIs, ImmutableDescriptor.union(descriptor, openType.getDescriptor()));
        this.openType = openType;
        descriptor = getDescriptor();
        this.defaultValue = valueFrom(descriptor, "defaultValue", openType);
        this.legalValues = valuesFrom(descriptor, "legalValues", openType);
        this.minValue = comparableValueFrom(descriptor, "minValue", openType);
        this.maxValue = comparableValueFrom(descriptor, "maxValue", openType);
        try {
            check(this);
        } catch (OpenDataException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Constructs an {@code OpenMBeanAttributeInfoSupport} instance,
     * which describes the attribute of an open MBean with the
     * specified {@code name}, {@code openType}, {@code description}
     * and {@code defaultValue}, and the specified read/write access
     * properties.
     *
     * @param name  cannot be a null or empty string.
     *
     * @param description  cannot be a null or empty string.
     *
     * @param openType  cannot be null.
     *
     * @param isReadable {@code true} if the attribute has a getter
     * exposed for management.
     *
     * @param isWritable {@code true} if the attribute has a setter
     * exposed for management.
     *
     * @param isIs {@code true} if the attribute's getter is of the
     * form <tt>is<i>XXX</i></tt>.
     *
     * @param defaultValue must be a valid value for the {@code
     * openType} specified for this attribute; default value not
     * supported for {@code ArrayType} and {@code TabularType}; can
     * be null, in which case it means that no default value is set.
     *
     * @param <T> allows the compiler to check that the {@code defaultValue},
     * if non-null, has the correct Java type for the given {@code openType}.
     *
     * @throws IllegalArgumentException if {@code name} or {@code
     * description} are null or empty string, or {@code openType} is
     * null.
     *
     * @throws OpenDataException if {@code defaultValue} is not a
     * valid value for the specified {@code openType}, or {@code
     * defaultValue} is non null and {@code openType} is an {@code
     * ArrayType} or a {@code TabularType}.
     */
    public <T> OpenMBeanAttributeInfoSupport(String name, String description, OpenType<T> openType, boolean isReadable, boolean isWritable, boolean isIs, T defaultValue) throws OpenDataException {
        this(name, description, openType, isReadable, isWritable, isIs, defaultValue, (T[]) null);
    }

    /**
     * <p>Constructs an {@code OpenMBeanAttributeInfoSupport} instance,
     * which describes the attribute of an open MBean with the
     * specified {@code name}, {@code openType}, {@code description},
     * {@code defaultValue} and {@code legalValues}, and the specified
     * read/write access properties.</p>
     *
     * <p>The contents of {@code legalValues} are copied, so subsequent
     * modifications of the array referenced by {@code legalValues}
     * have no impact on this {@code OpenMBeanAttributeInfoSupport}
     * instance.</p>
     *
     * @param name  cannot be a null or empty string.
     *
     * @param description  cannot be a null or empty string.
     *
     * @param openType  cannot be null.
     *
     * @param isReadable {@code true} if the attribute has a getter
     * exposed for management.
     *
     * @param isWritable {@code true} if the attribute has a setter
     * exposed for management.
     *
     * @param isIs {@code true} if the attribute's getter is of the
     * form <tt>is<i>XXX</i></tt>.
     *
     * @param defaultValue must be a valid value
     * for the {@code
     * openType} specified for this attribute; default value not
     * supported for {@code ArrayType} and {@code TabularType}; can
     * be null, in which case it means that no default value is set.
     *
     * @param legalValues each contained value must be valid for the
     * {@code openType} specified for this attribute; legal values
     * not supported for {@code ArrayType} and {@code TabularType};
     * can be null or empty.
     *
     * @param <T> allows the compiler to check that the {@code
     * defaultValue} and {@code legalValues}, if non-null, have the
     * correct Java type for the given {@code openType}.
     *
     * @throws IllegalArgumentException if {@code name} or {@code
     * description} are null or empty string, or {@code openType} is
     * null.
     *
     * @throws OpenDataException if {@code defaultValue} is not a
     * valid value for the specified {@code openType}, or one value in
     * {@code legalValues} is not valid for the specified {@code
     * openType}, or {@code defaultValue} is non null and {@code
     * openType} is an {@code ArrayType} or a {@code TabularType}, or
     * {@code legalValues} is non null and non empty and {@code
     * openType} is an {@code ArrayType} or a {@code TabularType}, or
     * {@code legalValues} is non null and non empty and {@code
     * defaultValue} is not contained in {@code legalValues}.
     */
    public <T> OpenMBeanAttributeInfoSupport(String name, String description, OpenType<T> openType, boolean isReadable, boolean isWritable, boolean isIs, T defaultValue, T[] legalValues) throws OpenDataException {
        this(name, description, openType, isReadable, isWritable, isIs, defaultValue, legalValues, null, null);
    }

    /**
     * Constructs an {@code OpenMBeanAttributeInfoSupport} instance,
     * which describes the attribute of an open MBean, with the
     * specified {@code name}, {@code openType}, {@code description},
     * {@code defaultValue}, {@code minValue} and {@code maxValue}.
     *
     * It is possible to specify minimal and maximal values only for
     * an open type whose values are {@code Comparable}.
     *
     * @param name  cannot be a null or empty string.
     *
     * @param description  cannot be a null or empty string.
     *
     * @param openType  cannot be null.
     *
     * @param isReadable {@code true} if the attribute has a getter
     * exposed for management.
     *
     * @param isWritable {@code true} if the attribute has a setter
     * exposed for management.
     *
     * @param isIs {@code true} if the attribute's getter is of the
     * form <tt>is<i>XXX</i></tt>.
     *
     * @param defaultValue must be a valid value for the {@code
     * openType} specified for this attribute; default value not
     * supported for {@code ArrayType} and {@code TabularType}; can be
     * null, in which case it means that no default value is set.
     *
     * @param minValue must be valid for the {@code openType}
     * specified for this attribute; can be null, in which case it
     * means that no minimal value is set.
     *
     * @param maxValue must be valid for the {@code openType}
     * specified for this attribute; can be null, in which case it
     * means that no maximal value is set.
     *
     * @param <T> allows the compiler to check that the {@code
     * defaultValue}, {@code minValue}, and {@code maxValue}, if
     * non-null, have the correct Java type for the given {@code
     * openType}.
     *
     * @throws IllegalArgumentException if {@code name} or {@code
     * description} are null or empty string, or {@code openType} is
     * null.
     *
     * @throws OpenDataException if {@code defaultValue}, {@code
     * minValue} or {@code maxValue} is not a valid value for the
     * specified {@code openType}, or {@code defaultValue} is non null
     * and {@code openType} is an {@code ArrayType} or a {@code
     * TabularType}, or both {@code minValue} and {@code maxValue} are
     * non-null and {@code minValue.compareTo(maxValue) > 0} is {@code
     * true}, or both {@code defaultValue} and {@code minValue} are
     * non-null and {@code minValue.compareTo(defaultValue) > 0} is
     * {@code true}, or both {@code defaultValue} and {@code maxValue}
     * are non-null and {@code defaultValue.compareTo(maxValue) > 0}
     * is {@code true}.
     */
    public <T> OpenMBeanAttributeInfoSupport(String name, String description, OpenType<T> openType, boolean isReadable, boolean isWritable, boolean isIs, T defaultValue, Comparable<T> minValue, Comparable<T> maxValue) throws OpenDataException {
        this(name, description, openType, isReadable, isWritable, isIs, defaultValue, null, minValue, maxValue);
    }

    private <T> OpenMBeanAttributeInfoSupport(String name, String description, OpenType<T> openType, boolean isReadable, boolean isWritable, boolean isIs, T defaultValue, T[] legalValues, Comparable<T> minValue, Comparable<T> maxValue) throws OpenDataException {
        super(name, (openType == null) ? null : openType.getClassName(), description, isReadable, isWritable, isIs, makeDescriptor(openType, defaultValue, legalValues, minValue, maxValue));
        this.openType = openType;
        Descriptor d = getDescriptor();
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.legalValues = (Set<?>) d.getFieldValue("legalValues");
        check(this);
    }

    /**
     * An object serialized in a version of the API before Descriptors were
     * added to this class will have an empty or null Descriptor.
     * For consistency with our
     * behavior in this version, we must replace the object with one
     * where the Descriptors reflect the same values of openType, defaultValue,
     * etc.
     **/
    private Object readResolve() {
        if (getDescriptor().getFieldNames().length == 0) {
            return new OpenMBeanAttributeInfoSupport(name, description, openType, isReadable(), isWritable(), isIs(), makeDescriptor(openType, defaultValue, legalValues, minValue, maxValue));
        } else return this;
    }

    static void check(OpenMBeanParameterInfo info) throws OpenDataException {
        OpenType openType = info.getOpenType();
        if (openType == null) throw new IllegalArgumentException("OpenType cannot be null");
        if (info.getName() == null || info.getName().trim().equals("")) throw new IllegalArgumentException("Name cannot be null or empty");
        if (info.getDescription() == null || info.getDescription().trim().equals("")) throw new IllegalArgumentException("Description cannot be null or empty");
        if (info.hasDefaultValue()) {
            if (openType.isArray() || (Object) openType instanceof TabularType) {
                throw new OpenDataException("Default value not supported " + "for ArrayType and TabularType");
            }
            if (!openType.isValue(info.getDefaultValue())) {
                final String msg = "Argument defaultValue's class [\"" + info.getDefaultValue().getClass().getName() + "\"] does not match the one defined in openType[\"" + openType.getClassName() + "\"]";
                throw new OpenDataException(msg);
            }
        }
        if (info.hasLegalValues() && (info.hasMinValue() || info.hasMaxValue())) {
            throw new OpenDataException("cannot have both legalValue and " + "minValue or maxValue");
        }
        if (info.hasMinValue() && !openType.isValue(info.getMinValue())) {
            final String msg = "Type of minValue [" + info.getMinValue().getClass().getName() + "] does not match OpenType [" + openType.getClassName() + "]";
            throw new OpenDataException(msg);
        }
        if (info.hasMaxValue() && !openType.isValue(info.getMaxValue())) {
            final String msg = "Type of maxValue [" + info.getMaxValue().getClass().getName() + "] does not match OpenType [" + openType.getClassName() + "]";
            throw new OpenDataException(msg);
        }
        if (info.hasDefaultValue()) {
            Object defaultValue = info.getDefaultValue();
            if (info.hasLegalValues() && !info.getLegalValues().contains(defaultValue)) {
                throw new OpenDataException("defaultValue is not contained " + "in legalValues");
            }
            if (info.hasMinValue()) {
                if (((Comparable) info.getMinValue()).compareTo(defaultValue) > 0) {
                    throw new OpenDataException("minValue cannot be greater " + "than defaultValue");
                }
            }
            if (info.hasMaxValue()) {
                if (((Comparable) info.getMaxValue()).compareTo(defaultValue) < 0) {
                    throw new OpenDataException("maxValue cannot be less " + "than defaultValue");
                }
            }
        }
        if (info.hasLegalValues()) {
            if ((Object) openType instanceof TabularType || openType.isArray()) {
                throw new OpenDataException("Legal values not supported " + "for TabularType and arrays");
            }
            for (Object v : info.getLegalValues()) {
                if (!openType.isValue(v)) {
                    final String msg = "Element of legalValues [" + v + "] is not a valid value for the specified openType [" + openType.toString() + "]";
                    throw new OpenDataException(msg);
                }
            }
        }
        if (info.hasMinValue() && info.hasMaxValue()) {
            if (((Comparable) info.getMinValue()).compareTo(info.getMaxValue()) > 0) {
                throw new OpenDataException("minValue cannot be greater " + "than maxValue");
            }
        }
    }

    static <T> Descriptor makeDescriptor(OpenType<T> openType, T defaultValue, T[] legalValues, Comparable<T> minValue, Comparable<T> maxValue) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (defaultValue != null) map.put("defaultValue", defaultValue);
        if (legalValues != null) {
            Set<T> set = new HashSet<T>();
            for (T v : legalValues) set.add(v);
            set = Collections.unmodifiableSet(set);
            map.put("legalValues", set);
        }
        if (minValue != null) map.put("minValue", minValue);
        if (maxValue != null) map.put("maxValue", maxValue);
        if (map.isEmpty()) {
            return openType.getDescriptor();
        } else {
            map.put("openType", openType);
            return new ImmutableDescriptor(map);
        }
    }

    static Descriptor makeDescriptor(OpenType openType, Object defaultValue, Set legalValues, Comparable minValue, Comparable maxValue) {
        Object[] legals;
        if (legalValues == null) legals = null; else {
            legals = new Object[legalValues.size()];
            legalValues.toArray(legals);
        }
        return makeDescriptor(openType, defaultValue, legals, minValue, maxValue);
    }

    static <T> T valueFrom(Descriptor d, String name, OpenType<T> openType) {
        Object x = d.getFieldValue(name);
        if (x == null) return null;
        try {
            return convertFrom(x, openType);
        } catch (Exception e) {
            final String msg = "Cannot convert descriptor field " + name + "  to " + openType.getTypeName();
            throw EnvHelp.initCause(new IllegalArgumentException(msg), e);
        }
    }

    static <T> Set<T> valuesFrom(Descriptor d, String name, OpenType<T> openType) {
        Object x = d.getFieldValue(name);
        if (x == null) return null;
        Collection<?> coll;
        if (x instanceof Set<?>) {
            Set<?> set = (Set<?>) x;
            boolean asis = true;
            for (Object element : set) {
                if (!openType.isValue(element)) {
                    asis = false;
                    break;
                }
            }
            if (asis) return (Set<T>) set;
            coll = set;
        } else if (x instanceof Object[]) {
            coll = Arrays.asList((Object[]) x);
        } else {
            final String msg = "Descriptor value for " + name + " must be a Set or " + "an array: " + x.getClass().getName();
            throw new IllegalArgumentException(msg);
        }
        Set<T> result = new HashSet<T>();
        for (Object element : coll) result.add(convertFrom(element, openType));
        return result;
    }

    static <T> Comparable comparableValueFrom(Descriptor d, String name, OpenType<T> openType) {
        T t = valueFrom(d, name, openType);
        if (t == null || t instanceof Comparable<?>) return (Comparable) t;
        final String msg = "Descriptor field " + name + " with value " + t + " is not Comparable";
        throw new IllegalArgumentException(msg);
    }

    private static <T> T convertFrom(Object x, OpenType<T> openType) {
        if (openType.isValue(x)) return (T) x;
        return convertFromStrings(x, openType);
    }

    private static <T> T convertFromStrings(Object x, OpenType<T> openType) {
        if (openType instanceof ArrayType<?>) return convertFromStringArray(x, openType); else if (x instanceof String) return convertFromString((String) x, openType);
        final String msg = "Cannot convert value " + x + " of type " + x.getClass().getName() + " to type " + openType.getTypeName();
        throw new IllegalArgumentException(msg);
    }

    private static <T> T convertFromString(String s, OpenType<T> openType) {
        Class<T> c;
        try {
            c = (Class<T>) Class.forName(openType.getClassName());
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.toString());
        }
        Method valueOf;
        try {
            valueOf = c.getMethod("valueOf", String.class);
            if (!Modifier.isStatic(valueOf.getModifiers()) || valueOf.getReturnType() != c) valueOf = null;
        } catch (NoSuchMethodException e) {
            valueOf = null;
        }
        if (valueOf != null) {
            try {
                return c.cast(valueOf.invoke(null, s));
            } catch (Exception e) {
                final String msg = "Could not convert \"" + s + "\" using method: " + valueOf;
                throw new IllegalArgumentException(msg);
            }
        }
        Constructor<T> con;
        try {
            con = c.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            con = null;
        }
        if (con != null) {
            try {
                return con.newInstance(s);
            } catch (Exception e) {
                final String msg = "Could not convert \"" + s + "\" using constructor: " + con;
                throw new IllegalArgumentException(msg);
            }
        }
        throw new IllegalArgumentException("Don't know how to convert " + "string to " + openType.getTypeName());
    }

    private static <T> T convertFromStringArray(Object x, OpenType<T> openType) {
        ArrayType<?> arrayType = (ArrayType<?>) openType;
        OpenType<?> baseType = arrayType.getElementOpenType();
        int dim = arrayType.getDimension();
        String squareBrackets = "[";
        for (int i = 1; i < dim; i++) squareBrackets += "[";
        Class<?> stringArrayClass;
        Class<?> targetArrayClass;
        try {
            stringArrayClass = Class.forName(squareBrackets + "Ljava.lang.String;");
            targetArrayClass = Class.forName(squareBrackets + "L" + baseType.getClassName() + ";");
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.toString());
        }
        if (!stringArrayClass.isInstance(x)) {
            final String msg = "Value for " + dim + "-dimensional array of " + baseType.getTypeName() + " must be same type or a String " + "array with same dimensions";
            throw new IllegalArgumentException(msg);
        }
        Class<?> targetComponentClass = targetArrayClass.getComponentType();
        OpenType<?> componentOpenType;
        if (dim == 1) componentOpenType = baseType; else {
            try {
                componentOpenType = new ArrayType(dim - 1, baseType);
            } catch (OpenDataException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }
        int n = Array.getLength(x);
        Object targetArray = Array.newInstance(targetArrayClass.getComponentType(), n);
        for (int i = 0; i < n; i++) {
            Object stringish = Array.get(x, i);
            Object converted = convertFromStrings(stringish, componentOpenType);
            Array.set(targetArray, i, converted);
        }
        return (T) targetArray;
    }

    /**
     * Returns the open type for the values of the attribute described
     * by this {@code OpenMBeanAttributeInfoSupport} instance.
     */
    public OpenType<?> getOpenType() {
        return openType;
    }

    /**
     * Returns the default value for the attribute described by this
     * {@code OpenMBeanAttributeInfoSupport} instance, if specified,
     * or {@code null} otherwise.
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns an unmodifiable Set of legal values for the attribute
     * described by this {@code OpenMBeanAttributeInfoSupport}
     * instance, if specified, or {@code null} otherwise.
     */
    public Set<?> getLegalValues() {
        return (legalValues);
    }

    /**
     * Returns the minimal value for the attribute described by this
     * {@code OpenMBeanAttributeInfoSupport} instance, if specified,
     * or {@code null} otherwise.
     */
    public Comparable<?> getMinValue() {
        return minValue;
    }

    /**
     * Returns the maximal value for the attribute described by this
     * {@code OpenMBeanAttributeInfoSupport} instance, if specified,
     * or {@code null} otherwise.
     */
    public Comparable<?> getMaxValue() {
        return maxValue;
    }

    /**
     * Returns {@code true} if this {@code
     * OpenMBeanAttributeInfoSupport} instance specifies a non-null
     * default value for the described attribute, {@code false}
     * otherwise.
     */
    public boolean hasDefaultValue() {
        return (defaultValue != null);
    }

    /**
     * Returns {@code true} if this {@code
     * OpenMBeanAttributeInfoSupport} instance specifies a non-null
     * set of legal values for the described attribute, {@code false}
     * otherwise.
     */
    public boolean hasLegalValues() {
        return (legalValues != null);
    }

    /**
     * Returns {@code true} if this {@code
     * OpenMBeanAttributeInfoSupport} instance specifies a non-null
     * minimal value for the described attribute, {@code false}
     * otherwise.
     */
    public boolean hasMinValue() {
        return (minValue != null);
    }

    /**
     * Returns {@code true} if this {@code
     * OpenMBeanAttributeInfoSupport} instance specifies a non-null
     * maximal value for the described attribute, {@code false}
     * otherwise.
     */
    public boolean hasMaxValue() {
        return (maxValue != null);
    }

    /**
     * Tests whether {@code obj} is a valid value for the attribute
     * described by this {@code OpenMBeanAttributeInfoSupport}
     * instance.
     *
     * @param obj the object to be tested.
     *
     * @return {@code true} if {@code obj} is a valid value for
     * the parameter described by this {@code
     * OpenMBeanAttributeInfoSupport} instance, {@code false}
     * otherwise.
     */
    public boolean isValue(Object obj) {
        return isValue(this, obj);
    }

    static boolean isValue(OpenMBeanParameterInfo info, Object obj) {
        if (info.hasDefaultValue() && obj == null) return true;
        return info.getOpenType().isValue(obj) && (!info.hasLegalValues() || info.getLegalValues().contains(obj)) && (!info.hasMinValue() || ((Comparable) info.getMinValue()).compareTo(obj) <= 0) && (!info.hasMaxValue() || ((Comparable) info.getMaxValue()).compareTo(obj) >= 0);
    }

    /**
     * Compares the specified {@code obj} parameter with this {@code
     * OpenMBeanAttributeInfoSupport} instance for equality.
     * <p>
     * Returns {@code true} if and only if all of the following statements are true:
     * <ul>
     * <li>{@code obj} is non null,</li>
     * <li>{@code obj} also implements the {@code OpenMBeanAttributeInfo} interface,</li>
     * <li>their names are equal</li>
     * <li>their open types are equal</li>
     * <li>their access properties (isReadable, isWritable and isIs) are equal</li>
     * <li>their default, min, max and legal values are equal.</li>
     * </ul>
     * This ensures that this {@code equals} method works properly for
     * {@code obj} parameters which are different implementations of
     * the {@code OpenMBeanAttributeInfo} interface.
     *
     * <p>If {@code obj} also implements {@link DescriptorRead}, then its
     * {@link DescriptorRead#getDescriptor() getDescriptor()} method must
     * also return the same value as for this object.</p>
     *
     * @param obj the object to be compared for equality with this
     * {@code OpenMBeanAttributeInfoSupport} instance.
     *
     * @return {@code true} if the specified object is equal to this
     * {@code OpenMBeanAttributeInfoSupport} instance.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof OpenMBeanAttributeInfo)) return false;
        OpenMBeanAttributeInfo other = (OpenMBeanAttributeInfo) obj;
        return this.isReadable() == other.isReadable() && this.isWritable() == other.isWritable() && this.isIs() == other.isIs() && equal(this, other);
    }

    static boolean equal(OpenMBeanParameterInfo x1, OpenMBeanParameterInfo x2) {
        if (x1 instanceof DescriptorRead) {
            if (!(x2 instanceof DescriptorRead)) return false;
            Descriptor d1 = ((DescriptorRead) x1).getDescriptor();
            Descriptor d2 = ((DescriptorRead) x2).getDescriptor();
            if (!d1.equals(d2)) return false;
        } else if (x2 instanceof DescriptorRead) return false;
        return x1.getName().equals(x2.getName()) && x1.getOpenType().equals(x2.getOpenType()) && (x1.hasDefaultValue() ? x1.getDefaultValue().equals(x2.getDefaultValue()) : !x2.hasDefaultValue()) && (x1.hasMinValue() ? x1.getMinValue().equals(x2.getMinValue()) : !x2.hasMinValue()) && (x1.hasMaxValue() ? x1.getMaxValue().equals(x2.getMaxValue()) : !x2.hasMaxValue()) && (x1.hasLegalValues() ? x1.getLegalValues().equals(x2.getLegalValues()) : !x2.hasLegalValues());
    }

    /**
     * <p>Returns the hash code value for this {@code
     * OpenMBeanAttributeInfoSupport} instance.</p>
     *
     * <p>The hash code of an {@code OpenMBeanAttributeInfoSupport}
     * instance is the sum of the hash codes of all elements of
     * information used in {@code equals} comparisons (ie: its name,
     * its <i>open type</i>, its default, min, max and legal
     * values, and its Descriptor).
     *
     * <p>This ensures that {@code t1.equals(t2)} implies that {@code
     * t1.hashCode()==t2.hashCode()} for any two {@code
     * OpenMBeanAttributeInfoSupport} instances {@code t1} and {@code
     * t2}, as required by the general contract of the method {@link
     * Object#hashCode() Object.hashCode()}.
     *
     * <p>However, note that another instance of a class implementing
     * the {@code OpenMBeanAttributeInfo} interface may be equal to
     * this {@code OpenMBeanAttributeInfoSupport} instance as defined
     * by {@link #equals(java.lang.Object)}, but may have a different
     * hash code if it is calculated differently.
     *
     * <p>As {@code OpenMBeanAttributeInfoSupport} instances are
     * immutable, the hash code for this instance is calculated once,
     * on the first call to {@code hashCode}, and then the same value
     * is returned for subsequent calls.
     *
     * @return the hash code value for this {@code
     * OpenMBeanAttributeInfoSupport} instance
     */
    public int hashCode() {
        if (myHashCode == null) myHashCode = hashCode(this);
        return myHashCode.intValue();
    }

    static int hashCode(OpenMBeanParameterInfo info) {
        int value = 0;
        value += info.getName().hashCode();
        value += info.getOpenType().hashCode();
        if (info.hasDefaultValue()) value += info.getDefaultValue().hashCode();
        if (info.hasMinValue()) value += info.getMinValue().hashCode();
        if (info.hasMaxValue()) value += info.getMaxValue().hashCode();
        if (info.hasLegalValues()) value += info.getLegalValues().hashCode();
        if (info instanceof DescriptorRead) value += ((DescriptorRead) info).getDescriptor().hashCode();
        return value;
    }

    /**
     * Returns a string representation of this
     * {@code OpenMBeanAttributeInfoSupport} instance.
     * <p>
     * The string representation consists of the name of this class (i.e.
     * {@code javax.management.openmbean.OpenMBeanAttributeInfoSupport}),
     * the string representation of the name and open type of the
     * described parameter, the string representation of its
     * default, min, max and legal values and the string
     * representation of its descriptor.
     *
     * <p>As {@code OpenMBeanAttributeInfoSupport} instances are
     * immutable, the string representation for this instance is
     * calculated once, on the first call to {@code toString}, and
     * then the same value is returned for subsequent calls.
     *
     * @return a string representation of this
     * {@code OpenMBeanAttributeInfoSupport} instance.
     */
    public String toString() {
        if (myToString == null) myToString = toString(this);
        return myToString;
    }

    static String toString(OpenMBeanParameterInfo info) {
        Descriptor d = (info instanceof DescriptorRead) ? ((DescriptorRead) info).getDescriptor() : null;
        return info.getClass().getName() + "(name=" + info.getName() + ",openType=" + info.getOpenType() + ",default=" + info.getDefaultValue() + ",minValue=" + info.getMinValue() + ",maxValue=" + info.getMaxValue() + ",legalValues=" + info.getLegalValues() + ((d == null) ? "" : ",descriptor=" + d) + ")";
    }
}
