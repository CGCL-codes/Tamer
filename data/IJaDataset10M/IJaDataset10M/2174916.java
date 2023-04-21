package org.jaffa.modules.messaging.services;

import javax.jms.Message;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;

/** The plugin class to support JMS Provider specific functionality.
 */
public interface IJmsProviderPlugin {

    /** Returns true if the JMS Provider is active.
     * @return true if the JMS Provider is active.
     */
    public boolean isJmsProviderActive();

    /** Converts the input queue name to conform to rules of the JMS Provider.
     * @param queueName the queue name.
     * @return the converted queue name.
     */
    public String toQueueName(String queueName);

    /** Converts the input topic name to conform to rules of the JMS Provider.
     * @param topicName the topic name.
     * @return the converted topic name.
     */
    public String toTopicName(String topicName);

    /** Returns the count of Active consumers for the input queue.
     * @param queueName the queue name.
     * @return the count of Active consumers for the input queue.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     */
    public int getActiveConsumersCount(String queueName) throws FrameworkException, ApplicationExceptions;

    /** Returns the in-process Messages for the input queue.
     * @param queueName the queue name.
     * @return the in-process Messages for the input queue.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     */
    public Message[] listInProcessMessages(String queueName) throws FrameworkException, ApplicationExceptions;

    /** Returns the in-process Messages for the input queue.
     * @param queueName the queue name.
     * @param filter the filter to be used for selecting the in-process Messages.
     * @return the in-process Messages for the input queue.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     */
    public Message[] listInProcessMessages(String queueName, String filter) throws FrameworkException, ApplicationExceptions;

    /** Restarts Message delivery to the consumer(s) attached to the input queue.
     * @param queueName the queue name.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     */
    public void startMessageDelivery(String queueName) throws FrameworkException, ApplicationExceptions;

    /** Stops Message delivery to the consumer(s) attached to the input queue.
     * @param queueName the queue name.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     */
    public void stopMessageDelivery(String queueName) throws FrameworkException, ApplicationExceptions;

    /** Caches the message handler processing a Message.
     * A plugin implementation may provide the ability to interrupt an InProcess Message by looking up this cache.
     * @param messageId the message Id.
     * @param messageHandler the message handler.
     */
    public void addManageableMessageHandler(String messageId, IManageableMessageHandler messageHandler);

    /** Removes the message handler for the given messageId, from the cache.
     * @param messageId the message Id.
     */
    public void removeManageableMessageHandler(String messageId);

    /** Returns an array of messageIds corresponding to the InProcess messages that can be interrupted.
     * @return an array of messageIds corresponding to the InProcess messages that can be interrupted.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     */
    public String[] listManageableMessageIds() throws FrameworkException, ApplicationExceptions;

    /** This method will be invoked to terminate a long running message-handling process.
     * @param messageId the message Id.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     */
    public void interrupt(String messageId) throws FrameworkException, ApplicationExceptions;
}
