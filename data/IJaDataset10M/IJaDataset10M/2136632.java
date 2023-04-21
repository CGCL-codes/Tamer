package org.nvframe.manager;

import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.Sound;
import org.nvframe.event.EventService;
import org.nvframe.event.eventtype.SoundEvent;
import org.nvframe.event.eventtype.SoundListener;
import org.nvframe.exception.NVFrameRuntimeException;

/**
 * 
 * @author Nik Van Looy
 */
public class SoundManager implements SoundListener {

    private static final SoundManager _instance = new SoundManager();

    public static SoundManager getInstance() {
        return _instance;
    }

    private Map<String, Sound> sounds;

    private SoundManager() {
        sounds = new HashMap<String, Sound>();
        EventService.getInstance().addEventListener(this);
    }

    /**
	 * Add a Sound resource to the Manager
	 * 
	 * @param id the identifier (name from resources xml)
	 * @param sound the Sound resource
	 */
    public void addSound(String id, Sound sound) {
        sounds.put(id, sound);
    }

    /**
	 * Plays a sound with a given id
	 * 
	 * @param id the soundId
	 */
    public void playSound(String id) {
        if (!sounds.containsKey(id)) throw new NVFrameRuntimeException("sound with id: " + id + " not found in SoundManager");
        if (!ConfigManager.getInstance().getActive("engine_soundenabled")) return;
        Sound sound = sounds.get(id);
        sound.play();
    }

    @Override
    public void playSound(SoundEvent event) {
        playSound(event.getSoundId());
    }
}
