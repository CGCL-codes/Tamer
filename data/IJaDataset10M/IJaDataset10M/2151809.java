package net.sf.opentranquera.msender.target;

import javax.jms.Connection;
import javax.jms.Destination;

/**
 * This class provide an alias for one {@link net.sf.opentranquera.msender.MessageSender}. This has one destination
 * target and one connection to JMS provider.<br>
 *
 * @author <a href="mailto:rdiegoc@gmail.com">Diego Campodonico</a>
 */
public interface MessageSenderTarget {

    public Destination getDestination();

    public Connection getConnection();

    public void release(Connection conn);
}
