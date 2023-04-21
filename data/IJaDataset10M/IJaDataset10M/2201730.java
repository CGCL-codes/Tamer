package rollmadness.util.image;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.util.BufferUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class PaintableTexture {

    private final BufferedImage IMAGE;

    private final ByteBuffer DATA;

    private final Image JMEIMAGE;

    private final Texture2D TEXTURE;

    private final Graphics2D GRAPHICS;

    private final int WIDTH;

    private final int HEIGHT;

    private final Material MATERIAL;

    private final AssetManager ASSET_MANAGER;

    public PaintableTexture(AssetManager am, int width, int height) {
        ASSET_MANAGER = am;
        WIDTH = width;
        HEIGHT = height;
        IMAGE = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        DATA = BufferUtils.createByteBuffer(width * height * 4);
        JMEIMAGE = new Image(Image.Format.RGBA8, width, height, DATA);
        TEXTURE = new Texture2D(JMEIMAGE);
        GRAPHICS = IMAGE.createGraphics();
        MATERIAL = new Material(am, "Common/MatDefs/Misc/SimpleTextured.j3md");
        MATERIAL.setTexture("m_ColorMap", TEXTURE);
    }

    public PaintableTexture(AssetManager am, Component c) {
        this(am, c.getPreferredSize().width, c.getPreferredSize().height);
    }

    public Material getMaterial() {
        return MATERIAL;
    }

    public Texture2D getTexture() {
        return TEXTURE;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }

    public Graphics2D getGraphics() {
        return GRAPHICS;
    }

    /**
     * Clears the buffer, paint the given component stretched (setBounds) to the
     * size of this image, update the underlying texture
     * @param c the component to print on this PaintableTexture
     */
    public void printAll(Component c) {
        clearBuffer();
        c.setBounds(0, 0, getWidth(), getHeight());
        c.validate();
        c.doLayout();
        c.setBounds(0, 0, getWidth(), getHeight());
        c.printAll(getGraphics());
        updateTexture();
    }

    public void updateTexture() {
        final int W = IMAGE.getWidth();
        final int H = IMAGE.getHeight();
        final int MAX = W * H;
        DATA.rewind();
        for (int i = 0; i < MAX; i++) {
            int x = i % W;
            int y = H - (i / W) - 1;
            int argb = IMAGE.getRGB(x, y);
            DATA.put((byte) ((argb >> 16) & 0xff)).put((byte) ((argb >> 8) & 0xff)).put((byte) (argb & 0xff)).put((byte) ((argb >> 24) & 0xff));
        }
        DATA.rewind();
        JMEIMAGE.setData(DATA);
        TEXTURE.setImage(JMEIMAGE);
    }

    public void clearBuffer() {
        Composite old = getGraphics().getComposite();
        getGraphics().setComposite(AlphaComposite.Clear);
        getGraphics().setPaint(new Color(0, 0, 0, 0));
        getGraphics().fillRect(0, 0, WIDTH, HEIGHT);
        getGraphics().setComposite(old);
    }
}
