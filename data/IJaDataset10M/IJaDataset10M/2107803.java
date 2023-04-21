package org.maestroframework.utils;

public interface StringSerializer<T> {

    public String serialize(T value);

    public T deserialize(String valueString);
}
