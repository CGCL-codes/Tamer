package com.techstar.framework.service.messaging.engine;

import java.io.*;

/**
 * 
 * @author yangjun
 *
 */
public abstract class Receiver {

    public Receiver() {
    }

    public abstract void open() throws ReceiverException;

    public abstract boolean next() throws ReceiverException;

    public abstract InputStream getInputStream() throws ReceiverException;

    public abstract void commit() throws ReceiverException;

    public abstract void rollback() throws ReceiverException;

    public abstract void close() throws ReceiverException;

    public abstract InputStream getInputStream(Object obj) throws ReceiverException;
}
