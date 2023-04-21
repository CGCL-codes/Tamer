package jme3test.asset;

import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.audio.AudioData;
import com.jme3.audio.plugins.WAVLoader;
import com.jme3.texture.Texture;
import com.jme3.texture.plugins.AWTLoader;

public class TestAbsoluteLocators {

    public static void main(String[] args) {
        AssetManager am = new DesktopAssetManager();
        am.registerLoader(AWTLoader.class, "jpg");
        am.registerLoader(WAVLoader.class, "wav");
        am.registerLocator("/", ClasspathLocator.class);
        AudioData audio = am.loadAudio("Sound/Effects/Gun.wav");
        Texture tex = am.loadTexture("Textures/Terrain/Pond/Pond.jpg");
        if (audio == null) throw new RuntimeException("Cannot find audio!"); else System.out.println("Audio loaded from Sounds/Effects/Gun.wav");
        if (tex == null) throw new RuntimeException("Cannot find texture!"); else System.out.println("Texture loaded from Textures/Terrain/Pond/Pond.jpg");
        System.out.println("Success!");
    }
}
