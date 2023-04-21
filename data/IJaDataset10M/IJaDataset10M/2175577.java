package com.hajo.server;

import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author hari
 */
public interface IHajoServer {

    void startThriftServer() throws TTransportException;

    void stopThriftServer();
}
