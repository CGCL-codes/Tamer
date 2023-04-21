package net.sourceforge.mandelbroccoli.textures;

import com.sixlegs.png.PngImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.ImageIcon;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Mathias Johansson with help from http://www.cokeandcode.com/info/tut2d-4.html
 * and http://www.exampledepot.com/egs/java.awt.image/Image2Buf.html
 */
public class Texture {

    protected int id;

    protected int width;

    protected int height;

    protected double heightPercent = 0;

    protected double widthPercent = 0;

    protected BufferedImage bufferedImage = null;

    protected static HashMap<String, WeakReference<Texture>> buffer = new HashMap<String, WeakReference<Texture>>();

    protected static ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 }, true, false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);

    protected static ColorModel glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 0 }, false, false, ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);

    private Texture(String path) {
        this.id = allocateNewTextureId();
        try {
            this.bufferedImage = load(path);
            bind();
            store(bufferedImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void store(BufferedImage bufferedImage) {
        int srcPixelFormat = 0;
        if (bufferedImage.getColorModel().hasAlpha()) {
            srcPixelFormat = GL11.GL_RGBA;
        } else {
            srcPixelFormat = GL11.GL_RGB;
        }
        ByteBuffer textureBuffer = convertImageData(bufferedImage, this);
        if (true) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, get2Fold(bufferedImage.getWidth()), get2Fold(bufferedImage.getHeight()), 0, srcPixelFormat, GL11.GL_UNSIGNED_BYTE, textureBuffer);
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
        widthPercent = (double) (bufferedImage.getWidth()) / (double) (texWidth);
        heightPercent = (double) (bufferedImage.getHeight()) / (double) (texHeight);
        texture.height = texHeight;
        texture.width = texWidth;
        if (bufferedImage.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null);
            texImage = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 3, null);
            texImage = new BufferedImage(glColorModel, raster, false, new Hashtable());
        }
        Graphics g = texImage.getGraphics();
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

    protected BufferedImage load(String path) throws Exception {
        BufferedImage image = null;
        if (path.endsWith(".png")) {
            InputStream stream = null;
            if (path.startsWith("http://")) stream = new URL(path).openStream(); else stream = Texture.class.getClassLoader().getResourceAsStream(path);
            image = new PngImage().read(stream, true);
        } else {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            if (path.startsWith("http://")) image = toBufferedImage(toolkit.createImage(new URL(path))); else {
                if (Texture.class.getClassLoader().getResourceAsStream(path) == null) throw new Exception("Image not found: " + path);
                image = toBufferedImage(toolkit.createImage(Texture.class.getClassLoader().getResource(path)));
            }
        }
        return image;
    }

    public int getId() {
        return id;
    }

    protected static int allocateNewTextureId() {
        int size = 1;
        ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
        temp.order(ByteOrder.nativeOrder());
        IntBuffer res = temp.asIntBuffer();
        GL11.glGenTextures(res);
        return res.get(0);
    }

    public static Texture get(String path) {
        if (buffer.get(path) == null || buffer.get(path).get() == null) buffer.put(path, new WeakReference<Texture>(new Texture(path)));
        return buffer.get(path).get();
    }

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        image = new ImageIcon(image).getImage();
        boolean hasAlpha = false;
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
        }
        if (bimage == null) {
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        Graphics g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    public static boolean hasAlpha(Image image) {
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public double getHeightPercent() {
        return heightPercent;
    }

    public double getWidthPercent() {
        return widthPercent;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
