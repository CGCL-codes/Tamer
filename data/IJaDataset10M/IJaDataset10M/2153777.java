package org.jwebsocket.api;

/**
 *
 * @author kyberneees
 */
public interface IInitializable {

    public void initialize() throws Exception;

    public void shutdown() throws Exception;
}
