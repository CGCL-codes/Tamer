package org.speakmon.babble.dcc.events;

import java.util.EventListener;

/**
 *
 * @author  speakmon
 */
public interface ChatMessageReceivedEventHandler extends EventListener {

    public void onChatMessageReceived(ChatMessageReceivedEvent chatMessageReceivedEvent);
}
