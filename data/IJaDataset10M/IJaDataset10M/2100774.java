package net.resplace.game.sprite;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Porfirio
 */
public class StripSprite {

    protected int stripWidth = 0;

    protected int stripHeight = 0;

    protected int width = 0;

    protected int height = 0;

    protected int hOffset = 0;

    protected int vOffset = 0;

    protected int hSeparation = 0;

    protected int vSeparation = 0;

    protected BufferedImage image;

    public StripSprite(Image image, int width, int height, int hOffset, int vOffset, int hSeparation, int vSeparation) {
        System.out.println(image.getWidth(null));
        this.image = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        this.image.getGraphics().drawImage(image, 0, 0, null);
        this.stripWidth = image.getWidth(null);
        this.stripHeight = image.getHeight(null);
        this.width = width;
        this.height = height;
        this.hOffset = hOffset;
        this.vOffset = vOffset;
        this.hSeparation = hSeparation;
        this.vSeparation = vSeparation;
    }

    public StripSprite(String image, int width, int height, int hOffset, int vOffset, int hSeparation, int vSeparation) {
        this(new ImageIcon(image).getImage(), width, height, hOffset, vOffset, hSeparation, vSeparation);
    }

    public StripSprite(URL image, int width, int height, int hOffset, int vOffset, int hSeparation, int vSeparation) {
        this(new ImageIcon(image).getImage(), width, height, hOffset, vOffset, hSeparation, vSeparation);
    }

    public StripSprite(Image image, int width, int height, int hOffset, int vOffset) {
        this(image, width, height, hOffset, vOffset, 0, 0);
    }

    public StripSprite(String image, int width, int height, int hOffset, int vOffset) {
        this(image, width, height, hOffset, vOffset, 0, 0);
    }

    public StripSprite(URL image, int width, int height, int hOffset, int vOffset) {
        this(image, width, height, hOffset, vOffset, 0, 0);
    }

    public StripSprite(Image image, int width, int height) {
        this(image, width, height, 0, 0);
    }

    public StripSprite(String image, int width, int height) {
        this(image, width, height, 0, 0);
    }

    public StripSprite(URL image, int width, int height) {
        this(image, width, height, 0, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getImage(int h, int v) {
        int y = (h * width) + hOffset;
        if (h > 0) {
            y += (h * hSeparation);
        }
        int x = (v * height) + vOffset;
        if (v > 0) {
            x += (v * vSeparation);
        }
        return image.getSubimage(x, y, width, height);
    }

    public Sprite getSprite(int h, int v) {
        return new Sprite(getImage(h, v));
    }

    public Sprite getSprite(int[][] positions) {
        Sprite sprite = new Sprite();
        for (int i = 0; i < positions.length; i++) {
            int[] is = positions[i];
            sprite.add(getImage(is[0], is[1]));
        }
        return sprite;
    }

    public Sprite getSprite(int... positions) {
        Sprite sprite = new Sprite();
        for (int i = 0; i < positions.length; i += 2) {
            if (i + 1 == positions.length) {
                break;
            }
            sprite.add(getImage(positions[i], positions[i + 1]));
        }
        return sprite;
    }
}
