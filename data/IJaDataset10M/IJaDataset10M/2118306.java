package de.huxhorn.sulky.generics.io;

/**
 * @param <E>
 * @deprecated Use sulky-codec instead.
 */
public interface Deserializer<E> {

    E deserialize(byte[] bytes);
}
