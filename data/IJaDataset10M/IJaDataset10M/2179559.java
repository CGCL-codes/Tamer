package hybris;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author menderleit
 */
public class Sprite {

    private BufferedImage img;

    private BufferedImage finalImg;

    private int x, y;

    private float alpha;

    public Sprite(String fileName) {
        x = 0;
        y = 0;
        alpha = 1.0f;
        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            img = null;
            System.out.println("Could not load image: " + e.getMessage());
        }
        finalImg = img;
    }

    public void draw(Graphics g, int x, int y) {
        this.x = x;
        this.y = y;
        Graphics2D g2d = (Graphics2D) g;
        AlphaComposite oldComposite = (AlphaComposite) g2d.getComposite();
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2d.setComposite(alphaComposite);
        g2d.drawImage(finalImg, x, y, null);
        g2d.setComposite(oldComposite);
    }

    public void setSize(int width, int height) {
        finalImg = (BufferedImage) img.getScaledInstance(width, height, BufferedImage.SCALE_FAST);
    }

    public void reset() {
        finalImg = img;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setAlpha(float alpha) {
        this.alpha = clampAlpha(alpha);
    }

    public float getAlpha() {
        return alpha;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return finalImg.getWidth();
    }

    public int getHeight() {
        return finalImg.getHeight();
    }

    private float clampAlpha(float alpha) {
        float newAlpha = alpha;
        if (alpha < 0.0f) {
            newAlpha = 0.0f;
        }
        if (alpha > 1.0f) {
            newAlpha = 1.0f;
        }
        return newAlpha;
    }
}
