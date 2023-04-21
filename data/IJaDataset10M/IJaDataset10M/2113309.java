package org.apache.axis.transport.jms;

/**
 * The <code>InvokeTimeoutException</code> is thrown when a method cannot
 * complete processing within the time allotted.  This occurs most often
 * when the broker has failed and the client is unable to reconnect.  This
 * may be thrown from any method within the wsclient that interacts with the
 * broker.  The timeout is defined within the environment parameter to
 * <code>createConnector</code> method in <code>JMSConnectorFactory</code>
 * The key in the table is <code>IJMSConstants.INTERACT_TIMEOUT_TIME</code>
 *
 * @author Jaime Meritt  (jmeritt@sonicsoftware.com)
 * @author Richard Chung (rchung@sonicsoftware.com)
 * @author Dave Chappell (chappell@sonicsoftware.com)
 *
 */
public class InvokeTimeoutException extends InvokeException {

    public InvokeTimeoutException(String message) {
        super(message);
    }
}
