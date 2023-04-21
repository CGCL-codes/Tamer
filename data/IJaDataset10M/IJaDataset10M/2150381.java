package org.dreamspeak.lib.events;

import java.util.EventListener;
import org.dreamspeak.lib.protocol.packets.inbound.reliablecontent.ReliableContent;

/**
 * TODO: Proper documentation
 * 
 * @author avithan
 */
public interface ContentRecievedListener extends EventListener {

    public void onContentRecieved(ReliableContent content);
}
