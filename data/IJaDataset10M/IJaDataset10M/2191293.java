package org.bing.adapter.com.caucho.services.message;

import java.util.HashMap;

/**
 * Service API for a bare-bones message server.
 *
 * <p>A minimal message server only needs to implement the MessageSender
 * interface.  Keeping the server API simple makes it easier for
 * implementations to start up message servers.
 *
 * <p>The MessageSender service is queue or topic specific.  So there's no
 * need for a <b>To</b> header if there's no routing involved.
 * In other words, the service URL generally includes the queue
 * or topic identifier, e.g.
 *
 * <pre>
 * http://www.caucho.com/hessian/hessian/message?ejbid=topic-a
 * <pre>
 *
 * <p>More complicated messaging topologies, including configurations
 * with routing mesaging servers, will use the headers to define the
 * final destination.  The headers have the same functional purpose as
 * RFC-822 mail headers.
 */
public interface MessageSender {

    /**
   * Send a message to the server.
   *
   * @param headers any message headers
   * @param message the message data
   *
   * @exception MessageServiceException if the message cannot be delivered
   */
    public void send(HashMap headers, Object message) throws MessageServiceException;
}
