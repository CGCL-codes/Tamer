package com.curlap.orb.io;

/**
 * SerializableStreamWriter
 */
public interface SerializableStreamWriter {

    public void write(Object obj) throws SerializerException;

    public void close() throws SerializerException;
}
