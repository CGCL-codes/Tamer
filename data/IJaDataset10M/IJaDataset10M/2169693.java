package org.dreamspeak.lib.events;

import java.util.EventListener;
import org.dreamspeak.lib.protocol.packets.inbound.Voice;

/**
 * TODO: Proper documentation
 * 
 * @author avithan
 */
public interface VoiceRecievedListener extends EventListener {

    public void onVoiceDataRecieved(Voice voiceData);
}
