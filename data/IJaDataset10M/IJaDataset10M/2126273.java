package net.sf.stitch.crud;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.hibernate.validator.NotNull;

/**
 * Represents an Entity Property.  We track the Field and the Getter and
 * Setter Methods because annotations may be made at either the field or
 * the getter method, and the presence of a setter tells us the field is
 * not read-only.
 * @author Logan Hutchinson
 */
public class CrudEntityProperty {

    /** Entity property field */
    private final Field field;

    /** Getter method */
    private Method getter;

    /** Setter method */
    private Method setter;

    public CrudEntityProperty(final Field field) {
        assert null != field;
        this.field = field;
    }

    public Method getGetter() {
        return getter;
    }

    public void setGetter(final Method getter) {
        assert getter.getReturnType().equals(this.field.getType());
        this.getter = getter;
    }

    public Method getSetter() {
        return setter;
    }

    public void setSetter(final Method setter) {
        this.setter = setter;
    }

    public Field getField() {
        return field;
    }

    /**
     * Convenience method to get the name of the field.
     * @return See Field.getName()
     */
    public String getFieldName() {
        return this.field.getName();
    }

    /**
     * If the property has no setter, the field is read-only.
     * @return True if no setter is specified, False otherwise.
     */
    public boolean isReadOnly() {
        return null == this.setter;
    }

    /**
     * Gets the annotation from the field or getter. 
     * TODO Does field or getter have preference?
     * @param annotationClass
     * @return Annotation for the field, if it exists, or the getter method,
     * if the annotation exists, otherwise null.
     */
    private <T extends Annotation> T getAnnotation(final Class<T> annotationClass) {
        final T annotationField = this.field.getAnnotation(annotationClass);
        final T annotationGetter = this.getter.getAnnotation(annotationClass);
        if (null != annotationField && null != annotationGetter && !annotationField.equals(annotationGetter)) {
            throw new RuntimeException("Inconsistent annotation (" + annotationClass.getCanonicalName() + ") for entity property " + this.getFieldName());
        }
        return (null != annotationField) ? annotationField : annotationGetter;
    }

    /**
     * Utility method to test for annotation present at the field or getter level.
     */
    private boolean isAnnotationPresent(final Class<? extends Annotation> annotationClass) {
        return this.field.isAnnotationPresent(annotationClass) || (null != this.getter && this.getter.isAnnotationPresent(annotationClass));
    }

    /**
     * Whether the field is required is determined from the type and its
     * annotations.
     * @return True if a null value is unacceptable, False otherwise.
     */
    public boolean isRequired() {
        final Class<?> fieldType = this.field.getType();
        boolean isRequired = fieldType.isPrimitive() || fieldType.isEnum();
        if (!isRequired) {
            final Basic basicAnnotation = getAnnotation(Basic.class);
            final boolean isOptional = (null != basicAnnotation) ? basicAnnotation.optional() : true;
            final Column columnAnnotation = getAnnotation(Column.class);
            final boolean isNullable = (null != columnAnnotation) ? columnAnnotation.nullable() : true;
            final ManyToOne manyToOneAnnotation = getAnnotation(ManyToOne.class);
            final boolean isNto1Optional = (null != manyToOneAnnotation) ? manyToOneAnnotation.optional() : true;
            final OneToOne oneToOneAnnotation = getAnnotation(OneToOne.class);
            final boolean is1to1Optional = (null != oneToOneAnnotation) ? oneToOneAnnotation.optional() : true;
            final boolean hibernateNotNull = isAnnotationPresent(NotNull.class);
            isRequired = !isOptional || !isNullable || !isNto1Optional || !is1to1Optional || hibernateNotNull;
        }
        return isRequired;
    }

    /**
     * Is the property annotated with @Id
     * @return true if the property is annotated with @Id, false otherwise.
     */
    public boolean isId() {
        return isAnnotationPresent(Id.class);
    }

