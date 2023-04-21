package net.sourceforge.pojosync.internal;

public interface IAccessCollection {

    Access getAccess(Class<?> iface, int id);

    void add(Access access);

    boolean contains(Access access);

    <T> T get(Class<T> clazz, int id);
}
