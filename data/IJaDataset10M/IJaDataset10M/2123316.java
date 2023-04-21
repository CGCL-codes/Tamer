package iceboatgame;

import iceboatgame.GLImage;
import java.util.*;
import java.nio.*;
import java.io.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author jam
 */
public class Textures {

    public static Map<String, Integer> texhash = new HashMap<String, Integer>();

    public static final int SIZE_INT = 4;

    public static int requestTexture(String filename) {
        int i;
        if (!Textures.texhash.containsKey(filename)) {
            i = makeTextureFromFile(filename);
            if (i > -1) Textures.texhash.put(filename, i);
        } else i = (Integer) Textures.texhash.get(filename);
        return i;
    }

    public static int makeTextureFromFile(String filename) {
        GLImage im = loadImage(filename);
        if (im == null) return -1;
        return makeTexture(im.pixelBuffer, im.w, im.h);
    }

    public static GLImage loadImage(String imgFilename) {
        GLImage img = new GLImage(imgFilename);
        if (img.isLoaded()) {
            return img;
        }
        return null;
    }

    public static int makeTexture(ByteBuffer pixels, int w, int h) {
        int textureHandle = allocateTexture();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
        return textureHandle;
    }

    public static int allocateTexture() {
        IntBuffer textureHandle = allocInts(1);
        GL11.glGenTextures(textureHandle);
        return textureHandle.get(0);
    }

    public static IntBuffer allocInts(int howmany) {
        return ByteBuffer.allocateDirect(howmany * SIZE_INT).order(ByteOrder.nativeOrder()).asIntBuffer();
    }
}
