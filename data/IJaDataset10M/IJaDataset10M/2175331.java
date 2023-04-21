package org.instedd.smsreminder.messaging.core;

import org.instedd.smsreminder.dataadapter.core.INextAppointment;

/**
 * Message sending service
 *
 */
public interface IMessagingService {

    /**
	 * 
	 * @return the codec used to code {@link INextAppointment}'s
	 */
    IAppointmentCodec getAppointmentEncoder();

    /**
	 * Sends a message
	 * @param message to send
	 * @return
	 * @throws Exception
	 */
    IMessageSendResult sendMessage(IOutboundMessage message) throws Exception;
}
