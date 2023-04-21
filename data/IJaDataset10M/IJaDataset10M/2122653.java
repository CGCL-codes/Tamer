package org.mule.providers.sms;

import org.mule.impl.ThreadSafeAccess;
import org.mule.providers.AbstractMessageAdapter;
import org.mule.umo.MessagingException;
import org.mule.umo.provider.MessageTypeNotSupportedException;

/**
 * @author Jeroen Benckhuijsen (jeroen.benckhuijsen@gmail.com)
 * 
 */
public class SmsMessageAdapter extends AbstractMessageAdapter {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4478020596803659467L;

    private SmsMessage message;

    @SuppressWarnings("unchecked")
    public SmsMessageAdapter(Object message) throws MessagingException {
        if (message instanceof SmsMessage) {
            this.message = (SmsMessage) message;
        } else {
            throw new MessageTypeNotSupportedException(message, getClass());
        }
    }

    protected SmsMessageAdapter(SmsMessageAdapter template) {
        super(template);
        message = template.message;
    }

    /**
	 * Converts the message implementation into a String representation
	 * 
	 * @param encoding
	 *            The encoding to use when transforming the message (if
	 *            necessary). The parameter is used when converting from a byte
	 *            array
	 * @return String representation of the message payload
	 * @throws Exception
	 *             Implementation may throw an endpoint specific exception
	 */
    @Override
    public String getPayloadAsString(String encoding) throws Exception {
        if (message instanceof SmsTextMessage) {
            return ((SmsTextMessage) message).getMessage();
        } else if (message instanceof SmsBinaryMessage) {
            return ((SmsBinaryMessage) message).getMessage().toString();
        } else {
            throw new IllegalArgumentException("Invalid message type");
        }
    }

    @Override
    public byte[] getPayloadAsBytes() throws Exception {
        if (message instanceof SmsTextMessage) {
            return ((SmsTextMessage) message).getMessage().getBytes();
        } else if (message instanceof SmsBinaryMessage) {
            return ((SmsBinaryMessage) message).getMessage();
        } else {
            throw new IllegalArgumentException("Invalid message type");
        }
    }

    @Override
    public Object getPayload() {
        return message;
    }

    @Override
    public ThreadSafeAccess newThreadCopy() {
        return new SmsMessageAdapter(this);
    }
}
