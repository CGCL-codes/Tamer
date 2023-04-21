package yager.resources;

import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;
import yager.images.BuiltinFile;
import yager.images.Image2D;
import yager.images.TGAFile;

/**
 * @author Ryan Hild (therealfreaker@sourceforge.net)
 */
public final class Image2DResourceManager {

    private static final Map resources = new HashMap();

    private Image2DResourceManager() {
    }

    public static final Image2D get(String name) throws LoadException {
        Object o = resources.get(name);
        if (o == null) throw new LoadException("Image not yet loaded");
        if (o instanceof Image2D) return (Image2D) o;
        throw new LoadException("Unable to find Image2D; name: " + name);
    }

    public static final void load(String filename, String name) throws LoadException {
        {
            Object o = resources.get(name);
            if (o != null) return;
        }
        ReadableByteChannel channel = ResourcePathManager.getChannel(filename);
        if (channel == null) throw new LoadException("Unable to open path: " + filename);
        Image2D image = null;
        if (filename.endsWith(".tga")) image = TGAFile.read(channel); else image = BuiltinFile.read(channel);
        if (image == null) throw new LoadException("Unable to load Image2D; path: " + filename);
        resources.put(name, image);
    }

    public static final void unload(String fileName) {
        resources.remove(fileName);
    }
}