    /**
     * Is the property annotated with @Version
     * @return true if the property is annotated with @Version, false otherwise.
     */
    public boolean isVersion() {
        return isAnnotationPresent(Version.class);
    }

    /**
     * Is the property annotated with @Lob
     * @return true if the property is annotated with @Lob, false otherwise.
     */
    public boolean isLob() {
        return isAnnotationPresent(Lob.class);
    }

    /**
     * 
     * @return true if the property is annotated with 
     */
    public boolean isFetchLazy() {
        final Basic basicAnnotation = getAnnotation(Basic.class);
        final boolean isBasicLazy = FetchType.LAZY == ((null != basicAnnotation) ? basicAnnotation.fetch() : FetchType.EAGER);
        final ManyToOne manyToOneAnnotation = getAnnotation(ManyToOne.class);
        final boolean isNto1Lazy = FetchType.LAZY == ((null != manyToOneAnnotation) ? manyToOneAnnotation.fetch() : FetchType.EAGER);
        final OneToOne oneToOneAnnotation = getAnnotation(OneToOne.class);
        final boolean is1to1Lazy = FetchType.LAZY == ((null != oneToOneAnnotation) ? oneToOneAnnotation.fetch() : FetchType.EAGER);
        final OneToMany oneToManyAnnotation = getAnnotation(OneToMany.class);
        final boolean is1toNLazy = FetchType.LAZY == ((null != oneToManyAnnotation) ? oneToManyAnnotation.fetch() : FetchType.EAGER);
        return isBasicLazy || isNto1Lazy || is1to1Lazy || is1toNLazy;
    }

    /**
     * Is the field a boolean (true/false, yes/no).
     * @return true if the field class is a Boolean or boolean.
     */
    public boolean isBoolean() {
        final Class<?> fieldType = this.field.getType();
        return fieldType.equals(Boolean.class) || fieldType.equals(boolean.class);
    }

    /**
     * Is the field an enum.
     * @return true if the field is an enum, false otherwise.
     */
    public boolean isEnum() {
        return this.field.getType().isEnum();
    }

    /**
     * Is the field an Entity.
     * 
     * @return true if the field is annotation with ManyToOne, OneToOne, or
     *         OneToMany, false otherwise.
     */
    public boolean isEntity() {
        return null != getAnnotation(ManyToOne.class) || null != getAnnotation(OneToOne.class) || null != getAnnotation(OneToMany.class);
    }

    /**
     * Is the property searchable from the EntityQuery.  In other words, would you want to
     * search for matches on this property.
     * TODO Rethink name (searchable).  Can this be expanded beyond String?
     * @return true if the field is a String, false otherwise.
     */
    public boolean isSearchable() {
        return this.field.getType().equals(String.class);
    }

    /**
     * Is the property listable from the EntityQuery.  In other words, would you want the
     * field to appear in a one-line summary for the entity. 
     * TODO Rethink name (listable).  Can this be derived differently?
     */
    public boolean isListable() {
        final Class<?> fieldType = this.field.getType();
        return (fieldType.isPrimitive() || fieldType.getCanonicalName().startsWith("java.lang.")) && !isId() && !isVersion() && !isFetchLazy() && !isLob();
    }

    /**
     * @return The maximum length of the property.
     */
    public Integer getMaxLength() {
        Integer maxLength = null;
        final Column columnAnnotation = getAnnotation(Column.class);
        if (this.field.getType().equals(String.class)) {
            maxLength = (null != columnAnnotation && 0 != columnAnnotation.length()) ? columnAnnotation.length() : 255;
        } else if (null != columnAnnotation && (0 != columnAnnotation.precision() || 0 != columnAnnotation.scale())) {
            maxLength = columnAnnotation.precision() + 1 + columnAnnotation.scale();
        }
        return maxLength;
    }

    /**
     * Input box size may be estimated based on the field type.
     */
    private static final Map<String, Integer> SIZE_MAP;

