package trstudio.blueboxalife.sound;

import javax.sound.sampled.AudioFormat;
import trstudio.classlibrary.drivers.Setting;
import trstudio.classlibrary.sound.SoundMixer;

/**
 * Gestionnaire de son.
 *
 * @author Sebastien Villemain
 */
public class SoundManager extends trstudio.classlibrary.sound.SoundManager {

    /**
	 * Nouveau gestionnaire de son avec le nombre maximum de channel.
	 *
	 * @param playbackFormat
	 */
    public SoundManager(AudioFormat playbackFormat) {
        this(playbackFormat, getMaxSimultaneousSounds(playbackFormat));
    }

    /**
	 * Nouveau gestionnaire de son.
	 *
	 * @param playbackFormat
	 * @param maxSimultaneousSounds Nombre de channel.
	 */
    public SoundManager(AudioFormat playbackFormat, int maxSimultaneousSounds) {
        super(playbackFormat, maxSimultaneousSounds);
        SoundMixer mixer = SoundMixer.getInstance();
        Setting setting = Setting.getInstance();
        mixer.setAudioFxVolume(setting.getVolumeFx());
        mixer.setAudioMusicVolume(setting.getVolumeMusic());
    }

    protected void pooledThreadStarted() {
        super.pooledThreadStarted();
        setMasterVolume(Setting.getInstance().getVolumeMaster());
    }
}
