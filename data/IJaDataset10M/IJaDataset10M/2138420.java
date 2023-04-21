package org.gbif.portal.util.mhf.message;

/**
 * A MessageAccessor is an object that accesses a message based on the
 * location passed in.  It is effectively a Mediator that follows the Command
 * pattern such that an accessor can be configured in the property store.
 * <p/>
 * An example of a MessageAccessor could be an XPathAccessor that is configured
 * to take an XPath expression and returns the result of accessing the XMLMessage.
 *
 * @author Tim Robertson
 */
public interface MessageAccessor {

    /**
   * Accesses the supplied message and returns the response in accordance
   * with the configuration of the Accessor
   *
   * @param message To access
   * @return The result of accessing the message
   * @throws MessageAccessException should there be an error accessing the message
   */
    Object invoke(Message message) throws MessageAccessException;
}
