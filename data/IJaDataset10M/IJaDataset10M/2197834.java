package eu.cherrytree.paj.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import eu.cherrytree.paj.base.AppDefinition;
import eu.cherrytree.paj.graphics.ImageManager;
import eu.cherrytree.paj.gui.Console;
import eu.cherrytree.paj.sound.SoundManager;

public class Configuration {

    private static class ConfigSaveFile implements Serializable {

        private static final long serialVersionUID = 20101116;

        public int resolutionX, resolutionY;

        public boolean fullscreen;

        public boolean anisotropy;

        public int antialiasing;

        public float sfxVolume;

        public float musicVolume;

        public int soundQuality;

        public int soundAPI;

        public int soundDevice;

        public float videoVolume;

        public boolean verticalSync;

        public int textureQuality;

        public boolean textureCompression;
    }

    private boolean configLoaded;

    private String homePath;

    private ConfigSaveFile savefile;

    public Configuration(String path) {
        homePath = path;
        configLoaded = loadConfig();
    }

    public boolean isConfigLoaded() {
        return configLoaded;
    }

    public void setResolution(int resX, int resY) {
        savefile.resolutionX = resX;
        savefile.resolutionY = resY;
    }

    public void setFullscreen(boolean full) {
        savefile.fullscreen = full;
    }

    public int getResolutionX() {
        return savefile.resolutionX;
    }

    public int getResolutionY() {
        return savefile.resolutionY;
    }

    public boolean getFullscreen() {
        return savefile.fullscreen;
    }

    public boolean getAnisotropy() {
        return savefile.anisotropy;
    }

    public int getAntialiasing() {
        return savefile.antialiasing;
    }

    public void setAnisotropy(boolean a) {
        savefile.anisotropy = a;
    }

    public void setAntialiasing(int a) {
        savefile.antialiasing = a;
    }

    public void setSfxVolume(float v) {
        savefile.sfxVolume = v;
    }

    public void setMusicVolume(float v) {
        savefile.musicVolume = v;
    }

    public float getSfxVolume() {
        return savefile.sfxVolume;
    }

    public float getMusicVolume() {
        return savefile.musicVolume;
    }

    public void setVideoVolume(float v) {
        savefile.videoVolume = v;
    }

    public float getVideoVolume() {
        return savefile.videoVolume;
    }

    public void setSoundQuality(int q) {
        savefile.soundQuality = q;
    }

    public void setSoundAPI(int a) {
        savefile.soundAPI = a;
    }

    public void setSoundDevice(int s) {
        savefile.soundDevice = s;
    }

    public int getSoundQuality() {
        return savefile.soundQuality;
    }

    public int getSoundAPI() {
        return savefile.soundAPI;
    }

    public int getSoundDevice() {
        return savefile.soundDevice;
    }

    public void setVerticalSync(boolean vs) {
        savefile.verticalSync = vs;
    }

    public void setTextureQuality(int tq) {
        savefile.textureQuality = tq;
    }

    public void setTextureCompression(boolean tc) {
        savefile.textureCompression = tc;
    }

    public boolean getVerticalSync() {
        return savefile.verticalSync;
    }

    public int getTextureQuality() {
        return savefile.textureQuality;
    }

    public boolean getTextureCompression() {
        return savefile.textureCompression;
    }

    public boolean saveConfig() {
        String fullPath = homePath + AppDefinition.getConfigFileName();
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fullPath));
            out.writeObject(savefile);
            out.close();
        } catch (Exception e) {
            Console.print("Couldn't save config.");
            return false;
        }
        return true;
    }

    public boolean loadConfig() {
        String fullPath = homePath + AppDefinition.getConfigFileName();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fullPath));
            savefile = (ConfigSaveFile) in.readObject();
            in.close();
        } catch (Exception e) {
            Console.print("Couldn't load config.");
            savefile = new ConfigSaveFile();
            return false;
        }
        return true;
    }

    public String getString() {
        if (savefile == null) return null;
        String ret = "";
        ret += "Resolution: " + savefile.resolutionX + "x" + savefile.resolutionY + "\n";
        ret += "Fullscreen: " + savefile.fullscreen + "\n";
        ret += "\n";
        ret += "Vertical Sync: " + savefile.verticalSync + "\n";
        ret += "Texture Quality: " + ImageManager.TextureQuality.values()[savefile.textureQuality] + "\n";
        ret += "Texture Compression: " + savefile.textureCompression + "\n";
        ret += "Anisotropy: " + savefile.anisotropy + "\n";
        ret += "Antialiasing: " + savefile.antialiasing + "\n";
        ret += "\n";
        ret += "SFX Volume: " + savefile.sfxVolume + "\n";
        ret += "Music Volume: " + savefile.musicVolume + "\n";
        ret += "Video Volume: " + savefile.videoVolume + "\n";
        ret += "\n";
        ret += "Sound Quality: " + SoundManager.SoundQuality.values()[savefile.soundQuality] + "\n";
        ret += "Sound API: " + SoundManager.SoundAPI.values()[savefile.soundAPI] + "\n";
        ret += "Sound Device: " + savefile.soundDevice + "\n";
        return ret;
    }
}