    static {
        final Integer int4 = Integer.valueOf(4);
        final Integer int6 = Integer.valueOf(6);
        final Integer int12 = Integer.valueOf(12);
        final Integer int20 = Integer.valueOf(20);
        SIZE_MAP = new TreeMap<String, Integer>();
        SIZE_MAP.put(Byte.class.getName(), int4);
        SIZE_MAP.put(Double.class.getName(), int20);
        SIZE_MAP.put(double.class.getName(), int20);
        SIZE_MAP.put(double.class.getName(), int20);
        SIZE_MAP.put(Float.class.getName(), int12);
        SIZE_MAP.put(float.class.getName(), int12);
        SIZE_MAP.put(Integer.class.getName(), int12);
        SIZE_MAP.put(int.class.getName(), int12);
        SIZE_MAP.put(Long.class.getName(), int20);
        SIZE_MAP.put(long.class.getName(), int20);
        SIZE_MAP.put(Short.class.getName(), int6);
        SIZE_MAP.put(short.class.getName(), int6);
    }

    static final int DEFAULT_SIZE = 20;

    /**
     * @return Reasonable estimate for input box size.
     */
    public int getSize() {
        int size;
        if (isAnnotationPresent(Temporal.class)) {
            switch(getTemporalType()) {
                case DATE:
                    size = 10;
                    break;
                case TIME:
                    size = 5;
                    break;
                case TIMESTAMP:
                    size = 16;
                    break;
                default:
                    assert false;
                    size = 99;
                    break;
            }
        } else {
            final Integer sizeForClass = SIZE_MAP.get(this.field.getType().getName());
            if (null != sizeForClass) {
                size = sizeForClass.intValue();
            } else {
                final Integer maxLength = getMaxLength();
                size = (null != maxLength) ? maxLength.intValue() : DEFAULT_SIZE;
            }
        }
        return size;
    }

    /**
     * Gets the resource bundle key for translating the field into a localized message. 
     * @return Typically, package.class.field
     */
    public String getMessageKey() {
        return this.field.getDeclaringClass().getName() + '.' + this.field.getName();
    }

    /**
     * Gets the Enumeration Type.
     * @return EnumType for enum fields.  This is only applicable for enum properties. 
     */
    public EnumType getEnumType() {
        assert isEnum();
        final Enumerated enumeration = getAnnotation(Enumerated.class);
        return (null != enumeration) ? enumeration.value() : EnumType.ORDINAL;
    }

    /**
     * Gets the Temporal Type.
     * @return TemporalType if a @Temporal annotation is present. 
     */
    public TemporalType getTemporalType() {
        final Temporal temporal = getAnnotation(Temporal.class);
        return (null != temporal) ? temporal.value() : null;
    }

    /**
     * Gets the appropriate converter for the property. 
     * @return Converter tag that can be embedded in a facelet,
     * if applicable.  Otherwise a null is returned.
     */
    public String getConverter() {
        String converter = null;
        final Class<?> fieldType = this.field.getType();
        if (fieldType.isEnum()) {
            converter = "<s:convertEnum/>";
        } else if (isAnnotationPresent(Temporal.class)) {
            switch(getTemporalType()) {
                case DATE:
                    converter = "<s:convertDateTime type=\"date\" dateStyle=\"short\" pattern=\"MM/dd/yyyy\"/>";
                    break;
                case TIME:
                    converter = "<s:convertDateTime type=\"time\"/>";
                    break;
                case TIMESTAMP:
                    converter = "<s:convertDateTime type=\"both\" dateStyle=\"short\"/>";
                    break;
                default:
                    assert false;
                    break;
            }
        } else if (fieldType.isPrimitive() || fieldType.getCanonicalName().startsWith("java.lang.")) {
            assert null == converter;
        } else {
            converter = "<s:convertEntity/>";
        }
        return converter;
    }
}
