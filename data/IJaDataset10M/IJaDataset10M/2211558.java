package de.matthiasmann.twl.model;

/**
 * Abstract base class to simplify implementing EnumModels.
 * 
 * @param <T> The enum type
 * @author Matthias Mann
 */
public abstract class AbstractEnumModel<T extends Enum<T>> extends HasCallback implements EnumModel<T> {

    private final Class<T> enumClass;

    protected AbstractEnumModel(Class<T> clazz) {
        this.enumClass = clazz;
    }

    public Class<T> getEnumClass() {
        return enumClass;
    }
}
