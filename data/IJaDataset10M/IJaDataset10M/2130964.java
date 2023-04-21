package org.mortbay.jetty.nio;

/** 
 * NIOConnector.
 * A marker interface that indicates that NIOBuffers can be handled (efficiently) by this Connector.
 * 
 * @author Nigel Canonizado
 * 
 */
public interface NIOConnector {

    boolean getUseDirectBuffers();
}
