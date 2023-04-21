package net.frontlinesms.messaging.sms;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.listener.SmsListener;
import net.frontlinesms.messaging.FrontlineMessagingService;

public interface SmsService extends FrontlineMessagingService {

    /** Set this device to be used for sending SMS messages. */
    public void setUseForSending(boolean use);

    /** Check whether this device actually supports SMS receipt. */
    public boolean supportsReceive();

    /** Set this device to be used for receiving messages. */
    public void setUseForReceiving(boolean use);

    /** Adds the supplied message to the outbox. */
    public void sendSMS(FrontlineMessage outgoingMessage);

    /** Sets the {@link SmsListener} attached to this {@link SmsService}. */
    public void setSmsListener(SmsListener smsListener);

    /** Check whether this device actually supports sending binary sms. */
    public boolean isBinarySendingSupported();

    /** Gets the MSISDN to be displayed for this device. */
    public String getMsisdn();

    /**
	 * Checks whether this device supports sending sms messages in the UCS2 characterset.
	 * FIXME this method is unnecessary as all handsets support UCS-2 so far!
	 */
    public boolean isUcs2SendingSupported();
}
