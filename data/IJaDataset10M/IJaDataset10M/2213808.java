package org.ejen;

import java.util.EventListener;

/**
 * Ejen listener class.
 * @author F. Wolff
 * @version 1.0
 * @see org.ejen.EjenEvent
 */
public interface EjenListener extends EventListener {

    /**
	 * Invoked when an EjenChildNode state has changed. See
	 * {@link org.ejen.EjenChildNode#sendStateEvent()}.
	 */
    public void stateChanged(EjenEvent ejenEvent);

    /**
	 * Invoked when an EjenChildNode sends a message. See
	 * {@link org.ejen.EjenChildNode#sendMessageEvent(String msg)}.
	 */
    public void nodeMessageSent(EjenEvent ejenEvent);

    /**
	 * Invoked when an XSL stylesheet sends a message. See
	 * {@link org.ejen.ext.Messenger#send(XSLProcessorContext context, ElemExtensionCall elem)}.
	 */
    public void xslMessageSent(EjenEvent ejenEvent);
}
