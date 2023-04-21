package org.netbeams.dsp;

import org.netbeams.dsp.message.Message;

public interface DSPComponent extends BaseComponent {

    /**
	 * Provides the description of the component. 
	 * 
	 * @return Compoe
	 */
    public ComponentDescriptor getComponentDescriptor();

    /**
	 * Method invoked by the data broker to delivery messages as result of a call to @see MessageBrokerAccessor.send(Message) 
	 * This methos is invoked asynchronously. The component implementaion should not block or take long to reply.
	 * 
	 * The message may require acknowlodgment (@see Message.).
	 * 
	 * @param message
	 * @throws DSPException
	 */
    public void deliver(Message message) throws DSPException;

    /**
	 * Method invoked by the data broker to pull messages as result of a call to @see MessageBrokerAccessor#send() 
	 * The <code>request</code> is used by the invoking component to provide the target component with
	 * additional data so it can fullfil the request.
	 * 
	 * This is a synchronous call.
	 * 
	 * @param request Data that can help define which data can should be returned
	 * @return Requested data
	 * @throws DSPException
	 */
    public Message deliverWithReply(Message message) throws DSPException;

    public Message deliverWithReply(Message message, long waitTime) throws DSPException;

    /**
	 * Invoked by the platform to inform the component it should start its processes. This method 
	 * should not block. After the return of this method, the platform may send or receive messages. 
	 */
    public void startComponent() throws DSPException;

    /**
	 * Invoked by the platform to inform the component it MUST stop its processes. This method 
	 * should not block. No message will be send after this invocation. 
	 */
    public void stopComponent() throws DSPException;
}
