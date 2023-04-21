package ru.cos.sim.visualizer.texture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.glu.GLU;

/**
 * A utility class to load textures for LWJGL. This source is based
 * on a texture that can be found in the Java Gaming (www.javagaming.org)
 * Wiki. 
 * 
 * OpenGL uses a particular image format. Since the images that are 
 * loaded from disk may not match this format this loader introduces
 * a intermediate image which the source image is copied into. In turn,
 * this image is used as source for the OpenGL texture.
 *
 * @author Kevin Glass
 * @author Brian Matzon
 */
public class TextureLoader {

    private static Logger logger = Logger.getLogger(TextureLoader.class.getName());

    /** The table of textures that have been loaded in this loader */
    private HashMap table = new HashMap();

    /** The colour model including alpha for the GL image */
    private ColorModel glAlphaColorModel;

    /** The colour model for the GL image */
    private ColorModel glColorModel;

    /** 
     * Create a new texture loader based on the game panel
     *
     * @param gl The GL content in which the textures should be loaded
     */
    public TextureLoader() {
        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 }, true, false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
        glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 0 }, false, false, ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);
    }

    /**
     * Create a new texture ID 
     *
     * @return A new texture ID
     */
    private int createTextureID() {
        IntBuffer tmp = createIntBuffer(1);
        GL11.glGenTextures(tmp);
        return tmp.get(0);
    }

    public Texture createTexture(String name, int width, int height) {
        Texture tex = (Texture) table.get(name);
        if (tex != null) {
            return tex;
        }
        int result = createTextureID();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, result);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        IntBuffer ib = createIntBuffer(1);
        ib.put(result);
        glGenRenderbuffersEXT(ib);
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, result);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL11.GL_DEPTH_COMPONENT, width, height);
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, 0);
        ib = createIntBuffer(1);
        ib.put(result);
        glGenFramebuffersEXT(ib);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, result);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, result, 0);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, result);
        boolean fboUsed = true;
        int status = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
        if (status != GL_FRAMEBUFFER_COMPLETE_EXT) fboUsed = false;
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        tex = new Texture(GL11.GL_TEXTURE_2D, result);
        table.put(name, result);
        return tex;
    }

    /**
     * Load a texture
     *
     * @param resourceName The location of the resource to load
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getTexture(String resourceName) throws IOException {
        Texture tex = (Texture) table.get(resourceName);
        if (tex != null) {
            return tex;
        }
        logger.log(Level.INFO, "Loading texture " + resourceName);
        tex = getTexture(resourceName, null, GL11.GL_TEXTURE_2D, GL11.GL_RGBA, GL11.GL_NEAREST, GL11.GL_NEAREST_MIPMAP_NEAREST);
        table.put(resourceName, tex);
        return tex;
    }

    /**
     * Load a texture
     *
     * @param resourceName The location of the resource to load
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getTexture(String resourceName, BufferedImage img) throws IOException {
        Texture tex = (Texture) table.get(resourceName);
        if (tex != null) {
            return tex;
        }
        logger.log(Level.INFO, "Loading texture " + resourceName);
        tex = new Texture(GL11.GL_TEXTURE_2D, loadImage(img));
        table.put(resourceName, tex);
        return tex;
    }

    public static byte[] intToByteArray(int value) {
        return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
    }

    public static int loadImage(BufferedImage bufferedImage) {
        try {
            short width = (short) bufferedImage.getWidth();
            short height = (short) bufferedImage.getHeight();
            int bpp = (byte) bufferedImage.getColorModel().getPixelSize();
            ByteBuffer byteBuffer;
            DataBuffer db = bufferedImage.getData().getDataBuffer();
            if (db instanceof DataBufferInt) {
                int intI[] = ((DataBufferInt) (bufferedImage.getData().getDataBuffer())).getData();
                byte newI[] = new byte[intI.length * 4];
                for (int i = 0; i < intI.length; i++) {
                    byte b[] = intToByteArray(intI[i]);
                    int newIndex = i * 4;
                    newI[newIndex] = b[1];
                    newI[newIndex + 1] = b[2];
                    newI[newIndex + 2] = b[3];
                    newI[newIndex + 3] = b[0];
                }
                byteBuffer = ByteBuffer.allocateDirect(width * height * (bpp / 8)).order(ByteOrder.nativeOrder()).put(newI);
            } else {
                byteBuffer = ByteBuffer.allocateDirect(width * height * (bpp / 8)).order(ByteOrder.nativeOrder()).put(((DataBufferByte) (bufferedImage.getData().getDataBuffer())).getData());
            }
            byteBuffer.flip();
            int internalFormat = GL11.GL_RGBA8, format = GL11.GL_RGBA;
            IntBuffer textureId = BufferUtils.createIntBuffer(1);
            ;
            GL11.glGenTextures(textureId);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId.get(0));
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
            GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, internalFormat, width, height, format, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            return textureId.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return -1;
    }

    /**
     * Load a texture
     *
     * @param resourceName The location of the resource to load
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getTextureFromStream(String resourceName, InputStream stream, boolean downloadForce) throws IOException {
        if (!downloadForce) {
            Texture tex = (Texture) table.get(resourceName);
            if (tex != null) {
                return tex;
            }
        }
        logger.log(Level.INFO, "Loading texture " + resourceName);
        Texture tex = getTexture(resourceName, stream, GL11.GL_TEXTURE_2D, GL11.GL_RGBA, GL11.GL_LINEAR, GL11.GL_NEAREST);
        table.put(resourceName, tex);
        return tex;
    }

    /**
     * Load a texture into OpenGL from a image reference on
     * disk.
     *
     * @param resourceName The location of the resource to load
     * @param target The GL target to load the texture against
     * @param dstPixelFormat The pixel format of the screen
     * @param minFilter The minimising filter
     * @param magFilter The magnification filter
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getTexture(String resourceName, InputStream stream, int target, int dstPixelFormat, int minFilter, int magFilter) throws IOException {
        int srcPixelFormat = 0;
        int textureID = createTextureID();
        Texture texture = new Texture(target, textureID);
        GL11.glBindTexture(target, textureID);
        BufferedImage bufferedImage = (stream == null) ? loadImage(resourceName) : loadImage(stream);
        texture.setWidth(bufferedImage.getWidth());
        texture.setHeight(bufferedImage.getHeight());
        if (bufferedImage.getColorModel().hasAlpha()) {
            srcPixelFormat = GL11.GL_RGBA;
        } else {
            srcPixelFormat = GL11.GL_RGB;
        }
        ByteBuffer textureBuffer = convertImageData(bufferedImage, texture);
        if (target == GL11.GL_TEXTURE_2D) {
        }
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
        GLU.gluBuild2DMipmaps(target, srcPixelFormat, get2Fold(bufferedImage.getWidth()), get2Fold(bufferedImage.getHeight()), srcPixelFormat, GL11.GL_UNSIGNED_BYTE, textureBuffer);
        return texture;
    }

    /**
     * Load a texture into OpenGL from a Buffered Image
     * 
     *
     * @param resourceName The location of the resource to load
     * @param target The GL target to load the texture against
     * @param dstPixelFormat The pixel format of the screen
     * @param minFilter The minimising filter
     * @param magFilter The magnification filter
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getTexture(String resourceName, int target, BufferedImage bufferedImage, int dstPixelFormat, int minFilter, int magFilter) throws IOException {
        int srcPixelFormat = 0;
        int textureID = createTextureID();
        Texture texture = new Texture(target, textureID);
        GL11.glBindTexture(target, textureID);
        texture.setWidth(bufferedImage.getWidth());
        texture.setHeight(bufferedImage.getHeight());
        if (bufferedImage.getColorModel().hasAlpha()) {
            srcPixelFormat = GL11.GL_RGBA;
        } else {
            srcPixelFormat = GL11.GL_RGB;
        }
        ByteBuffer textureBuffer = convertImageData(bufferedImage, texture);
        if (target == GL11.GL_TEXTURE_2D) {
        }
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
        GLU.gluBuild2DMipmaps(target, srcPixelFormat, get2Fold(bufferedImage.getWidth()), get2Fold(bufferedImage.getHeight()), srcPixelFormat, GL11.GL_UNSIGNED_BYTE, textureBuffer);
        return texture;
    }

    /**
     * Get the closest greater power of 2 to the fold number
     * 
     * @param fold The target number
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
     * @param bufferedImage The image to convert to a texture
     * @param texture The texture to store the data into
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
        texture.setTextureHeight(texHeight);
        texture.setTextureWidth(texWidth);
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
        g.translate(0, texHeight);
        AffineTransform t = AffineTransform.getScaleInstance(1, -1);
        g.drawImage(bufferedImage, t, null);
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();
        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();
        return imageBuffer;
    }

    /** 
     * Load a given resource as a buffered image
     * 
     * @param ref The location of the resource to load
     * @return The loaded buffered image
     * @throws IOException Indicates a failure to find a resource
     */
    private BufferedImage loadImage(String ref) throws IOException {
        URL url = TextureLoader.class.getClassLoader().getResource(ref);
        File file = new File(ref);
        BufferedImage bufferedImage = ImageIO.read(this.getClass().getResourceAsStream(ref));
        return bufferedImage;
    }

    private BufferedImage loadImage(InputStream stream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(stream);
        return bufferedImage;
    }

    /**
     * Creates an integer buffer to hold specified ints
     * - strictly a utility method
     *
     * @param size how many int to contain
     * @return created IntBuffer
     */
    protected IntBuffer createIntBuffer(int size) {
        ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
        temp.order(ByteOrder.nativeOrder());
        return temp.asIntBuffer();
    }
}
