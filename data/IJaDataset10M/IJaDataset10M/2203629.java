package org.epistem.code.javaclass;

import org.epistem.code.javaclass.attributes.ConstantValueAttribute;
import org.epistem.code.javaclass.flags.FieldFlag;
import org.epistem.code.type.ValueType;

/**
 * A member field
 *
 * @author nickmain
 */
public class JavaField extends JavaMember<FieldFlag> {

    /**
     * The field type (as a value type since fields cannot be void)
     */
    public final ValueType fieldType;

    public JavaField(JavaClass containerClass, String name, ValueType type) {
        super(containerClass, name, type, FieldFlag.class);
        this.fieldType = type;
    }

    /**
     * Get the constant value for the field.
     *  
     * @return null if none, otherwise String or Number derivative
     */
    public Object constantValue() {
        ConstantValueAttribute cval = (ConstantValueAttribute) attributes.get(JavaAttribute.Name.ConstantValue.name());
        if (cval == null) return null;
        return cval.value;
    }

    @Override
    public final String toString() {
        return "field " + super.toString();
    }

    /**
     * Equality is based on the name only 
     * @see java.lang.Object#equals(java.lang.Object) 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof JavaField)) return false;
        return ((JavaField) obj).name.equals(name);
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
