package net.sf.ezmorph.array;

import java.lang.reflect.Array;
import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.primitive.BooleanMorpher;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Morphs an array to a Boolean[].
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public final class BooleanObjectArrayMorpher extends AbstractArrayMorpher {

    private static final Class BOOLEAN_OBJECT_ARRAY_CLASS = Boolean[].class;

    private Boolean defaultValue;

    public BooleanObjectArrayMorpher() {
        super(false);
    }

    public BooleanObjectArrayMorpher(Boolean defaultValue) {
        super(true);
        this.defaultValue = defaultValue;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BooleanObjectArrayMorpher)) {
            return false;
        }
        BooleanObjectArrayMorpher other = (BooleanObjectArrayMorpher) obj;
        EqualsBuilder builder = new EqualsBuilder();
        if (isUseDefault() && other.isUseDefault()) {
            builder.append(getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        } else if (!isUseDefault() && !other.isUseDefault()) {
            return builder.isEquals();
        } else {
            return false;
        }
    }

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        if (isUseDefault()) {
            builder.append(getDefaultValue());
        }
        return builder.toHashCode();
    }

    public Object morph(Object array) {
        if (array == null) {
            return null;
        }
        if (BOOLEAN_OBJECT_ARRAY_CLASS.isAssignableFrom(array.getClass())) {
            return (Boolean[]) array;
        }
        if (array.getClass().isArray()) {
            int length = Array.getLength(array);
            int dims = getDimensions(array.getClass());
            int[] dimensions = createDimensions(dims, length);
            Object result = Array.newInstance(Boolean.class, dimensions);
            if (dims == 1) {
                BooleanMorpher morpher = null;
                if (isUseDefault()) {
                    if (defaultValue == null) {
                        for (int index = 0; index < length; index++) {
                            Array.set(result, index, null);
                        }
                        return result;
                    } else {
                        morpher = new BooleanMorpher(defaultValue.booleanValue());
                    }
                } else {
                    morpher = new BooleanMorpher();
                }
                for (int index = 0; index < length; index++) {
                    Array.set(result, index, morpher.morph(Array.get(array, index)) ? Boolean.TRUE : Boolean.FALSE);
                }
            } else {
                for (int index = 0; index < length; index++) {
                    Array.set(result, index, morph(Array.get(array, index)));
                }
            }
            return result;
        } else {
            throw new MorphException("argument is not an array: " + array.getClass());
        }
    }

    public Class morphsTo() {
        return BOOLEAN_OBJECT_ARRAY_CLASS;
    }
}
