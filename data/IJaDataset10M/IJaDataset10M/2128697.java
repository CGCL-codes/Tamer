package org.charvolant.properties;

import java.io.File;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.text.Collator;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import org.charvolant.properties.annotations.DisplayHint;
import org.charvolant.properties.annotations.Property;
import org.charvolant.tmsnet.i18n.Localisable;
import org.charvolant.tmsnet.i18n.TMSNetBundle;
import org.charvolant.tmsnet.i18n.TitleMaker;

/**
 * 
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
public class PropertyDescription {

    /** The classes that this property can handle */
    public Class<?>[] VALID_CLASSES = { String.class, int.class, long.class, double.class, boolean.class, Enum.class, Date.class, File.class, URI.class, URL.class };

    /** The bean class */
    private Class<?> beanClass;

    /** The property name */
    private String property;

    /** The property annotation */
    private Property annotation;

    /** The field for access */
    private Field field;

    /** The getter method */
    private Method getter;

    /** The text method */
    private Method text;

    /** The setter method */
    private Method setter;

    /** The validator method */
    private Method validator;

    /** The default value accessor, if there is one */
    private AccessibleObject defaulter;

    /** The values list accessor, if there is one */
    private AccessibleObject values;

    /**
	 * Construct property information from a field.
	 * <p>
	 * The accessor methods are derived from the field name.
	 *
	 * @param field The field
	 * 
	 * @throws NoSuchMethodException if unable to find a matching method for one of the annotations
	 * @throws IllegalArgumentException if the property is otherwise invalid
	 */
    protected PropertyDescription(Field field) throws NoSuchMethodException, IllegalArgumentException {
        super();
        this.beanClass = field.getDeclaringClass();
        this.field = field;
        this.annotation = field.getAnnotation(Property.class);
        if (this.annotation == null) throw new IllegalArgumentException(this.field.getName() + " is not annotated");
        this.property = this.buildPropertyName();
        this.validateType();
        this.getter = this.buildGetter();
        this.text = this.buildText();
        this.setter = this.buildSetter();
        this.validator = this.buildValidator();
        this.defaulter = this.buildDefaulter();
        this.values = this.buildValues();
    }

    /**
	 * Construct property information from a getter method.
	 * <p>
	 * The accessor methods are derived from the method name.
	 *
	 * @param field The field
	 * 
	 * @throws NoSuchMethodException if unable to find a matching method for one of the annotations
	 * @throws IllegalArgumentException if the property is otherwise invalid
	 */
    public PropertyDescription(Method method) throws NoSuchMethodException, IllegalArgumentException {
        super();
        this.beanClass = method.getDeclaringClass();
        this.field = null;
        this.annotation = method.getAnnotation(Property.class);
        if (this.annotation == null) throw new IllegalArgumentException(this.field.getName() + " is not annotated");
        this.getter = method;
        this.validateType();
        this.text = this.buildText();
        this.property = this.buildPropertyName();
        this.setter = this.buildSetter();
        this.validator = this.buildValidator();
        this.defaulter = this.buildDefaulter();
        this.values = this.buildValues();
    }

    /**
	 * Get the property name.
	 *
	 * @return the property name
	 */
    public String getProperty() {
        return this.property;
    }

    /**
	 * Get the type of this property.
	 * <p>
	 * Derived either from the field or the getter.
	 * 
	 * @return The type
	 */
    public Class<?> getType() {
        if (this.field != null) return this.field.getType();
        if (this.getter != null) return this.getter.getReturnType();
        throw new IllegalStateException("No type source");
    }

    /**
	 * Get the i18n label key.
	 * 
	 * @return The label key
	 * 
	 * @see org.charvolant.properties.annotations.Property#label()
	 */
    public String getLabelKey() {
        return this.annotation.label().equals(Property.NONE) ? null : this.annotation.label();
    }

    /**
	 * Get the display hint.
	 * 
	 * @return The display hint
	 * 
	 * @see org.charvolant.properties.annotations.Property#display()
	 */
    public DisplayHint getDisplay() {
        return this.annotation.display();
    }

    /**
	 * Get the i18n detail key.
	 * 
	 * @return The detail key
	 * 
	 * @see org.charvolant.properties.annotations.Property#detail()
	 */
    public String getDetailKey() {
        return this.annotation.detail().equals(Property.NONE) ? null : this.annotation.detail();
    }

    /**
	 * Get the group key (also used for i18n labels)
	 * 
	 * @return The group key
	 * 
	 * @see org.charvolant.properties.annotations.Property#group()
	 */
    public String getGroupKey() {
        return this.annotation.group().equals(Property.NONE) ? null : this.annotation.group();
    }

    /**
	 * Get a label for this property.
	 * 
	 * @param resources The resource bundle to use
	 * 
	 * @return The label or null for not available
	 */
    public String getLabel(ResourceBundle resources) {
        if (this.getLabelKey() == null) return null;
        return resources.getString(this.getLabelKey());
    }

    /**
	 * Get a tooltip for this property.
	 * 
	 * @param resources The resource bundle to use
	 * 
	 * @return The tooltip or null for not available
	 */
    public String getDetail(ResourceBundle resources) {
        if (this.getDetailKey() == null) return null;
        return MessageFormat.format(resources.getString(this.getDetailKey()), this.getDefault());
    }

    /**
	 * Get a group name for this property.
	 * 
	 * @param resources The resource bundle to use
	 * 
	 * @return The group name or null for not available
	 */
    public String getGroup(ResourceBundle resources) {
        if (this.getGroupKey() == null) return null;
        return resources.getString(this.getGroupKey());
    }

    /**
	 * Get the default value of the property
	 * 
	 * @return The default value
	 */
    public Object getDefault() {
        if (this.defaulter == null) return null;
        try {
            if (this.defaulter instanceof Field) return ((Field) this.defaulter).get(null);
            if (this.defaulter instanceof Method) return ((Method) this.defaulter).invoke(null);
        } catch (Exception ex) {
        }
        return null;
    }

    /**
	 * Get the default value of the property as a string
	 * 
	 * @return The default value as a string
	 */
    public String getDefaultAsString() {
        Object value = this.getDefault();
        if (value == null) return null;
        if (value instanceof Localisable) return ((Localisable) value).getLocalisedName();
        return value.toString();
    }

    /**
	 * Get the possible values that this object can take.
	 * 
	 * @return The list of values
	 */
    public Object[] getValues() {
        if (this.values == null) return null;
        try {
            if (this.values instanceof Field) return (Object[]) ((Field) this.values).get(null);
            if (this.values instanceof Method) return (Object[]) ((Method) this.values).invoke(null);
        } catch (Exception ex) {
        }
        return null;
    }

    /**
	 * Get the value of a property from the bean.
	 * <p>
	 * If there is no matching getter or field for the bean then
	 * null is returned.
	 * 
	 * @param <Type> The property type
	 * @param bean The source bean
	 * 
	 * @return The value of the property
	 * 
	 * @throws Exception if unable to access the bean/property
	 */
    @SuppressWarnings("unchecked")
    public <Type> Type get(Object bean) throws Exception {
        if (bean == null) return null;
        if (!this.beanClass.isAssignableFrom(bean.getClass())) throw new IllegalArgumentException(bean.toString() + " must be of class " + this.beanClass);
        if (this.getter != null) return (Type) this.getter.invoke(bean);
        if (this.field != null) return (Type) this.field.get(bean);
        return null;
    }

    /**
	 * Get the value of a property from the bean.
	 * <p>
	 * If the bean does not have value for this property and
	 * we do have a default then the defualt is returned.
	 * 
	 * @param <Type> The property type
	 * @param bean The source bean
	 * 
	 * @return The value of the property or the default if the value is null
	 * 
	 * @throws Exception if unable to access the bean/property
	 */
    @SuppressWarnings("unchecked")
    public <Type> Type getWithDefault(Object bean) throws Exception {
        Type value = (Type) this.get(bean);
        if (value != null) return value;
        return (Type) this.getDefault();
    }

    /**
   * Get the value of a property from the bean as a string.
   * <p>
   * If the bean does not have value for this property and
   * we do have a default then the defualt is returned.
   * 
   * @param bean The source bean
   * 
   * @return The value of the property or the default if the value is null
   * 
   * @throws Exception if unable to access the bean/property
   */
    public String getAsString(Object bean) throws Exception {
        Object value;
        if (bean != null && this.text != null) value = this.text.invoke(bean); else value = this.get(bean);
        if (value == null) return "";
        if (value instanceof Date) return DateFormat.getDateTimeInstance().format((Date) value);
        if (value instanceof Localisable) return ((Localisable) value).getLocalisedName();
        return value.toString();
    }

    /**
   * Get the wrapper class for primitive type
   * 
   * @param clazz The primitive type
   * 
   * @return The wrapper class
   */
    private Class<?> getWrapper(Class<?> clazz) {
        if (!clazz.isPrimitive()) return clazz;
        if (clazz == int.class) return Integer.class;
        if (clazz == long.class) return Long.class;
        if (clazz == double.class) return Double.class;
        if (clazz == boolean.class) return Boolean.class;
        throw new IllegalStateException("Un-catered for primitive type " + clazz);
    }

    /**
	 * Validate a value.
	 * 
	 * @param <Type> The type of the value
	 * 
	 * @param bean The bean to validate against
	 * @param value The value to validate
	 * 
	 * @return True if the value is valid, false otherwise
	 */
    public <Type> boolean validate(Object bean, Type value) {
        Class<?> type = this.getType();
        if (type.isPrimitive()) type = this.getWrapper(type);
        if (value != null && !type.isAssignableFrom(value.getClass())) return false;
        if (!this.checkAgainstValues(value)) return false;
        if (this.validator == null) return true;
        try {
            return ((Boolean) this.validator.invoke(bean, value)).booleanValue();
        } catch (Exception ex) {
            return false;
        }
    }

    /**
	 * Check to see if we have a legitimate value
	 * 
	 * @param value The value to test
	 * 
	 * @return True if there are no legitimate values or there is a match
	 */
    private <Type> boolean checkAgainstValues(Type value) {
        Object[] validValues = this.getValues();
        if (validValues == null || value == null) return true;
        for (Object candidate : validValues) if (candidate.equals(value)) return true;
        return false;
    }

    /**
	 * Try to validate a string value.
	 * 
	 * @param bean The bean to validate against
	 * @param value The string representation of the value
	 * 
	 * @return If the value is parsable and validatable
	 */
    public boolean validateString(Object bean, String value) {
        try {
            return this.validate(bean, this.parseString(value));
        } catch (Exception ex) {
            return false;
        }
    }

    /**
	 * Set the value of the property.
	 * <p>
	 * If there is no matching setter or field then the value is ignored.
	 * 
	 * @param <Type> The type of value
	 * 
	 * @param bean The bean to set the property on
	 * @param value The value to set
	 * 
	 * @throws Exception if unable to set the property
	 */
    public void set(Object bean, Object value) throws Exception {
        Class<?> type = this.getType();
        if (type.isPrimitive() && value == null) {
            if (type == boolean.class) value = false; else if (type == int.class) value = 0; else if (type == long.class) value = 0L; else if (type == double.class) value = 0.0;
        }
        if (!this.checkAgainstValues(value)) throw new IllegalArgumentException("Value " + value + " is not allowed");
        if (this.setter != null) this.setter.invoke(bean, value); else if (this.field != null) this.field.set(bean, value);
    }

    /**
	 * Try set a string value.
	 * 
	 * @param bean The bean to set the property on
	 * @param value The string representation of the value
	 * 
	 * @return If the value is parsable and settable
	 */
    public void setString(Object bean, String value) throws Exception {
        this.set(bean, this.parseString(value));
    }

    /**
	 * Convert a string into an assignable object.
	 * <p>
	 * Null or empty strings are interpreted as null.
	 * 
	 * @param value The string value
	 * 
	 * @return The parsed value
	 * 
	 * @throws Exception for almost anything untoward
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object parseString(String value) throws Exception {
        Class<?> type = this.getType();
        if (value == null || value.isEmpty()) return null;
        if (this.values != null) {
            Object[] validValues = this.getValues();
            for (Object candidate : validValues) {
                String match = (candidate instanceof Localisable) ? ((Localisable) candidate).getLocalisedName() : candidate.toString();
                if (match.equals(value)) return candidate;
            }
            throw new IllegalArgumentException("Value " + value + " has no match");
        }
        if (String.class.isAssignableFrom(type)) return value;
        if (long.class.isAssignableFrom(type)) return Long.parseLong(value);
        if (int.class.isAssignableFrom(type)) return Integer.parseInt(value);
        if (double.class.isAssignableFrom(type)) return Double.parseDouble(value);
        if (boolean.class.isAssignableFrom(type)) return Boolean.parseBoolean(value);
        if (File.class.isAssignableFrom(type)) return new File(value);
        if (URL.class.isAssignableFrom(type)) return new URL(value);
        if (Enum.class.isAssignableFrom(type)) {
            if (Localisable.class.isAssignableFrom(type)) {
                Enum<?>[] values = (Enum<?>[]) type.getMethod("values").invoke(null);
                for (Enum<?> val : values) {
                    if (((Localisable) val).getLocalisedName().equals(value)) return val;
                }
            }
            return Enum.valueOf((Class<Enum>) type, value);
        }
        if (Date.class.isAssignableFrom(type)) return DateFormat.getDateTimeInstance().parse(value);
        throw new IllegalStateException("Can't parse " + value + " to " + type);
    }

    /**
   * Build the property name.
   * 
   * @param field The field name
   * @param annotation The field annotation
   * 
   * @return The property name
   */
    private String buildPropertyName() {
        if (!this.annotation.property().equals(Property.NONE)) return this.annotation.property();
        if (this.field != null) return this.field.getName();
        if (this.getter != null) {
            String name = this.getter.getName();
            if (name.startsWith("is")) return name.substring(2, 3).toLowerCase() + name.substring(3);
            if (name.startsWith("get")) return name.substring(3, 4).toLowerCase() + name.substring(4);
            return name;
        }
        throw new IllegalArgumentException("Unable to build property name");
    }

    /**
	 * Build a potential getter
	 * 
	 * @param prefix The getter prefix
	 * 
	 * @return The getter method or null for not present
	 */
    private Method buildGetter(String prefix, String propName) {
        String getter = prefix + Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
        try {
            return this.beanClass.getMethod(getter);
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    /**
	 * Build the getter method.
	 * <p>
	 * Look for a method with the property name or field name.
	 * If it is a boolean method try a method name with "is" rather than "get" first.
	 * 
	 * @return The getter method or null for none
	 */
    private Method buildGetter() {
        Method getter;
        if (this.field.getType() == boolean.class) {
            if ((getter = this.buildGetter("is", this.property)) != null) return getter;
            if (this.field != null && (getter = this.buildGetter("is", this.field.getName())) != null) return getter;
        }
        if ((getter = this.buildGetter("get", this.property)) != null) return getter;
        if (this.field != null && (getter = this.buildGetter("get", this.field.getName())) != null) return getter;
        return null;
    }

    /**
   * Build the text method.
   * <p>
   * This is derived from the {@link Property#text()} attribute, if one is set
   * 
   * @return The getter method or null for none
   * 
   * @throws NoSuchMethodException if unable to find the method
   * @throws SecurityException if unable to access the method
   */
    private Method buildText() throws SecurityException, NoSuchMethodException {
        Method text;
        if (this.annotation.text().equals(Property.NONE)) return null;
        text = this.beanClass.getMethod(this.annotation.text());
        if (!text.getReturnType().equals(String.class)) throw new NoSuchMethodException("Text method " + text + " must return a string");
        return text;
    }

    /**
   * Build a potential setter
   * 
   * @param prefix The getter prefix
   * 
   * @return The getter method or null for not present
   */
    private Method buildSetter(String prefix, String propName, Class<?> type) {
        String setter = prefix + Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
        try {
            return this.beanClass.getMethod(setter, type);
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    /**
	 * Build the setter method.
	 * <p>
	 * Look for a method with the property name or field name.
	 * 
	 * @return The setter method or null for none
	 */
    private Method buildSetter() {
        Method setter;
        Class<?> type = this.getType();
        if (this.getter != null) {
            if ((setter = this.buildSetter("set", this.getter.getName().replaceFirst("get", ""), type)) != null) return setter;
        }
        if ((setter = this.buildSetter("set", this.property, type)) != null) return setter;
        if (this.field != null && (setter = this.buildSetter("set", this.field.getName(), type)) != null) return setter;
        return null;
    }

    /**
	 * Build the validator method.
	 * <p>
	 * Look for a method with the supplied validator name.
	 * 
	 * @return The setter method or null for none
	 * 
	 * @throws NoSuchMethodException if there is no validator of that name
	 */
    private Method buildValidator() throws NoSuchMethodException {
        Method validator;
        Class<?> type = this.getType();
        if (this.annotation.validator().equals(Property.NONE)) return null;
        validator = this.beanClass.getMethod(this.annotation.validator(), type);
        if (validator.getReturnType() != boolean.class) throw new NoSuchMethodException("Validator " + validator + " must return a boolean");
        return validator;
    }

    /**
	 * Build the defaulter method or field.
	 * <p>
	 * Look for a field with the name or a no-argument method that
	 * returns an object of the correct type.
	 * 
	 * @return The setter method or null for none
	 * 
	 * @throws NoSuchMethodException if there is no validator of that name
	 */
    private AccessibleObject buildDefaulter() throws NoSuchMethodException {
        Field field;
        Method method;
        Class<?> type = this.getType();
        if (this.annotation.defaulter().equals(Property.NONE)) return null;
        try {
            field = this.beanClass.getField(this.annotation.defaulter());
            if (type.isAssignableFrom(field.getType()) && (field.getModifiers() & Modifier.STATIC) != 0) return field;
        } catch (NoSuchFieldException ex) {
        }
        method = this.beanClass.getMethod(this.annotation.defaulter());
        if (!type.isAssignableFrom(method.getReturnType()) && (method.getModifiers() & Modifier.STATIC) != 0) throw new NoSuchMethodException("Incorect type for " + method + " expecting " + type);
        return method;
    }

    /**
	 * Build the values method or field.
	 * <p>
	 * Look for a field with the name or a no-argument method that
	 * returns an array or list objects of the correct type.
	 * 
	 * @return The setter method or null for none
	 * 
	 * @throws NoSuchMethodException if there is no validator of that name
	 */
    private AccessibleObject buildValues() throws NoSuchMethodException {
        Field field;
        Method method;
        Class<?> type = this.getType();
        Class<?> sourceType;
        if (this.annotation.values().equals(Property.NONE)) return null;
        try {
            field = this.beanClass.getField(this.annotation.values());
            sourceType = field.getType();
            if (sourceType.isArray() && type.isAssignableFrom(sourceType.getComponentType()) && (field.getModifiers() & Modifier.STATIC) != 0) return field;
        } catch (NoSuchFieldException ex) {
        }
        method = this.beanClass.getMethod(this.annotation.values());
        sourceType = method.getReturnType();
        if (!sourceType.isArray() || !type.isAssignableFrom(sourceType.getComponentType())) throw new NoSuchMethodException("Incorrect type for " + method + " expecting array or list of " + type);
        if ((method.getModifiers() & Modifier.STATIC) == 0) throw new NoSuchMethodException("Method " + method + " must be static");
        return method;
    }

    /**
	 * Check that the type is a valid type.
	 * <p>
	 * There is a list of acceptable types
	 * 
	 * @throws IllegalArgumentException if we can't convert to and from this type.
	 */
    private void validateType() throws IllegalArgumentException {
        Class<?> type = this.getType();
        if (type == null) throw new IllegalArgumentException("Unable to determine type for " + this.beanClass + "/" + this.property);
        if (!this.annotation.values().equals(Property.NONE)) return;
        for (Class<?> validClass : this.VALID_CLASSES) if (validClass.isAssignableFrom(type)) return;
        throw new IllegalArgumentException("Property class for " + this.beanClass + "/" + this.property + " invalid, must have a values method or be one of " + this.VALID_CLASSES);
    }

    /**
	 * Load the value of this property from a set of preferences.
	 * <p>
	 * If the preference is absent, then the default is loaded
	 * 
	 * @param bean The target bean
	 * @param preferences The preference set
	 * 
	 * @throws Exception if unable to save the result
	 */
    public void loadPreference(Object bean, Preferences preferences) throws Exception {
        String value;
        if (this.annotation.preference().equals(Property.NONE)) return;
        value = preferences.get(this.annotation.preference(), this.getDefaultAsString());
        try {
            this.setString(bean, value);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to load preference " + this.property, ex);
        }
    }

    /**
	 * Save the value of this property to a set of preferences.
	 * 
	 * @param bean The target bean
	 * @param preferences The preference set
	 * 
	 * @throws Exception if unable to save the result
	 */
    public void savePreference(Object bean, Preferences preferences) throws Exception {
        String value;
        if (this.annotation.preference().equals(Property.NONE)) return;
        value = this.getAsString(bean);
        if (value == null || value.isEmpty() || value.equals(this.getDefaultAsString())) preferences.remove(this.annotation.preference()); else preferences.put(this.annotation.preference(), value);
    }

    /**
   * Create a comparator for this property.
   * 
   * @param locale The locale under which the comparator works
   * 
   * @return The comparator
   */
    public <S> Comparator<S> createComparator(Locale locale) {
        return new Sorter<S>(locale);
    }

    /**
   * A comparator for this property.
   * <p>
   * Number elements are sorted as numbers.
   * Otherwise, strings are used, ignoring case distinctions.
   * <p>
   * If the {@link DisplayHint#TITLE} hint is set, strings are sorted as titles,
   * using a {@link TitleMaker}.
   * 
   * @author Doug Palmer &lt;doug@charvolant.org&gt;
   *
   */
    public class Sorter<S> implements Comparator<S> {

        private Collator collator;

        private TitleMaker titles;

        public Sorter(Locale locale) {
            super();
            this.collator = Collator.getInstance(locale);
            if (PropertyDescription.this.getDisplay() == DisplayHint.TITLE) this.titles = new TitleMaker(TMSNetBundle.getBundle(locale)); else this.titles = null;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public int compare(S o1, S o2) {
            Class<?> type = PropertyDescription.this.getType();
            Object v1, v2;
            try {
                v1 = PropertyDescription.this.get(o1);
                v2 = PropertyDescription.this.get(o2);
                if (v1 instanceof String) {
                    String s1 = (String) v1;
                    String s2 = (String) v2;
                    if (this.titles != null) {
                        s1 = this.titles.getSortableTitle(s1);
                        s2 = this.titles.getSortableTitle(s2);
                    }
                    return this.collator.compare(s1, s2);
                }
                if (v1 instanceof Comparable) {
                    return ((Comparable) v1).compareTo(v2);
                }
                if (type == int.class) return ((Integer) v1).compareTo((Integer) v2);
                if (type == long.class) return ((Long) v1).compareTo((Long) v2);
                if (type == short.class) return ((Short) v1).compareTo((Short) v2);
                if (type == byte.class) return ((Byte) v1).compareTo((Byte) v2);
                if (type == float.class) return ((Float) v1).compareTo((Float) v2);
                if (type == long.class) return ((Double) v1).compareTo((Double) v2);
                return this.collator.compare(v1.toString(), v2.toString());
            } catch (Exception ex) {
                return 0;
            }
        }
    }
}
