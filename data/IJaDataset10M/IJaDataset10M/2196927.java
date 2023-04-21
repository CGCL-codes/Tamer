package de.xirp.ui.event;

import java.util.EventObject;

/**
 * A voice change event.
 * 
 * @author Matthias Gernand
 */
public class VoiceChangedEvent extends EventObject {

    /**
	 * The serial version unique identifier
	 */
    private static final long serialVersionUID = 5280962842820330485L;

    /**
	 * The name of the new active voice
	 */
    private String voiceName;

    /**
	 * Creates a new event.
	 * 
	 * @param source
	 *            The source of the event
	 * @param voiceName
	 *            the name of the new voice.
	 */
    public VoiceChangedEvent(Object source, String voiceName) {
        super(source);
        this.voiceName = voiceName;
    }

    /**
	 * Gets the name of the new active voice.
	 * 
	 * @return the voice name
	 */
    public String getVoiceName() {
        return voiceName;
    }
}
