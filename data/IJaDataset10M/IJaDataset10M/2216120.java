package net.sourceforge.jcoupling.bus.server;

import org.apache.log4j.Logger;
import net.sourceforge.jcoupling.bus.dao.*;
import net.sourceforge.jcoupling.peer.Message;
import net.sourceforge.jcoupling.peer.destination.Destination;
import net.sourceforge.jcoupling.peer.interaction.InteractionSignal;
import net.sourceforge.jcoupling.peer.interaction.MessageID;
import net.sourceforge.jcoupling.peer.interaction.ResponseContainer;
import net.sourceforge.jcoupling.wca.TimeCoupling;
import java.io.Serializable;

/**
 * Processes send requests and allows message transfer to be time Coupled between Sender and
 * receiver.  This means that a request to send will be complete once the message has received
 * by all eligible receivers of the message.
 * @author Lachlan Aldred
 */
public class InteractionProcessor {

    private MessageBusController _controller;

    private Logger _logger = Logger.getLogger(getClass());

    private ExchangerDao _exchangerDao;

    public InteractionProcessor(ExchangerDao exchangerDao, MessageBusController controller) {
        _controller = controller;
        _exchangerDao = exchangerDao;
    }

    /**
     * Processes a send request into the messaging server.
     * @param destination the destination of the message.
     * @param message the message to be delivered.
     * @return the result of the interaction/sub-interaction (time Coupled/Decoupled).
     * @throws InterruptedException if interrupted while waiting.
     * @throws NotFoundException if the destination is not found.
     */
    public ResponseContainer processSendTimeDecoupled(Destination destination, Message message) throws InterruptedException, NotFoundException {
        MessageEvent event = _controller.createEvent(destination, message, false);
        _controller.putEvent(event);
        ResponseContainer responseContainer = new ResponseContainer(message.getID());
        responseContainer.add(new Response(InteractionSignal.MSG_BUFFERED, null));
        return responseContainer;
    }

    /**
     * Processes a send request into the messaging server.
     * @param destination the destination of the message.
     * @param message the message to be delivered.
     * @return the result of the interaction/sub-interaction (time Coupled/Decoupled).
     * @throws InterruptedException if interrupted while waiting.
     * @throws NotFoundException if the destination is not found.
     */
    public ResponseContainer processSendTimeCoupled(Destination destination, Message message) throws InterruptedException, NotFoundException {
        _logger.debug("1 msg [" + message.getID() + "] dest [" + destination + "] hcde [" + message.hashCode() + "]");
        int numConsumers;
        MessageEvent event = _controller.createEvent(destination, message, true);
        if (event instanceof MessagePublishEvent) {
            numConsumers = ((MessagePublishEvent) event).getOriginalReceivers().size();
        } else {
            numConsumers = 1;
        }
        _logger.debug("2 msg [" + message.getID() + "] dest [" + destination + "] hcde [" + message.hashCode() + "]");
        SynchExchanger synchronizer = new SynchExchanger(numConsumers, message.getID());
        _logger.debug("3 msg [" + message.getID() + "] dest [" + destination + "] hcde [" + message.hashCode() + "]");
        _exchangerDao.insert(synchronizer);
        _controller.putEvent(event);
        _logger.debug("About to wait. msg [" + message.getID() + "] dest [" + destination + "] Num cons: [" + numConsumers + "].");
        synchronizer.await();
        _logger.debug("Finished wait. msg [" + message.getID() + "] dest [" + destination + "].");
        ResponseContainer result = new ResponseContainer(message.getID());
        result.addAll(synchronizer.getExchangedObjects());
        _exchangerDao.remove(synchronizer);
        return result;
    }

    /**
     * Processes a send request into the messaging server.
     *
     * @param destination the destination of the message.
     * @param message     the message to be delivered.
     * @return the result of the interaction/sub-interaction (time Coupled/Decoupled).
     * @throws InterruptedException if interrupted while waiting.
     * @throws NotFoundException if the destination is not found.
     */
    public ResponseContainer processSendRequest(Destination destination, Message message, TimeCoupling mode) throws InterruptedException, NotFoundException {
        if (TimeCoupling.Coupled.equals(mode)) {
            _logger.debug("Msg [" + message.getID() + "] dest [" + destination + "]");
            return processSendTimeCoupled(destination, message);
        } else {
            _logger.debug("Msg [" + message.getID() + "] dest [" + destination + "]");
            return processSendTimeDecoupled(destination, message);
        }
    }

    /**
     * Processes a request to receive a message off a destination.
     * @param destination the address/topic/channel.
     * @param communicator the id of the communicator.
     * @return the message.
     * @throws InterruptedException if interrupted while waiting.
     * @throws NotFoundException if there is no subscription matching that destination,
     * communicator.
     */
    public Message processReceiveRequest(Destination destination, CommunicatorID communicator) throws InterruptedException, NotFoundException {
        return _controller.getMessage(destination, communicator);
    }

    /**
     * Adds the result object to the exchanger, to be picked up by the Sender.  Notifies
     * a time-Coupled Sender that the message has been processed.
     * Pre: There exists an un-archived message event in storage that corresponds to
     * messageID.
     * Post: MessageID relates to a non p2p channel then the message event gets archived.
     * MessageID relates to a non p2p channel then the message event only gets archived
     * if this ack is from the last subscriber.
     * @param result
     * @param messageID
     */
    public void processAcknowledgeReceipt(Serializable result, CommunicatorID communicatorID, MessageID messageID) throws NotFoundException {
        MessageEvent messageEvent = _controller.removeUnacknowledged(messageID);
        if (messageEvent == null) {
            throw new NotFoundException("MessageID not valid.");
        }
        archiveIfComplete(messageEvent);
        SynchExchanger synchExchanger = _exchangerDao.find(messageID);
        _logger.debug("SynchExchanger [" + messageID + "] found " + (synchExchanger != null));
        if (synchExchanger != null) {
            synchExchanger.addExchangedObject(result, communicatorID);
            synchExchanger.countDown();
        } else {
            _logger.debug("Message [" + messageID + "] was time Decoupled.");
        }
    }

    private void archiveIfComplete(MessageEvent messageEvent) {
        if (messageEvent instanceof MessagePublishEvent) {
            MessagePublishEvent publishEvent = (MessagePublishEvent) messageEvent;
            if (publishEvent.getReceivers().size() == 0) {
                _controller.archiveEvent(messageEvent);
            }
        } else {
            _controller.archiveEvent(messageEvent);
        }
    }
}
