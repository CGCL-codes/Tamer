package de.xirp.ui.event;

import java.util.EventListener;

/**
 * A voice change listener.
 * 
 * @author Matthias Gernand
 */
public interface VoiceChangedListener extends EventListener {

    /**
	 * This method is called, when the voice changed.
	 * 
	 * @param event
	 *            The event which occurred
	 */
    public void voiceChanged(VoiceChangedEvent event);
}
