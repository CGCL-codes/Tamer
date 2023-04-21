package com.mw3d.game;

import java.io.File;
import com.jme.app.AbstractGame;
import com.jme.renderer.Camera;
import com.jmex.sound.openAL.SoundSystem;
import com.jmex.sound.openAL.scene.SoundNode;
import com.mw3d.core.level.Level;

public class GameResources {

    public static Level level = null;

    public static int sNode;

    public static SoundNode soundNode;

    public static AbstractGame abstractGame;

    /**
	 * Creates the sample sound for this entity.
	 */
    public static int createSound(String url, int min, int max, int vol, boolean loop, Camera camera) {
        int soundId = -1;
        try {
            soundId = SoundSystem.create3DSample(new File(url).toURL());
            if (!loop) SoundSystem.bindEventToSample(soundId, soundId);
            SoundSystem.setSampleMaxAudibleDistance(soundId, max);
            SoundSystem.setSampleMinAudibleDistance(soundId, min);
            SoundSystem.setSamplePosition(soundId, camera.getLocation().x, camera.getLocation().y, camera.getLocation().z);
            SoundSystem.addSampleToNode(soundId, sNode);
            SoundSystem.setSampleVolume(soundId, vol);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soundId;
    }
}
