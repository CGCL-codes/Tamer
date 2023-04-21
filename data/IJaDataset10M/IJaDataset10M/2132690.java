package net.sf.mxlosgi.mxlosgichatbundle.listener;

import net.sf.mxlosgi.mxlosgichatbundle.Chat;
import net.sf.mxlosgi.mxlosgixmppbundle.Message;

/**
 * @author noah
 *
 */
public interface ChatListener {

    /**
	 * 
	 * @param message
	 */
    public void processMessage(Chat chat, Message message);

    /**
	 * 
	 * @param currentChatResource
	 */
    public void resourceChanged(Chat chat, String currentChatResource);
}
