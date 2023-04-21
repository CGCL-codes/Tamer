package net.sf.json.util;

import net.sf.ezmorph.ObjectMorpher;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class EnumMorpher implements ObjectMorpher {

    private Class enumClass;

    public EnumMorpher(Class enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("enumClass is null");
        }
        if (!Enum.class.isAssignableFrom(enumClass)) {
            throw new IllegalArgumentException("enumClass is not an Enum class");
        }
        this.enumClass = enumClass;
    }

    public Object morph(Object value) {
        if (value == null) {
            return enumClass.cast(null);
        }
        return Enum.valueOf(enumClass, String.valueOf(value));
    }

    public Class morphsTo() {
        return enumClass;
    }

    public boolean supports(Class clazz) {
        return String.class.isAssignableFrom(clazz);
    }
}
