package gilgamesh.beans.util;

public interface PropertyAccess {

    public abstract void setValue(final Object o, final String field, final String value) throws NoSuchFieldException, IllegalAccessException;

    public abstract Object getValue(final Object bean, final String name);
}
