package mrusanov.fantasyruler.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class SoundConfiguration {

    public static final int VOLUME_MIN = 0;

    public static final int VOLUME_MAX = 100;

    private boolean music;

    private int musicVolume;

    private boolean sound;

    private int soundVolume;

    @XmlElement
    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    @XmlElement
    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    @XmlElement
    public int getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
    }

    @XmlElement
    public int getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(int soundVolume) {
        this.soundVolume = soundVolume;
    }
}
