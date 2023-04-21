package gear.application.events;

import gear.application.Event;

/**
 * Event intercepted by SundPlayer class 
 * @author Stefano Driussi
 *
 */
public class SoundEvent extends Event {

    private String soundPath;

    /**
	 * SoundEvent constructor
	 * @param sender instance of the class calling this event
	 * @param SoundPath path of the sound to be played
	 */
    public SoundEvent(Object sender, String SoundPath) {
        super(sender);
        this.soundPath = SoundPath;
    }

    /**
	 * Returns the sound path
	 * @return sound path
	 */
    public String getSoundPath() {
        return soundPath;
    }

    /**
	 * Returns event category
	 * @return current Event.Category.SOUND
	 * @see gear.application.Event.Category
	 */
    public Category getCategory() {
        return Category.HARDWARE;
    }
}
