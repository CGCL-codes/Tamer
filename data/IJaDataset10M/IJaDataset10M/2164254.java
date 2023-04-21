package de.tud.kom.nat.comm.obs;

import java.util.UUID;

/**
 * This class provides functionality to get or observe the state of
 * a queued message. A message can have 4 states:
 * <ul>
 * <li><tt>MessageState.UNKNOWN</tt>: We dont have any information.</li>
 * <li><tt>MessageState.QUEUED</tt>: The message is queued, but not yet sent.</li>
 * <li><tt>MessageState.SENT</tt>: The message is sent, but it is not clear whether
 * the receiver already received it completely.</li>
 * <li><tt>MessageState.ACKNOWLEDGED</tt>: The receiver has confirmed that he has the
 * complete message</li>.
 * </ul>
 * <br>
 * 
 * All methods are threadsafe.
 * 
 * @author Matthias Weinert
 */
public interface IMessageTracer {

    /**
	 * A message can have these states.
	 */
    public enum MessageState {

        UNKNOWN, QUEUED, SENT, ACKNOWLEDGED
    }

    /**
	 * The message subsystem sets the state using this method.
	 * 
	 * @param messageID id of the message
	 * @param state new state
	 */
    public abstract void setMessageState(UUID messageID, MessageState state);

    /**
	 * This function retrieves the state of the message with the given message ID. 
	 * If no state is found, <tt>MessageState.UNKNOWN</tt> is returned.
	 * @param messageID ID of message
	 * @return state of the message
	 */
    public abstract MessageState getMessageState(UUID messageID);

    /**
	 * Registers a <tt>IMessageStateObserver</tt> which is informed when the state
	 * of the message with the given ID changes.
	 * 
	 * @param messageID ID of message
	 * @param obs new observer
	 */
    public abstract void registerObserver(UUID messageID, IMessageStatusObserver obs);
}
