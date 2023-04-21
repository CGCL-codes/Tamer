package fr.irisa.asap.debug.spy;

import fr.irisa.asap.debug.replay.realTime.SequenceRealTime;

/**
 * Interface IForwarding
 * 
 * This interface should be implemented by all the classes which process a
 * message in output.<br/>
 * It is the case for forwarder, to transmit the message, and the case of
 * differents replays.<br/><br/>
 *
 * <strong>
 * All realizations of this class MUST provide a default constructor
 * </strong>, otherwise a runtime exception will occure.
 *
 * @author DebugProject_grobinea
 * @Date cr�ation : 26/02/2007
 * @Date derni�re modification : 27/02/2007
 * @Version : $Id: IForwarding.java 212 2007-04-21 15:21:06Z anthony.loiseau $
 *
 * @see Forwarder
 * @see SequenceRealTime
 */
public interface IForwarding {

    /**
	 * This method permits to process a message in output.<br/>
     * 
	 * In fact, when this method is called, the message is transmit to the
     * receiver of this message.
	 * This method send the initial message or a NetworkMsg. It depends on the
     * type of the communication (Input or Output).
     * 
	 * @param msg It is the message that we would transmit.
	 */
    public void processMsg(MsgDescriptor msg);
}
