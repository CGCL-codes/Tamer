package sourceforge.shinigami.music;

import java.io.File;
import sourceforge.shinigami.io.FileExtensionUnsupportedException;

public abstract class Sound {

    public abstract void play();

    public abstract void stop();

    public static Sound create(String filepath) {
        String[] parts = filepath.split("\\.");
        String ext = parts[parts.length - 1];
        if (ext.equalsIgnoreCase("mid")) return new Midi(filepath); else if (ext.equalsIgnoreCase("mp3")) return new Mp3(filepath); else if (ext.equalsIgnoreCase("wav")) return new Wave(filepath); else throw new FileExtensionUnsupportedException("File extension " + ext + " not supported by Sound class." + " Use mid, mp3 or wav instead");
    }

    public static Sound create(File bgm) {
        if (bgm == null) return null; else return Sound.create(bgm.getPath());
    }
}
