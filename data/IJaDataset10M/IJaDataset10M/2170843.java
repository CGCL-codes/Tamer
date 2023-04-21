package org.jrfe.res.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import org.jrfe.res.Manager;
import org.jrfe.res.Texture;
import org.jrfe.res.TextureManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 * A utility class to load textures for OpenGL. This source is based on a
 * texture that can be found in the Java Gaming (www.javagaming.org) Wiki. It
 * has been simplified slightly for explicit 2D graphics use.
 * 
 * OpenGL uses a particular image format. Since the images that are loaded from
 * disk may not match this format this loader introduces a intermediate image
 * which the source image is copied into. In turn, this image is used as source
 * for the OpenGL texture.
 * 
 * @author Kevin Glass
 * @author Brian Matzon
 * @author Kalvis Freimanis
 */
public class TextureManagerImpl implements TextureManager {

    /** The table of textures that have been loaded in this loader */
    private HashMap<String, Texture> table = new HashMap<String, Texture>();

    /** The colour model including alpha for the GL image */
    private ColorModel glAlphaColorModel;

    /** The colour model for the GL image */
    private ColorModel glColorModel;

    /** Scratch buffer for texture ID's */
    private IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(1);

    private boolean inited = false;

    /**
	 * Create a new texture loader based on the game panel
	 * 
	 * @param gl
	 *            The GL content in which the textures should be loaded
	 */
    public TextureManagerImpl() {
    }

    @Override
    public boolean isInitialized() {
        return inited;
    }

    @Override
    public boolean initilizeManager(Object... pars) {
        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 }, true, false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
        glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 0 }, false, false, ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);
        inited = true;
        return inited;
    }

    @Override
    public void destoryManager() {
        for (Entry<String, Texture> ent : table.entrySet()) {
            GL11.glDeleteTextures(BufferUtils.createIntBuffer(1).put(0, ent.getValue().getTextureID()));
        }
    }

    /**
	 * Create a new texture ID
	 * 
	 * @return A new texture ID
	 */
    private int createTextureID() {
        GL11.glGenTextures(textureIDBuffer);
        return textureIDBuffer.get(0);
    }

    /**
	 * Load a texture
	 * 
	 * @param resourceName
	 *            The location of the resource to load
	 * @return The loaded texture
	 * @throws IOException
	 *             Indicates a failure to access the resource
	 */
    @Override
    public Texture loadTexture(String resourceName) throws IOException {
        Texture tex = (Texture) table.get(resourceName);
        if (tex != null) {
            return tex;
        }
        tex = getTexture(resourceName, GL11.GL_RGBA, GL11.GL_LINEAR, GL11.GL_NEAREST);
        table.put(resourceName, tex);
        return tex;
    }

    /**
	 * Load a texture into OpenGL from a image reference on disk.
	 * 
	 * @param resourceName
	 *            The location of the resource to load
	 * @param dstPixelFormat
	 *            The pixel format of the screen
	 * @param minFilter
	 *            The minimising filter
	 * @param magFilter
	 *            The magnification filter
	 * @return The loaded texture
	 * @throws IOException
	 *             Indicates a failure to access the resource
	 */
    private Texture getTexture(String resourceName, int dstPixelFormat, int minFilter, int magFilter) throws IOException {
        int srcPixelFormat = 0;
        int textureID = createTextureID();
        Texture texture = new Texture(textureID, "", resourceName);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        BufferedImage bufferedImage = loadImage(resourceName);
        texture.setWidth(bufferedImage.getWidth());
        texture.setHeight(bufferedImage.getHeight());
        if (bufferedImage.getColorModel().hasAlpha()) {
            srcPixelFormat = GL11.GL_RGBA;
        } else {
            srcPixelFormat = GL11.GL_RGB;
        }
        ByteBuffer textureBuffer = convertImageData(bufferedImage, texture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, dstPixelFormat, get2Fold(bufferedImage.getWidth()), get2Fold(bufferedImage.getHeight()), 0, srcPixelFormat, GL11.GL_UNSIGNED_BYTE, textureBuffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        return texture;
    }

    /**
	 * Get the closest greater power of 2 to the fold number
	 * 
	 * @param fold
	 *            The target number
	 * @return The power of 2
	 */
    private int get2Fold(int fold) {
        int ret = 2;
        while (ret < fold) {
            ret *= 2;
        }
        return ret;
    }

    /**
	 * Convert the buffered image to a texture
	 * 
	 * @param bufferedImage
	 *            The image to convert to a texture
	 * @param texture
	 *            The texture to store the data into
	 * @return A buffer containing the data
	 */
    private ByteBuffer convertImageData(BufferedImage bufferedImage, Texture texture) {
        ByteBuffer imageBuffer = null;
        WritableRaster raster;
        BufferedImage texImage;
        int texWidth = 2;
        int texHeight = 2;
        while (texWidth < bufferedImage.getWidth()) {
            texWidth *= 2;
        }
        while (texHeight < bufferedImage.getHeight()) {
            texHeight *= 2;
        }
        texture.setHeight(texHeight);
        texture.setWidth(texWidth);
        if (bufferedImage.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null);
            texImage = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 3, null);
            texImage = new BufferedImage(glColorModel, raster, false, new Hashtable());
        }
        Graphics2D g = (Graphics2D) texImage.getGraphics();
        g.setColor(new Color(0f, 0f, 0f, 0f));
        g.fillRect(0, 0, texWidth, texHeight);
        g.drawImage(bufferedImage, 0, 0, null);
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();
        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();
        return imageBuffer;
    }

    @Override
    public void destroyTexture(Texture tex) {
        table.remove(tex.getResourcePath());
        GL11.glDeleteTextures(BufferUtils.createIntBuffer(1).put(0, tex.getTextureID()));
    }

    /**
	 * Load a given resource as a buffered image
	 * 
	 * @param ref
	 *            The location of the resource to load
	 * @return The loaded buffered image
	 * @throws IOException
	 *             Indicates a failure to find a resource
	 */
    private BufferedImage loadImage(String ref) throws IOException {
        URL url = TextureManagerImpl.class.getClassLoader().getResource(ref);
        if (url == null) {
            throw new IOException("Cannot find: " + ref);
        }
        BufferedImage bufferedImage = ImageIO.read(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(ref)));
        return bufferedImage;
    }
}
