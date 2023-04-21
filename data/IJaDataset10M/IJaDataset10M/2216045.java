package ch.olsen.routes.data;

import ch.olsen.routes.data.VoidDataElement.VoidDataType;

/**
 * Long Data Element
 * Automatic cast to Long, Integer and Double
 * @author vito
 *
 */
public class EnumDataElementImpl<E extends Enum<E>> extends DataElementAbstr implements StringDataElement, EnumDataElement<E> {

    private static final long serialVersionUID = 1L;

    public static final String ENUMID = "enum";

    E e;

    final EnumDataType type;

    public EnumDataElementImpl(E e) {
        this.e = e;
        this.type = new EnumDataType(e);
    }

    public DataType getType() {
        return type;
    }

    public E enumValue() {
        return e;
    }

    public String stringValue() {
        return e.name();
    }

    public void update(String newvalue) {
        try {
            this.e = (E) Enum.valueOf(e.getClass(), newvalue);
        } catch (Exception e) {
            throw new RuntimeException("Unable to set new enum value");
        }
    }

    public void update(E value) {
        this.e = value;
    }

    /**
	 * Automatic cast
	 */
    @Override
    public StringDataElement toStringDE() throws ClassCastException {
        return this;
    }

    /**
	 * Automatic cast
	 */
    @Override
    public <T extends Enum<T>> EnumDataElement<T> toEnumDE(Enum<T> type) throws ClassCastException {
        return (EnumDataElement<T>) this;
    }

    @Override
    public EnumDataElement toEnumDE() throws ClassCastException {
        return this;
    }

    public static class EnumDataType implements DataType {

        private static final long serialVersionUID = 1L;

        final Class type;

        public EnumDataType(Enum e) {
            this.type = e.getClass();
        }

        public boolean isCompatible(DataType other) {
            if (other instanceof VoidDataType) return true;
            if (!(other instanceof EnumDataType)) return false;
            EnumDataType et = (EnumDataType) other;
            return et.type.equals(type);
        }

        @Override
        public String toString() {
            return "Enum";
        }
    }

    @Override
    public String toString() {
        return ENUMID + ":" + e;
    }

    public final PrimitiveType getPrimiteType() {
        return PrimitiveType.ENUM;
    }
}
