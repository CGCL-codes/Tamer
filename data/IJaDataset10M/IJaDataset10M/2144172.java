package org.chaoticengine.cgll.factory;

import org.chaoticengine.cgll.exceptions.LoadingException;
import java.util.HashMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.simpleframework.xml.Serializer;

/**
 * This singleton class is responsible for loading special sound effects.
 *
 * @author Matt v.d. Westhuizen
 */
public class SoundFactory extends AbstractFactory {

    private static SoundFactory instance = null;

    private GameContainer gc = null;

    private Serializer srl = null;

    private HashMap<String, Sound> sounds = new HashMap<String, Sound>();

    private HashMap<String, Music> musics = new HashMap<String, Music>();

    private SoundFactory() {
    }

    public static SoundFactory getInstance() {
        if (instance == null) {
            instance = new SoundFactory();
        }
        return (instance);
    }

    public Sound getSound(String name) throws LoadingException {
        String filename = "data/sounds/" + name;
        if (!sounds.containsKey(filename)) {
            Sound snd = null;
            try {
                snd = new Sound(filename);
            } catch (SlickException sEx) {
                throw new LoadingException(sEx);
            }
            sounds.put(filename, snd);
        }
        return (sounds.get(filename));
    }

    public Music getMusic(String name) throws LoadingException {
        String filename = "data/music/" + name;
        if (!musics.containsKey(filename)) {
            Music msc = null;
            try {
                msc = new Music(filename);
            } catch (SlickException sEx) {
                throw new LoadingException(sEx);
            }
            musics.put(filename, msc);
        }
        return (musics.get(filename));
    }

    public GameContainer getGameContainer() {
        return gc;
    }

    public void setGameContainer(GameContainer gc) {
        this.gc = gc;
    }

    public Serializer getSerializer() {
        return srl;
    }

    public void setSerializer(Serializer srl) {
        this.srl = srl;
    }
}
