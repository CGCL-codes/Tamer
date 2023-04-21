package mw.server.socket;

public interface ZippedObject<T> {

    void zip(T object);

    T unzip();
}
