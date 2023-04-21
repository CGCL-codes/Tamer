package com.curlap.orb.io;

/**
 * SerializableStreamReader
 */
public interface SerializableStreamReader {

    public Object read() throws SerializerException;

    public void close() throws SerializerException;
}
