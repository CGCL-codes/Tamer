package com.netstoke.services.sms;

import com.netstoke.common.IEntity;

/**
 * <p>The <code>ISmsMessage</code> defines a wireless message.</p>
 * @author kmckee &lt;<a href="mailto:kevin.mckee@netstoke.com">kevin.mckee@netstoke.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public interface ISmsMessage extends IEntity {

    /**
	 * Sets the destination of where the message goes to.
	 * @param address String
	 */
    public void setDestinationAddress(String address);

    /**
	 * Returns the destination of where the message goes to.
	 * @return String
	 */
    public String getDestinationAddress();

    /**
	 * Sets the source of where the message is coming from.
	 * @param address
	 */
    public void setSourceAddress(String address);

    /**
	 * Returns the source of where the message is coming from.
	 * @return String
	 */
    public String getSourceAddress();

    /**
	 * Sets the message from text.
	 * @param from String
	 */
    public void setMessageFrom(String from);

    /**
	 * Returns the message from text.
	 * @return String
	 */
    public String getMessageFrom();

    /**
	 * Sets the text message.
	 * @param message String
	 */
    public void setMessageText(String message);

    /**
	 * Returns the text message.
	 * @return String
	 */
    public String getMessageText();
}
