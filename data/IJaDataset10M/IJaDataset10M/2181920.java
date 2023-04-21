package net.sourceforge.jcoupling.bus.dao;

import net.sourceforge.jcoupling.peer.destination.Channel;
import net.sourceforge.jcoupling.peer.destination.Destination;
import net.sourceforge.jcoupling.peer.interaction.MessageID;
import net.sourceforge.jcoupling.bus.server.CommunicatorID;
import java.util.Set;

/**
 * Data Access Object (Dao Pattern) for creating, updating, and deleting Message
 * queue events.
 * @author Lachlan Aldred
 */
public interface MessageQueueEventDao {

    /**
     * Adds a new message queuing event to the store of events.
     * @param event the event.
     */
    public boolean insertMessageEvent(MessageEvent event);

    /**
     * Removes an event from the runtime store of events.
     * @param event the event
     * @return if successful.
     */
    public boolean deleteEvent(MessageEvent event);

    public MessageEvent consumeMessage(MessageID messageid, CommunicatorID communicatorID);

    /**
     * Retreives the next unconsumed message publish event for that subscription.
     * This applies to a subscription over a Pub-Sub Channel or an Address (P2P Channels
     * can not use this method). The call will block until the message is available.
     * Pre: true.
     * Post: a publish event matching 'subscription' has arrived, and is returned.
     * @param subscription
     * @return the next unconsumed event for that subscription.
     * @throws InterruptedException if interrupted while waiting.
     */
    public MessagePublishEvent nextPublishEvent(Subscription subscription) throws InterruptedException;

    /**
     * Gets the next message queue event for a point to point channel.
     * The call will block until the message is available.
     * Pre: true.
     * Post: a queue event matching 'channel' has arrived and is returned.
     * @param channel a point to point channel.
     * @return the next unconsumed event for that channel.
     * @throws InterruptedException if interrupted while waiting.
     */
    public MessageQueueEvent nextQueueEvent(Channel channel) throws InterruptedException;

    /**
     * Gets the set of all message events related to this destination.
     * Pre: true
     * Post: returned all the events for that destination.
     * @param destination
     * @return the list of events for the destination.
     */
    public Set<? extends MessageEvent> getEvents(Destination destination);

    /**
     * Updates the messageEvent.
     * @param event
     * @return if successful.
     */
    public boolean updateEvent(MessageEvent event);

    /**
     * Find the event with messageID.
     * Post: return not null implies return.getMessage().getID() equals messageID
     * @param messageID
     * @return the event, if available.
     */
    public MessageEvent getEvent(MessageID messageID);

    /**
     * Archives the message event.
     * Pre: getEvent(event) != null
     * Post: getEvent(event) == null
     * @param messageEvent
     */
    public void archiveEvent(MessageEvent messageEvent);

    /**
     * Removes an unacknowledged event from storage.
     * Pre: nextMessageEvent() or nextQueueEvent() has already been called for this
     * message a greater number of times than this message has been called for the same
     * message.
     * Post: return not null. 
     * @param messageID
     * @return the unacknowledged event.
     */
    public MessageEvent removeUnacknowledged(MessageID messageID);
}
