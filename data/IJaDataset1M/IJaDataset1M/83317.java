package de.dlr.davinspector.history;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.event.EventListenerList;
import de.dlr.davinspector.common.Constant;
import de.dlr.davinspector.common.Constant.Direction;
import de.dlr.davinspector.common.Util;

/**
 * This class saves and provides access to the processed messages.
 *
 * @version $LastChangedRevision$
 * @author Jochen Wuest
 */
public class MessageHistory {

    /** List of listeners for new messages. */
    private EventListenerList myListenersNewMessage = new EventListenerList();

    /** List of listeners for the "loading of a message" event. */
    private EventListenerList myListenersLoadMessage = new EventListenerList();

    /** This vector stores the messages. */
    private List<AMessage> myMessages = null;

    /**
     * Constructor for message history. Client and server buffers and the 
     * vector for storing the messages are initialized.
     */
    public MessageHistory() {
        myMessages = new Vector<AMessage>();
    }

    /**
     * This method stores a message. 
     * Afterwards  a {@link MessageEvent} is generated and all {@link INewMessageListener} are notified.
     * 
     * @param message AMessage
     */
    public void storeMessage(AMessage message) {
        myMessages.add(message);
        notifyNewMessage(new MessageEvent(this, message));
    }

    /**
     * Save the message history of the client or server in a file. 
     * 
     * @param direction Direction
     * @param filename String
     */
    public void exportToFile(Direction direction, String filename) {
        String buffer = "";
        File file = new File(filename);
        try {
            FileOutputStream out = new FileOutputStream(file);
            for (Iterator<AMessage> iterator = myMessages.iterator(); iterator.hasNext(); ) {
                AMessage message = (AMessage) iterator.next();
                if (message.getDirection() == direction || direction == Direction.DontCare) {
                    buffer = message.getRawData();
                    buffer += Constant.EXPORT_DELIMITER;
                    out.write(buffer.getBytes());
                }
            }
            out.flush();
            out.close();
        } catch (IOException ioe) {
            Util.showIOExceptionMessageDialog(ioe);
        }
    }

    /**
     * Deletes all stored messages.
     */
    public void deleteAllMessages() {
        myMessages.clear();
    }

    /**
     * This method loads a message identified by the ID.
     * Afterwards  a {@link MessageEvent} is generated and all {@link ILoadMessageListener} are notified.
     * 
     * @param id     int
     */
    public void loadMessageByID(int id) {
        for (Iterator<AMessage> iterator = myMessages.iterator(); iterator.hasNext(); ) {
            AMessage message = (AMessage) iterator.next();
            if (message.getId() == id) {
                notifyLoadMessage(new MessageEvent(this, message));
                break;
            }
        }
    }

    /**
     * Returns the message identified by ID.
     * 
     * @param id Integer
     * @return {@link AMessage}
     */
    public AMessage getMessage(int id) {
        for (Iterator<AMessage> iterator = myMessages.iterator(); iterator.hasNext(); ) {
            AMessage message = (AMessage) iterator.next();
            if (message.getId() == id) {
                return message;
            }
        }
        return null;
    }

    /** 
     * Adds an {@link INewMessageListener} to MessageHistory. 
     * 
     * @param listener      the {@link INewMessageListener} to be added 
     */
    public void addNewMessageListener(INewMessageListener listener) {
        myListenersNewMessage.add(INewMessageListener.class, listener);
    }

    /** 
     * Removes an {@link INewMessageListener} from MessageHistory. 
     * 
     * @param listener      the {@link INewMessageListener} to be removed 
     */
    public void removeNewMessageListener(INewMessageListener listener) {
        myListenersNewMessage.remove(INewMessageListener.class, listener);
    }

    /** 
     * Notifies all {@link INewMessageListener}s that have registered interest for 
     * notification on an {@link MessageEvent}. 
     * 
     * @param event         the {@link MessageEvent} object 
     */
    protected synchronized void notifyNewMessage(MessageEvent event) {
        for (INewMessageListener listener : myListenersNewMessage.getListeners(INewMessageListener.class)) {
            listener.newMessage(event);
        }
    }

    /** 
     * Adds an {@link ILoadMessageListener} to MessageHistory. 
     * 
     * @param listener      the {@link ILoadMessageListener} to be added 
     */
    public void addLoadMessageListener(ILoadMessageListener listener) {
        myListenersLoadMessage.add(ILoadMessageListener.class, listener);
    }

    /** 
     * Removes an {@link ILoadMessageListener} from MessageHistory. 
     * 
     * @param listener      the {@link ILoadMessageListener} to be removed 
     */
    public void removeLoadMessageListener(ILoadMessageListener listener) {
        myListenersLoadMessage.remove(ILoadMessageListener.class, listener);
    }

    /** 
     * Notifies all {@link ILoadMessageListener}s that have registered interest for 
     * notification on an {@link MessageEvent}. 
     * 
     * @param event         the {@link MessageEvent} object 
     */
    protected synchronized void notifyLoadMessage(MessageEvent event) {
        for (ILoadMessageListener listener : myListenersLoadMessage.getListeners(ILoadMessageListener.class)) {
            listener.loadMessage(event);
        }
    }
}
